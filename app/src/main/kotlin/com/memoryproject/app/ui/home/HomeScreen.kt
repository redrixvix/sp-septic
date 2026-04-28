package com.memoryproject.app.ui.home
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.BorderStroke
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToBooks: () -> Unit,
    onNavigateToBook: (Int) -> Unit,
    onNavigateToAddMemory: (Int, String?) -> Unit = { _, _ -> },
    darkTheme: Boolean,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scaffoldBg = if (darkTheme) DarkBackground else Cornsilk
    val primaryText = if (darkTheme) DarkOnSurface else Charcoal
    val mutedText = if (darkTheme) DarkOnSurfaceVariant else CharcoalMuted
    val cardBg = if (darkTheme) DarkSurface else WarmWhite

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(scaffoldBg)
    ) {
        // Warm ambient top glow in light mode — premium page depth
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .align(Alignment.TopCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (darkTheme) {
                            listOf(DarkBackground.copy(alpha = 0.0f), Color.Unspecified)
                        } else {
                            listOf(Papaya.copy(alpha = 0.22f), Color.Transparent)
                        }
                    )
                )
        )

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .statusBarsPadding()
                        ) {
                            // Compact brand icon badge
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = if (darkTheme) listOf(DarkBronze, DarkBronzeLight) else listOf(Bronze, BronzeLight)
                                        ),
                                        shape = RoundedCornerShape(7.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                    contentDescription = null,
                                    tint = WarmWhite,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Memory Project",
                                style = MaterialTheme.typography.titleMedium,
                                color = primaryText,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
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
                    onAddMemory = { uiState.books.firstOrNull()?.let { onNavigateToAddMemory(it.id, null) } },
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
                        darkTheme = darkTheme,
                        primaryText = primaryText,
                        mutedText = mutedText,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        onPromptSelect = { prompt ->
                            uiState.books.firstOrNull()?.let { firstBook ->
                                onNavigateToAddMemory(firstBook.id, prompt)
                            }
                        }
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
                        onAddMemory = { onNavigateToBooks() },
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            // Recent memories section
            if (uiState.recentMemories.isNotEmpty()) {
                item(key = "recent_header") {
                    SectionHeader(
                        title = "Recent Stories",
                        subtitle = "Moments worth remembering",
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
                                "View all stories →",
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
    }
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
                        // Simple 2-line skeleton — avatar shows immediately
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
                            val greeting = if (userName.isNotBlank()) rememberWarmGreeting(userName) else rememberGreeting()
                            Text(
                                text = greeting,
                                style = MaterialTheme.typography.headlineSmall,
                                color = primaryText,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = if (userName.isNotBlank()) {
                                    "What will you remember today?"
                                } else {
                                    "Let's capture something great today"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = mutedText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Avatar — shown immediately, no skeleton
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
private fun rememberWarmGreeting(userName: String): String {
    val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
    val suffix = if (userName.isNotBlank()) ", $userName" else ""
    return remember(userName, hour) {
        when {
            hour < 12 -> "Good morning$suffix"
            hour < 17 -> "Good afternoon$suffix"
            else -> "Good evening$suffix"
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
            icon = "book",
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
            icon = "memory",
            value = memoriesCount.toString(),
            label = if (memoriesCount == 1) "Memory" else "Memories",
            accentColor = if (darkTheme) DarkBronze else TeaGreen,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        brush = Brush.linearGradient(
                            colors = if (darkTheme) {
                                listOf(DarkBronze.copy(alpha = 0.45f), DarkSurfaceVariant)
                            } else {
                                listOf(Bronze.copy(alpha = 0.3f), Papaya.copy(alpha = 0.8f))
                            }
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (icon == "book") Icons.Default.Book else Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = if (darkTheme) DarkBronze else Bronze,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = primaryText,
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
            label = "Add Memory",
            onClick = onAddMemory,
            isPrimary = true,
            darkTheme = darkTheme,
            cardBg = cardBg,
            primaryText = primaryText,
            modifier = Modifier.weight(1f)
        )
        QuickActionButton(
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
    label: String,
    onClick: () -> Unit,
    isPrimary: Boolean,
    darkTheme: Boolean,
    cardBg: Color,
    primaryText: Color,
    modifier: Modifier = Modifier
) {
    if (isPrimary) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Bronze,
                contentColor = WarmWhite
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = WarmWhite,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = WarmWhite,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    } else {
        // Secondary button — outlined style with Bronze border
        OutlinedCard(
            onClick = onClick,
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = Color.Transparent,
                contentColor = Bronze
            ),
            border = BorderStroke(1.5.dp, if (darkTheme) DarkBronze else Bronze)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = if (darkTheme) DarkBronzeLight else Bronze,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (darkTheme) DarkBronzeLight else Bronze,
                    fontWeight = FontWeight.SemiBold
                )
            }
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
                                colors = if (darkTheme) listOf(DarkSurfaceVariant.copy(alpha = 0.8f), DarkSurface) else listOf(Papaya.copy(alpha = 0.7f), Beige.copy(alpha = 0.7f))
                            ),
                            shape = RoundedCornerShape(22.dp)
                        )
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = if (darkTheme) listOf(DarkSurfaceVariant, DarkSurface.copy(alpha = 0.9f)) else listOf(Papaya.copy(alpha = 0.5f), Beige.copy(alpha = 0.4f))
                                ),
                                shape = RoundedCornerShape(18.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = if (darkTheme) DarkBronze else Bronze,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Every great book starts with a single page",
                    style = MaterialTheme.typography.headlineSmall,
                    color = primaryText,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Capture your first memory. It's easier than writing a letter, more meaningful than a photo — and your family will treasure it for generations.",
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
                        text = "Start Writing",
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = if (darkTheme) {
                        listOf(DarkSurfaceVariant.copy(alpha = 0.0f), DarkSurfaceVariant.copy(alpha = 0.0f))
                    } else {
                        // Subtle warm left-to-right fade for editorial feel
                        listOf(Papaya.copy(alpha = 0.25f), Cornsilk.copy(alpha = 0.0f))
                    }
                )
            )
            .padding(horizontal = 4.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Small bronze accent dot
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(
                        color = if (darkTheme) DarkBronze else Bronze,
                        shape = CircleShape
                    )
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = if (darkTheme) DarkOnSurface else Charcoal,
                fontWeight = FontWeight.SemiBold
            )
        }
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = mutedText,
            modifier = Modifier.padding(start = 14.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
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
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 4.dp else 1.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "recentCardElevation"
    )
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "recentCardScale"
    )

    val accentColors = listOf(CardAccentBronze, CardAccentTea, CardAccentPapaya)
    val accentColor = accentColors[accentIndex % accentColors.size]
    val promptLabelBg = if (darkTheme) DarkSurfaceVariant else Papaya.copy(alpha = 0.8f)
    val promptLabelText = if (darkTheme) DarkOnSurface else BronzeDark

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .scale(cardScale),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        // Warm premium gradient tint — adds subtle depth and emotional warmth
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (darkTheme) {
                            listOf(accentColor.copy(alpha = 0.05f), Color.Transparent)
                        } else {
                            listOf(accentColor.copy(alpha = 0.03f), Color.Transparent)
                        }
                    )
                )
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.6f),
                                accentColor.copy(alpha = 0.2f)
                            )
                        ),
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
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = mutedText,
                                modifier = Modifier.size(12.dp)
                            )
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
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 6.dp else 2.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "miniBookElevation"
    )
    val cardScale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "miniBookScale"
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .scale(cardScale),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Left accent strip — bronze brand marker
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = if (darkTheme) {
                                listOf(DarkBronze.copy(alpha = 0.55f), DarkBronzeLight.copy(alpha = 0.2f))
                            } else {
                                listOf(Bronze.copy(alpha = 0.55f), BronzeLight.copy(alpha = 0.2f))
                            }
                        ),
                        shape = RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp)
                    )
            )
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
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = null,
                        tint = WarmWhite,
                        modifier = Modifier.size(20.dp)
                    )
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
                        text = (book.memories_count ?: 0).toString() + " " + (if ((book.memories_count ?: 0) == 1) "memory" else "memories"),
                        style = MaterialTheme.typography.bodySmall,
                        color = mutedText
                    )
                }
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = mutedText,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun HomeSkeletonCard(darkTheme: Boolean) {
    val shimmerBase = if (darkTheme) DarkDivider else Divider
    val shimmerHighlight = if (darkTheme) DarkOnSurface.copy(alpha = 0.06f) else Divider.copy(alpha = 0.5f)

    // Continuous shimmer sweep — premium loading feel
    val infiniteTransition = rememberInfiniteTransition(label = "homeSkeletonShimmer")
    val shimmerOffset by infiniteTransition.animateFloat(
        initialValue = -300f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "homeShimmerOffset"
    )
    val shimmerBrush = remember(shimmerOffset, shimmerBase, shimmerHighlight) {
        Brush.linearGradient(
            colors = listOf(shimmerBase, shimmerHighlight.copy(alpha = 0.4f), shimmerBase.copy(alpha = 0.6f)),
            start = androidx.compose.ui.geometry.Offset(shimmerOffset, 0f),
            end = androidx.compose.ui.geometry.Offset(shimmerOffset + 200f, 0f)
        )
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
    darkTheme: Boolean,
    primaryText: Color,
    mutedText: Color,
    modifier: Modifier = Modifier,
    onPromptSelect: (String) -> Unit
) {
    val prompts = remember {
        HOME_PROMPTS.shuffled().take(3)
    }
    // Distinctive card background — warm Papaya card that stands out from the page
    val sectionCardBg = if (darkTheme) DarkSurfaceVariant else Papaya
    val accentColor = if (darkTheme) DarkBronze else Bronze
    val promptItemBg = if (darkTheme) DarkSurface else WarmWhite

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = sectionCardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Left accent strip — bronze brand marker
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxSize()
                    .background(
                        color = accentColor.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(topStart = 18.dp, bottomStart = 18.dp)
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Section label
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = if (darkTheme) DarkOnSurfaceVariant else BronzeDark,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Inspiration",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (darkTheme) DarkOnSurfaceVariant else BronzeDark,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "Need inspiration?",
                    style = MaterialTheme.typography.titleMedium,
                    color = primaryText,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Tap any prompt to start writing",
                    style = MaterialTheme.typography.bodySmall,
                    color = mutedText,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                prompts.forEachIndexed { index, prompt ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(prompt) { visible = true }
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(animationSpec = tween(350, delayMillis = index * 60)) +
                                slideInHorizontally(animationSpec = tween(350, delayMillis = index * 60), initialOffsetX = { it / 4 })
                    ) {
                        PromptCard(
                            prompt = prompt,
                            onClick = { onPromptSelect(prompt) },
                            darkTheme = darkTheme,
                            cardBg = promptItemBg,
                            primaryText = primaryText,
                            mutedText = mutedText
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PromptCard(
    prompt: String,
    onClick: () -> Unit,
    darkTheme: Boolean,
    cardBg: Color,
    primaryText: Color,
    mutedText: Color
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow),
        label = "promptScale"
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .scale(scale),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Subtle accent icon
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = if (darkTheme) DarkBronze.copy(alpha = 0.15f) else Bronze.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = if (darkTheme) DarkBronze.copy(alpha = 0.5f) else Bronze.copy(alpha = 0.4f),
                    modifier = Modifier.size(14.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = prompt,
                style = MaterialTheme.typography.bodyMedium,
                color = primaryText,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = mutedText,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

private fun formatDate(isoDate: String): String {
    return try {
        val cleanDate = isoDate.substringBefore("T").substringBefore(" ")
        val parts = cleanDate.split("-")
        if (parts.size < 3) return isoDate
        val year = parts[0].toInt()
        val month = parts[1].toInt()
        val day = parts[2].split("T")[0].toInt()
        if (month < 1 || month > 12) return isoDate
        val months = listOf("", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        "${months[month]} $day, $year"
    } catch (e: Exception) { isoDate }
}
