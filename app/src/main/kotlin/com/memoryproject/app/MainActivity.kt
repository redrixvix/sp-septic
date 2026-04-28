package com.memoryproject.app

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.memoryproject.app.data.preferences.PreferencesManager
import com.memoryproject.app.ui.auth.AuthScreen
import com.memoryproject.app.ui.auth.AuthViewModel
import com.memoryproject.app.ui.books.BookDetailScreen
import com.memoryproject.app.ui.books.BooksScreen
import com.memoryproject.app.ui.home.HomeScreen
import com.memoryproject.app.ui.invite.InviteScreen
import com.memoryproject.app.ui.onboarding.OnboardingScreen
import com.memoryproject.app.ui.screens.ProfileScreen
import com.memoryproject.app.ui.settings.SettingsScreen
import com.memoryproject.app.ui.theme.*
import org.koin.androidx.viewmodel.ext.android.viewModel

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : BottomNavItem("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    data object Books : BottomNavItem("books", "Books", Icons.Filled.AutoStories, Icons.Outlined.AutoStories)
    data object Profile : BottomNavItem("profile", "Profile", Icons.Filled.Person, Icons.Outlined.Person)
}

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefsManager = PreferencesManager(this)
        val startDestination = if (!prefsManager.onboardingCompleted) "onboarding" else "auth"

        setContent {
            var darkThemeEnabled by remember { mutableStateOf(prefsManager.darkMode) }

            MemoryProjectTheme(darkTheme = darkThemeEnabled) {
                MemoryNavHost(
                    modifier = Modifier.fillMaxSize(),
                    startDestination = startDestination,
                    authViewModel = authViewModel,
                    onOnboardingComplete = {
                        prefsManager.onboardingCompleted = true
                    },
                    darkThemeEnabled = darkThemeEnabled,
                    onDarkThemeToggle = {
                        val newValue = !prefsManager.darkMode
                        prefsManager.darkMode = newValue
                        darkThemeEnabled = newValue
                    }
                )
            }
        }

        // Handle OAuth callback deep link if app was launched with one
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val data = intent.data ?: return
        val scheme = data.scheme ?: return

        if (scheme == "memoryproject" && data.host == "oauth" && data.path == "/callback") {
            authViewModel.setPendingGoogleCallback(data.toString())
        } else if (scheme == "memoryproject" && data.host == "invite") {
            // Existing invite deep link — handled by Navigation deep link
        }
    }
}

private fun launchCustomTab(context: Context, uri: Uri) {
    try {
        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
            .launchUrl(context, uri)
    } catch (e: Exception) {
        val browserIntent = Intent(Intent.ACTION_VIEW, uri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(browserIntent)
    }
}

@Composable
fun MemoryNavHost(
    modifier: Modifier = Modifier,
    startDestination: String,
    authViewModel: AuthViewModel,
    onOnboardingComplete: () -> Unit,
    darkThemeEnabled: Boolean = false,
    onDarkThemeToggle: () -> Unit = {}
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(BottomNavItem.Home, BottomNavItem.Books, BottomNavItem.Profile)

    // Determine if we should show bottom nav (hide on auth, settings, book detail, onboarding)
    val showBottomNav = currentDestination?.route in listOf("home", "books", "profile")

    val isDark = darkThemeEnabled
    val scaffoldBg = if (isDark) DarkBackground else Cornsilk
    val selectedColor = if (isDark) DarkBronze else Bronze
    val unselectedColor = if (isDark) DarkOnSurfaceVariant else CharcoalMuted

    val context = androidx.compose.ui.platform.LocalContext.current
    val workOSAuthorizationUrl by authViewModel.workOSAuthorizationUrl.collectAsState()
    LaunchedEffect(workOSAuthorizationUrl) {
        val url = workOSAuthorizationUrl ?: return@LaunchedEffect
        launchCustomTab(context, Uri.parse(url))
        authViewModel.consumeWorkOSAuthorizationUrl()
    }

    Scaffold(
        modifier = modifier.background(scaffoldBg),
        containerColor = scaffoldBg,
        bottomBar = {
            if (showBottomNav) {
                // Wrapping Box gives us full-width with proper rounded corners
                Box(modifier = Modifier.fillMaxWidth().navigationBarsPadding()) {
                    NavigationBar(
                        containerColor = if (isDark) DarkSurface.copy(alpha = 0.95f) else WarmWhite.copy(alpha = 0.95f),
                        contentColor = if (isDark) DarkOnSurface else Charcoal,
                        tonalElevation = 0.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .border(
                                width = 1.dp,
                                color = if (isDark) DarkBorder.copy(alpha = 0.6f) else Border.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(24.dp)
                            )
                    ) {
                        bottomNavItems.forEach { item ->
                            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                            val pressInteraction = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                            val isPressed by pressInteraction.collectIsPressedAsState()
                            val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current
                            val iconScale by animateFloatAsState(
                                targetValue = when {
                                    isPressed -> 0.82f
                                    selected -> 1.12f
                                    else -> 1f
                                },
                                animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
                                label = "navIconScale"
                            )
                            LaunchedEffect(isPressed) {
                                if (isPressed) haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                            }
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                interactionSource = pressInteraction,
                                icon = {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.title,
                                        modifier = Modifier
                                            .scale(iconScale)
                                            .size(26.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = selectedColor,
                                    selectedTextColor = selectedColor,
                                    unselectedIconColor = unselectedColor,
                                    unselectedTextColor = unselectedColor,
                                    indicatorColor = if (isDark) DarkBronze.copy(alpha = 0.15f) else Bronze.copy(alpha = 0.12f)
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(padding)
        ) {
            composable(
                "onboarding",
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                OnboardingScreen(
                    onComplete = {
                        onOnboardingComplete()
                        navController.navigate("auth?google_callback=") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    },
                    darkTheme = darkThemeEnabled
                )
            }

            composable(
                route = "auth?google_callback={google_callback}",
                arguments = listOf(
                    navArgument("google_callback") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                ),
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "memoryproject://oauth/callback"
                    }
                ),
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                AuthScreen(
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                        }
                    },
                    darkTheme = darkThemeEnabled,
                    viewModel = authViewModel
                )
            }

            composable(
                "home",
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                HomeScreen(
                    onNavigateToBooks = {
                        navController.navigate("books")
                    },
                    onNavigateToBook = { bookId ->
                        navController.navigate("book/$bookId")
                    },
                    onNavigateToAddMemory = { bookId, prompt ->
                        if (prompt != null) {
                            navController.navigate("book/$bookId?prompt=${java.net.URLEncoder.encode(prompt, "UTF-8")}")
                        } else {
                            navController.navigate("book/$bookId")
                        }
                    },
                    darkTheme = darkThemeEnabled
                )
            }

            composable(
                "books",
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                BooksScreen(
                    onBookClick = { bookId ->
                        navController.navigate("book/$bookId")
                    },
                    onLogout = {
                        navController.navigate("auth") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onSettings = {
                        navController.navigate("settings")
                    },
                    darkTheme = darkThemeEnabled
                )
            }

            composable(
                route = "book/{bookId}?prompt={prompt}",
                arguments = listOf(
                    navArgument("bookId") { type = NavType.IntType },
                    navArgument("prompt") { type = NavType.StringType; nullable = true; defaultValue = null },
                ),
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) }
            ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getInt("bookId") ?: return@composable
                val startPrompt = backStackEntry.arguments?.getString("prompt")?.let {
                    java.net.URLDecoder.decode(it, "UTF-8")
                }
                BookDetailScreen(
                    bookId = bookId,
                    onBack = { navController.popBackStack() },
                    darkTheme = darkThemeEnabled,
                    startPrompt = startPrompt,
                    onShareBook = { /* TODO: implement book sharing */ }
                )
            }

            composable(
                route = "book/{bookId}",
                arguments = listOf(navArgument("bookId") { type = NavType.IntType }),
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) }
            ) { backStackEntry ->
                val bookId = backStackEntry.arguments?.getInt("bookId") ?: return@composable
                BookDetailScreen(
                    bookId = bookId,
                    onBack = { navController.popBackStack() },
                    darkTheme = darkThemeEnabled,
                    onShareBook = { /* TODO: implement book sharing */ }
                )
            }

            composable(
                "settings",
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                SettingsScreen(
                    onBack = { navController.popBackStack() },
                    onLogout = {
                        navController.navigate("auth") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onToggleDarkMode = onDarkThemeToggle,
                    onProfileClick = {
                        navController.navigate("profile")
                    },
                    darkTheme = darkThemeEnabled
                )
            }

            composable(
                "profile",
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) }
            ) {
                ProfileScreen(
                    onBack = { navController.popBackStack() },
                    darkTheme = darkThemeEnabled
                )
            }

            composable(
                "invite/{token}",
                arguments = listOf(navArgument("token") { type = NavType.StringType }),
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) }
            ) { backStackEntry ->
                val token = backStackEntry.arguments?.getString("token") ?: return@composable
                InviteScreen(
                    token = token,
                    darkTheme = darkThemeEnabled,
                    onAccepted = { bookId ->
                        navController.navigate("book/$bookId") {
                            popUpTo("invite/{token}") { inclusive = true }
                        }
                    },
                    onLoginRequired = {
                        navController.navigate("auth") {
                            popUpTo("invite/{token}") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
