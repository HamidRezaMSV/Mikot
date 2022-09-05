package hamid.msv.mikot.presentation.screen.welcome

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.*
import hamid.msv.mikot.R
import hamid.msv.mikot.domain.model.OnBoardingPage
import hamid.msv.mikot.navigation.Screen
import hamid.msv.mikot.ui.theme.*
import hamid.msv.mikot.util.ON_BOARDING_PAGE_COUNT

@ExperimentalPagerApi
@Composable
fun WelcomeScreen(
    navController: NavHostController,
    viewModel: WelcomeViewModel = hiltViewModel()
) {

    val pages = listOf(OnBoardingPage.First, OnBoardingPage.Second, OnBoardingPage.Third)
    val pagerState = rememberPagerState()

    val activeIndicatorColor = when(pagerState.currentPage){
        0 -> { MaterialTheme.colors.activeWelcomeIndicator1Color }
        1 -> { MaterialTheme.colors.activeWelcomeIndicator2Color }
        else -> { MaterialTheme.colors.activeWelcomeIndicator3Color }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.welcomeScreenBackgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            modifier = Modifier.weight(9f),
            count = ON_BOARDING_PAGE_COUNT,
            state = pagerState,
            verticalAlignment = Alignment.CenterVertically
        ) { position -> PageContent(onBoardingPage = pages[position]) }

        HorizontalPagerIndicator(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally),
            pagerState = pagerState,
            activeColor = activeIndicatorColor,
            inactiveColor = MaterialTheme.colors.inActiveWelcomeIndicatorColor,
            indicatorWidth = PAGER_INDICATOR_WIDTH,
            spacing = PAGER_INDICATOR_SPACE
        )

        FinishButton(
            modifier = Modifier
                .requiredHeight(FINISH_BUTTON_HEIGHT)
                .weight(1f),
            pagerState = pagerState,
            onFinishClicked = {
                viewModel.saveOnBoardingState(completed = true)
                navController.popBackStack()
                navController.navigate(Screen.SignUp.route)
            }
        )
    }

}

@Composable
fun PageContent(onBoardingPage: OnBoardingPage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MEDIUM_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            modifier = Modifier
                .background(color = Color.White)
                .fillMaxWidth()
                .aspectRatio(1f),
            painter = painterResource(id = onBoardingPage.image),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = LARGE_PADDING),
            text = stringResource(id = onBoardingPage.description),
            fontWeight = FontWeight.SemiBold,
            fontSize = MaterialTheme.typography.subtitle1.fontSize,
            color = MaterialTheme.colors.welcomeDescriptionColor,
            textAlign = TextAlign.Center
        )
    }
}

@ExperimentalPagerApi
@Composable
fun FinishButton(modifier: Modifier, pagerState: PagerState, onFinishClicked: () -> Unit) {
    val enterAnimation: EnterTransition = fadeIn(animationSpec = tween(500)) +
        expandVertically(expandFrom = Alignment.Top, animationSpec = tween(500))

    val exitAnimation : ExitTransition = fadeOut(animationSpec = tween(500)) +
        shrinkVertically(animationSpec = tween(500), shrinkTowards = Alignment.Bottom)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = LARGE_PADDING),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            modifier = modifier.fillMaxWidth(),
            visible = pagerState.currentPage == pagerState.pageCount - 1,
            enter = enterAnimation,
            exit = exitAnimation
        ) {
            Card(
                shape = RoundedCornerShape(size = FINISH_BUTTON_CORNER_RADIUS) ,
                elevation = FINISH_BUTTON_ELEVATION
            ) {
                Button(
                    onClick = { onFinishClicked() },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.finishButtonBackgroundColor,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.finish) ,
                        fontSize = MaterialTheme.typography.h6.fontSize
                    )
                }
            }
        }
    }
}


