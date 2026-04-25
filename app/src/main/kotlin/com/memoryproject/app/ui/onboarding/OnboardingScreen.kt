package com.memoryproject.app.ui.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memoryproject.app.ui.theme.Bronze
import com.memoryproject.app.ui.theme.BronzeLight
import com.memoryproject.app.ui.theme.Cornsilk
import com.memoryproject.app.ui.theme.DarkBackground
import com.memoryproject.app.ui.theme.DarkOnSurface
import com.memoryproject.app.ui.theme.DarkOnSurfaceVariant
import com.memoryproject.app.ui.theme.DarkSurface
import com.memoryproject.app.ui.theme.TeaGreen
import com.memoryproject.app.ui.theme.WarmWhite
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

private data class OnboardingPage(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val subtitle: String,
    val accentColor: Color
)

private val PAGES = listOf(
    OnboardingPage(
        icon = Icons.AutoMirrored.Filled.MenuBook,
        title = "Your family's stories\ndeserve to live forever",
        subtitle = "Gather the moments, stories, and wisdom that make your family who they are — and preserve them forever.",
        accentColor = Bronze
    ),
    OnboardingPage(
        icon = Icons.Default.AutoAwesome,
        title = "Write in your own\nwords, your own time",
        subtitle = "Answer guided prompts or write freely — your stories, in your voice, on your own time.",
        accentColor = TeaGreen
    ),
    OnboardingPage(
        icon = Icons.Default.LocalLibrary,
        title = "A keepsake\nyour family will keep",
        subtitle = "Every story is beautifully preserved — a timeless keepsake your family will treasure for generations.",
        accentColor = BronzeLight
    )
)

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    darkTheme: Boolean,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val isDark = darkTheme
    val scaffoldBg = if (isDark) DarkBackground else Cornsilk
    val cardBg = if (isDark) DarkSurface else WarmWhite
    val primaryText = if (isDark) DarkOnSurface else Charcoal
    val mutedText = if (isDark) DarkOnSurfaceVariant else CharcoalMuted

    val pagerState = rememberPagerState(pageCount = { PAGES.size })
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(scaffoldBg)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Skip button — top right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onComplete,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        "Skip",
                        style = MaterialTheme.typography.bodyMedium,
                        color = mutedText
                    )
                }
            }

            // Pager
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { page ->
                val p = PAGES[page]
                OnboardingPageContent(
                    page = p,
                    isDark = isDark,
                    primaryText = primaryText,
                    mutedText = mutedText,
                    cardBg = cardBg
                )
            }

            // Page indicators + CTA
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(PAGES.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        val size by animateDpAsState(
                            targetValue = if (isSelected) 10.dp else 6.dp,
                            animationSpec = spring(stiffness = Spring.StiffnessHigh),
                            label = "dotSize"
                        )
                        val dotScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.15f else 1f,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium),
                            label = "dotScale"
                        )
                        Box(
                            modifier = Modifier
                                .size(size)
                                .scale(dotScale)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) PAGES[index].accentColor else mutedText.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // CTA button
                val isLastPage = pagerState.currentPage == PAGES.size - 1
                Button(
                    onClick = {
                        if (isLastPage) {
                            viewModel.completeOnboarding()
                            onComplete()
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLastPage) Bronze else cardBg,
                        contentColor = if (isLastPage) WarmWhite else if (isDark) DarkOnSurface else Bronze
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = if (isLastPage) 6.dp else 2.dp
                    )
                ) {
                    Text(
                        text = if (isLastPage) "Start Capturing" else "Continue",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (!isLastPage) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OnboardingPageContent(
    page: OnboardingPage,
    isDark: Boolean,
    primaryText: Color,
    mutedText: Color,
    cardBg: Color
) {
    // Static gradient ring — warm brand accent behind icon (rotation removed for performance)
    val ringColor = page.accentColor

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Rotating gradient ring — warm brand accent behind icon
        Box(
            modifier = Modifier.size(160.dp),
            contentAlignment = Alignment.Center
        ) {
            // Outer ring — static warm gradient accent
            Box(
                modifier = Modifier
                    .size(156.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                page.accentColor.copy(alpha = 0.35f),
                                page.accentColor.copy(alpha = 0.08f),
                                page.accentColor.copy(alpha = 0.35f)
                            )
                        ),
                        shape = RoundedCornerShape(40.dp)
                    )
            )
            // Inner icon card — floats in front of ring
            Card(
                modifier = Modifier.size(140.dp),
                shape = RoundedCornerShape(36.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    page.accentColor.copy(alpha = 0.2f),
                                    cardBg
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = page.icon,
                        contentDescription = null,
                        tint = page.accentColor,
                        modifier = Modifier.size(72.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            color = primaryText,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = page.subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = mutedText,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
        )
    }
}