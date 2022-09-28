package hamid.msv.mikot.navigation

sealed class Screen(val route: String) {
    object Splash : Screen(route = "splash_screen")
    object SignIn : Screen(route = "sign_in_screen")
    object SignUp : Screen(route = "sign_up_screen")
    object Home : Screen(route = "home_screen")
    object Chat : Screen(route = "chat_screen/{receiverId}") {
        fun passReceiverId(id: String): String =
            this.route.replace(oldValue = "{receiverId}", newValue = id)
    }
    object Profile : Screen(route = "profile_screen")
    object Welcome : Screen(route = "welcome_screen")
}
