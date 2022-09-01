package hamid.msv.mikot.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hamid.msv.mikot.presentation.screen.splash.SplashScreen
import hamid.msv.mikot.presentation.screen.welcome.WelcomeScreen

@Composable
fun SetupNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route){
        composable(route = Screen.Splash.route){ SplashScreen(navController) }
        composable(route = Screen.Welcome.route){ WelcomeScreen() }
        composable(route = Screen.SignUp.route){  }
        composable(route = Screen.SignIn.route){  }
        composable(route = Screen.Home.route){  }
        composable(route = Screen.Chat.route){  }
        composable(route = Screen.Profile.route){  }
    }
}