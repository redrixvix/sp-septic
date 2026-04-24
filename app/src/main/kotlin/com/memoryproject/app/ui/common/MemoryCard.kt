package com.memoryproject.app.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Share
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.memoryproject.app.data.model.Memory
import com.memoryproject.app.ui.theme.*

@Composable
fun MemoryCard(
    memory: Memory,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onShareClick: () -> Unit = {},
    onPhotoClick: (String) -> Unit = {},
    accentIndex: Int = 0,
    modifier: Modifier = Modifier
) {
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val elevation by animateDpAsState(
        targetValue = if (isPressed) 6.dp else 2.dp,
        animationSpec = spring(),
        label = "memCardElevation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "memCardScale"
    )

    // Rotate through accent colors
    val accentColors = listOf(CardAccentBronze, CardAccentTea, CardAccentPapaya)
    val accentColor = accentColors[accentIndex % accentColors.size]

    val cardBg = if (isDark) DarkSurface else WarmWhite
    val promptLabelBg = if (isDark) DarkSurfaceVariant else Papaya.copy(alpha = 0.8f)
    val promptLabelText = if (isDark) DarkOnSurface else BronzeDark
    val bodyText = if (isDark) DarkOnSurface else Charcoal
    val mutedText = if (isDark) DarkOnSurfaceVariant else CharcoalMuted
    val extraPhotoBg = if (isDark) DarkBronze.copy(alpha = 0.2f) else Bronze.copy(alpha = 0.15f)

    // Photo viewer state — scoped to this card
    var photoIndexToShow by remember { mutableStateOf<String?>(null) }
    val photosToShow = memory.photo_urls.take(3)
    val extraCount = (memory.photo_urls.size - 3).coerceAtLeast(0)
    val showExtraCount = extraCount > 0

    // Photo viewer dialog — triggered when a photo thumbnail is tapped
    photoIndexToShow?.let { photoUrl ->
        AlertDialog(
            onDismissRequest = { photoIndexToShow = null },
            confirmButton = {
                TextButton(onClick = { photoIndexToShow = null }) {
                    Text("Close", color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted)
                }
            },
            title = null,
            text = {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Full-size photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                )
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { }
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Left color accent strip
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
                // Header row: prompt label + actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = promptLabelBg,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = memory.prompt_question,
                            style = MaterialTheme.typography.labelMedium,
                            color = promptLabelText,
                            fontWeight = FontWeight.Medium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 200.dp)
                        )
                    }
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(300, delayMillis = 100)) +
                            scaleIn(
                                initialScale = 0.5f,
                                animationSpec = tween(300, delayMillis = 100)
                            )
                    ) {
                        Row {
                            IconButton(
                                onClick = onEdit,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = mutedText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            IconButton(
                                onClick = onShareClick,
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Share,
                                    contentDescription = "Share",
                                    tint = mutedText,
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
                                    tint = mutedText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Answer text
                Text(
                    text = memory.answer_text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = bodyText,
                    lineHeight = 26.sp
                )

                // Photo thumbnails with lightbox on tap
                if (photosToShow.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = if (photosToShow.size <= 1) Arrangement.Start else Arrangement.spacedBy(8.dp)
                    ) {
                        photosToShow.forEach { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = "Memory photo",
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        val index = memory.photo_urls.indexOf(url)
                                        if (index >= 0) photoIndexToShow = url
                                    },
                                onError = { }
                            )
                        }
                        if (showExtraCount) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        color = extraPhotoBg,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        memory.photo_urls.getOrNull(3)?.let { url ->
                                            val index = memory.photo_urls.indexOf(url)
                                            if (index >= 0) photoIndexToShow = url
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "+$extraCount",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isDark) DarkOnSurface else BronzeDark
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Date footer
                Text(
                    text = formatMemoryDate(memory.created_at),
                    style = MaterialTheme.typography.bodySmall,
                    color = mutedText
                )
            }
        }
    }
}

internal fun formatMemoryDate(isoDate: String): String {
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
        "${months[month]} $day, ${parts[0].toInt()}"
    } catch (e: Exception) {
        isoDate
    }
}
