package hamid.msv.mikot.presentation.screen.setting

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun SettingScreen(navController: NavHostController) {

    BackHandler { navController.popBackStack() }

}