package hamid.msv.mikot.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
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
import hamid.msv.mikot.util.NAVIGATION_ANIMATION_DURATION

@Composable
@ExperimentalPagerApi
@ExperimentalPermissionsApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
fun SetupNavGraph() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(navController = navController, startDestination = Screen.Splash.route){
        animatedComposable(route = Screen.Splash.route){ SplashScreen(navController) }
        animatedComposable(route = Screen.Welcome.route){ WelcomeScreen(navController) }
        animatedComposable(route = Screen.SignUp.route){ RegisterScreen(navController) }
        animatedComposable(route = Screen.SignIn.route){ LoginScreen(navController) }
        animatedComposable(route = Screen.Home.route){ HomeScreen(navController) }
        animatedComposable(route = Screen.Contact.route){ ContactScreen(navController) }
        animatedComposable(route = Screen.Profile.route){ ProfileScreen(navController) }
        animatedComposable(route = Screen.Setting.route){ SettingScreen(navController) }
        animatedComposable(
            route = Screen.Chat.route ,
            hasArgument = true,
            arguments = listOf(navArgument(CHAT_SCREEN_ARG_KEY){ type = NavType.StringType })
        ) {
            val receiverId = it.arguments?.getString(CHAT_SCREEN_ARG_KEY)
            ChatScreen(navController, receiverId = receiverId)
        }
    }
}

@ExperimentalAnimationApi
fun NavGraphBuilder.animatedComposable(
    route: String,
    hasArgument: Boolean = false,
    arguments: List<NamedNavArgument>? = null,
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    if (hasArgument){
        composable(
            route = route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left ,
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left ,
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right ,
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION)
                )
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right ,
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION)
                )
            },
            arguments = arguments!!,
            content = { content(it) }
        )
    }else{
        composable(
            route = route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left ,
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Left ,
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Right ,
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION)
                )
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentScope.SlideDirection.Right ,
                    animationSpec = tween(NAVIGATION_ANIMATION_DURATION)
                )
            },
            content = { content(it) }
        )
    }
}