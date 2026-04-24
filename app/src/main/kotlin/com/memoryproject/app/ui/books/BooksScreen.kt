package com.memoryproject.app.ui.books

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memoryproject.app.data.model.Book
import com.memoryproject.app.ui.theme.*
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksScreen(
    onBookClick: (Int) -> Unit,
    onLogout: () -> Unit,
    onSettings: () -> Unit,
    onProfile: () -> Unit,
    viewModel: BooksViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMenuForBookId by remember { mutableStateOf<Int?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Filter books by search query
    val filteredBooks = if (searchQuery.isBlank()) {
        uiState.books
    } else {
        uiState.books.filter { it.title.contains(searchQuery, ignoreCase = true) }
    }

    // Greeting — kept simple, no personalization to avoid shared-screen privacy concerns
    val greeting = "My Books"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "My Books",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Charcoal
                            )
                            if (uiState.userInitial.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(
                                            color = Bronze.copy(alpha = 0.15f),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = uiState.userInitial,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Bronze,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                        if (uiState.books.isNotEmpty()) {
                            Text(
                                "${uiState.books.size} ${if (uiState.books.size == 1) "book" else "books"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalMuted
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = onSettings
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = CharcoalMuted
                        )
                    }
                    IconButton(
                        onClick = onProfile
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = CharcoalMuted
                        )
                    }
                    IconButton(
                        onClick = { showLogoutDialog = true }
                    ) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = "Sign out",
                            tint = CharcoalMuted
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Cornsilk
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = Bronze,
                contentColor = WarmWhite,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "New Book",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        containerColor = Cornsilk
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading && uiState.books.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        repeat(4) { SkeletonCard() }
                    }
                }

                uiState.error != null && uiState.books.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "😕", fontSize = 56.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Couldn't load your books",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Charcoal
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.error!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = CharcoalMuted,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedButton(
                            onClick = { viewModel.loadBooks() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Bronze)
                        ) {
                            Text("Try Again")
                        }
                    }
                }

                uiState.books.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier.size(100.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Papaya),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(Papaya, Beige)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.MenuBook,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = Bronze
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(28.dp))
                        Text(
                            text = "Your library is empty",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Charcoal
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Every family has stories worth keeping. Start yours here.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = CharcoalMuted,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(28.dp))
                        Button(
                            onClick = { showCreateDialog = true },
                            modifier = Modifier.height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Bronze,
                                contentColor = WarmWhite
                            )
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
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
                    val context = LocalContext.current
                    val pullRefreshState = rememberPullRefreshState(
                        refreshing = uiState.isLoading,
                        onRefresh = { viewModel.loadBooks() }
                    )
                    val onShareBook: (Book) -> Unit = { book ->
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, book.title)
                            putExtra(Intent.EXTRA_TEXT, "${book.title}\n\nhttps://web-redrixvixs-projects.vercel.app/books/${book.id}")
                        }
                        context.startActivity(Intent.createChooser(intent, "Share via"))
                    }
                    val onOpenInBrowser: (Book) -> Unit = { book ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://web-redrixvixs-projects.vercel.app/books/${book.id}"))
                        context.startActivity(intent)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pullRefresh(pullRefreshState)
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Search bar
                            item {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 4.dp),
                                    placeholder = { Text("Search books...") },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Default.Search,
                                            contentDescription = null,
                                            tint = CharcoalMuted
                                        )
                                    },
                                    trailingIcon = {
                                        if (searchQuery.isNotBlank()) {
                                            IconButton(onClick = { searchQuery = "" }) {
                                                Icon(
                                                    Icons.Default.Clear,
                                                    contentDescription = "Clear search",
                                                    tint = CharcoalMuted
                                                )
                                            }
                                        }
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Bronze,
                                        unfocusedBorderColor = Border,
                                        focusedLabelColor = Bronze
                                    )
                                )
                            }

                            // Empty search result
                            if (filteredBooks.isEmpty() && searchQuery.isNotBlank()) {
                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 32.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "No books match \"$searchQuery\"",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = CharcoalMuted
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        TextButton(onClick = { searchQuery = "" }) {
                                            Text("Clear search", color = Bronze)
                                        }
                                    }
                                }
                            }

                            items(
                                items = filteredBooks,
                                key = { it.id }
                            ) { book ->
                                Box {
                                    BookCard(
                                        book = book,
                                        onClick = { onBookClick(book.id) },
                                        showMenu = showMenuForBookId == book.id,
                                        onMenuClick = { showMenuForBookId = book.id },
                                        onMenuDismiss = { showMenuForBookId = null },
                                        onShareClick = { onShareBook(book) },
                                        onOpenInBrowserClick = { onOpenInBrowser(book) }
                                    )
                                }
                            }
                        }
                        PullRefreshIndicator(
                            refreshing = uiState.isLoading,
                            state = pullRefreshState,
                            modifier = Modifier.align(Alignment.TopCenter),
                            contentColor = Bronze,
                            backgroundColor = Papaya
                        )
                    }
                }
            }
        }
    }

    // Create book dialog
    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = {
                showCreateDialog = false
                newTitle = ""
                newDescription = ""
            },
            icon = {
                Icon(
                    Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = Bronze,
                    modifier = Modifier.size(28.dp)
                )
            },
            title = {
                Text(
                    "Create a New Book",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        "Give your book a title — you can always change it later.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = CharcoalMuted
                    )
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it.take(80) },
                        label = { Text("Book Title") },
                        placeholder = { Text("e.g. Grandma's Stories") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Bronze,
                            unfocusedBorderColor = Border,
                            focusedLabelColor = Bronze
                        ),
                        supportingText = {
                            Text(
                                "${newTitle.length}/80",
                                color = if (newTitle.length > 60) Bronze else CharcoalMuted,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                    OutlinedTextField(
                        value = newDescription,
                        onValueChange = { newDescription = it },
                        label = { Text("Description (optional)") },
                        placeholder = { Text("What will this book hold?") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 2,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Bronze,
                            unfocusedBorderColor = Border,
                            focusedLabelColor = Bronze
                        ),
                        supportingText = {
                            Text(
                                "Optional — describe who's book this is",
                                color = CharcoalMuted,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.createBook(newTitle, newDescription)
                        showCreateDialog = false
                        newTitle = ""
                        newDescription = ""
                    },
                    enabled = newTitle.isNotBlank() && !uiState.isCreating,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Bronze,
                        contentColor = WarmWhite
                    )
                ) {
                    if (uiState.isCreating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = WarmWhite,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Creating...")
                    } else {
                        Text("Create Book")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showCreateDialog = false
                        newTitle = ""
                        newDescription = ""
                    }
                ) {
                    Text("Cancel", color = CharcoalMuted)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Logout confirmation dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    "Sign Out?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Text(
                    "Your books will still be here when you come back.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CharcoalMuted
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
                    Text("Sign Out", color = ErrorRed, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = CharcoalMuted)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
fun SkeletonCard() {
    val shimmerAlpha = remember { Animatable(0.3f) }

    LaunchedEffect(Unit) {
        while (true) {
            shimmerAlpha.animateTo(0.7f, animationSpec = tween(800, easing = FastOutSlowInEasing))
            shimmerAlpha.animateTo(0.3f, animationSpec = tween(800, easing = FastOutSlowInEasing))
        }
    }

    val shimmerBrush = remember(shimmerAlpha.value) {
        Brush.linearGradient(
            colors = listOf(
                Divider,
                Divider.copy(alpha = shimmerAlpha.value),
                Divider
            )
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(18.dp)
                    .background(shimmerBrush, RoundedCornerShape(4.dp))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(14.dp)
                    .background(shimmerBrush, RoundedCornerShape(4.dp))
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(12.dp)
                    .background(shimmerBrush, RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
fun BookCard(
    book: Book,
    onClick: () -> Unit,
    showMenu: Boolean = false,
    onMenuClick: () -> Unit = {},
    onMenuDismiss: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onOpenInBrowserClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 8.dp else 3.dp,
        animationSpec = androidx.compose.animation.core.spring(),
        label = "cardElevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(if (isPressed) 0.98f else 1f)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    if (showMenu) onMenuDismiss() else onClick()
                }
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book icon with warm accent
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Bronze.copy(alpha = 0.15f), Bronze.copy(alpha = 0.08f))
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Book,
                    contentDescription = null,
                    tint = Bronze,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Charcoal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!book.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = book.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = CharcoalMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Owner avatar (32dp circle with initial)
                            if (book.owner_name.isNotBlank()) {
                                val ownerInitial = book.owner_name.firstOrNull()?.uppercaseChar()?.toString() ?: ""
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(
                                            color = Papaya,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = ownerInitial,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Bronze,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = if (book.role != "owner") "Shared with you" else "Created by ${book.owner_name}",
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalMuted
                            )
                            if (book.memories_count != null) {
                                Text(
                                    text = "•",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CharcoalMuted
                                )
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = Bronze.copy(alpha = 0.12f),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 7.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "${book.memories_count} ${if (book.memories_count == 1) "memory" else "memories"}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Bronze,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            val bytes = book.storage_used_bytes.toLongOrNull() ?: 0L
                            if (bytes > 0) {
                                Text(text = "•", style = MaterialTheme.typography.bodySmall, color = CharcoalMuted)
                                Text(
                                    text = formatStorage(bytes),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CharcoalLight
                                )
                            }
                            if (book.updated_at != null) {
                                Text(text = "•", style = MaterialTheme.typography.bodySmall, color = CharcoalMuted)
                                Text(
                                    text = formatRelativeDate(book.updated_at),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = CharcoalLight
                                )
                            }
                        }
                    }
                }
            }

            // Accent pill for shared books
            if (book.role != "owner") {
                Box(
                    modifier = Modifier
                        .background(
                            color = Bronze.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Shared",
                        style = MaterialTheme.typography.labelSmall,
                        color = Bronze,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Storage indicator
            val storageBytes = book.storage_used_bytes.toLongOrNull() ?: 0
            if (storageBytes > 0) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .background(
                            color = TeaGreen.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = formatStorageSize(storageBytes),
                        style = MaterialTheme.typography.labelSmall,
                        color = TeaGreenDark,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Kebab menu
            Box {
                IconButton(onClick = onMenuClick, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "More options",
                        tint = CharcoalMuted,
                        modifier = Modifier.size(20.dp)
                    )
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = onMenuDismiss) {
                    DropdownMenuItem(
                        text = { Text("Open in browser", color = Charcoal) },
                        onClick = {
                            onMenuDismiss()
                            onOpenInBrowserClick()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.OpenInNew, contentDescription = null, tint = CharcoalMuted)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Share", color = Charcoal) },
                        onClick = {
                            onMenuDismiss()
                            onShareClick()
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Share, contentDescription = null, tint = CharcoalMuted)
                        }
                    )
                }
            }
        }
    }
}

private fun formatStorageSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "${bytes}B"
        bytes < 1024 * 1024 -> "${bytes / 1024}KB"
        bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)}MB"
        else -> "${bytes / (1024 * 1024 * 1024)}GB"
    }
}

private fun formatRelativeDate(isoDate: String): String {
    return try {
        val dateParts = isoDate.substringBefore("T").split("-")
        val year = dateParts[0].toInt()
        val month = dateParts[1].toInt()
        val day = dateParts[2].toInt()
        val now = java.util.Calendar.getInstance()
        val then = java.util.Calendar.getInstance().apply {
            set(year, month - 1, day)
        }
        val diffDays = ((now.timeInMillis - then.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
        when {
            diffDays == 0 -> "Today"
            diffDays == 1 -> "Yesterday"
            diffDays < 7 -> "$diffDays days ago"
            diffDays < 30 -> "${diffDays / 7} weeks ago"
            diffDays < 365 -> "${diffDays / 30} months ago"
            else -> "${diffDays / 365} years ago"
        }
    } catch (e: Exception) {
        isoDate.substringBefore("T")
    }
}