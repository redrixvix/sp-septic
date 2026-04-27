package com.memoryproject.app.ui.auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.memoryproject.app.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import com.memoryproject.app.R
import androidx.compose.foundation.Image

@Composable
private fun GoogleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "googleBtnScale"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .scale(scale),
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFF3c4043),
            disabledContainerColor = Color.White,
            disabledContentColor = Color(0xFF3c4043),
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp
        ),
        interactionSource = interactionSource
    ) {
        // Official Google "G" logo
        Image(
            painter = painterResource(id = R.drawable.ic_google_g),
            contentDescription = "Google logo",
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "Continue with Google",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    googleCallbackUri: String? = null,
    darkTheme: Boolean = false,
    viewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val isDark = darkTheme
    val backgroundGradient = if (isDark) listOf(DarkBackground, DarkSurfaceVariant) else listOf(Cornsilk, Papaya.copy(alpha = 0.35f), Cornsilk)
    val cardBg = if (isDark) DarkSurface else WarmWhite
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

    // No pending callback — native Credential Manager handles the entire sign-in flow

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
        // Subtle warm radial glow behind content — premium depth
        if (!isDark) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)
                    .align(Alignment.TopCenter)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Papaya.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
                .navigationBarsPadding()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Brand mark
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
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    tint = WarmWhite,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Memory Project",
                style = MaterialTheme.typography.displayMedium,
                color = primaryText,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Preserve the moments that matter most.",
                style = MaterialTheme.typography.bodyLarge,
                color = mutedText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Mode toggle
            val isSignUp = uiState.isSignUp
            val indicatorOffset by animateFloatAsState(
                targetValue = if (isSignUp) 1f else 0f,
                animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessMedium),
                label = "pillPosition"
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(
                        color = cardBg,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.5f)
                        .padding(vertical = 2.dp)
                        .graphicsLayer {
                            val totalWidth = this.size.width
                            val pillWidth = totalWidth / 2f
                            translationX = indicatorOffset * (totalWidth - pillWidth)
                        }
                        .shadow(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(8.dp),
                            ambientColor = Bronze.copy(alpha = 0.3f),
                            spotColor = Bronze.copy(alpha = 0.2f)
                        )
                        .background(
                            color = Bronze,
                            shape = RoundedCornerShape(8.dp)
                        )
                )
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(
                        "Sign In" to !isSignUp,
                        "Create Account" to isSignUp
                    ).forEach { (label, isActive) ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = {
                                        focusManager.clearFocus()
                                        viewModel.toggleMode()
                                        email = ""
                                        password = ""
                                    }
                                ),
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
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Auth mode header
            AnimatedContent(
                targetState = uiState.isSignUp,
                transitionSpec = {
                    (slideInHorizontally(animationSpec = tween(300)) { it / 2 } + fadeIn(animationSpec = tween(300)))
                        .togetherWith(slideOutHorizontally(animationSpec = tween(300)) { -it / 2 } + fadeOut(animationSpec = tween(300)))
                },
                label = "authHeaderTransition"
            ) { isSignUpNow ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isSignUpNow) "Create your account" else "Welcome back",
                        style = MaterialTheme.typography.headlineMedium,
                        color = primaryText,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isSignUpNow)
                            "Start preserving your family stories"
                        else
                            "Sign in to continue your journey",
                        style = MaterialTheme.typography.bodyMedium,
                        color = mutedText
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Google sign-in button — always visible at top of form
            GoogleButton(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.initiateGoogleLogin()
                },
                enabled = !uiState.isLoading && !uiState.isGoogleLoading
            )

            // Google loading state
            if (uiState.isGoogleLoading) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = Bronze
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Opening Google sign-in...",
                        style = MaterialTheme.typography.bodySmall,
                        color = mutedText
                    )
                }
            }

            // Divider with "or"
            if (!uiState.isGoogleLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.5.dp)
                            .background(if (isDark) DarkBorder else Border)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "or",
                        style = MaterialTheme.typography.bodySmall,
                        color = mutedText
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.5.dp)
                            .background(if (isDark) DarkBorder else Border)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            val emailBorderColor by animateColorAsState(
                targetValue = when {
                    emailError != null && email.isNotEmpty() -> ErrorRed
                    email.isNotEmpty() && email.contains("@") && email.contains(".") -> SuccessGreen
                    else -> if (isDark) DarkBorder else Border
                },
                animationSpec = tween(300),
                label = "emailBorder"
            )
            val nameBorderColor by animateColorAsState(
                targetValue = if (isDark) DarkBorder else Border,
                animationSpec = tween(300),
                label = "nameBorder"
            )
            val passwordBorderColor by animateColorAsState(
                targetValue = if (isDark) DarkBorder else Border,
                animationSpec = tween(300),
                label = "passwordBorder"
            )

            // Email field
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
                        tint = mutedText,
                        modifier = Modifier.size(20.dp)
                    )
                },
                isError = emailError != null && email.isNotEmpty(),
                supportingText = {
                    when {
                        emailError != null && email.isNotEmpty() -> Text(emailError, color = if (isDark) DarkError else ErrorRed)
                        email.isNotEmpty() && email.contains("@") && email.contains(".") -> Text("Looks good!", color = if (isDark) DarkSuccess else SuccessGreen)
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
                    unfocusedBorderColor = emailBorderColor,
                    focusedLabelColor = Bronze,
                    unfocusedLabelColor = mutedText,
                    cursorColor = Bronze,
                    errorBorderColor = ErrorRed,
                    errorLabelColor = ErrorRed
                ),
                textStyle = MaterialTheme.typography.bodyLarge,
                enabled = !uiState.isGoogleLoading
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
                            unfocusedBorderColor = nameBorderColor,
                            focusedLabelColor = Bronze,
                            unfocusedLabelColor = mutedText,
                            cursorColor = Bronze
                        ),
                        textStyle = MaterialTheme.typography.bodyLarge,
                        enabled = !uiState.isGoogleLoading
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Password field
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
                        tint = mutedText,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (showPassword) "Hide password" else "Show password",
                            tint = mutedText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                isError = password.isNotEmpty() && uiState.error != null,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
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
                    unfocusedBorderColor = passwordBorderColor,
                    focusedLabelColor = Bronze,
                    unfocusedLabelColor = mutedText,
                    cursorColor = Bronze,
                    errorBorderColor = ErrorRed,
                    errorLabelColor = ErrorRed
                ),
                textStyle = MaterialTheme.typography.bodyLarge,
                enabled = !uiState.isGoogleLoading
            )

            // Error message
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
                                color = if (isDark) DarkError.copy(alpha = 0.12f) else ErrorRed.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "\u26A0",
                            fontSize = 14.sp,
                            color = if (isDark) DarkOnSurface else Charcoal
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = uiState.error ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isDark) DarkOnSurface else ErrorRed
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Primary action button
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
                enabled = !uiState.isLoading && !uiState.isGoogleLoading && isFormValid,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDark) DarkBronze.copy(alpha = 0.95f) else BronzeDark,
                    contentColor = WarmWhite,
                    disabledContainerColor = if (isDark) DarkBronze.copy(alpha = 0.5f) else Color(0xFFD4A373).copy(alpha = 0.65f),
                    disabledContentColor = if (isDark) DarkOnSurface.copy(alpha = 0.7f) else Charcoal.copy(alpha = 0.65f)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = if (isDark) 2.dp else 8.dp,
                    pressedElevation = if (isDark) 4.dp else 12.dp,
                    disabledElevation = 0.dp
                )
            ) {
                if (uiState.isLoading) {
                    val infiniteTransition = rememberInfiniteTransition(label = "spinnerPulse")
                    val spinnerScale by infiniteTransition.animateFloat(
                        initialValue = 0.85f,
                        targetValue = 1.1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(600, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "spinnerScale"
                    )
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(22.dp)
                            .scale(spinnerScale),
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

            Spacer(modifier = Modifier.height(16.dp))

            // Forgot password link
            AnimatedVisibility(visible = !uiState.isSignUp) {
                Column {
                    TextButton(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.showForgotPassword()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 4.dp),
                        enabled = !uiState.isGoogleLoading
                    ) {
                        Text(
                            text = "Forgot your password?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = mutedText
                        )
                    }
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

            Spacer(modifier = Modifier.height(8.dp))

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
                    contentPadding = PaddingValues(horizontal = 6.dp),
                    enabled = !uiState.isGoogleLoading
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
                text = "\uD83D\uDD12 Your data is always private",
                style = MaterialTheme.typography.bodySmall,
                color = mutedText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

private fun launchCustomTab(context: Context, uri: Uri) {
    try {
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        customTabsIntent.launchUrl(context, uri)
    } catch (e: Exception) {
        // Fallback: open in browser if Custom Tab fails
        val browserIntent = Intent(Intent.ACTION_VIEW, uri).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(browserIntent)
    }
}
