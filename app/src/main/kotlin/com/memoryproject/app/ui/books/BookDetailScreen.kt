package com.memoryproject.app.ui.books
import androidx.compose.material3.ExperimentalMaterial3Api
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.memoryproject.app.data.model.Memory
import com.memoryproject.app.ui.common.MemoryCard
import com.memoryproject.app.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.compose.koinInject
import com.memoryproject.app.data.model.Book
import com.memoryproject.app.data.model.BookMember
import androidx.compose.foundation.text.KeyboardActions
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

private val PROMPT_SUGGESTIONS = listOf(
    "What's your favorite memory with your mom or dad?",
    "Tell me about a holiday tradition your family always had",
    "What's the best piece of advice you ever received?",
    "Describe a day that made you genuinely happy",
    "What's something you've always wanted to tell your family?",
    "Tell me about your first home",
    "What was the happiest day of your life so far?",
    "What's a skill or hobby you're most proud of?",
    "Describe your best friend from childhood",
    "What do you want your family to never forget?"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookDetailScreen(
    bookId: Int,
    onBack: () -> Unit,
    darkTheme: Boolean,
    startPrompt: String? = null,
    onShareBook: (com.memoryproject.app.data.model.Book) -> Unit = {},
    viewModel: BookDetailViewModel = koinViewModel { parametersOf(bookId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    var promptInput by remember { mutableStateOf("") }
    var answerInput by remember { mutableStateOf("") }
    var pendingSuggestion by remember { mutableStateOf<String?>(startPrompt) }
    var showPhotoViewer by remember { mutableStateOf<String?>(null) }
    var showMembersSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scaffoldBg = if (darkTheme) DarkBackground else Cornsilk
    val topBarTitleColor = if (darkTheme) DarkOnSurface else Charcoal

    // Clear form when dialogs close
    LaunchedEffect(uiState.showAddMemory, uiState.editMemory) {
        if (!uiState.showAddMemory && uiState.editMemory == null) {
            promptInput = ""
            answerInput = ""
        }
    }

    // Pre-fill edit form
    LaunchedEffect(uiState.editMemory) {
        uiState.editMemory?.let { memory ->
            promptInput = memory.prompt_question
            answerInput = memory.answer_text
        }
    }

    // Trigger add-memory dialog when navigated with a prompt suggestion
    LaunchedEffect(startPrompt, uiState.book) {
        if (startPrompt != null && uiState.book != null) {
            viewModel.showAddMemory()
        }
    }

    // Apply pending suggestion when dialog opens
    LaunchedEffect(uiState.showAddMemory, pendingSuggestion) {
        if (uiState.showAddMemory) {
            pendingSuggestion?.let {
                promptInput = it
                pendingSuggestion = null
            }
        }
    }

    // Show snackbar on errors, then clear them
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Short)
            viewModel.clearError()
        }
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Brand icon badge
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = if (darkTheme) listOf(DarkBronze, DarkBronzeLight) else listOf(Bronze, BronzeLight)
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = WarmWhite,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                        Column {
                            Text(
                                uiState.book?.title ?: "Book",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = topBarTitleColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val memCount = uiState.memories.size.coerceAtLeast(uiState.book?.memories_count ?: 0)
                                if (memCount > 0) {
                                    Text(
                                        "$memCount ${if (memCount == 1) "story" else "stories"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (darkTheme) DarkOnSurfaceVariant else CharcoalMuted
                                    )
                                }
                                uiState.book?.description?.takeIf { it.isNotBlank() }?.let { desc ->
                                    if (memCount > 0) {
                                        Text("·", style = MaterialTheme.typography.bodySmall, color = if (darkTheme) DarkOnSurfaceVariant else CharcoalMuted)
                                    }
                                    Text(
                                        desc,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = if (darkTheme) DarkOnSurfaceVariant else CharcoalMuted,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                color = if (darkTheme) DarkSurface else WarmWhite,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = if (darkTheme) DarkOnSurface else Charcoal
                        )
                    }
                },
                actions = {
                    // Overflow menu for book-level actions
                    var showBookMenu by remember { mutableStateOf(false) }
                    val book = uiState.book
                    val canManage = book?.role == "owner"
                    Box {
                        IconButton(
                            onClick = { showBookMenu = true },
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    color = if (darkTheme) DarkSurface else WarmWhite,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = if (darkTheme) DarkOnSurface else Charcoal
                            )
                        }
                        DropdownMenu(
                            expanded = showBookMenu,
                            onDismissRequest = { showBookMenu = false },
                            modifier = Modifier.background(
                                color = if (darkTheme) DarkSurface else WarmWhite,
                                shape = RoundedCornerShape(12.dp)
                            )
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Share,
                                            contentDescription = null,
                                            tint = if (darkTheme) DarkOnSurface else Charcoal,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text("Share", color = if (darkTheme) DarkOnSurface else Charcoal)
                                    }
                                },
                                onClick = {
                                    showBookMenu = false
                                    book?.let { onShareBook(it) }
                                }
                            )
                            if (canManage) {
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                Icons.Default.Edit,
                                                contentDescription = null,
                                                tint = if (darkTheme) DarkOnSurface else Charcoal,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Text("Edit Book", color = if (darkTheme) DarkOnSurface else Charcoal)
                                        }
                                    },
                                    onClick = {
                                        showBookMenu = false
                                        // Future: show edit book dialog
                                    }
                                )
                            }
                        }
                    }
                    IconButton(
                        onClick = { showMembersSheet = true },
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                color = if (darkTheme) DarkSurface else WarmWhite,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            Icons.Default.Group,
                            contentDescription = "Members",
                            tint = if (darkTheme) DarkOnSurface else Charcoal
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = scaffoldBg
                )
            )
        },
        floatingActionButton = {
            // FAB with scale press animation and warm-toned shadow
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val scale by animateFloatAsState(
                targetValue = if (isPressed) 0.93f else 1f,
                animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
                label = "fabScale"
            )
            ExtendedFloatingActionButton(
                onClick = { viewModel.showAddMemory() },
                containerColor = Bronze,
                contentColor = WarmWhite,
                modifier = Modifier.scale(scale),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 12.dp
                )
            ) {
                // Warm icon background circle
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(BronzeLight.copy(alpha = 0.4f), BronzeLight.copy(alpha = 0.15f))
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add memory",
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "Capture a Memory",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = scaffoldBg
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading && uiState.memories.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        repeat(3) { MemorySkeletonCard(darkTheme) }
                    }
                }

                uiState.error != null && uiState.memories.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("😕", fontSize = 56.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Couldn't load this book",
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (darkTheme) DarkOnSurface else Charcoal
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            uiState.error!!,
                            style = MaterialTheme.typography.bodyMedium,
                            color = CharcoalMuted
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        OutlinedButton(
                            onClick = { viewModel.loadBook() },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Bronze)
                        ) {
                            Text("Try Again")
                        }
                    }
                }

                uiState.memories.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Warm illustrated card with layered depth — premium empty state
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = if (darkTheme) listOf(DarkSurfaceVariant, DarkSurfaceVariant) else listOf(Papaya, Beige)
                                    ),
                                    shape = RoundedCornerShape(24.dp)
                                )
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(88.dp)
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = if (darkTheme) {
                                                listOf(DarkSurfaceVariant.copy(alpha = 0.6f), DarkSurface.copy(alpha = 0.4f))
                                            } else {
                                                listOf(Papaya.copy(alpha = 0.7f), Cornsilk.copy(alpha = 0.5f))
                                            }
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
                                                colors = if (darkTheme) {
                                                    listOf(DarkBronze.copy(alpha = 0.22f), DarkSurface.copy(alpha = 0.0f))
                                                } else {
                                                    listOf(Bronze.copy(alpha = 0.18f), Beige.copy(alpha = 0.0f))
                                                }
                                            ),
                                            shape = RoundedCornerShape(18.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = if (darkTheme) DarkBronze else Bronze,
                                        modifier = Modifier.size(42.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            "Your story starts here",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (darkTheme) DarkOnSurface else Charcoal
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Every memory you capture becomes a gift for the people who'll carry your story forward.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (darkTheme) DarkOnSurfaceVariant else CharcoalMuted,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = { viewModel.showAddMemory() },
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
                            Icon(Icons.Default.Add, contentDescription = "Add your first memory")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Add Your First Memory",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                else -> {
                    val context = LocalContext.current
                    val onShareMemory: (Memory) -> Unit = { memory ->
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, memory.prompt_question)
                            putExtra(Intent.EXTRA_TEXT, "${memory.prompt_question}\n\n${memory.answer_text}")
                        }
                        context.startActivity(Intent.createChooser(intent, "Share memory"))
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Prompt suggestions — staggered entrance
                            item {
                                PromptSuggestionsSection(
                                    darkTheme = darkTheme,
                                    onSuggestionSelected = { prompt ->
                                        pendingSuggestion = prompt
                                        viewModel.showAddMemory()
                                    }
                                )
                            }

                            // Memory count header
                            item {
                                Text(
                                    "${uiState.memories.size} ${if (uiState.memories.size == 1) "memory" else "memories"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (darkTheme) DarkOnSurfaceVariant else CharcoalMuted,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                            items(
                                items = uiState.memories,
                                key = { it.id }
                            ) { memory ->
                                var visible by remember { mutableStateOf(false) }
                                val scale by animateFloatAsState(
                                    targetValue = if (visible) 1f else 0.95f,
                                    animationSpec = spring(
                                        dampingRatio = androidx.compose.animation.core.Spring.DampingRatioMediumBouncy,
                                        stiffness = androidx.compose.animation.core.Spring.StiffnessLow
                                    ),
                                    label = "cardScale"
                                )
                                val alpha by animateFloatAsState(
                                    targetValue = if (visible) 1f else 0f,
                                    animationSpec = tween(durationMillis = 300),
                                    label = "cardAlpha"
                                )

                                LaunchedEffect(memory.id) {
                                    delay(50L * uiState.memories.indexOf(memory).coerceAtMost(5).toLong())
                                    visible = true
                                }

                                MemoryCard(
                                    memory = memory,
                                    onEdit = { viewModel.showEditMemory(memory) },
                                    onDelete = { viewModel.showDeleteConfirm(memory) },
                                    onShareClick = { onShareMemory(memory) },
                                    onPhotoClick = { photoUrl -> showPhotoViewer = photoUrl },
                                    accentIndex = uiState.memories.indexOf(memory),
                                    darkTheme = darkTheme,
                                    modifier = Modifier.graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                        this.alpha = alpha
                                    }
                                )

                            }
                        }
                        // PullToRefreshContainer removed for compatibility
                    }
                }
            }
        }
    }

    // Add Memory Dialog — premium card-based bottom sheet style
    if (uiState.showAddMemory) {
        MemoryDialog(
            title = "Add a Memory",
            promptInput = promptInput,
            answerInput = answerInput,
            onPromptChange = { promptInput = it },
            onAnswerChange = { answerInput = it },
            onDismiss = {
                viewModel.hideAddMemory()
                promptInput = ""
                answerInput = ""
            },
            onConfirm = {
                viewModel.addMemory(promptInput, answerInput)
                promptInput = ""
                answerInput = ""
            },
            isSaving = uiState.isSaving,
            confirmLabel = "Add Memory",
            darkTheme = darkTheme
        )
    }

    // Edit Memory Dialog
    uiState.editMemory?.let { memory ->
        MemoryDialog(
            title = "Edit Memory",
            promptInput = promptInput,
            answerInput = answerInput,
            onPromptChange = { promptInput = it },
            onAnswerChange = { answerInput = it },
            onDismiss = {
                viewModel.hideEditMemory()
                promptInput = ""
                answerInput = ""
            },
            onConfirm = {
                viewModel.editMemory(memory.id, promptInput, answerInput)
                promptInput = ""
                answerInput = ""
            },
            isSaving = uiState.isSaving,
            confirmLabel = "Save Changes",
            darkTheme = darkTheme
        )
    }

    // Delete Confirmation Dialog
    uiState.deleteConfirmMemory?.let { memory ->
        AlertDialog(
            onDismissRequest = { viewModel.hideDeleteConfirm() },
            icon = {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null,
                    tint = ErrorRed,
                    modifier = Modifier.size(28.dp)
                )
            },
            title = {
                Text(
                    "Delete this memory?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Text(
                    "This memory will be permanently removed. This cannot be undone.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CharcoalMuted
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteMemory(memory.id)
                        viewModel.hideDeleteConfirm()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorRed,
                        contentColor = WarmWhite
                    )
                ) {
                    Text("Delete", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.hideDeleteConfirm() }) {
                    Text("Keep memory", color = if (darkTheme) DarkOnSurfaceVariant else CharcoalMuted)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Photo viewer — full-screen animated lightbox
    showPhotoViewer?.let { photoUrl ->
        PhotoViewer(
            photoUrl = photoUrl,
            onDismiss = { showPhotoViewer = null },
            darkTheme = darkTheme
        )
    }

    if (showMembersSheet) {
        MembersBottomSheet(
            bookId = bookId,
            onDismiss = { showMembersSheet = false },
            darkTheme = darkTheme,
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun PromptSuggestionsSection(
    darkTheme: Boolean,
    onSuggestionSelected: (String) -> Unit
) {
    val suggestions = remember {
        PROMPT_SUGGESTIONS.shuffled().take(6)
    }
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    val cardBg = if (darkTheme) DarkSurfaceVariant else Papaya.copy(alpha = 0.5f)
    val primaryText = if (darkTheme) DarkOnSurface else Charcoal
    val mutedText = if (darkTheme) DarkOnSurfaceVariant else CharcoalMuted
    val accentColor = if (darkTheme) DarkBronze else Bronze

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (darkTheme) DarkSurfaceVariant.copy(alpha = 0.7f) else Papaya.copy(alpha = 0.45f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Inspiration header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "Inspiration",
                    style = MaterialTheme.typography.labelMedium,
                    color = accentColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(400, delayMillis = 80)) + slideInVertically(animationSpec = tween(400, delayMillis = 80), initialOffsetY = { -it / 8 })
            ) {
                Text(
                    "Need inspiration? Tap a prompt to start writing",
                    style = MaterialTheme.typography.bodySmall,
                    color = mutedText,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 10.dp)
                )
            }
            // Horizontally scrollable prompt chips — keeps the page clean and readable
            LazyRow(
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                state = rememberLazyListState()
            ) {
                items(suggestions) { prompt ->
                    SuggestionChip(
                        prompt = prompt,
                        cardBg = cardBg,
                        primaryText = primaryText,
                        darkTheme = darkTheme,
                        onClick = { onSuggestionSelected(prompt) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestionChip(
    prompt: String,
    cardBg: Color,
    primaryText: Color,
    darkTheme: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow),
        label = "suggestionScale"
    )
    val borderColor = if (darkTheme) DarkBorder else Border

    Box(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .background(cardBg)
            .then(
                Modifier.border(
                    BorderStroke(width = 0.5.dp, color = borderColor.copy(alpha = 0.4f)),
                    RoundedCornerShape(20.dp)
                )
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                tint = if (darkTheme) DarkBronze else Bronze,
                modifier = Modifier.size(12.dp)
            )
            Text(
                text = prompt,
                style = MaterialTheme.typography.bodySmall,
                color = primaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun PhotoViewer(
    photoUrl: String,
    onDismiss: () -> Unit,
    darkTheme: Boolean
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // Double tap to toggle zoom between 1x and 2x
    val doubleTapModifier = Modifier.pointerInput(photoUrl) {
        detectTapGestures(
            onDoubleTap = {
                scale = if (scale > 1f) 1f else 2f
                offset = Offset.Zero
            }
        )
    }

    // Swipe down to dismiss (only when not zoomed)
    val swipeDownModifier = Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDrag = { _, dragAmount ->
                if (scale <= 1f && dragAmount.y > 80) onDismiss()
            }
        )
    }

    // Animated entrance
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }
    val overlayAlpha by animateFloatAsState(
        targetValue = if (isVisible) if (darkTheme) 0.98f else 0.94f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "overlayAlpha"
    )
    val contentScale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.75f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessMedium),
        label = "contentScale"
    )

    val showZoomBadge = scale > 1f

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = overlayAlpha))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                )
                .then(swipeDownModifier)
                .then(doubleTapModifier),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = contentScale
                        scaleY = contentScale
                    },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Full size photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offset.x
                            translationY = offset.y
                        },
                    contentScale = ContentScale.Fit
                )
            }
        }

        // Overlay UI
        Box(modifier = Modifier.fillMaxSize()) {

          // Zoom level badge
          AnimatedVisibility(
              visible = showZoomBadge,
              enter = scaleIn(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium)) + fadeIn(),
              modifier = Modifier
                  .align(Alignment.TopCenter)
                  .padding(top = 48.dp)
          ) {
              Box(
                  modifier = Modifier
                      .background(
                          color = Color.Black.copy(alpha = 0.5f),
                          shape = RoundedCornerShape(16.dp)
                      )
                      .padding(horizontal = 12.dp, vertical = 6.dp)
              ) {
                  Text(
                      "${(scale * 100).toInt()}%",
                      style = MaterialTheme.typography.labelMedium,
                      color = Color.White,
                      fontWeight = FontWeight.SemiBold
                  )
              }
          }

          // Zoom controls — only visible when zoomed in
          AnimatedVisibility(
              visible = showZoomBadge,
              enter = fadeIn(animationSpec = tween(300)) + slideInVertically(animationSpec = tween(300), initialOffsetY = { it }),
              exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
              modifier = Modifier
                  .align(Alignment.BottomCenter)
                  .padding(bottom = 48.dp)
          ) {
              Row(
                  horizontalArrangement = Arrangement.spacedBy(16.dp),
                  verticalAlignment = Alignment.CenterVertically
              ) {
                  IconButton(
                      onClick = {
                          scale = (scale - 0.25f).coerceAtLeast(1f)
                          if (scale <= 1f) offset = Offset.Zero
                      },
                      modifier = Modifier
                          .size(44.dp)
                          .background(color = Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                  ) {
                      Icon(Icons.Default.ZoomOut, "Zoom out", tint = Color.White, modifier = Modifier.size(22.dp))
                  }

                  TextButton(
                      onClick = { scale = 1f; offset = Offset.Zero },
                      modifier = Modifier
                          .background(color = Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
                          .padding(horizontal = 12.dp, vertical = 4.dp)
                  ) {
                      Text("Reset", style = MaterialTheme.typography.labelMedium, color = Color.White, fontWeight = FontWeight.Medium)
                  }

                  IconButton(
                      onClick = { scale = (scale + 0.25f).coerceAtMost(4f) },
                      modifier = Modifier
                          .size(44.dp)
                          .background(color = Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                  ) {
                      Icon(Icons.Default.ZoomIn, "Zoom in", tint = Color.White, modifier = Modifier.size(22.dp))
                  }
              }
          }

          // Close button
          IconButton(
              onClick = onDismiss,
              modifier = Modifier
                  .align(Alignment.TopEnd)
                  .padding(24.dp)
                  .size(44.dp)
                  .background(color = Color.Black.copy(alpha = 0.5f), shape = CircleShape)
          ) {
              Icon(Icons.Default.Close, "Close", tint = Color.White)
          }

          // Hint text
          var showHint by remember { mutableStateOf(true) }
          LaunchedEffect(Unit) { delay(3000); showHint = false }
          AnimatedVisibility(
              visible = showHint && !showZoomBadge,
              enter = fadeIn(), exit = fadeOut(),
              modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 24.dp)
          ) {
              Text("Pinch to zoom · Swipe down to close", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.45f))
          }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MemoryDialog(
    title: String,
    promptInput: String,
    answerInput: String,
    onPromptChange: (String) -> Unit,
    onAnswerChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isSaving: Boolean,
    confirmLabel: String,
    darkTheme: Boolean = false
) {
    // Shuffle suggestions once when dialog opens, stable while dialog is visible
    var suggestions by remember { mutableStateOf(emptyList<String>()) }
    LaunchedEffect(Unit) {
        suggestions = PROMPT_SUGGESTIONS.shuffled().take(6)
    }

    // Auto-focus prompt field when dialog opens
    val promptFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        promptFocusRequester.requestFocus()
    }

    val cardBg = if (darkTheme) DarkSurface else WarmWhite
    val borderColor = if (darkTheme) DarkBorder else Border
    val textColor = if (darkTheme) DarkOnSurface else Charcoal
    val mutedTextColor = if (darkTheme) DarkOnSurfaceVariant else CharcoalMuted

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = cardBg,
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = (-16).dp)
                    .padding(bottom = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = if (darkTheme)
                                        listOf(DarkSurfaceVariant, DarkBronze.copy(alpha = 0.4f))
                                    else
                                        listOf(Papaya.copy(alpha = 0.8f), Beige.copy(alpha = 0.6f))
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = if (darkTheme) DarkBronze else Bronze,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = textColor
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.4f)
                                .height(3.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = if (darkTheme) listOf(DarkBronze, DarkBronzeLight) else listOf(Bronze, BronzeLight)
                                    ),
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = promptInput,
                    onValueChange = onPromptChange,
                    label = { Text("What's this memory about?") },
                    placeholder = { Text("e.g. Tell me about your first job") },
                    modifier = Modifier.fillMaxWidth().focusRequester(promptFocusRequester),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Bronze,
                        unfocusedBorderColor = borderColor,
                        focusedContainerColor = if (darkTheme) DarkSurfaceVariant.copy(alpha = 0.2f) else Papaya.copy(alpha = 0.15f),
                        unfocusedContainerColor = if (darkTheme) DarkSurfaceVariant.copy(alpha = 0.08f) else Papaya.copy(alpha = 0.08f)
                    ),
                    supportingText = {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            when {
                                promptInput.isBlank() -> {
                                    Text(
                                        "Tip: pick a prompt below to get started",
                                        color = mutedTextColor,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                promptInput.length > 80 -> {
                                    Text(
                                        "${promptInput.length}/80 · keep it short and focused",
                                        color = if (darkTheme) DarkError else ErrorRed,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                else -> {
                                    Text(
                                        "${promptInput.length}/80 characters",
                                        color = mutedTextColor,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                )

                // Prompt picker — warm, inviting
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    color = if (darkTheme) DarkBronze.copy(alpha = 0.3f) else Bronze.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = if (darkTheme) DarkBronze else Bronze,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Try one of these prompts",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (darkTheme) DarkOnSurface else BronzeDark,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestions.forEach { prompt ->
                            val isSelected = promptInput == prompt
                            val interactionSource = remember { MutableInteractionSource() }
                            val isPressed by interactionSource.collectIsPressedAsState()
                            val chipScale by animateFloatAsState(
                                targetValue = if (isPressed) 0.95f else 1f,
                                animationSpec = spring(dampingRatio = 0.7f, stiffness = Spring.StiffnessLow),
                                label = "chipScale"
                            )
                            Box(
                                modifier = Modifier
                                    .scale(chipScale)
                                    .background(
                                        color = if (isSelected) if (darkTheme) DarkBronze.copy(alpha = 0.35f) else Bronze.copy(alpha = 0.2f) else if (darkTheme) DarkSurfaceVariant else Papaya.copy(alpha = 0.7f),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable(
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) { onPromptChange(prompt) }
                                    .padding(horizontal = 14.dp, vertical = 9.dp)
                            ) {
                                Text(
                                    text = prompt,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isSelected) if (darkTheme) DarkBronzeLight else BronzeDark else if (darkTheme) DarkOnSurface else Charcoal,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = answerInput,
                    onValueChange = onAnswerChange,
                    label = { Text("Your story") },
                    placeholder = { Text("Write freely — this is yours.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 140.dp, max = 320.dp),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 5,
                    maxLines = 12,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (promptInput.isNotBlank() && answerInput.isNotBlank()) onConfirm()
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Bronze,
                        unfocusedBorderColor = borderColor,
                        focusedLabelColor = Bronze,
                        focusedContainerColor = if (darkTheme) DarkSurfaceVariant.copy(alpha = 0.3f) else Papaya.copy(alpha = 0.25f),
                        unfocusedContainerColor = if (darkTheme) DarkSurfaceVariant.copy(alpha = 0.15f) else Papaya.copy(alpha = 0.1f)
                    ),
                    supportingText = {
                        if (answerInput.isBlank()) {
                            Text(
                                "Share what matters",
                                color = mutedTextColor,
                                style = MaterialTheme.typography.bodySmall
                            )
                        } else {
                            val words = answerInput.split("\\s+".toRegex()).filter { it.isNotBlank() }.size
                            Text(
                                "${answerInput.length} characters · $words ${if (words == 1) "word" else "words"}",
                                color = mutedTextColor,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = promptInput.isNotBlank() && answerInput.isNotBlank() && !isSaving,
                modifier = Modifier.fillMaxWidth(),
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
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = if (darkTheme) DarkSurface else WarmWhite,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Saving...")
                } else {
                    Text(confirmLabel, fontWeight = FontWeight.SemiBold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = mutedTextColor)
            }
        }
    )
}

@Composable
private fun MemorySkeletonCard(darkTheme: Boolean) {
    val shimmerBase = if (darkTheme) DarkDivider else Divider
    val shimmerHighlight = if (darkTheme) DarkOnSurface.copy(alpha = 0.08f) else Divider.copy(alpha = 0.5f)

    // Single-pass shimmer
    val shimmerAlpha = remember { Animatable(0.3f) }
    LaunchedEffect(Unit) {
        shimmerAlpha.animateTo(0.65f, animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing))
    }
    val shimmerBrush = remember(shimmerAlpha.value, shimmerBase, shimmerHighlight) {
        Brush.linearGradient(listOf(shimmerBase, shimmerHighlight.copy(alpha = shimmerAlpha.value), shimmerBase))
    }
    val cardBg = if (darkTheme) DarkSurface else WarmWhite

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(4.dp).fillMaxSize()
                    .background(
                        color = DarkBronze.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp)
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(16.dp)
                        .background(shimmerBrush, RoundedCornerShape(4.dp))
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .background(shimmerBrush, RoundedCornerShape(4.dp))
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(14.dp)
                        .background(shimmerBrush, RoundedCornerShape(4.dp))
                )
            }
        }
    }
}

@Composable
private fun MembersBottomSheet(
    bookId: Int,
    onDismiss: () -> Unit,
    darkTheme: Boolean,
    snackbarHostState: SnackbarHostState
) {
    val repository: com.memoryproject.app.data.repository.MemoryRepository = koinInject()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var members by remember { mutableStateOf<List<BookMember>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var inviteEmail by remember { mutableStateOf("") }
    var isInviting by remember { mutableStateOf(false) }
    var inviteError by remember { mutableStateOf<String?>(null) }

    val primaryText = if (darkTheme) DarkOnSurface else Charcoal
    val mutedText = if (darkTheme) DarkOnSurfaceVariant else CharcoalMuted
    val borderColor = if (darkTheme) DarkBorder else Border

    LaunchedEffect(Unit) {
        repository.getBookMembers(bookId)
            .onSuccess { members = it }
            .onFailure {
                snackbarHostState.showSnackbar("Couldn't load members", duration = SnackbarDuration.Short)
            }
        isLoading = false
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = if (darkTheme) DarkSurfaceVariant else Cornsilk,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Collaborators",
                    style = MaterialTheme.typography.titleLarge,
                    color = primaryText,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = mutedText)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Email invite row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inviteEmail,
                    onValueChange = { inviteEmail = it },
                    label = { Text("Email address") },
                    placeholder = { Text("family@email.com") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Bronze,
                        unfocusedBorderColor = borderColor,
                        focusedLabelColor = Bronze,
                        cursorColor = Bronze
                    ),
                    shape = RoundedCornerShape(14.dp),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (inviteEmail.isNotBlank()) {
                                isInviting = true
                                scope.launch {
                                    repository.inviteMember(bookId, inviteEmail)
                                        .onSuccess {
                                            inviteEmail = ""
                                            repository.getBookMembers(bookId).onSuccess { members = it }
                                            snackbarHostState.showSnackbar("Invite sent! ✉️", duration = SnackbarDuration.Short)
                                        }
                                        .onFailure { e -> inviteError = e.message }
                                    isInviting = false
                                }
                            }
                        }
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Button(
                    onClick = {
                        if (inviteEmail.isBlank()) return@Button
                        isInviting = true
                        inviteError = null
                        scope.launch {
                            repository.inviteMember(bookId, inviteEmail)
                                .onSuccess {
                                    inviteEmail = ""
                                    repository.getBookMembers(bookId).onSuccess { members = it }
                                    snackbarHostState.showSnackbar("Invite sent! ✉️", duration = SnackbarDuration.Short)
                                }
                                .onFailure { e -> inviteError = e.message }
                            isInviting = false
                        }
                    },
                    enabled = !isInviting && inviteEmail.isNotBlank(),
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Bronze, contentColor = WarmWhite),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                ) {
                    if (isInviting) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = WarmWhite, strokeWidth = 2.dp)
                    } else {
                        Text("Invite", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            inviteError?.let { err ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(err, style = MaterialTheme.typography.bodySmall, color = ErrorRed)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Members list
            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Bronze, strokeWidth = 2.dp)
                }
            } else if (members.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                    Text(
                        "Only you so far — invite family!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = mutedText
                    )
                }
            } else {
                members.forEach { member ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    color = if (darkTheme) DarkBronze.copy(alpha = 0.3f) else Bronze.copy(alpha = 0.15f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                (member.name.firstOrNull() ?: member.email.firstOrNull() ?: '?').uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                color = if (darkTheme) DarkOnSurface else Charcoal,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                member.name.takeIf { it.isNotBlank() } ?: member.invite_email ?: member.email,
                                style = MaterialTheme.typography.bodyMedium,
                                color = primaryText,
                                fontWeight = FontWeight.Medium
                            )
                            if (member.invite_email != null && member.joined_at == null) {
                                Text(
                                    "Pending invite",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = mutedText
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    color = if (member.role == "owner")
                                        (if (darkTheme) Bronze.copy(alpha = 0.2f) else Bronze.copy(alpha = 0.1f))
                                    else
                                        (if (darkTheme) TeaGreen.copy(alpha = 0.2f) else TeaGreen.copy(alpha = 0.1f)),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                member.role.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.labelSmall,
                                color = if (member.role == "owner") Bronze else (if (darkTheme) TeaGreen else SuccessGreenDark),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    if (member != members.last()) {
                        HorizontalDivider(color = borderColor.copy(alpha = 0.5f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Invite family members to add memories to this book together.",
                style = MaterialTheme.typography.bodySmall,
                color = mutedText,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
