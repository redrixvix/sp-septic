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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Share
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.memoryproject.app.data.model.Memory
import com.memoryproject.app.ui.theme.*
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

private val PROMPT_SUGGESTIONS = listOf(
    "Tell me about your first job",
    "What was your wedding day like?",
    "Describe your childhood home",
    "What's your favorite holiday memory?",
    "Tell me about your parents",
    "What was school like for you?",
    "Describe a time you laughed until you cried",
    "What's the best advice you ever received?",
    "Tell me about learning to drive",
    "What music did you grow up listening to?"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookDetailScreen(
    bookId: Int,
    onBack: () -> Unit,
    viewModel: BookDetailViewModel = koinViewModel { parametersOf(bookId) }
) {
    val uiState by viewModel.uiState.collectAsState()
    var promptInput by remember { mutableStateOf("") }
    var answerInput by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

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

    // Show snackbar on errors, then clear them
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it, duration = SnackbarDuration.Long)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            uiState.book?.title ?: "Book",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        uiState.book?.description?.takeIf { it.isNotBlank() }?.let { desc ->
                            Text(
                                desc,
                                style = MaterialTheme.typography.bodyMedium,
                                color = CharcoalMuted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                color = WarmWhite,
                                shape = RoundedCornerShape(12.dp)
                            )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Charcoal
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Cornsilk,
                    scrolledContainerColor = Cornsilk
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.showAddMemory() },
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
                    "Add Memory",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Cornsilk
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
                        repeat(3) { MemorySkeletonCard() }
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
                            color = Charcoal
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
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(Papaya, Beige)
                                    ),
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✨", fontSize = 48.sp)
                        }
                        Spacer(modifier = Modifier.height(28.dp))
                        Text(
                            "Start your collection",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = Charcoal
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Capture a moment, preserve a story. Your memories begin here.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = CharcoalMuted,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = { viewModel.showAddMemory() },
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
                    val pullRefreshState = rememberPullRefreshState(
                        refreshing = uiState.isLoading,
                        onRefresh = { viewModel.loadBook() }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pullRefresh(pullRefreshState)
                    ) {
                        LazyColumn(
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Memory count header
                            item {
                                Text(
                                    "${uiState.memories.size} ${if (uiState.memories.size == 1) "memory" else "memories"}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = CharcoalMuted,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            }
                            items(
                                items = uiState.memories,
                                key = { it.id }
                            ) { memory ->
                                MemoryCard(
                                    memory = memory,
                                    onEdit = { viewModel.showEditMemory(memory) },
                                    onDelete = { viewModel.showDeleteConfirm(memory) },
                                    onShareClick = { onShareMemory(memory) },
                                    accentIndex = uiState.memories.indexOf(memory)
                                )
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

    // Add Memory Dialog
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
            confirmLabel = "Add Memory"
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
            confirmLabel = "Save Changes"
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
                    Text("Keep It", color = CharcoalMuted)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
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
    confirmLabel: String
) {
    // Shuffle suggestions each time the dialog opens — fresh variety keeps exploration fun
    // rememberSaveable survives process death, shuffled on each dialog open
    var suggestions by rememberSaveable { mutableStateOf(emptyList<String>()) }
    LaunchedEffect(title) {
        suggestions = PROMPT_SUGGESTIONS.shuffled(java.util.Random(System.currentTimeMillis() / 10000)).take(6)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = promptInput,
                    onValueChange = onPromptChange,
                    label = { Text("What moment is this about?") },
                    placeholder = { Text("e.g. Tell me about your first job") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Bronze,
                        unfocusedBorderColor = Border,
                        focusedLabelColor = Bronze
                    ),
                    supportingText = if (promptInput.isBlank()) {
                        { Text("Or pick a prompt below to get started", color = CharcoalMuted, style = MaterialTheme.typography.bodySmall) }
                    } else null
                )

                // Prompt picker — warm, inviting, visually distinct from form fields
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Or choose a prompt to get started:",
                        style = MaterialTheme.typography.labelMedium,
                        color = CharcoalMuted,
                        fontWeight = FontWeight.Medium
                    )
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestions.forEach { prompt ->
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (promptInput == prompt) Bronze.copy(alpha = 0.15f) else Papaya,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .then(
                                        if (promptInput != prompt) {
                                            Modifier.clickable { onPromptChange(prompt) }
                                        } else Modifier
                                    )
                                    .padding(horizontal = 14.dp, vertical = 9.dp)
                            ) {
                                Text(
                                    text = prompt,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (promptInput == prompt) BronzeDark else Charcoal,
                                    fontWeight = if (promptInput == prompt) FontWeight.SemiBold else FontWeight.Normal,
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
                        .heightIn(min = 140.dp),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 5,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Bronze,
                        unfocusedBorderColor = Border,
                        focusedLabelColor = Bronze
                    ),
                    supportingText = {
                        Text(
                            "${answerInput.length} characters",
                            color = CharcoalMuted,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = promptInput.isNotBlank() && answerInput.isNotBlank() && !isSaving,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Bronze,
                    contentColor = WarmWhite
                )
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = WarmWhite,
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
                Text("Cancel", color = CharcoalMuted)
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun MemorySkeletonCard() {
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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        color = Bronze.copy(alpha = 0.1f),
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
private fun MemoryCard(
    memory: Memory,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onShareClick: () -> Unit = {},
    accentIndex: Int = 0
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 6.dp else 2.dp,
        animationSpec = androidx.compose.animation.core.spring(),
        label = "memCardElevation"
    )

    // Rotate through accent colors based on index
    val accentColors = listOf(CardAccentBronze, CardAccentTea, CardAccentPapaya)
    val accentColor = accentColors[accentIndex % accentColors.size]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(if (isPressed) 0.99f else 1f)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { }
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = WarmWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Left color accent strip — subtle vertical bar, varies by index
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
                    .padding(18.dp)
            ) {
                // Header row: prompt + actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Prompt badge — premium feel with softer background
                    Box(
                        modifier = Modifier
                            .background(
                                color = Papaya.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { onEdit() }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = memory.prompt_question,
                            style = MaterialTheme.typography.labelMedium,
                            color = BronzeDark,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }
                    Row {
                        IconButton(
                            onClick = onShareClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Share,
                                contentDescription = "Share",
                                tint = CharcoalMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = CharcoalMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Answer text — the actual memory content with improved line height
                Text(
                    text = memory.answer_text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Charcoal,
                    lineHeight = 26.sp
                )

                // Photo thumbnails with count overlay
                if (memory.photo_urls.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        memory.photo_urls.take(3).forEach { url ->
                            Box {
                                AsyncImage(
                                    model = url,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(RoundedCornerShape(10.dp)),
                                    onError = { /* Silently handle failed image loads */ }
                                )
                            }
                        }
                        // Show count if more than 3 photos
                        val extraCount = memory.photo_urls.size - 3
                        if (extraCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        color = Bronze.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { onEdit() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+$extraCount",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = Bronze,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        // Edit button at end
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    color = Papaya,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .clickable { onEdit() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit memory",
                                tint = Bronze,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = formatDate(memory.created_at),
                    style = MaterialTheme.typography.bodySmall,
                    color = CharcoalMuted
                )
            }
        }
    }
}

private fun formatDate(isoDate: String): String {
    return try {
        val parts = isoDate.substringBefore("T").split("-")
        if (parts.size < 3) return isoDate
        val months = listOf(
            "", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val month = parts[1].toInt()
        val day = parts[2].toInt()
        if (month < 1 || month > 12) return isoDate
        "${months[month]} ${day}, ${parts[0].toInt()}"
    } catch (e: Exception) {
        isoDate
    }
}