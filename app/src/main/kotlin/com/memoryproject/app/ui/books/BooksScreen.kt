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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullRefreshIndicator
import androidx.compose.material3.pulltorefresh.pullRefresh
import androidx.compose.material3.pulltorefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memoryproject.app.data.model.Book
import com.memoryproject.app.ui.theme.*
import org.koin.androidx.compose.koinViewModel

private enum class SortOption { NEWEST, OLDEST, ALPHABETICAL, MOST_MEMORIES }

@Composable
private fun SortMenu(selected: SortOption, onSelect: (SortOption) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        TextButton(
            onClick = { expanded = true },
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
            modifier = Modifier.height(24.dp)
        ) {
            Text(
                text = when (selected) {
                    SortOption.NEWEST -> "Newest"
                    SortOption.OLDEST -> "Oldest"
                    SortOption.ALPHABETICAL -> "A–Z"
                    SortOption.MOST_MEMORIES -> "Most memories"
                },
                style = MaterialTheme.typography.bodySmall,
                color = Bronze,
                fontWeight = FontWeight.Medium
            )
            Icon(
                Icons.Default.ExpandMore,
                contentDescription = null,
                tint = Bronze,
                modifier = Modifier.size(14.dp)
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Newest first", color = if (selected == SortOption.NEWEST) Bronze else Charcoal) },
                onClick = { onSelect(SortOption.NEWEST); expanded = false },
                leadingIcon = { if (selected == SortOption.NEWEST) Icon(Icons.Default.Check, null, tint = Bronze, modifier = Modifier.size(16.dp)) else null }
            )
            DropdownMenuItem(
                text = { Text("Oldest first", color = if (selected == SortOption.OLDEST) Bronze else Charcoal) },
                onClick = { onSelect(SortOption.OLDEST); expanded = false },
                leadingIcon = { if (selected == SortOption.OLDEST) Icon(Icons.Default.Check, null, tint = Bronze, modifier = Modifier.size(16.dp)) else null }
            )
            DropdownMenuItem(
                text = { Text("Alphabetical", color = if (selected == SortOption.ALPHABETICAL) Bronze else Charcoal) },
                onClick = { onSelect(SortOption.ALPHABETICAL); expanded = false },
                leadingIcon = { if (selected == SortOption.ALPHABETICAL) Icon(Icons.Default.Check, null, tint = Bronze, modifier = Modifier.size(16.dp)) else null }
            )
            DropdownMenuItem(
                text = { Text("Most memories", color = if (selected == SortOption.MOST_MEMORIES) Bronze else Charcoal) },
                onClick = { onSelect(SortOption.MOST_MEMORIES); expanded = false },
                leadingIcon = { if (selected == SortOption.MOST_MEMORIES) Icon(Icons.Default.Check, null, tint = Bronze, modifier = Modifier.size(16.dp)) else null }
            )
        }
    }
}

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

    var sortOption by remember { mutableStateOf(SortOption.NEWEST) }

    // Filter and sort books
    val filteredBooks = remember(sortOption, searchQuery, uiState.books) {
        if (searchQuery.isBlank()) {
            when (sortOption) {
                SortOption.NEWEST -> uiState.books.sortedByDescending { it.created_at }
                SortOption.OLDEST -> uiState.books.sortedBy { it.created_at }
                SortOption.ALPHABETICAL -> uiState.books.sortedBy { it.title.lowercase() }
                SortOption.MOST_MEMORIES -> uiState.books.sortedByDescending { it.memories_count ?: 0 }
            }
        } else {
            val filtered = uiState.books.filter { it.title.contains(searchQuery, ignoreCase = true) }
            when (sortOption) {
                SortOption.NEWEST -> filtered.sortedByDescending { it.created_at }
                SortOption.OLDEST -> filtered.sortedBy { it.created_at }
                SortOption.ALPHABETICAL -> filtered.sortedBy { it.title.lowercase() }
                SortOption.MOST_MEMORIES -> filtered.sortedByDescending { it.memories_count ?: 0 }
            }
        }
    }

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
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val displayCount = if (searchQuery.isNotBlank() && filteredBooks.size != uiState.books.size) {
                                "${filteredBooks.size} of ${uiState.books.size} books"
                            } else {
                                "${uiState.books.size} ${if (uiState.books.size == 1) "book" else "books"}"
                            }
                            Text(
                                displayCount,
                                style = MaterialTheme.typography.bodySmall,
                                color = CharcoalMuted
                            )
                            if (uiState.books.size > 1) {
                                SortMenu(
                                    selected = sortOption,
                                    onSelect = { sortOption = it }
                                )
                            }
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = CharcoalMuted)
                    }
                    IconButton(onClick = onProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = CharcoalMuted)
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(Icons.Default.Logout, contentDescription = "Sign out", tint = CharcoalMuted)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Cornsilk)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = Bronze,
                contentColor = WarmWhite,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp, pressedElevation = 12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("New Book", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
            }
        },
        containerColor = Cornsilk
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                uiState.isLoading && uiState.books.isEmpty() -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) { repeat(4) { SkeletonCard() } }
                }
                uiState.error != null && uiState.books.isEmpty() -> {
                    Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("😕", fontSize = 56.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Couldn't load your books", style = MaterialTheme.typography.headlineSmall, color = Charcoal)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(uiState.error!!, style = MaterialTheme.typography.bodyMedium, color = CharcoalMuted, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedButton(
                            onClick = { viewModel.loadBooks() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Bronze)
                        ) { Text("Try Again") }
                    }
                }
                uiState.books.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(horizontal = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier.size(100.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Papaya),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize().background(brush = Brush.radialGradient(colors = listOf(Papaya, Beige))),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(48.dp), tint = Bronze)
                            }
                        }
                        Spacer(modifier = Modifier.height(28.dp))
                        Text("Your library is empty", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold, color = Charcoal)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("Every family has stories worth keeping. Start yours here.", style = MaterialTheme.typography.bodyLarge, color = CharcoalMuted, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(28.dp))
                        Button(
                            onClick = { showCreateDialog = true },
                            modifier = Modifier.height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Bronze, contentColor = WarmWhite)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Create Your First Book", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                else -> {
                    val context = LocalContext.current
                    val pullRefreshState = rememberPullRefreshState(refreshing = uiState.isLoading, onRefresh = { viewModel.loadBooks() })
                    val onShareBook: (Book) -> Unit = { book ->
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, book.title)
                            putExtra(Intent.EXTRA_TEXT, "${book.title}\n\nhttps://web-redrixvixs-projects.vercel.app/books/${book.id}")
                        }
                        context.startActivity(Intent.createChooser(intent, "Share via"))
                    }
                    val onOpenInBrowser: (Book) -> Unit = { book ->
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://web-redrixvixs-projects.vercel.app/books/${book.id}")))
                    }
                    Box(modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
                        LazyColumn(
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            item {
                                OutlinedTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                                    placeholder = { Text("Search books...") },
                                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CharcoalMuted) },
                                    trailingIcon = {
                                        if (searchQuery.isNotBlank()) {
                                            IconButton(onClick = { searchQuery = "" }) {
                                                Icon(Icons.Default.Clear, contentDescription = "Clear search", tint = CharcoalMuted)
                                            }
                                        }
                                    },
                                    singleLine = true,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Bronze, unfocusedBorderColor = Border, focusedLabelColor = Bronze)
                                )
                            }
                            if (filteredBooks.isEmpty() && searchQuery.isNotBlank()) {
                                item {
                                    Column(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text("No books match \"$searchQuery\"", style = MaterialTheme.typography.bodyLarge, color = CharcoalMuted)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        TextButton(onClick = { searchQuery = "" }) { Text("Clear search", color = Bronze) }
                                    }
                                }
                            }
                            items(items = filteredBooks, key = { it.id }) { book ->
                                Box { BookCard(book = book, onClick = { onBookClick(book.id) }, showMenu = showMenuForBookId == book.id, onMenuClick = { showMenuForBookId = book.id }, onMenuDismiss = { showMenuForBookId = null }, onShareClick = { onShareBook(book) }, onOpenInBrowserClick = { onOpenInBrowser(book) }) }
                            }
                        }
                        PullRefreshIndicator(refreshing = uiState.isLoading, state = pullRefreshState, modifier = Modifier.align(Alignment.TopCenter), contentColor = Bronze, backgroundColor = Papaya)
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = { showCreateDialog = false; newTitle = ""; newDescription = "" },
            icon = { Icon(Icons.Default.MenuBook, contentDescription = null, tint = Bronze, modifier = Modifier.size(28.dp)) },
            title = { Text("Create a New Book", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Book title") },
                        placeholder = { Text("e.g., Grandma's Life Story") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Bronze, unfocusedBorderColor = Border, focusedLabelColor = Bronze)
                    )
                    OutlinedTextField(
                        value = newDescription,
                        onValueChange = { newDescription = it },
                        label = { Text("Description (optional)") },
                        placeholder = { Text("A short note about this book...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Bronze, unfocusedBorderColor = Border, focusedLabelColor = Bronze)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTitle.isNotBlank()) {
                            viewModel.createBook(newTitle.trim(), newDescription.trim())
                            showCreateDialog = false
                            newTitle = ""
                            newDescription = ""
                        }
                    },
                    enabled = newTitle.isNotBlank() && !uiState.isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Bronze, contentColor = WarmWhite)
                ) { Text("Create") }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false; newTitle = ""; newDescription = "" }) { Text("Cancel", color = CharcoalMuted) }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sign Out?", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold) },
            text = { Text("Your books will still be here when you come back.", style = MaterialTheme.typography.bodyMedium, color = CharcoalMuted) },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; viewModel.logout(); onLogout() }) { Text("Sign Out", color = ErrorRed, fontWeight = FontWeight.SemiBold) }
            },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel", color = CharcoalMuted) } },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun BookCard(
    book: Book,
    onClick: () -> Unit,
    showMenu: Boolean,
    onMenuClick: () -> Unit,
    onMenuDismiss: () -> Unit,
    onShareClick: () -> Unit,
    onOpenInBrowserClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.98f else 1f, animationSpec = spring(), label = "bookCardScale")

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().scale(scale),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPressed) 8.dp else 3.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(52.dp).background(brush = Brush.linearGradient(colors = listOf(Bronze, BronzeLight)), shape = RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) { Text("📖", fontSize = 24.sp) }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(book.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = Charcoal, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false))
                        // Shared pill — shows when user is a collaborator, not owner
                        if (book.role != "owner") {
                            Box(
                                modifier = Modifier.background(color = TeaGreen.copy(alpha = 0.3f), shape = RoundedCornerShape(6.dp)).padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text("Shared", style = MaterialTheme.typography.labelSmall, color = TeaGreenDark, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                    if (book.description.isNotBlank()) {
                        Text(book.description, style = MaterialTheme.typography.bodySmall, color = CharcoalMuted, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.padding(top = 2.dp))
                    }
                    // Show owner name for shared books
                    if (book.role != "owner" && book.owner_name.isNotBlank()) {
                        Text("Owned by ${book.owner_name}", style = MaterialTheme.typography.labelSmall, color = CharcoalMuted, modifier = Modifier.padding(top = 3.dp))
                    }
                }
                Box {
                    IconButton(onClick = onMenuClick, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options", tint = CharcoalMuted, modifier = Modifier.size(20.dp))
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = onMenuDismiss) {
                        DropdownMenuItem(text = { Text("Share") }, onClick = { onShareClick(); onMenuDismiss() }, leadingIcon = { Icon(Icons.Default.Share, null, tint = CharcoalMuted) })
                        DropdownMenuItem(text = { Text("Open in browser") }, onClick = { onOpenInBrowserClick(); onMenuDismiss() }, leadingIcon = { Icon(Icons.Default.OpenInNew, null, tint = CharcoalMuted) })
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                LabelChip(text = "${book.memories_count ?: 0} ${(book.memories_count ?: 0) == 1 ? "memory" else "memories"}", icon = "💬")
                LabelChip(text = formatDate(book.created_at), icon = "📅")
            }
        }
    }
}

@Composable
private fun LabelChip(text: String, icon: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(icon, fontSize = 12.sp)
        Text(text, style = MaterialTheme.typography.labelSmall, color = CharcoalMuted)
    }
}

private fun formatDate(isoDate: String): String {
    return try {
        val parts = isoDate.substringBefore("T").split("-")
        val months = listOf("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val month = parts[1].toInt()
        val day = parts[2].toInt()
        "${months[month]} $day, ${parts[0]}"
    } catch (e: Exception) { "" }
}

@Composable
private fun SkeletonCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(52.dp).background(Beige, RoundedCornerShape(14.dp)))
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.fillMaxWidth(0.6f).height(16.dp).background(Beige, RoundedCornerShape(4.dp)))
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth(0.4f).height(12.dp).background(Beige, RoundedCornerShape(4.dp)))
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Box(modifier = Modifier.width(80.dp).height(14.dp).background(Beige, RoundedCornerShape(4.dp)))
                    Box(modifier = Modifier.width(60.dp).height(14.dp).background(Beige, RoundedCornerShape(4.dp)))
                }
            }
        }
    }
}