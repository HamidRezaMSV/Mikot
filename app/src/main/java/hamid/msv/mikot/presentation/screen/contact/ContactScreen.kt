package hamid.msv.mikot.presentation.screen.contact

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import hamid.msv.mikot.Application
import hamid.msv.mikot.navigation.Screen
import hamid.msv.mikot.presentation.component.ContactScreenItem
import hamid.msv.mikot.ui.theme.EXTRA_SMALL_PADDING

@Composable
fun ContactScreen(
    navController: NavHostController,
    viewModel: ContactViewModel = hiltViewModel()
) {

    val userList = viewModel.userList.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize() ,
        contentPadding = PaddingValues(EXTRA_SMALL_PADDING)
    ){
        items(userList.value){ user ->
            ContactScreenItem(
                user = user,
                onItemSelected = { userId ->
                    Application.receiverId = userId
                    navController.popBackStack()
                    navController.navigate(Screen.Chat.passReceiverId(userId))
                }
            )
        }
    }
}