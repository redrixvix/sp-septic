package com.memoryproject.app.ui.auth

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.draw.shadow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.browser.customtabs.CustomTabsIntent
import com.memoryproject.app.ui.theme.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val isDark = isSystemInDarkTheme()
    val backgroundGradient = if (isDark) listOf(DarkBackground, DarkSurfaceVariant) else listOf(Cornsilk, Papaya.copy(alpha = 0.5f))
    val cardBg = if (isDark) DarkSurface else WarmWhite
    val primaryText = if (isDark) DarkOnSurface else Charcoal
    val mutedText = if (isDark) DarkOnSurfaceVariant else CharcoalMuted

    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var magicCode by remember { mutableStateOf("") }

    val emailError = remember(email) {
        when {
            email.isEmpty() -> null
            !email.contains("@") || !email.contains(".") -> "Please enter a valid email address"
            else -> null
        }
    }

    val passwordHint = remember(password, uiState.isSignUp) {
        if (!uiState.isSignUp || password.isEmpty()) null
        else when {
            password.length < 8 -> "At least 8 characters recommended"
            else -> null
        }
    }

    // Handle Google OAuth callback deep link
    LaunchedEffect(Unit) {
        val intent = (context as? android.app.Activity)?.intent
        intent?.data?.let { uri ->
            if (uri.scheme == "memoryproject" && uri.host == "auth" && uri.path == "/callback") {
                val session = uri.getQueryParameter("session")
                if (session != null) {
                    viewModel.onGoogleAuthComplete(session)
                }
            }
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
            Spacer(modifier = Modifier.height(64.dp))

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
                    text = "\uD83D\uDCDA",
                    fontSize = 32.sp
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

            when (uiState.authMode) {
                AuthMode.MAGIC_LINK_CODE -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { viewModel.resetToEmailPassword() },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = primaryText
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Enter code",
                            style = MaterialTheme.typography.headlineMedium,
                            color = primaryText,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "We sent a 6-digit code to ${uiState.magicEmail}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = mutedText
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    OutlinedTextField(
                        value = magicCode,
                        onValueChange = {
                            if (it.length <= 6 && it.all { c -> c.isDigit() }) {
                                magicCode = it
                                if (it.length == 6) {
                                    viewModel.verifyMagicCode(it)
                                }
                            }
                        },
                        label = { Text("6-digit code") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = mutedText
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                if (magicCode.length == 6) {
                                    viewModel.verifyMagicCode(magicCode)
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
                        typography = MaterialTheme.typography.bodyLarge
                    )

                    AnimatedVisibility(
                        visible = uiState.authMessage != null,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
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
                                    text = uiState.authMessage ?: "",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = BronzeDark
                                )
                            }
                        }
                    }

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
                                Text("⚠", fontSize = 14.sp)
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

                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.verifyMagicCode(magicCode)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !uiState.isLoading && magicCode.length == 6,
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
                                text = "Verify code",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = { viewModel.resetToEmailPassword() },
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        Text(
                            text = "Use a different sign-in method",
                            style = MaterialTheme.typography.bodyMedium,
                            color = mutedText
                        )
                    }
                }

                AuthMode.GOOGLE -> {
                    LaunchedEffect(uiState.googleAuthUrl) {
                        uiState.googleAuthUrl?.let { url ->
                            val customTabsIntent = CustomTabsIntent.Builder()
                                .setShowTitle(false)
                                .build()
                            customTabsIntent.launchUrl(context, Uri.parse(url))
                            viewModel.clearGoogleAuthUrl()
                        }
                    }

                    Text(
                        text = "Complete sign-in in the browser",
                        style = MaterialTheme.typography.headlineMedium,
                        color = primaryText,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "A browser window will open for Google sign-in",
                        style = MaterialTheme.typography.bodyMedium,
                        color = mutedText,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    if (uiState.isLoading || uiState.googleAuthLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = Bronze,
                            strokeWidth = 3.dp
                        )
                    }
                    if (uiState.error != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = uiState.error ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ErrorRed
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextButton(
                            onClick = { viewModel.resetToEmailPassword() },
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Use a different sign-in method",
                                style = MaterialTheme.typography.bodyMedium,
                                color = mutedText
                            )
                        }
                    }
                }

                AuthMode.EMAIL_PASSWORD -> {
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
                                    .shadow(
                                        elevation = if (isActive) 6.dp else 0.dp,
                                        shape = RoundedCornerShape(8.dp),
                                        ambientColor = Bronze.copy(alpha = 0.25f),
                                        spotColor = Bronze.copy(alpha = 0.2f)
                                    )
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
                        supportingText = if (emailError != null && email.isNotEmpty()) {
                            { Text(emailError, color = ErrorRed) }
                        } else null,
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
                        typography = MaterialTheme.typography.bodyLarge
                    )

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
                                typography = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

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
                        supportingText = if (passwordHint != null) {
                            { Text(passwordHint, color = mutedText) }
                        } else null,
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
                            unfocusedBorderColor = if (isDark) DarkBorder else Border,
                            focusedLabelColor = Bronze,
                            unfocusedLabelColor = mutedText,
                            cursorColor = Bronze
                        ),
                        typography = MaterialTheme.typography.bodyLarge
                    )

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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(if (isDark) DarkBorder else Border)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "or",
                            style = MaterialTheme.typography.bodyMedium,
                            color = mutedText
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                                .background(if (isDark) DarkBorder else Border)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.sendMagicLink(email)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Bronze
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.linearGradient(listOf(Bronze, Bronze))
                        )
                    ) {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Send Magic Link",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.startGoogleAuth()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = primaryText
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.linearGradient(
                                listOf(
                                    if (isDark) Color(0xFF5A5A5A) else Color(0xFFE0E0E0),
                                    if (isDark) Color(0xFF5A5A5A) else Color(0xFFE0E0E0)
                                )
                            )
                        )
                    ) {
                        GoogleIcon()
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Continue with Google",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

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
    }
}

@Composable
private fun GoogleIcon() {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "G",
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF4285F4),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}