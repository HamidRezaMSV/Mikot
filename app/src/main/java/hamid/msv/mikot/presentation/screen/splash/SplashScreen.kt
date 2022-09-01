package hamid.msv.mikot.presentation.screen.splash

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun SplashScreen(
    navController : NavHostController ,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val onBoardingState by viewModel.onBoardingState.collectAsState()
    Log.d("Splash_Hamid" , onBoardingState.toString())
}