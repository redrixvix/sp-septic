package com.memoryproject.app.ui.books
import androidx.compose.foundation.lazy.itemsIndexed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.material3.pullrefresh.PullRefreshState
import androidx.compose.material3.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.pullrefresh.pullRefresh
import androidx.compose.material3.pullrefresh.PullRefreshIndicator

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memoryproject.app.data.model.Book
import com.memoryproject.app.ui.theme.*
import org.koin.androidx.compose.koinViewModel

@Composable
fun BooksScreen(
    onBookClick: (Int) -> Unit,
    onLogout: () -> Unit,
    onSettings: () -> Unit,
    onProfile: () -> Unit,
    darkTheme: Boolean,
    viewModel: BooksViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showCreateDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var newBookTitle by remember { mutableStateOf("") }
    var newBookDescription by remember { mutableStateOf("") }

    val scaffoldBg = if (darkTheme) DarkBackground else Cornsilk
    val cardBg = if (darkTheme) DarkSurface else WarmWhite
    val primaryText = if (darkTheme) DarkOnSurface else Charcoal
    val mutedText = if (darkTheme) DarkOnSurfaceVariant else CharcoalMuted

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = { viewModel.loadBooks() }
    )

    // Show error as snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Long)
            viewModel.clearError()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(scaffoldBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top app bar
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "My Books",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = primaryText
                        )
                        if (uiState.books.isNotEmpty()) {
                            Text(
                                uiState.books.size.toString() + " " + (if (uiState.books.size == 1) "book" else "books"),
                                style = MaterialTheme.typography.bodySmall,
                                color = mutedText
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = onSettings,
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = cardBg,
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = primaryText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = scaffoldBg)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                when {
                    uiState.isLoading && uiState.books.isEmpty() -> {
                        // Skeleton loading
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            items(4) { _ ->
                                BooksSkeletonCard(darkTheme = darkTheme)
                            }
                        }
                    }

                    uiState.error != null && uiState.books.isEmpty() -> {
                        // Error state
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(
                                        color = ErrorRed.copy(alpha = 0.08f),
                                        shape = RoundedCornerShape(20.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("😕", fontSize = 40.sp)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                "Something went wrong",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryText
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                uiState.error!!,
                                style = MaterialTheme.typography.bodyMedium,
                                color = mutedText
                            )
                            Spacer(modifier = Modifier.height(28.dp))
                            Button(
                                onClick = { viewModel.loadBooks() },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Bronze,
                                    contentColor = WarmWhite
                                )
                            ) {
                                Text("Try Again", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    uiState.books.isEmpty() && !uiState.isLoading -> {
                        // Empty state — warm, inviting
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(96.dp)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = if (darkTheme) listOf(DarkSurfaceVariant, DarkSurface) else listOf(Papaya, Beige)
                                        ),
                                        shape = RoundedCornerShape(24.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("📖", fontSize = 48.sp)
                            }
                            Spacer(modifier = Modifier.height(28.dp))
                            Text(
                                "Your memories begin here",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryText,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "Create your first book to start preserving the moments that matter.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = mutedText,
                                textAlign = TextAlign.Center,
                                lineHeight = 24.sp
                            )
                            Spacer(modifier = Modifier.height(32.dp))
                            Button(
                                onClick = { showCreateDialog = true },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
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
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Create Your First Book",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.books) { book ->
                                BookCard(
                                    book = book,
                                    onClick = { onBookClick(book.id) },
                                    darkTheme = darkTheme,
                                    cardBg = cardBg,
                                    primaryText = primaryText,
                                    mutedText = mutedText
                                )
                            }
                        }
                    }
                }

                PullRefreshIndicator(
                    refreshing = uiState.isLoading,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter),
                    contentColor = Bronze,
                    backgroundColor = if (darkTheme) DarkSurfaceVariant else Papaya
                )
            }
        }

        // FAB — always visible when books exist
        if (uiState.books.isNotEmpty()) {
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 96.dp),
                containerColor = Bronze,
                contentColor = WarmWhite,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp, pressedElevation = 12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("New Book", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
        )
    }

    // Create Book Dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = {
                showCreateDialog = false
                newBookTitle = ""
                newBookDescription = ""
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                brush = Brush.linearGradient(listOf(Bronze, BronzeLight)),
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📖", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "New Book",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value = newBookTitle,
                        onValueChange = { newBookTitle = it },
                        label = { Text("Book title") },
                        placeholder = { Text("e.g. Grandma's Life Story") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Bronze,
                            unfocusedBorderColor = if (darkTheme) DarkBorder else Border,
                            focusedLabelColor = Bronze
                        )
                    )
                    OutlinedTextField(
                        value = newBookDescription,
                        onValueChange = { newBookDescription = it },
                        label = { Text("Description (optional)") },
                        placeholder = { Text("A short note about this book") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Bronze,
                            unfocusedBorderColor = if (darkTheme) DarkBorder else Border,
                            focusedLabelColor = Bronze
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newBookTitle.isNotBlank()) {
                            viewModel.createBook(newBookTitle, newBookDescription.takeIf { it.isNotBlank() })
                            showCreateDialog = false
                            newBookTitle = ""
                            newBookDescription = ""
                        }
                    },
                    enabled = newBookTitle.isNotBlank() && !uiState.isCreating,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Bronze, contentColor = WarmWhite)
                ) {
                    if (uiState.isCreating) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = WarmWhite, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCreateDialog = false
                        newBookTitle = ""
                        newBookDescription = ""
                    }
                ) {
                    Text("Cancel", color = mutedText)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Logout Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Sign out",
                    tint = CharcoalMuted,
                    modifier = Modifier.size(24.dp)
                )
            },
            title = {
                Text(
                    "Sign out?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
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
                Button(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout()
                        onLogout()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorRed,
                        contentColor = WarmWhite
                    )
                ) {
                    Text("Sign Out", fontWeight = FontWeight.SemiBold)
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
private fun BookCard(
    book: Book,
    onClick: () -> Unit,
    darkTheme: Boolean,
    cardBg: Color,
    primaryText: Color,
    mutedText: Color
) {
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 6.dp else 2.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "bookCardElevation"
    )
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "bookCardScale"
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .scale(cardScale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book icon in accent box
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = if (darkTheme) listOf(DarkBronze.copy(alpha = 0.25f), DarkSurfaceVariant) else listOf(Bronze.copy(alpha = 0.15f), Papaya)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("📖", fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = primaryText,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (book.role != "owner") {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (darkTheme) TeaGreen.copy(alpha = 0.2f) else TeaGreen.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Shared",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (darkTheme) TeaGreen else Color(0xFF4A7A4A),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                if (!book.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = book.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = mutedText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = (book.memories_count ?: 0).toString() + " " + (if ((book.memories_count ?: 0) == 1) "memory" else "memories"),
                        style = MaterialTheme.typography.bodySmall,
                        color = mutedText
                    )
                    if (book.owner_name.isNotBlank() && book.role != "owner") {
                        Text(
                            text = "·",
                            style = MaterialTheme.typography.bodySmall,
                            color = mutedText
                        )
                        Text(
                            text = book.owner_name,
                            style = MaterialTheme.typography.bodySmall,
                            color = mutedText
                        )
                    }
                    Text(
                        text = "·",
                        style = MaterialTheme.typography.bodySmall,
                        color = mutedText
                    )
                    Text(
                        text = formatDate(book.updated_at ?: book.created_at),
                        style = MaterialTheme.typography.bodySmall,
                        color = mutedText
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text("→", style = MaterialTheme.typography.titleMedium, color = mutedText)
        }
    }
}

@Composable
private fun BooksSkeletonCard(darkTheme: Boolean) {
    val shimmerBase = if (darkTheme) DarkDivider else Divider
    val shimmerHighlight = if (darkTheme) DarkOnSurface.copy(alpha = 0.06f) else Divider.copy(alpha = 0.5f)

    var shimmerAlpha by remember { mutableFloatStateOf(0.3f) }
    LaunchedEffect(Unit) {
        shimmerAlpha = 0.7f
        delay(800)
        shimmerAlpha = 0.3f
    }
    val shimmerBrush = remember(shimmerAlpha, shimmerBase, shimmerHighlight) {
        Brush.linearGradient(listOf(shimmerBase, shimmerHighlight.copy(alpha = shimmerAlpha), shimmerBase))
    }

    val cardBg = if (darkTheme) DarkSurface else WarmWhite

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(52.dp).background(shimmerBrush, RoundedCornerShape(14.dp)))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.fillMaxWidth(0.6f).height(16.dp).background(shimmerBrush, RoundedCornerShape(4.dp)))
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth(0.35f).height(12.dp).background(shimmerBrush, RoundedCornerShape(4.dp)))
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth(0.5f).height(12.dp).background(shimmerBrush, RoundedCornerShape(4.dp)))
            }
        }
    }
}

private fun formatDate(isoDate: String): String {
    return try {
        val parts = isoDate.substringBefore("T").split("-")
        if (parts.size < 3) return isoDate
        val months = listOf("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val month = parts[1].toInt()
        val day = parts[2].toInt()
        val year = parts[0].toInt()
        if (month < 1 || month > 12) return isoDate
        "${months[month]} $day, $year"
    } catch (e: Exception) { isoDate }
}

@Composable
private fun ShimmerCreateBookButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shimmerAlpha = remember { Animatable(0.1f) }
    LaunchedEffect(Unit) {
        while (true) {
            shimmerAlpha.animateTo(0.2f, animationSpec = tween(1000, easing = FastOutSlowInEasing))
            shimmerAlpha.animateTo(0.1f, animationSpec = tween(1000, easing = FastOutSlowInEasing))
        }
    }
    val shimmerColor = Bronze.copy(alpha = shimmerAlpha.value)
    
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Bronze,
            contentColor = WarmWhite
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Your First Book")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create Your First Book", fontWeight = FontWeight.SemiBold)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(shimmerColor)
                    .align(Alignment.Center)
            )
        }
    }
}