package hamid.msv.mikot.presentation.screen.profile

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(navController: NavHostController) {

    BackHandler { navController.popBackStack() }

}