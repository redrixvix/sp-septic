package com.memoryproject.app.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

    // Rotate through accent colors
    val accentColors = listOf(CardAccentBronze, CardAccentTea, CardAccentPapaya)
    val accentColor = accentColors[accentIndex % accentColors.size]

    val cardBg = if (isDark) DarkSurface else WarmWhite
    val promptLabelBg = if (isDark) DarkSurfaceVariant else Papaya.copy(alpha = 0.8f)
    val promptLabelText = if (isDark) DarkOnSurface else BronzeDark
    val bodyText = if (isDark) DarkOnSurface else Charcoal
    val mutedText = if (isDark) DarkOnSurfaceVariant else CharcoalMuted

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onEdit
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
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
                // Warm gradient overlay — fixed: no fillMaxHeight in Row parent
                Box(
                    modifier = Modifier
                        .width(48.dp)
                        .fillMaxHeight()
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
                    .padding(18.dp)
            ) {
                // Header row: prompt label + share/delete actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.linearGradient(
                                    colors = if (isDark) {
                                        listOf(DarkBronze.copy(alpha = 0.25f), DarkSurfaceVariant)
                                    } else {
                                        listOf(Bronze.copy(alpha = 0.12f), Papaya.copy(alpha = 0.75f))
                                    }
                                ),
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
                    // Share + Delete + Edit icon buttons in header row
                    Row {
                        val editInteraction = remember { MutableInteractionSource() }
                        val editPressed by editInteraction.collectIsPressedAsState()
                        val editScale by animateFloatAsState(
                            targetValue = if (editPressed) 1.15f else 1f,
                            animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
                            label = "editScale"
                        )
                        IconButton(
                            onClick = onEdit,
                            modifier = Modifier.size(36.dp),
                            interactionSource = editInteraction
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = if (editPressed) (if (isDark) DarkBronze else Bronze) else mutedText,
                                modifier = Modifier
                                    .size(18.dp)
                                    .graphicsLayer { scaleX = editScale; scaleY = editScale }
                            )
                        }
                        val shareInteraction = remember { MutableInteractionSource() }
                        val sharePressed by shareInteraction.collectIsPressedAsState()
                        val shareScale by animateFloatAsState(
                            targetValue = if (sharePressed) 1.15f else 1f,
                            animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
                            label = "shareScale"
                        )
                        IconButton(
                            onClick = onShareClick,
                            modifier = Modifier.size(36.dp),
                            interactionSource = shareInteraction
                        ) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = "Share",
                                tint = if (sharePressed) (if (isDark) DarkBronze else Bronze) else mutedText,
                                modifier = Modifier
                                    .size(18.dp)
                                    .graphicsLayer { scaleX = shareScale; scaleY = shareScale }
                            )
                        }
                        val deleteInteraction = remember { MutableInteractionSource() }
                        val deletePressed by deleteInteraction.collectIsPressedAsState()
                        val deleteScale by animateFloatAsState(
                            targetValue = if (deletePressed) 1.15f else 1f,
                            animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
                            label = "deleteScale"
                        )
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(36.dp),
                            interactionSource = deleteInteraction
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = if (deletePressed) ErrorRed else mutedText,
                                modifier = Modifier
                                    .size(18.dp)
                                    .graphicsLayer { scaleX = deleteScale; scaleY = deleteScale }
                            )
                        }
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

                // Photo thumbnails
                if (memory.photo_urls.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = if (memory.photo_urls.size <= 1) Arrangement.Start else Arrangement.spacedBy(8.dp)
                    ) {
                        memory.photo_urls.take(3).forEach { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = "Memory photo",
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { onPhotoClick(url) },
                                onError = { }
                            )
                        }
                        val extraCount = memory.photo_urls.size - 3
                        if (extraCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .background(
                                        color = if (isDark) DarkBronze.copy(alpha = 0.35f) else Bronze.copy(alpha = 0.22f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { memory.photo_urls.getOrNull(3)?.let { onPhotoClick(it) } },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "+$extraCount",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isDark) DarkOnSurface else BronzeDark,
                                    fontWeight = FontWeight.SemiBold
                                )
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
        "${months[month]} ${ordinalOfMem(day)}, $year"
    } catch (e: Exception) {
        isoDate
    }
}

internal fun ordinalOfMem(n: Int): String {
    val d = n % 10
    val suffix = when {
        d == 1 && n != 11 -> "st"
        d == 2 && n != 12 -> "nd"
        d == 3 && n != 13 -> "rd"
        else -> "th"
    }
    return "$n$suffix"
}