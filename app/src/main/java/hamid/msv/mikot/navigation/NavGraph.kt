package hamid.msv.mikot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import hamid.msv.mikot.presentation.screen.chat.ChatScreen
import hamid.msv.mikot.presentation.screen.contact.ContactScreen
import hamid.msv.mikot.presentation.screen.home.HomeScreen
import hamid.msv.mikot.presentation.screen.login.LoginScreen
import hamid.msv.mikot.presentation.screen.profile.ProfileScreen
import hamid.msv.mikot.presentation.screen.register.RegisterScreen
import hamid.msv.mikot.presentation.screen.setting.SettingScreen
import hamid.msv.mikot.presentation.screen.splash.SplashScreen
import hamid.msv.mikot.presentation.screen.welcome.WelcomeScreen
import hamid.msv.mikot.util.CHAT_SCREEN_ARG_KEY

@Composable
@ExperimentalPagerApi
@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
fun SetupNavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route){
        composable(route = Screen.Splash.route){ SplashScreen(navController) }
        composable(route = Screen.Welcome.route){ WelcomeScreen(navController) }
        composable(route = Screen.SignUp.route){ RegisterScreen(navController) }
        composable(route = Screen.SignIn.route){ LoginScreen(navController) }
        composable(route = Screen.Home.route){ HomeScreen(navController) }
        composable(route = Screen.Contact.route){ ContactScreen(navController) }
        composable(route = Screen.Profile.route){ ProfileScreen(navController) }
        composable(route = Screen.Setting.route){ SettingScreen(navController) }
        composable(
            route = Screen.Chat.route ,
            arguments = listOf(navArgument(CHAT_SCREEN_ARG_KEY){ type = NavType.StringType })
        ) {
            val receiverId = it.arguments?.getString(CHAT_SCREEN_ARG_KEY)
            ChatScreen(navController, receiverId = receiverId)
        }
    }
}