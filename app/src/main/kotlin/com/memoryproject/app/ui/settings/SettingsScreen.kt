package com.memoryproject.app.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import com.memoryproject.app.BuildConfig
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.memoryproject.app.ui.theme.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onToggleDarkMode: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    darkTheme: Boolean,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val isDark = darkTheme
    val scaffoldBg = if (isDark) DarkBackground else Cornsilk
    val primaryText = if (isDark) DarkOnSurface else Charcoal
    val mutedText = if (isDark) DarkOnSurfaceVariant else CharcoalMuted
    val cardBg = if (isDark) DarkSurface else WarmWhite

    // Show coming soon snackbar for profile row
    LaunchedEffect(uiState.profileMessage) {
        uiState.profileMessage?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.clearProfileMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = primaryText
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                color = if (isDark) DarkSurface else WarmWhite,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = primaryText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = scaffoldBg
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = scaffoldBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Account section
            SettingsSection(title = "Account", cardBg = cardBg, isDark = isDark) {
                SettingsItem(
                    icon = Icons.Default.Person,
                    title = "Profile",
                    subtitle = uiState.userName.ifBlank { uiState.userEmail.ifBlank { "Loading..." } },
                    trailing = {
                        IconButton(
                            onClick = onProfileClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit profile",
                                tint = if (isDark) DarkBronze else Bronze,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    },
                    onClick = onProfileClick,
                    bgColor = cardBg,
                    isDark = isDark
                )
            }
            // Appearance section
            SettingsSection(title = "Appearance", cardBg = cardBg, isDark = isDark) {
                val darkModeActivePill: (@Composable () -> Unit)? = if (uiState.isDarkMode) {
                    {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isDark) DarkBronze.copy(alpha = 0.25f) else Bronze.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "Active",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isDark) DarkBronze else BronzeDark,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else null
                SettingsItem(
                    icon = Icons.Default.DarkMode,
                    title = "Dark Mode",
                    subtitle = if (uiState.isDarkMode) "🌙 Dark" else "☀️ Light",
                    trailing = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (darkModeActivePill != null) {
                                darkModeActivePill()
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            val darkModeTrackColor by animateColorAsState(
                                targetValue = if (uiState.isDarkMode) Bronze else if (isDark) DarkBorder else Border,
                                animationSpec = tween(durationMillis = 300),
                                label = "darkModeTrack"
                            )
                            Switch(
                                checked = uiState.isDarkMode,
                                onCheckedChange = {
                                    viewModel.toggleDarkMode()
                                    onToggleDarkMode()
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = WarmWhite,
                                    checkedTrackColor = darkModeTrackColor,
                                    uncheckedThumbColor = WarmWhite,
                                    uncheckedTrackColor = darkModeTrackColor
                                )
                            )
                        }
                    },
                    onClick = {
                        viewModel.toggleDarkMode()
                        onToggleDarkMode()
                    },
                    bgColor = cardBg,
                    isDark = isDark
                )
            }

            // Notifications section
            SettingsSection(title = "Notifications", cardBg = cardBg, isDark = isDark) {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = "Reminders",
                    subtitle = "Get gentle reminders to add memories",
                    trailing = {
                        val notifTrackColor by animateColorAsState(
                            targetValue = if (uiState.notificationsEnabled) Bronze else if (isDark) DarkBorder else Border,
                            animationSpec = tween(durationMillis = 300),
                            label = "notifTrack"
                        )
                        Switch(
                            checked = uiState.notificationsEnabled,
                            onCheckedChange = { viewModel.toggleNotifications() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = WarmWhite,
                                checkedTrackColor = notifTrackColor,
                                uncheckedThumbColor = WarmWhite,
                                uncheckedTrackColor = notifTrackColor
                            )
                        )
                    },
                    onClick = { viewModel.toggleNotifications() },
                    bgColor = cardBg,
                    isDark = isDark
                )
            }
            // About section
            SettingsSection(title = "About", cardBg = cardBg, isDark = isDark) {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "Memory Project",
                    subtitle = "Version ${BuildConfig.VERSION_NAME}",
                    trailing = { ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (isDark) DarkBronze.copy(alpha = 0.2f) else Bronze.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "✓ Up to date",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isDark) DarkBronze else BronzeDark,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                            )
                        }
                    },
                    showDivider = true,
                    onClick = { },
                    bgColor = cardBg,
                    isDark = isDark
                )
                SettingsItem(
                    icon = Icons.Default.PrivacyTip,
                    title = "Privacy Policy",
                    subtitle = "Your data is always private",
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://web-redrixvixs-projects.vercel.app/privacy"))
                        context.startActivity(intent)
                    },
                    bgColor = cardBg,
                    isDark = isDark
                )
                SettingsItem(
                    icon = Icons.Default.Star,
                    title = "Rate This App",
                    subtitle = "Share your experience",
                    onClick = {
                        // Open Play Store — use market:// for Play Store app, fallback to web URL
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${context.packageName}"))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}"))
                            context.startActivity(webIntent)
                        }
                    },
                    bgColor = cardBg,
                    isDark = isDark
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sign out button
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ErrorRed.copy(alpha = 0.1f),
                    contentColor = ErrorRed
                )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Sign Out",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    "Sign Out?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                )
            },
            text = {
                Text(
                    "Your books will still be here when you come back.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = mutedText
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout()
                        onLogout()
                    }
                ) {
                    Text("Sign Out", color = ErrorRed, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = mutedText)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    cardBg: androidx.compose.ui.graphics.Color,
    isDark: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    val sectionTitleColor = if (isDark) DarkOnSurfaceVariant else CharcoalMuted

    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = sectionTitleColor,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        // Subtle warm divider above each section card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(horizontal = 4.dp)
                .background(
                    color = if (isDark) DarkDivider.copy(alpha = 0.5f) else Border.copy(alpha = 0.5f)
                )
        )
        Spacer(modifier = Modifier.height(6.dp))
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    trailing: (@Composable () -> Unit)? = null,
    showDivider: Boolean = false,
    onClick: () -> Unit,
    bgColor: androidx.compose.ui.graphics.Color,
    isDark: Boolean
) {
    val iconBgColor = if (isDark) DarkSurfaceVariant else Bronze.copy(alpha = 0.1f)
    val primaryText = if (isDark) DarkOnSurface else Charcoal
    val mutedText = if (isDark) DarkOnSurfaceVariant else CharcoalMuted
    val dividerColor = if (isDark) DarkDivider else Divider

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgColor)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = iconBgColor,
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = Bronze,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = primaryText,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = mutedText
                )
            }

            if (trailing != null) {
                trailing()
            } else {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = "Navigate to $title",
                    tint = mutedText,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 70.dp),
                color = dividerColor,
                thickness = 0.5.dp
            )
        }
    }
}
