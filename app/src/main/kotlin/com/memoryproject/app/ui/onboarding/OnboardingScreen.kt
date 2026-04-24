package com.memoryproject.app.ui.onboarding

import androidx.compose.animation.animateDpAsState
import androidx.compose.animation.animateFloatAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.foundation.isSystemInDarkTheme

private data class OnboardingPage(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val accentColor: Color
)

private val PAGES = listOf(
    OnboardingPage(
        emoji = "📖",
        title = "Every family has stories\nworth preserving",
        subtitle = "The moments, voices, and wisdom that make your family uniquely yours — preserved forever.",
        accentColor = Bronze
    ),
    OnboardingPage(
        emoji = "✨",
        title = "Write freely,\nat your own pace",
        subtitle = "Answer thoughtful prompts or simply start writing. No pressure, no structure — just your story.",
        accentColor = TeaGreen
    ),
    OnboardingPage(
        emoji = "🌿",
        title = "A gift that lasts\nfor generations",
        subtitle = "Every memory becomes a beautifully printed book — a keepsake your family will treasure for generations.",
        accentColor = BronzeLight
    )
)

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val isDark = isSystemInDarkTheme()
    val scaffoldBg = if (isDark) DarkBackground else Cornsilk
    val cardBg = if (isDark) DarkSurface else WarmWhite
    val primaryText = if (isDark) DarkOnSurface else Color(0xFF2D2D2D)
    val mutedText = if (isDark) DarkOnSurfaceVariant else Color(0xFF777777)

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
                        contentColor = if (isDark) DarkOnSurface else WarmWhite
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = if (isLastPage) 6.dp else 2.dp
                    )
                ) {
                    Text(
                        text = if (isLastPage) "Begin Your Story" else "Continue",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    if (!isLastPage) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.ArrowForward,
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Large emoji icon in decorative card
        Card(
            modifier = Modifier.size(140.dp),
            shape = RoundedCornerShape(36.dp),
            colors = CardDefaults.cardColors(containerColor = cardBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                page.accentColor.copy(alpha = 0.25f),
                                cardBg
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(page.emoji, fontSize = 72.sp)
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