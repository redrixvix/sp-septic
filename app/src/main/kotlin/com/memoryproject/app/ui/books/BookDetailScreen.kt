package com.memoryproject.app.ui.books



import android.content.Intent

import androidx.compose.animation.*

import androidx.compose.animation.core.*

import androidx.compose.foundation.background

import androidx.compose.foundation.isSystemInDarkTheme

import androidx.compose.foundation.clickable

import androidx.compose.foundation.interaction.MutableInteractionSource

import androidx.compose.foundation.interaction.collectIsPressedAsState

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material.icons.automirrored.filled.Groups

import androidx.compose.material.icons.automirrored.filled.Share

import androidx.compose.material.icons.filled.PersonRemove

import androidx.compose.material.icons.filled.Add

import androidx.compose.material.icons.filled.Close

import androidx.compose.material.icons.filled.Delete

import androidx.compose.material.icons.filled.Edit

import androidx.compose.material3.*

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip

import androidx.compose.ui.draw.scale

import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer

import androidx.compose.ui.input.pointer.pointerInput

import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.text.input.ImeAction

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
import com.memoryproject.app.data.model.BookMember
import androidx.compose.foundation.text.KeyboardActions
import kotlinx.coroutines.launch



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

    var showPhotoViewer by remember { mutableStateOf<String?>(null) }
    var showMembersSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val isDark = isSystemInDarkTheme()
    val scaffoldBg = if (isDark) DarkBackground else Cornsilk
    val topBarTitleColor = if (isDark) DarkOnSurface else Charcoal
    val topBarSubtitleColor = if (isDark) DarkOnSurfaceVariant else CharcoalMuted

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = { viewModel.loadBook() }
    )

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

                            color = topBarTitleColor,

                            maxLines = 1,

                            overflow = TextOverflow.Ellipsis

                        )

                        uiState.book?.description?.takeIf { it.isNotBlank() }?.let { desc ->

                            Text(

                                desc,

                                style = MaterialTheme.typography.bodyMedium,

                                color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted,

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

                                color = if (isDark) DarkSurface else WarmWhite,

                                shape = RoundedCornerShape(12.dp)

                            )

                    ) {

                        Icon(

                            Icons.AutoMirrored.Filled.ArrowBack,

                            contentDescription = "Back",

                            tint = if (isDark) DarkOnSurface else Charcoal

                        )

                    }

                },

                actions = {

                    IconButton(

                        onClick = { showMembersSheet = true },

                        modifier = Modifier

                            .size(44.dp)

                            .background(

                                color = if (isDark) DarkSurface else WarmWhite,

                                shape = RoundedCornerShape(12.dp)

                            )

                    ) {

                        Icon(

                            Icons.AutoMirrored.Filled.Groups,

                            contentDescription = "Members",

                            tint = if (isDark) DarkOnSurface else Charcoal

                        )

                    }

                },

                colors = TopAppBarDefaults.largeTopAppBarColors(

                    containerColor = scaffoldBg,

                    scrolledContainerColor = scaffoldBg

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

        containerColor = scaffoldBg

    ) { padding ->

        Box(

            modifier = Modifier

                .fillMaxSize()

                .padding(padding)

                .pullRefresh(pullRefreshState)

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

                            color = if (isDark) DarkOnSurface else Charcoal

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

                                    brush = Brush.verticalGradient(

                                        colors = if (isDark) listOf(DarkSurfaceVariant, DarkSurface) else listOf(Papaya, Beige)

                                    ),

                                    shape = RoundedCornerShape(24.dp)

                                ),

                            contentAlignment = Alignment.Center

                        ) {

                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                Bronze.copy(alpha = 0.2f),
                                                Color.Transparent
                                            )
                                        ),
                                        shape = CircleShape
                                    )
                            )
                            Text("✨", fontSize = 48.sp)

                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(

                            "Add your first memory",

                            style = MaterialTheme.typography.headlineSmall,

                            fontWeight = FontWeight.SemiBold,

                            color = if (isDark) DarkOnSurface else Charcoal

                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(

                            "Capture a moment, preserve a story — your memories begin here.",

                            style = MaterialTheme.typography.bodyLarge,

                            color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted,

                            textAlign = TextAlign.Center

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

                            putExtra(Intent.EXTRA_TEXT, "${memory.prompt_question}

${memory.answer_text}")

                        }

                        context.startActivity(Intent.createChooser(intent, "Share memory"))

                    }

                    LazyColumn(

                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),

                        verticalArrangement = Arrangement.spacedBy(14.dp)

                    ) {

                        // Memory count header

                        item {

                            Text(

                                "${uiState.memories.size} ${if (uiState.memories.size == 1) "memory" else "memories"}",

                                style = MaterialTheme.typography.bodyMedium,

                                color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted,

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

                                delay(50L * uiState.memories.indexOf(memory).coerceAtMost(5))

                                visible = true

                            }

                            MemoryCard(

                                memory = memory,

                                onEdit = { viewModel.showEditMemory(memory) },

                                onDelete = { viewModel.showDeleteConfirm(memory) },

                                onShareClick = { onShareMemory(memory) },

                                onPhotoClick = { photoUrl -> showPhotoViewer = photoUrl },

                                accentIndex = uiState.memories.indexOf(memory),

                                modifier = Modifier.graphicsLayer {

                                    scaleX = scale

                                    scaleY = scale

                                    this.alpha = alpha

                                }

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
                    backgroundColor = if (isDark) DarkSurfaceVariant else Papaya
                )

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

                    Text("Keep It", color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted)

                }

            },

            shape = RoundedCornerShape(20.dp)

        )

    }



    // Photo viewer — full-screen lightbox
    showPhotoViewer?.let { photoUrl ->
        PhotoViewer(
            photoUrl = photoUrl,
            onDismiss = { showPhotoViewer = null }
        )
    }

    // Members sheet
    if (showMembersSheet) {
        MembersBottomSheet(
            bookId = bookId,
            onDismiss = { showMembersSheet = false },
            isDark = isDark,
            snackbarHostState = snackbarHostState
        )
    }
}



@Composable

private fun PhotoViewer(

    photoUrl: String,

    onDismiss: () -> Unit

) {

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

                .background(Color.Black.copy(alpha = 0.92f))

                .clickable(

                    interactionSource = remember { MutableInteractionSource() },

                    indication = null,

                    onClick = onDismiss

                ),

            contentAlignment = Alignment.Center

        ) {

            AsyncImage(

                model = photoUrl,

                contentDescription = "Full size photo",

                modifier = Modifier

                    .fillMaxWidth()

                    .padding(24.dp)

                    .clip(RoundedCornerShape(16.dp)),

                contentScale = ContentScale.Fit

            )



            // Close button

            IconButton(

                onClick = onDismiss,

                modifier = Modifier

                    .align(Alignment.TopEnd)

                    .padding(24.dp)

                    .size(44.dp)

                    .background(

                        color = Color.Black.copy(alpha = 0.5f),

                        shape = CircleShape

                    )

            ) {

                Icon(

                    Icons.Default.Close,

                    contentDescription = "Close",

                    tint = Color.White

                )

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

    confirmLabel: String

) {

    // Shuffle suggestions each time the dialog opens

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

                    label = { Text("What's this memory about?") },

                    placeholder = { Text("e.g. Tell me about your first job") },

                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(12.dp),

                    singleLine = true,

                    colors = OutlinedTextFieldDefaults.colors(

                        focusedBorderColor = Bronze,

                        unfocusedBorderColor = if (isDark) DarkBorder else Border,

                        focusedLabelColor = Bronze

                    ),

                    supportingText = if (promptInput.isBlank()) {

                        { Text("Or pick a prompt below to get started", color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted, style = MaterialTheme.typography.bodySmall) }

                    } else null

                )



                // Prompt picker — warm, inviting

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    Text(

                        "Or choose a prompt to get started:",

                        style = MaterialTheme.typography.labelMedium,

                        color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted,

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

                                        color = if (promptInput == prompt) if (isDark) DarkBronze.copy(alpha = 0.3f) else Bronze.copy(alpha = 0.15f) else if (isDark) DarkSurfaceVariant else Papaya,

                                        shape = RoundedCornerShape(20.dp)

                                    )

                                    .clickable { onPromptChange(prompt) }

                                    .padding(horizontal = 14.dp, vertical = 9.dp)

                            ) {

                                Text(

                                    text = prompt,

                                    style = MaterialTheme.typography.labelMedium,

                                    color = if (promptInput == prompt) if (isDark) DarkBronzeLight else BronzeDark else if (isDark) DarkOnSurface else Charcoal,

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

                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),

                    keyboardActions = KeyboardActions(

                        onDone = {

                            if (promptInput.isNotBlank() && answerInput.isNotBlank()) onConfirm()

                        }

                    ),

                    colors = OutlinedTextFieldDefaults.colors(

                        focusedBorderColor = Bronze,

                        unfocusedBorderColor = if (isDark) DarkBorder else Border,

                        focusedLabelColor = Bronze

                    ),

                    supportingText = {
                        if (answerInput.isBlank()) {
                            Text(
                                "Share what matters",
                                color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted,
                                style = MaterialTheme.typography.bodySmall
                            )
                        } else {
                            val words = answerInput.split("\\s+".toRegex()).filter { it.isNotBlank() }.size
                            Text(
                                "${answerInput.length} characters · $words ${if (words == 1) "word" else "words"}",
                                color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted,
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

                shape = RoundedCornerShape(12.dp),

                colors = ButtonDefaults.buttonColors(

                    containerColor = Bronze,

                    contentColor = WarmWhite

                )

            ) {

                if (isSaving) {

                    CircularProgressIndicator(

                        modifier = Modifier.size(18.dp),

                        color = if (isDark) DarkSurface else WarmWhite,

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

                Text("Cancel", color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted)

            }

        },

        shape = RoundedCornerShape(20.dp)

    )

}



@Composable

private fun MemorySkeletonCard() {

    val isDark = isSystemInDarkTheme()

    val shimmerAlpha = remember { Animatable(0.3f) }



    LaunchedEffect(Unit) {

        while (true) {

            shimmerAlpha.animateTo(0.7f, animationSpec = tween(800, easing = FastOutSlowInEasing))

            shimmerAlpha.animateTo(0.3f, animationSpec = tween(800, easing = FastOutSlowInEasing))

        }

    }



    val shimmerBase = if (isDark) DarkDivider else Divider

    val shimmerHighlight = if (isDark) DarkOnSurface.copy(alpha = 0.08f) else Divider.copy(alpha = shimmerAlpha.value)

    val shimmerBrush = remember(shimmerAlpha.value, isDark) {

        Brush.linearGradient(

            colors = listOf(shimmerBase, shimmerHighlight, shimmerBase)

        )

    }

    val cardBg = if (isDark) DarkSurface else WarmWhite



    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(14.dp),

        colors = CardDefaults.cardColors(containerColor = cardBg),

        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)

    ) {

        Row(modifier = Modifier.fillMaxWidth()) {

            Box(

                modifier = Modifier

                    .width(4.dp)

                    .fillMaxHeight()

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
    isDark: Boolean,
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

    val cardBg = if (isDark) DarkSurface else WarmWhite
    val primaryText = if (isDark) DarkOnSurface else Charcoal
    val mutedText = if (isDark) DarkOnSurfaceVariant else CharcoalMuted
    val borderColor = if (isDark) DarkBorder else Border

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
        containerColor = if (isDark) DarkSurfaceVariant else Cornsilk,
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
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp)
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
                                    color = if (isDark) DarkBronze.copy(alpha = 0.3f) else Bronze.copy(alpha = 0.15f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                (member.name.firstOrNull() ?: member.email.firstOrNull() ?: '?').uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isDark) DarkOnSurface else Charcoal,
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
                                        (if (isDark) Bronze.copy(alpha = 0.2f) else Bronze.copy(alpha = 0.1f))
                                    else
                                        (if (isDark) TeaGreen.copy(alpha = 0.2f) else TeaGreen.copy(alpha = 0.1f)),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                member.role.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.labelSmall,
                                color = if (member.role == "owner") Bronze else (if (isDark) TeaGreen else Color(0xFF4A7A4A)),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    if (member != members.last()) {
                        Divider(color = borderColor.copy(alpha = 0.5f))
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
