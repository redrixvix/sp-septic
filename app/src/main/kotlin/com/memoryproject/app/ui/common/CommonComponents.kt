package com.memoryproject.app.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memoryproject.app.ui.theme.*
import org.koin.compose.koinInject
import com.memoryproject.app.data.preferences.PreferencesManager
import androidx.compose.foundation.BorderStroke

// ─── Premium Button ────────────────────────────────────────────────────────────

@Composable
fun PremiumButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    isPrimary: Boolean = true,
    icon: @Composable (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "btnScale"
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .height(52.dp)
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPrimary) Bronze else WarmWhite,
            contentColor = if (isPrimary) WarmWhite else Charcoal,
            disabledContainerColor = if (isPrimary) Bronze.copy(alpha = 0.4f) else WarmWhite.copy(alpha = 0.7f),
            disabledContentColor = if (isPrimary) WarmWhite.copy(alpha = 0.6f) else Charcoal.copy(alpha = 0.4f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isPrimary) 4.dp else 1.dp,
            pressedElevation = if (isPrimary) 8.dp else 4.dp
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = if (isPrimary) WarmWhite else Bronze,
                strokeWidth = 2.5.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text("Please wait...", style = MaterialTheme.typography.labelLarge)
        } else {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ─── Outline Button ─────────────────────────────────────────────────────────────

@Composable
fun OutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable (() -> Unit)? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Bronze),
        border = BorderStroke(1.5.dp, Bronze)
    ) {
        if (icon != null) {
            icon()
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ─── Premium Card ──────────────────────────────────────────────────────────────

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    backgroundColor: Color = WarmWhite,
    elevation: Int = 2,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedElevation by animateDpAsState(
        targetValue = if (isPressed) (elevation + 4).dp else elevation.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "cardElevation"
    )

    Card(
        modifier = modifier
            .then(
                if (onClick != null) Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                ) else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = animatedElevation)
    ) {
        Column(content = content)
    }
}

// ─── Shimmer Skeleton ──────────────────────────────────────────────────────────

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    widthFraction: Float = 1f,
    height: androidx.compose.ui.unit.Dp = 14.dp,
    cornerRadius: androidx.compose.ui.unit.Dp = 6.dp,
    baseColor: Color = Divider,
    highlightColor: Color = Divider.copy(alpha = 0.5f)
) {
    val shimmerAlpha = remember { Animatable(0.3f) }
    LaunchedEffect(Unit) {
        repeat(2) {
            shimmerAlpha.animateTo(0.7f, animationSpec = tween(900, easing = FastOutSlowInEasing))
            shimmerAlpha.animateTo(0.3f, animationSpec = tween(900, easing = FastOutSlowInEasing))
        }
        shimmerAlpha.animateTo(0.3f)
    }
    val shimmerBrush = remember(shimmerAlpha.value, baseColor, highlightColor) {
        Brush.linearGradient(listOf(baseColor, highlightColor.copy(alpha = shimmerAlpha.value), baseColor))
    }
    Box(
        modifier = modifier
            .fillMaxWidth(widthFraction)
            .height(height)
            .background(shimmerBrush, RoundedCornerShape(cornerRadius))
    )
}

@Composable
fun SkeletonCard(modifier: Modifier = Modifier, isDark: Boolean = false) {
    val cardBg = if (isDark) DarkSurface else WarmWhite
    val base = if (isDark) DarkDivider else Divider
    val highlight = if (isDark) DarkOnSurface.copy(alpha = 0.05f) else Divider.copy(alpha = 0.5f)

    val shimmerAlpha = remember { Animatable(0.3f) }
    LaunchedEffect(Unit) {
        repeat(2) {
            shimmerAlpha.animateTo(0.7f, animationSpec = tween(800, easing = FastOutSlowInEasing))
            shimmerAlpha.animateTo(0.3f, animationSpec = tween(800, easing = FastOutSlowInEasing))
        }
        shimmerAlpha.animateTo(0.3f)
    }
    val shimmerBrush = remember(shimmerAlpha.value, base, highlight) {
        Brush.linearGradient(listOf(base, highlight.copy(alpha = shimmerAlpha.value), base))
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(shimmerBrush, RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.fillMaxWidth(0.5f).height(14.dp).background(shimmerBrush, RoundedCornerShape(4.dp)))
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth(0.3f).height(12.dp).background(shimmerBrush, RoundedCornerShape(4.dp)))
            }
        }
    }
}

// ─── Empty State ───────────────────────────────────────────────────────────────

@Composable
fun EmptyState(
    emoji: String,
    title: String,
    subtitle: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    isDark: Boolean = false
) {
    val titleColor = if (isDark) DarkOnSurface else Charcoal
    val subtitleColor = if (isDark) DarkOnSurfaceVariant else CharcoalMuted
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = if (isDark) {
                            listOf(DarkBronze.copy(alpha = 0.3f), DarkSurface.copy(alpha = 0.3f))
                        } else {
                            listOf(Papaya.copy(alpha = 0.4f), Beige.copy(alpha = 0.3f))
                        }
                    ),
                    shape = RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(emoji, fontSize = 48.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = titleColor,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = subtitleColor,
            textAlign = TextAlign.Center
        )
        if (actionLabel != null && onAction != null) {
            Spacer(modifier = Modifier.height(28.dp))
            PremiumButton(
                text = actionLabel,
                onClick = onAction,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ─── Error State ───────────────────────────────────────────────────────────────

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    isDark: Boolean = false
) {
    Column(
        modifier = modifier.padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    color = ErrorRed.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                tint = ErrorRed,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (isDark) DarkOnSurface else Charcoal
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDark) DarkOnSurfaceVariant else CharcoalMuted,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton(
            onClick = onRetry,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Bronze)
        ) {
            Text("Try Again", fontWeight = FontWeight.SemiBold)
        }
    }
}

// ─── Loading Overlay ───────────────────────────────────────────────────────────

@Composable
fun LoadingOverlay(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Bronze,
                    strokeWidth = 3.dp
                )
            }
        }
    }
}

// ─── Section Title ─────────────────────────────────────────────────────────────

@Composable
fun SectionTitle(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    titleColor: Color = Charcoal,
    subtitleColor: Color = CharcoalMuted
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = titleColor,
            fontWeight = FontWeight.SemiBold
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = subtitleColor
            )
        }
    }
}

// ─── Divider Spacing ──────────────────────────────────────────────────────────

object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 16.dp
    val lg = 24.dp
    val xl = 32.dp
    val xxl = 48.dp
}