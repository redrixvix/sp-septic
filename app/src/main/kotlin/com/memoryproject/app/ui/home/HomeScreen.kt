package com.memoryproject.app.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullRefreshIndicator
import androidx.compose.material3.pulltorefresh.pullRefresh
import androidx.compose.material3.pulltorefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memoryproject.app.data.model.Book
import com.memoryproject.app.data.model.Memory
import com.memoryproject.app.ui.theme.*
import org.koin.androidx.compose.koinViewModel

private val HOME_PROMPTS = listOf(
    "What made you laugh today?",
    "Describe your favorite meal growing up",
    "Tell me about a place that feels like home",
    "What's a skill you're proud of?",
    "Describe your best friend from childhood",
    "What holiday brings back the best memories?"
)

@Composable
fun HomeScreen(
    onNavigateToBooks: () -> Unit,
    onNavigateToBook: (Int) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToAddMemory: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val darkTheme = isSystemInDarkTheme()
    val scaffoldBg = if (darkTheme) DarkBackground else Cornsilk
    val primaryText = if (darkTheme) DarkOnSurface else Charcoal
    val mutedText = if (darkTheme) DarkOnSurfaceVariant else CharcoalMuted
    val cardBg = if (darkTheme) DarkSurface else WarmWhite

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = { viewModel.refresh() }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(scaffoldBg)
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Welcome header — always first
            item(key = "welcome_header") {
                WelcomeHeader(
                    userName = uiState.userName,
                    userInitial = uiState.userInitial,
                    isLoading = uiState.isLoading,
                    primaryText = primaryText,
                    mutedText = mutedText,
                    cardBg = cardBg,
                    darkTheme = darkTheme
                )
            }

            // Quick stats — appears quickly, no skeleton needed
            if (!uiState.isLoading || uiState.booksCount > 0 || uiState.memoriesCount > 0) {
                item(key = "quick_stats") {
                    QuickStats(
                        booksCount = uiState.booksCount,
                        memoriesCount = uiState.memoriesCount,
                        darkTheme = darkTheme,
                        cardBg = cardBg,
                        primaryText = primaryText,
                        mutedText = mutedText,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // Quick action buttons
            item(key = "quick_actions") {
                QuickActions(
                    onAddMemory = onNavigateToAddMemory,
                    onBrowseBooks = onNavigateToBooks,
                    darkTheme = darkTheme,
                    cardBg = cardBg,
                    primaryText = primaryText,
                    mutedText = mutedText,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Memory prompts section (only when user has books)
            if (uiState.booksCount > 0) {
                item(key = "memory_prompts") {
                    MemoryPromptsSection(
                        books = uiState.books,
                        darkTheme = darkTheme,
                        cardBg = cardBg,
                        primaryText = primaryText,
                        mutedText = mutedText,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        onPromptSelect = { /* prompt selected — navigates via onAddMemory */ }
                    )
                }
            }

            // Empty home — encouragement when no books yet
            if (uiState.booksCount == 0 && !uiState.isLoading) {
                item(key = "empty_home") {
                    EmptyHomeCard(
                        darkTheme = darkTheme,
                        cardBg = cardBg,
                        primaryText = primaryText,
                        mutedText = mutedText,
                        onAddMemory = onNavigateToAddMemory,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // Recent memories section
            if (uiState.recentMemories.isNotEmpty()) {
                item(key = "recent_header") {
                    SectionHeader(
                        title = "Recent Memories",
                        subtitle = "Stories from your collection",
                        darkTheme = darkTheme,
                        mutedText = mutedText,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }

                itemsIndexed(
                    items = uiState.recentMemories.take(5),
                    key = { _, memory -> "memory_${memory.id}" }
                ) { index, memory ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(memory.id) {
                        visible = true
                    }
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(400, delayMillis = index * 60)) +
                                slideInVertically(animationSpec = tween(400, delayMillis = index * 60), initialOffsetY = { it / 4 })
                    ) {
                        RecentMemoryCard(
                            memory = memory,
                            onClick = { onNavigateToBook(memory.book_id) },
                            accentIndex = index,
                            darkTheme = darkTheme,
                            cardBg = cardBg,
                            primaryText = primaryText,
                            mutedText = mutedText
                        )
                    }
                }

                if (uiState.recentMemories.size > 5) {
                    item(key = "view_all_memories") {
                        TextButton(
                            onClick = onNavigateToBooks,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                "View all memories →",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (darkTheme) DarkBronze else Bronze,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Books preview when user has books but no recent memories
            if (uiState.booksCount > 0 && uiState.recentMemories.isEmpty() && !uiState.isLoading) {
                item(key = "books_header") {
                    SectionHeader(
                        title = "Your Books",
                        subtitle = "Capture more memories",
                        darkTheme = darkTheme,
                        mutedText = mutedText,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }

                itemsIndexed(
                    items = uiState.books.take(3),
                    key = { _, book -> "book_${book.id}" }
                ) { index, book ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(book.id) {
                        visible = true
                    }
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(350, delayMillis = index * 80)) +
                                slideInVertically(animationSpec = tween(350, delayMillis = index * 80), initialOffsetY = { it / 4 })
                    ) {
                        BookMiniCard(
                            book = book,
                            onClick = { onNavigateToBook(book.id) },
                            darkTheme = darkTheme,
                            cardBg = cardBg,
                            primaryText = primaryText,
                            mutedText = mutedText
                        )
                    }
                }

                if (uiState.booksCount > 3) {
                    item(key = "see_all_books") {
                        TextButton(
                            onClick = onNavigateToBooks,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                "See all ${uiState.booksCount} books →",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (darkTheme) DarkBronze else Bronze,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Loading skeletons — only show on cold start (no books, no recent memories)
            if (uiState.isLoading && uiState.recentMemories.isEmpty() && uiState.booksCount == 0) {
                item(key = "skeletons") {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        repeat(3) { index ->
                            var visible by remember { mutableStateOf(false) }
                            LaunchedEffect(index) {
                                visible = true
                            }
                            AnimatedVisibility(
                                visible = visible,
                                enter = fadeIn(animationSpec = tween(300, delayMillis = index * 100))
                            ) {
                                HomeSkeletonCard(darkTheme = darkTheme)
                            }
                        }
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

@Composable
private fun WelcomeHeader(
    userName: String,
    userInitial: String,
    isLoading: Boolean,
    primaryText: Color,
    mutedText: Color,
    cardBg: Color,
    darkTheme: Boolean
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(500)) + slideInVertically(animationSpec = tween(500), initialOffsetY = { -it / 6 })
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = if (darkTheme) {
                                listOf(DarkSurfaceVariant.copy(alpha = 0.5f), DarkSurface.copy(alpha = 0.3f))
                            } else {
                                listOf(Papaya.copy(alpha = 0.35f), Cornsilk.copy(alpha = 0.1f))
                            }
                        )
                    )
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        if (isLoading && userName.isBlank()) {
                            Box(
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(26.dp)
                                    .background(
                                        color = if (darkTheme) DarkDivider else Divider,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(16.dp)
                                    .background(
                                        color = if (darkTheme) DarkDivider else Divider,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                            )
                        } else {
                            val displayName = if (userName.isNotBlank()) userName.split(" ").first() else null
                            val greeting = rememberGreeting()
                            Text(
                                text = if (displayName != null) "$greeting, $displayName" else greeting,
                                style = MaterialTheme.typography.headlineSmall,
                                color = primaryText,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (displayName != null) {
                                    "Ready to capture another moment?"
                                } else {
                                    "Welcome to Memory Project"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = mutedText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Avatar — animated entrance
                    var avatarVisible by remember { mutableStateOf(false) }
                    LaunchedEffect(userInitial) { avatarVisible = true }
                    AnimatedVisibility(
                        visible = avatarVisible,
                        enter = scaleIn(animationSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = 0.7f), initialScale = 0.6f) + fadeIn()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = if (userInitial.isNotBlank()) {
                                            listOf(Bronze, BronzeLight)
                                        } else {
                                            listOf(if (darkTheme) DarkBorder else Border, if (darkTheme) DarkDivider else Divider)
                                        }
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userInitial.ifBlank { "?" },
                                style = MaterialTheme.typography.titleLarge,
                                color = if (darkTheme) DarkOnSurface else WarmWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberGreeting(): String {
    return remember {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        when {
            hour < 12 -> "Good morning"
            hour < 17 -> "Good afternoon"
            else -> "Good evening"
        }
    }
}

@Composable
private fun QuickStats(
    booksCount: Int,
    memoriesCount: Int,
    darkTheme: Boolean,
    cardBg: Color,
    primaryText: Color,
    mutedText: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            icon = "📚",
            value = booksCount.toString(),
            label = if (booksCount == 1) "Book" else "Books",
            accentColor = Bronze,
            darkTheme = darkTheme,
            cardBg = cardBg,
            primaryText = primaryText,
            mutedText = mutedText,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            icon = "✨",
            value = memoriesCount.toString(),
            label = if (memoriesCount == 1) "Memory" else "Memories",
            accentColor = if (darkTheme) DarkBronze else CardAccentTea,
            darkTheme = darkTheme,
            cardBg = cardBg,
            primaryText = primaryText,
            mutedText = mutedText,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCard(
    icon: String,
    value: String,
    label: String,
    accentColor: Color,
    darkTheme: Boolean,
    cardBg: Color,
    primaryText: Color,
    mutedText: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = if (darkTheme) DarkSurfaceVariant else Papaya.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 22.sp)
            }
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = mutedText
                )
            }
        }
    }
}

@Composable
private fun QuickActions(
    onAddMemory: () -> Unit,
    onBrowseBooks: () -> Unit,
    darkTheme: Boolean,
    cardBg: Color,
    primaryText: Color,
    mutedText: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionButton(
            icon = "✏️",
            label = "Add Memory",
            onClick = onAddMemory,
            isPrimary = true,
            darkTheme = darkTheme,
            cardBg = cardBg,
            primaryText = primaryText,
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
            icon = "📖",
            label = "Browse Books",
            onClick = onBrowseBooks,
            isPrimary = false,
            darkTheme = darkTheme,
            cardBg = cardBg,
            primaryText = primaryText,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickActionButton(
    icon: String,
    label: String,
    onClick: () -> Unit,
    isPrimary: Boolean,
    darkTheme: Boolean,
    cardBg: Color,
    primaryText: Color,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPrimary) Bronze else cardBg,
            contentColor = if (isPrimary) WarmWhite else primaryText
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPrimary) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(icon, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (isPrimary) WarmWhite else primaryText,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun EmptyHomeCard(
    darkTheme: Boolean,
    cardBg: Color,
    primaryText: Color,
    mutedText: Color,
    onAddMemory: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(500, delayMillis = 200)) + slideInVertically(animationSpec = tween(500, delayMillis = 200), initialOffsetY = { it / 6 })
    ) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Warm illustrated icon
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = if (darkTheme) listOf(DarkSurfaceVariant, DarkSurface) else listOf(Papaya, Beige)
                            ),
                            shape = RoundedCornerShape(22.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🌟", fontSize = 44.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Your story starts here",
                    style = MaterialTheme.typography.headlineSmall,
                    color = primaryText,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Every family has stories worth preserving. Capture your first memory and begin building something precious.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = mutedText,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = onAddMemory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Bronze,
                        contentColor = WarmWhite
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Add Your First Memory",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String,
    darkTheme: Boolean,
    mutedText: Color,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = if (darkTheme) DarkOnSurface else Charcoal,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = mutedText
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun RecentMemoryCard(
    memory: Memory,
    onClick: () -> Unit,
    accentIndex: Int,
    darkTheme: Boolean,
    cardBg: Color,
    primaryText: Color,
    mutedText: Color
) {
    val accentColors = listOf(CardAccentBronze, CardAccentTea, CardAccentPapaya)
    val accentColor = accentColors[accentIndex % accentColors.size]
    val promptLabelBg = if (darkTheme) DarkSurfaceVariant else Papaya.copy(alpha = 0.8f)
    val promptLabelText = if (darkTheme) DarkOnSurface else BronzeDark

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        color = accentColor.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp)
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(color = promptLabelBg, shape = RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = memory.prompt_question,
                        style = MaterialTheme.typography.labelMedium,
                        color = promptLabelText,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = memory.answer_text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = primaryText,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatDate(memory.created_at),
                        style = MaterialTheme.typography.bodySmall,
                        color = mutedText
                    )
                    if (memory.photo_urls.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📷", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${memory.photo_urls.size}",
                                style = MaterialTheme.typography.labelSmall,
                                color = mutedText
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookMiniCard(
    book: Book,
    onClick: () -> Unit,
    darkTheme: Boolean,
    cardBg: Color,
    primaryText: Color,
    mutedText: Color
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = if (darkTheme) listOf(DarkBronze, DarkBronzeLight) else listOf(Bronze, BronzeLight)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("📖", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = primaryText,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${book.memories_count ?: 0} ${(book.memories_count ?: 0) == 1 ? "memory" else "memories"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = mutedText
                )
            }
            Text(
                "→",
                style = MaterialTheme.typography.titleMedium,
                color = mutedText
            )
        }
    }
}

@Composable
private fun HomeSkeletonCard(darkTheme: Boolean) {
    val shimmerBase = if (darkTheme) DarkDivider else Divider
    val shimmerHighlight = if (darkTheme) DarkOnSurface.copy(alpha = 0.06f) else Divider.copy(alpha = 0.5f)

    val shimmerAlpha = remember { Animatable(0.3f) }
    LaunchedEffect(Unit) {
        while (true) {
            shimmerAlpha.animateTo(0.7f, animationSpec = tween(800, easing = FastOutSlowInEasing))
            shimmerAlpha.animateTo(0.3f, animationSpec = tween(800, easing = FastOutSlowInEasing))
        }
    }
    val shimmerBrush = remember(shimmerAlpha.value, shimmerBase, shimmerHighlight) {
        Brush.linearGradient(listOf(shimmerBase, shimmerHighlight.copy(alpha = shimmerAlpha.value), shimmerBase))
    }

    val cardBg = if (darkTheme) DarkSurface else WarmWhite

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(shimmerBrush, RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.55f)
                        .height(14.dp)
                        .background(shimmerBrush, RoundedCornerShape(4.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(12.dp)
                        .background(shimmerBrush, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Composable
private fun MemoryPromptsSection(
    books: List<Book>,
    darkTheme: Boolean,
    cardBg: Color,
    primaryText: Color,
    mutedText: Color,
    modifier: Modifier = Modifier,
    onPromptSelect: (String) -> Unit
) {
    val prompts = remember {
        HOME_PROMPTS.shuffled().take(3)
    }
    val promptBg = if (darkTheme) DarkSurfaceVariant else Papaya.copy(alpha = 0.5f)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Writing prompts",
            style = MaterialTheme.typography.titleMedium,
            color = primaryText,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "Tap a prompt to start writing",
            style = MaterialTheme.typography.bodySmall,
            color = mutedText,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        prompts.forEach { prompt ->
            Card(
                onClick = { onPromptSelect(prompt) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = promptBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("💬", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.bodyMedium,
                        color = primaryText,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        "→",
                        color = mutedText
                    )
                }
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
        if (month < 1 || month > 12) return isoDate
        "${months[month]} $day"
    } catch (e: Exception) { isoDate }
}