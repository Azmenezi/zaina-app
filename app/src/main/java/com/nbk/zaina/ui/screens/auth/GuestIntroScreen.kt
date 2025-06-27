package com.nbk.zaina.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.nbk.rise.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController

@Composable
fun GuestIntroScreen(navController: NavHostController) {
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF101010), Color(0xFF303030))
                )
            )
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                count = pages.size,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                val item = pages[page]
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .graphicsLayer {
                            shape = RoundedCornerShape(24.dp)
                            clip = true
                        }
                        .background(
                            color = Color.White.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .shadow(12.dp, shape = RoundedCornerShape(24.dp), clip = false)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = item.imageRes),
                            contentDescription = null,
                            modifier = Modifier.size(220.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            item.title,
                            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 24.sp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            item.description,
                            textAlign = TextAlign.Center,
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = (pagerState.currentPage + 1) / pages.size.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RectangleShape),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (pagerState.currentPage == 0) {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
                ) {
                    Text(
                        text = pages[1].title,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
                    ) {
                        Text("Back", maxLines = 1, color = Color.White)
                    }

                    Button(
                        onClick = {
                            val currentPage = pagerState.currentPage
                            if (currentPage == pages.lastIndex) {
                                navController.navigate("register")
                            } else {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(currentPage + 1)
                                }
                            }
                        },
                        modifier = Modifier.weight(2f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = if (pagerState.currentPage == pages.lastIndex) {
                                "Apply Now"
                            } else {
                                pages[pagerState.currentPage + 1].title
                            },
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

data class GuestIntroPage(val title: String, val description: String, val imageRes: Int)
