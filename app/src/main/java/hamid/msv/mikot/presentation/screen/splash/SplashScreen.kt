package hamid.msv.mikot.presentation.screen.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import hamid.msv.mikot.R
import hamid.msv.mikot.navigation.Screen
import hamid.msv.mikot.ui.theme.splashAppNameColor
import hamid.msv.mikot.util.SPLASH_LOTTIE_ANIMATION_SPEED
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val onBoardingCompleted by viewModel.onBoardingCompleted.collectAsState()

    LaunchedEffect(key1 = true) {

        delay(3000)

        navController.popBackStack()
        when{
            onBoardingCompleted -> { navController.navigate(Screen.Home.route) }
            else -> { navController.navigate(Screen.Welcome.route) }
        }
    }

    SplashContent()
}

@Composable
fun SplashContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.weight(0.8f)
        ) {
            LottieSplashAnimation()
        }
        Text(
            modifier = Modifier.weight(0.2f),
            text = stringResource(id = R.string.app_name),
            fontSize = MaterialTheme.typography.h4.fontSize,
            fontWeight = FontWeight.Bold ,
            color = MaterialTheme.colors.splashAppNameColor
        )
    }
}

@Composable
fun LottieSplashAnimation() {
    val spec = LottieCompositionSpec.RawRes(R.raw.splash_lottie_animation)
    val composition by rememberLottieComposition(spec = spec)
    val progress by animateLottieCompositionAsState(
        composition = composition,
        speed = SPLASH_LOTTIE_ANIMATION_SPEED,
        clipSpec = LottieClipSpec.Progress(max = 0.9f)
    )
    LottieAnimation(composition = composition, progress = progress)
}

@Composable
@Preview(showBackground = true)
fun SplashContentPreview() {
    SplashContent()
}