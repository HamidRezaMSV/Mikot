package hamid.msv.mikot.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import hamid.msv.mikot.presentation.screen.home.HomeScreen
import hamid.msv.mikot.presentation.screen.login.LoginScreen
import hamid.msv.mikot.presentation.screen.register.RegisterScreen
import hamid.msv.mikot.presentation.screen.splash.SplashScreen
import hamid.msv.mikot.presentation.screen.welcome.WelcomeScreen

@ExperimentalPagerApi
@Composable
fun SetupNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Home.route){
        composable(route = Screen.Splash.route){ SplashScreen(navController) }
        composable(route = Screen.Welcome.route){ WelcomeScreen(navController) }
        composable(route = Screen.SignUp.route){ RegisterScreen(navController) }
        composable(route = Screen.SignIn.route){ LoginScreen(navController) }
        composable(route = Screen.Home.route){ HomeScreen(navController) }
        composable(route = Screen.Chat.route){  }
        composable(route = Screen.Profile.route){  }
    }
}