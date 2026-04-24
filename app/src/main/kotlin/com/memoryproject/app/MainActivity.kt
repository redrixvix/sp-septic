package com.memoryproject.app

import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.memoryproject.app.data.preferences.PreferencesManager
import com.memoryproject.app.ui.auth.AuthScreen
import com.memoryproject.app.ui.books.BookDetailScreen
import com.memoryproject.app.ui.books.BooksScreen
import com.memoryproject.app.ui.screens.ProfileScreen
import com.memoryproject.app.ui.settings.SettingsScreen
import com.memoryproject.app.ui.theme.MemoryProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val prefsManager = PreferencesManager(this)
        val initialDarkMode = prefsManager.darkMode

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (!notificationManager.areNotificationsEnabled()) {
                // Notification permission not granted — could show a dialog here
            }
        }

        setContent {
            var darkThemeEnabled by remember { mutableStateOf(initialDarkMode) }
            MemoryProjectTheme(darkTheme = darkThemeEnabled) {
                MemoryNavHost(
                    modifier = Modifier.fillMaxSize(),
                    darkThemeEnabled = darkThemeEnabled,
                    onDarkThemeToggle = { darkThemeEnabled = !darkThemeEnabled }
                )
            }
        }
    }
}

@Composable
fun MemoryNavHost(
    modifier: Modifier = Modifier,
    darkThemeEnabled: Boolean = false,
    onDarkThemeToggle: () -> Unit = {}
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "auth",
        modifier = modifier
    ) {
        composable(
            "auth",
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            AuthScreen(
                onLoginSuccess = {
                    navController.navigate("books") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
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
                        popUpTo("books") { inclusive = true }
                    }
                },
                onSettings = {
                    navController.navigate("settings")
                },
                onProfile = {
                    navController.navigate("profile")
                }
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
                onBack = { navController.popBackStack() }
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
                        popUpTo("books") { inclusive = true }
                    }
                },
                onToggleDarkMode = onDarkThemeToggle,
                onProfileClick = {
                    navController.navigate("profile")
                }
            )
        }

        composable(
            "profile",
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            ProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "toggleDarkMode",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            LaunchedEffect(Unit) {
                onDarkThemeToggle()
                navController.popBackStack()
            }
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}