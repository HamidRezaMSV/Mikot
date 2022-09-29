package hamid.msv.mikot.presentation.screen.home

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import hamid.msv.mikot.navigation.Screen
import hamid.msv.mikot.presentation.component.HomeFAB
import hamid.msv.mikot.presentation.component.HomeTopBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController
) {

    Scaffold(
        floatingActionButton = {
            HomeFAB(onClick = { navController.navigate(Screen.Contact.route) })
        },
        topBar = { HomeTopBar() },
        backgroundColor = Color.White,
        content = { }
    )
}
