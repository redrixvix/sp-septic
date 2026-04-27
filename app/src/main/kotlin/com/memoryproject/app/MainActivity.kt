package com.memoryproject.app

import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: android.content.Intent) {
        val data = intent.data ?: return
        val scheme = data.scheme ?: return

        if (scheme == "memoryproject" && data.host == "oauth" && data.path == "/callback") {
            // Google OAuth callback — store pending callback in ViewModel.
            // MemoryNavHost's LaunchedEffect watches pendingGoogleCallback and
            // performs the navigation to auth screen from inside the composable,
            // ensuring the NavController is available.
            authViewModel.setPendingGoogleCallback(data.toString())
        } else if (scheme == "memoryproject" && data.host == "invite") {
            // Existing invite deep link — handled by Navigation deep link
        }
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
    val navBg = if (isDark) DarkSurface else WarmWhite
    val selectedColor = if (isDark) DarkBronze else Bronze
    val unselectedColor = if (isDark) DarkOnSurfaceVariant else CharcoalMuted

    // Watch pending Google OAuth callback set by handleIntent (from onNewIntent).
    // When set, navigate to auth screen from inside the composable so NavController is available.
    val pendingGoogleCallback by authViewModel.pendingGoogleCallback.collectAsState()
    LaunchedEffect(pendingGoogleCallback) {
        pendingGoogleCallback ?: return@LaunchedEffect
        navController.navigate("auth?google_callback=") {
            popUpTo("auth") { inclusive = true }
            launchSingleTop = true
        }
    }

    Scaffold(
        modifier = modifier.background(scaffoldBg),
        containerColor = scaffoldBg,
        bottomBar = {
            if (showBottomNav) {
                NavigationBar(
                    containerColor = navBg,
                    contentColor = if (isDark) DarkOnSurface else Charcoal,
                    tonalElevation = 4.dp,
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                        .height(72.dp)
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
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
                            icon = {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
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
                                indicatorColor = if (isDark) DarkBronze.copy(alpha = 0.18f) else Bronze.copy(alpha = 0.15f)
                            )
                        )
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
            ) { backStackEntry ->
                val googleCallback = backStackEntry.arguments?.getString("google_callback")
                AuthScreen(
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                        }
                    },
                    googleCallbackUri = googleCallback,
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
                    onNavigateToProfile = {
                        navController.navigate("profile")
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
                    onProfile = {
                        navController.navigate("profile")
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
                    startPrompt = startPrompt
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
                    darkTheme = darkThemeEnabled
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
