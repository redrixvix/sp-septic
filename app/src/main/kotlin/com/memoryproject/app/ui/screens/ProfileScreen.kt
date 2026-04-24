package com.memoryproject.app.ui.screens
import androidx.compose.ui.graphics.Brush

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memoryproject.app.ui.theme.*
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.rememberCoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    darkTheme: Boolean,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showEditNameDialog by remember { mutableStateOf(false) }
    var showInviteDialog by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf("") }
    var inviteEmail by remember { mutableStateOf("") }

    val isDark = darkTheme
    val scaffoldBg = if (isDark) DarkBackground else Cornsilk
    val cardBg = if (isDark) DarkSurface else WarmWhite
    val primaryText = if (isDark) DarkOnSurface else Charcoal
    val mutedText = if (isDark) DarkOnSurfaceVariant else CharcoalMuted
    val backIconTint = primaryText

    LaunchedEffect(uiState.message) {
        uiState.message?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.clearMessage()
        }
    }

    // Edit name dialog
    if (showEditNameDialog) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog = false },
            title = {
                Text(
                    "Edit Display Name",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Display Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Bronze,
                        unfocusedBorderColor = if (isDark) DarkBorder else Border,
                        focusedLabelColor = Bronze
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateDisplayName(editedName.trim())
                        showEditNameDialog = false
                    },
                    enabled = editedName.isNotBlank() && editedName.trim() != uiState.userName.ifBlank { "" },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Bronze,
                        contentColor = if (isDark) DarkOnSurface else WarmWhite
                    )
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog = false }) {
                    Text("Cancel", color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Invite dialog
    if (showInviteDialog) {
        AlertDialog(
            onDismissRequest = { showInviteDialog = false },
            title = {
                Text(
                    "Invite a Family Member",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Column {
                    Text(
                        "Enter their email to share your books with them",
                        style = MaterialTheme.typography.bodyMedium,
                        color = mutedText
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = inviteEmail,
                        onValueChange = { inviteEmail = it },
                        label = { Text("Email address") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Bronze,
                            unfocusedBorderColor = if (isDark) DarkBorder else Border,
                            focusedLabelColor = Bronze
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showInviteDialog = false
                        inviteEmail = ""
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("We'll notify you when family sharing is ready.", duration = SnackbarDuration.Short)
                        }
                    },
                    enabled = inviteEmail.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Bronze,
                        contentColor = if (isDark) DarkOnSurface else WarmWhite
                    )
                ) {
                    Text("Send Invite")
                }
            },
            dismissButton = {
                TextButton(onClick = { showInviteDialog = false }) {
                    Text("Cancel", color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Profile",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
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
                            tint = backIconTint
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Profile avatar + name card — warm, inviting, premium
            val profileCardBg = if (isDark) DarkSurface else WarmWhite
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = profileCardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Papaya.copy(alpha = 0.3f),
                                    Cornsilk.copy(alpha = 0.1f)
                                )
                            )
                        )
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Avatar — larger, warmer, with subtle glow
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = if (uiState.userInitial != "?")
                                            listOf(Bronze, BronzeLight)
                                        else
                                            listOf(if (isDark) DarkBorder else Border, if (isDark) DarkDivider else Divider)
                                    ),
                                    shape = CircleShape
                                )
                                .then(
                                    if (uiState.userInitial != "?") {
                                        Modifier.background(
                                            color = Bronze.copy(alpha = 0.08f),
                                            shape = CircleShape
                                        )
                                    } else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = uiState.userInitial,
                                style = MaterialTheme.typography.displayLarge,
                                color = if (isDark) DarkOnSurface else WarmWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Name — warm, confident
                        Text(
                            text = uiState.userName.ifBlank { "Your Story" },
                            style = MaterialTheme.typography.headlineMedium,
                            color = primaryText,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )

                        if (uiState.userEmail.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = uiState.userEmail,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Personal info section
            SettingsSection(title = "Personal", cardBg = cardBg, isDark = isDark) {
                SettingsRow(
                    icon = Icons.Default.Person,
                    label = "Display Name",
                    value = uiState.userName.ifBlank { "Tap to set" },
                    showChevron = true,
                    onClick = {
                        editedName = uiState.userName.ifBlank { "" }
                        showEditNameDialog = true
                    },
                    bgColor = cardBg,
                    isDark = isDark
                )
                HorizontalDivider(color = if (isDark) DarkDivider else Divider, thickness = 0.5.dp)
                SettingsRow(
                    icon = Icons.Default.Email,
                    label = "Email",
                    value = uiState.userEmail.ifBlank { "Not set" },
                    onClick = { },
                    bgColor = cardBg,
                    isDark = isDark
                )
            }

            // Usage stats section
            SettingsSection(title = "Your Library", cardBg = cardBg, isDark = isDark) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(
                            value = "${uiState.booksCount}",
                            label = if (uiState.booksCount == 1) "Book" else "Books",
                            highlight = uiState.booksCount > 0,
                            isDark = isDark
                        )
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(if (isDark) DarkDivider else Divider)
                        )
                        StatItem(
                            value = "${uiState.memoriesCount}",
                            label = if (uiState.memoriesCount == 1) "Memory" else "Memories",
                            highlight = uiState.memoriesCount > 0,
                            isDark = isDark
                        )
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(40.dp)
                                .background(if (isDark) DarkDivider else Divider)
                        )
                        StatItem(
                            value = if (uiState.storageUsedBytes > 0) formatStorage(uiState.storageUsedBytes) else "Starter",
                            label = "Plan",
                            highlight = uiState.storageUsedBytes > 0,
                            isDark = isDark
                        )
                    }
                    if (uiState.userCreatedAt.isNotBlank()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = uiState.userCreatedAt,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    if (uiState.storageUsedBytes <= 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "You can upgrade anytime to unlock more space.",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            SettingsSection(title = "Family Sharing", cardBg = cardBg, isDark = isDark) {
                SettingsRow(
                    icon = Icons.Default.Book,
                    label = "Invite family member",
                    value = "Share books with family members",
                    onClick = { showInviteDialog = true },
                    bgColor = cardBg,
                    isDark = isDark
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StatItem(value: String, label: String, highlight: Boolean = false, isDark: Boolean) {
    val mutedTextColor = if (isDark) DarkOnSurfaceVariant else CharcoalMuted
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = if (highlight) if (isDark) DarkBronze else Bronze else mutedTextColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = mutedTextColor
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
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
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
private fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    showChevron: Boolean = false,
    onClick: () -> Unit = {},
    bgColor: androidx.compose.ui.graphics.Color = WarmWhite,
    isDark: Boolean
) {
    val iconBgColor = if (isDark) DarkSurfaceVariant else Bronze.copy(alpha = 0.1f)
    val primaryText = if (isDark) DarkOnSurface else Charcoal
    val mutedText = if (isDark) DarkOnSurfaceVariant else CharcoalMuted

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
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
                contentDescription = label,
                tint = Bronze,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = primaryText,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = mutedText
            )
        }

        if (showChevron) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "Edit",
                tint = mutedText,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

private fun formatStorage(bytes: Long): String {
    return when {
        bytes < 1024 -> "${bytes}B"
        bytes < 1024 * 1024 -> "${bytes / 1024}KB"
        bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)}MB"
        else -> "${bytes / (1024 * 1024 * 1024)}GB"
    }
}