package com.memoryproject.app.ui.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocalLibrary
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.pointerInput
import com.memoryproject.app.ui.theme.Bronze
import com.memoryproject.app.ui.theme.BronzeLight
import com.memoryproject.app.ui.theme.Charcoal
import com.memoryproject.app.ui.theme.CharcoalMuted
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
        subtitle = "Gather the moments, stories, and wisdom that make your family who they are.",
        accentColor = Bronze
    ),
    OnboardingPage(
        icon = Icons.Default.AutoAwesome,
        title = "Write in your own\nwords, your own time",
        subtitle = "Answer guided prompts or write freely — your stories, in your voice.",
        accentColor = TeaGreen
    ),
    OnboardingPage(
        icon = Icons.Default.LocalLibrary,
        title = "Preserved forever,\ntreasured always",
        subtitle = "Every story is beautifully preserved — a timeless keepsake for generations.",
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
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Skip button — top right, safe area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
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
                // Dots — tappable for direct page navigation
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(PAGES.size) { index ->
                        val isSelected = pagerState.currentPage == index
                        val size by animateDpAsState(
                            targetValue = if (isSelected) 10.dp else 6.dp,
                            animationSpec = spring(stiffness = 600f),
                            label = "dotSize"
                        )
                        val dotScale by animateFloatAsState(
                            targetValue = if (isSelected) 1.15f else 1f,
                            animationSpec = spring(stiffness = 300f),
                            label = "dotScale"
                        )
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .scale(dotScale)
                                .clip(CircleShape)
                                .background(Color.Transparent)
                                .pointerInput(index) {
                                    detectTapGestures(
                                        onTap = {
                                            coroutineScope.launch {
                                                pagerState.animateScrollToPage(index)
                                            }
                                        }
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(size)
                                    .scale(dotScale)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) PAGES[index].accentColor else mutedText.copy(alpha = 0.5f),
                                        shape = CircleShape
                                    )
                            )
                        }
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
    primaryText: Color,
    mutedText: Color,
    cardBg: Color
) {
    val accentColor = page.accentColor

    // Mobile-first: icon + text stacked, no overflow, always safe
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top breathing room — keeps icon away from very top
        Spacer(modifier = Modifier.height(24.dp))

        // Icon card — compact, centered, no overflow
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            // Subtle radial glow behind icon
            Box(
                modifier = Modifier
                    .size(116.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                accentColor.copy(alpha = 0.22f),
                                accentColor.copy(alpha = 0.04f),
                                accentColor.copy(alpha = 0.22f)
                            )
                        ),
                        shape = RoundedCornerShape(30.dp)
                    )
            )
            // Icon card — white card floating in glow
            Card(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(accentColor.copy(alpha = 0.16f), cardBg)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = page.icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(52.dp)
                    )
                }
            }
        }

        // Space between icon and text
        Spacer(modifier = Modifier.height(32.dp))

        // Title — scaled for mobile
        Text(
            text = page.title,
            style = MaterialTheme.typography.titleLarge,
            color = primaryText,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Subtitle — smaller, safe at bottom
        Text(
            text = page.subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = mutedText,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        // Push dots/button area down when content is short
        Spacer(modifier = Modifier.height(24.dp))
    }
}