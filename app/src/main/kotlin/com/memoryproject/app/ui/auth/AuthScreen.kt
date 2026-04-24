package com.memoryproject.app.ui.auth

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memoryproject.app.ui.theme.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    darkTheme: Boolean = false,
    viewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    val isDark = darkTheme
    val backgroundGradient = if (isDark) listOf(DarkBackground, DarkSurfaceVariant) else listOf(Cornsilk, Papaya.copy(alpha = 0.5f))
    val cardBg = if (isDark) DarkSurface else WarmWhite
    val cardBgBrush = if (isDark) Brush.linearGradient(listOf(DarkSurface, DarkSurfaceVariant)) else null
    val primaryText = if (isDark) DarkOnSurface else Charcoal
    val mutedText = if (isDark) DarkOnSurfaceVariant else CharcoalMuted

    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }

    // Real-time email format validation
    val emailError = remember(email) {
        when {
            email.isEmpty() -> null
            !email.contains("@") || !email.contains(".") -> "Please enter a valid email address"
            else -> null
        }
    }

    // Password strength hint (informational, not blocking)
    val passwordHint = remember(password, uiState.isSignUp) {
        if (!uiState.isSignUp || password.isEmpty()) null
        else when {
            password.length < 8 -> "At least 8 characters recommended"
            else -> null
        }
    }

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) onLoginSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = backgroundGradient
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Brand mark — subtle, premium
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Bronze, BronzeLight)
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📖",
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // App name — confident, not shouty
            Text(
                text = "Memory Project",
                style = MaterialTheme.typography.displayMedium,
                color = primaryText,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline — warm, inviting
            Text(
                text = "Preserve the moments that matter most.",
                style = MaterialTheme.typography.bodyLarge,
                color = mutedText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Mode toggle — clean, minimal (tabs actually switch modes)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = cardBg,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(4.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.toggleMode()
                            email = ""
                            password = ""
                        }),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(
                    "Sign In" to !uiState.isSignUp,
                    "Create Account" to uiState.isSignUp
                ).forEach { (label, isActive) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                color = if (isActive) Bronze else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isActive) WarmWhite else mutedText,
                            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Auth mode header
            Text(
                text = if (uiState.isSignUp) "Create your account" else "Welcome back",
                style = MaterialTheme.typography.headlineMedium,
                color = primaryText,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (uiState.isSignUp)
                    "Start capturing your memories today"
                else
                    "Sign in to continue your story",
                style = MaterialTheme.typography.bodyMedium,
                color = mutedText
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Email field with inline validation
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (uiState.error != null) viewModel.clearError()
                },
                label = { Text("Email address") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = mutedText
                    )
                },
                isError = emailError != null && email.isNotEmpty(),
                supportingText = {
                    if (emailError != null && email.isNotEmpty()) {
                        Text(emailError, color = ErrorRed)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Bronze,
                    unfocusedBorderColor = if (isDark) DarkBorder else Border,
                    focusedLabelColor = Bronze,
                    unfocusedLabelColor = mutedText,
                    cursorColor = Bronze,
                    errorBorderColor = ErrorRed,
                    errorLabelColor = ErrorRed
                ),
                textStyle = MaterialTheme.typography.bodyLarge
            )

            // Name field (sign up only)
            AnimatedVisibility(
                visible = uiState.isSignUp,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            if (uiState.error != null) viewModel.clearError()
                        },
                        label = { Text("Your name") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = mutedText
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down) }
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Bronze,
                            unfocusedBorderColor = if (isDark) DarkBorder else Border,
                            focusedLabelColor = Bronze,
                            unfocusedLabelColor = mutedText,
                            cursorColor = Bronze
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Password field with visibility toggle
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    if (uiState.error != null) viewModel.clearError()
                },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = mutedText
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { showPassword = !showPassword },
                        modifier = Modifier.size(48.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Icon(
                            imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (showPassword) "Hide password" else "Show password",
                            tint = mutedText,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                supportingText = {
                    if (passwordHint != null) {
                        Text(passwordHint, color = mutedText)
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        // Only submit if no email format error
                        val canSubmit = emailError == null && email.isNotBlank() && password.isNotBlank() && (!uiState.isSignUp || name.isNotBlank())
                        if (canSubmit) {
                            if (uiState.isSignUp) viewModel.signup(email, name, password)
                            else viewModel.login(email, password)
                        }
                    }
                ),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Bronze,
                    unfocusedBorderColor = if (isDark) DarkBorder else Border,
                    focusedLabelColor = Bronze,
                    unfocusedLabelColor = mutedText,
                    cursorColor = Bronze
                ),
                textStyle = MaterialTheme.typography.bodyLarge
            )

            // Error message (from ViewModel — server-side errors)
            AnimatedVisibility(
                visible = uiState.error != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = ErrorRed.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚠",
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = uiState.error ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ErrorRed
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Primary action button — large, confident, inviting
            val isFormValid = email.isNotBlank() && password.isNotBlank() &&
                emailError == null && (!uiState.isSignUp || name.isNotBlank())

            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (uiState.isSignUp) viewModel.signup(email, name, password)
                    else viewModel.login(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isLoading && isFormValid,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Bronze,
                    contentColor = if (isDark) DarkOnSurface else WarmWhite,
                    disabledContainerColor = Bronze.copy(alpha = 0.4f),
                    disabledContentColor = if (isDark) DarkOnSurfaceVariant else WarmWhite.copy(alpha = 0.6f)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 8.dp
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = WarmWhite,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = if (uiState.isSignUp) "Create Account" else "Sign In",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Forgot password link — shown only in sign-in mode
            AnimatedVisibility(visible = !uiState.isSignUp) {
                Column {
                    TextButton(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.showForgotPassword()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        Text(
                            text = "Forgot your password?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = mutedText
                        )
                    }
                    // Forgot password confirmation message
                    AnimatedVisibility(
                        visible = uiState.forgotPasswordMessage != null,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Bronze.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("\uD83D\uDCA1", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiState.forgotPasswordMessage ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = BronzeDark
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Toggle mode link
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (uiState.isSignUp) "Already have an account?" else "New to Memory Project?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = mutedText
                )
                TextButton(
                    onClick = {
                        viewModel.toggleMode()
                        email = ""
                        password = ""
                    },
                    contentPadding = PaddingValues(horizontal = 6.dp)
                ) {
                    Text(
                        text = if (uiState.isSignUp) "Sign in" else "Create account",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Bronze,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Footer trust message
            Text(
                text = "🔒 Your data is always private",
                style = MaterialTheme.typography.bodySmall,
                color = mutedText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
