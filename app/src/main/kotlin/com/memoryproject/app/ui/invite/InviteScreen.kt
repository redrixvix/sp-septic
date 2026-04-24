package com.memoryproject.app.ui.invite

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memoryproject.app.data.repository.MemoryRepository
import com.memoryproject.app.ui.theme.*
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

sealed class InviteUiState {
    data object Loading : InviteUiState()
    data class Preview(
        val bookId: Int,
        val bookTitle: String,
        val role: String,
        val inviteEmail: String
    ) : InviteUiState()
    data object Accepted : InviteUiState()
    data class Error(val message: String) : InviteUiState()
    data class LoggedOut(val message: String) : InviteUiState()
}

@Composable
fun InviteScreen(
    token: String,
    onAccepted: (Int) -> Unit,
    onLoginRequired: () -> Unit,
    repository: MemoryRepository = koinInject()
) {
    var uiState by remember { mutableStateOf<InviteUiState>(InviteUiState.Loading) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(token) {
        // First validate the invite token
        repository.getInviteInfo(token)
            .onSuccess { info ->
                uiState = InviteUiState.Preview(
                    bookId = info.book_id,
                    bookTitle = info.book_title,
                    role = info.role,
                    inviteEmail = info.invite_email
                )
            }
            .onFailure { e ->
                uiState = InviteUiState.Error(e.message ?: "This invite link is invalid or has expired.")
            }
    }

    val isDark = darkTheme
    val scaffoldBg = if (isDark) DarkBackground else Cornsilk
    val cardBg = if (isDark) DarkSurface else WarmWhite
    val primaryText = if (isDark) DarkOnSurface else Charcoal
    val mutedText = if (isDark) DarkOnSurfaceVariant else CharcoalMuted

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = if (isDark) listOf(DarkBackground, DarkSurfaceVariant) else listOf(Cornsilk, Papaya.copy(alpha = 0.5f))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Animated book icon
            var iconVisible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { iconVisible = true }
            AnimatedVisibility(
                visible = iconVisible,
                enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow), initialScale = 0.5f) + fadeIn()
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Bronze, BronzeLight)
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("📖", fontSize = 48.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            when (val state = uiState) {
                is InviteUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = Bronze,
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Checking invite...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = mutedText
                    )
                }

                is InviteUiState.Preview -> {
                    var accepted by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "You've been invited",
                            style = MaterialTheme.typography.headlineMedium,
                            color = primaryText,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Book info card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBg),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = if (isDark) listOf(DarkBronze.copy(alpha = 0.3f), DarkSurfaceVariant) else listOf(Bronze.copy(alpha = 0.15f), Papaya)
                                            ),
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("📚", fontSize = 28.sp)
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    state.bookTitle,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = primaryText,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = if (isDark) TeaGreen.copy(alpha = 0.2f) else TeaGreen.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            state.role.replaceFirstChar { it.uppercase() },
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (isDark) TeaGreen else Color(0xFF4A7A4A),
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Text(
                                        "access",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = mutedText
                                    )
                                }
                            }
                        }

                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = {
                            if (accepted) return@Button
                            accepted = true
                            scope.launch {
                                repository.acceptInvite(token)
                                    .onSuccess {
                                        uiState = InviteUiState.Accepted
                                        onAccepted(state.bookId)
                                    }
                                    .onFailure { e ->
                                        uiState = InviteUiState.Error(e.message ?: "Failed to accept invite.")
                                    }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !accepted,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Bronze,
                            contentColor = WarmWhite
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 4.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        if (accepted) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = WarmWhite,
                                strokeWidth = 2.5.dp
                            )
                        } else {
                            Text(
                                "Accept & Open Book",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "You'll need to sign in or create an account to accept.",
                        style = MaterialTheme.typography.bodySmall,
                        color = mutedText,
                        textAlign = TextAlign.Center
                    )
                }

                is InviteUiState.Accepted -> {
                    // Animated success
                    var showCheck by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) { showCheck = true }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedVisibility(
                            visible = showCheck,
                            enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)) + fadeIn()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(SuccessGreen, SuccessGreen.copy(alpha = 0.8f))
                                        ),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = WarmWhite,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            "You're in!",
                            style = MaterialTheme.typography.headlineMedium,
                            color = primaryText,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            "Welcome to the book. Your memories are waiting.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = mutedText,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is InviteUiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    color = ErrorRed.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(20.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("😕", fontSize = 40.sp)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            "Invite unavailable",
                            style = MaterialTheme.typography.headlineSmall,
                            color = primaryText,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            state.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = mutedText,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is InviteUiState.LoggedOut -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Sign in to accept",
                            style = MaterialTheme.typography.headlineSmall,
                            color = primaryText,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            state.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = mutedText,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = onLoginRequired,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Bronze,
                                contentColor = WarmWhite
                            )
                        ) {
                            Text("Sign In", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}