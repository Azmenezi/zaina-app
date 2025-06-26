package com.nbk.zaina.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.nbk.rise.R

@Composable
fun GuestIntroScreen(onFinish: () -> Unit) {
    val pages = listOf(
        GuestIntroPage(
            title = "Welcome to RISE",
            description = "A global women leadership initiative by NBK.\nLet’s change mindsets and empower future women leaders.",
            imageRes = R.drawable.intro_1
        ),
        GuestIntroPage(
            title = "How It Started",
            description = "Shaikha Al-Bahar’s call to action sparked a movement by women, for women, to close the leadership gender gap.",
            imageRes = R.drawable.intro_2
        ),
        GuestIntroPage(
            title = "The Journey",
            description = "From the inaugural exhibition to global training, RISE has empowered over 45 participants from 7+ countries.",
            imageRes = R.drawable.intro_3
        ),
        GuestIntroPage(
            title = "Join the Movement",
            description = "Explore the impact, and when you're ready — apply to be part of the next generation of women leaders.",
            imageRes = R.drawable.intro_4
        )
    )

    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val item = pages[page]
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = item.imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(item.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(item.description, textAlign = TextAlign.Center)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Indicator
        LinearProgressIndicator(
            progress = (pagerState.currentPage + 1) / pages.size.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (pagerState.currentPage == 0) {
            // Only show one full-width button on first page
            Button(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = pages[1].title,
                    maxLines = 1,
                    softWrap = false,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Back Button
                Button(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Back", maxLines = 1)
                }

                // Next or Finish Button
                Button(
                    onClick = {
                        val currentPage = pagerState.currentPage
                        if (currentPage == pages.lastIndex) {
                            onFinish()
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(currentPage + 1)
                            }
                        }
                    },
                    modifier = Modifier.weight(2f)
                ) {
                    Text(
                        text = if (pagerState.currentPage == pages.lastIndex) {
                            "Apply Now"
                        } else {
                            pages[pagerState.currentPage + 1].title
                        },
                        maxLines = 1,
                        softWrap = false,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

data class GuestIntroPage(val title: String, val description: String, val imageRes: Int)
