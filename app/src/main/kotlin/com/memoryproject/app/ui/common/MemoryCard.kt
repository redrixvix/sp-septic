package com.memoryproject.app.ui.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
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
    darkTheme: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isDark = darkTheme
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

    val haptic = LocalHapticFeedback.current

    // Rotate through accent colors
    val accentColors = listOf(CardAccentBronze, CardAccentTea, CardAccentPapaya)
    val accentColor = accentColors[accentIndex % accentColors.size]

    val cardBg = if (isDark) DarkSurface else WarmWhite
    val promptLabelText = if (isDark) DarkOnSurface else Charcoal
    val bodyText = if (isDark) DarkOnSurface else Charcoal
    val mutedText = if (isDark) DarkOnSurfaceVariant else CharcoalMuted

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        tryAwaitRelease()
                    },
                    onTap = { onEdit() }
                )
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        // Warm premium tint — subtle gradient background that adds depth without overpowering
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = if (isDark) {
                            listOf(accentColor.copy(alpha = 0.04f), Color.Transparent)
                        } else {
                            listOf(accentColor.copy(alpha = 0.025f), Color.Transparent)
                        }
                    )
                )
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            // Left color accent strip with warm gradient overlay
            Box(
                modifier = Modifier
                    .width(4.dp)
            ) {
                // Base accent color
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = accentColor.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp)
                        )
                )
                // Warm gradient overlay — fills parent height via matchParentSize
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.25f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header row: prompt label + action buttons (share + delete)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .background(
                                color = if (isDark) DarkSurfaceVariant else Beige.copy(alpha = 0.75f),
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
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    // Direct share button — no longer hidden in a menu
                    val shareInteractionSource = remember { MutableInteractionSource() }
                    val shareIsPressed by shareInteractionSource.collectIsPressedAsState()
                    val shareScale by animateFloatAsState(
                        targetValue = if (shareIsPressed) 0.85f else 1f,
                        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
                        label = "shareBtnScale"
                    )
                    IconButton(
                        onClick = onShareClick,
                        modifier = Modifier
                            .size(40.dp)
                            .scale(shareScale),
                        interactionSource = shareInteractionSource,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = mutedText
                        )
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share memory",
                            tint = mutedText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    // Delete button — always visible
                    val deleteInteractionSource = remember { MutableInteractionSource() }
                    val deleteIsPressed by deleteInteractionSource.collectIsPressedAsState()
                    val deleteScale by animateFloatAsState(
                        targetValue = if (deleteIsPressed) 0.85f else 1f,
                        animationSpec = spring(dampingRatio = 0.6f, stiffness = 500f),
                        label = "deleteBtnScale"
                    )
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .size(40.dp)
                            .scale(deleteScale),
                        interactionSource = deleteInteractionSource,
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = ErrorRed.copy(alpha = 0.8f)
                        )
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Delete memory",
                            tint = ErrorRed.copy(alpha = 0.8f),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Answer text
                Text(
                    text = memory.answer_text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = bodyText,
                    lineHeight = 26.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                // Photo thumbnails — clean 4-up grid with badge for overflow
                if (memory.photo_urls.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        memory.photo_urls.take(4).forEachIndexed { index, url ->
                            Box {
                                AsyncImage(
                                    model = url,
                                    contentDescription = "Memory photo ${index + 1}",
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { onPhotoClick(url) }
                                )
                                // Overflow badge if more than 4 photos
                                if (index == 3 && memory.photo_urls.size > 4) {
                                    Box(
                                        modifier = Modifier
                                            .size(72.dp)
                                            .background(
                                                Color.Black.copy(alpha = 0.45f),
                                                shape = RoundedCornerShape(10.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "+${memory.photo_urls.size - 4}",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
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
            "", "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )
        val month = parts[1].toInt()
        val day = parts[2].toInt()
        val year = parts[0].toInt()
        if (month < 1 || month > 12) return isoDate
        "${months[month]} $day, $year"
    } catch (e: Exception) {
        isoDate
    }
}
