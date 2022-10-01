package hamid.msv.mikot.presentation.screen.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import hamid.msv.mikot.R
import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.navigation.Screen
import hamid.msv.mikot.presentation.component.HomeFAB
import hamid.msv.mikot.presentation.component.HomeScreenItem
import hamid.msv.mikot.presentation.component.HomeTopBar
import hamid.msv.mikot.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val connectionState = viewModel.connectionState.collectAsState()
    val lastMessages = viewModel.lastMessages.collectAsState()

    Scaffold(
        floatingActionButton = {
            HomeFAB(onClick = { navController.navigate(Screen.Contact.route) })
        },
        topBar = { HomeTopBar(connectionState.value) },
        backgroundColor = Color.White,
        content = {
            HomeScreenContent(
                lastMessages = lastMessages.value,
                onItemSelected = { userId ->
                    navController.navigate(Screen.Chat.passReceiverId(userId))
                }
            )
        }
    )
}

@Composable
fun HomeScreenContent(lastMessages: List<LastMessage>,onItemSelected: (userId:String) -> Unit) {
    if (lastMessages.isNotEmpty()){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ){
            items(lastMessages){ item ->
                HomeScreenItem(
                    lastMessage = item,
                    onItemSelected = { userId ->  onItemSelected(userId) }
                )
            }
        }
    }else{
        EmptyHomeScreenContent()
    }
}

@Composable
fun EmptyHomeScreenContent() {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(1f)
            .padding(EXTRA_LARGE_PADDING),
        backgroundColor = Color.White ,
        shape = RoundedCornerShape(size = EMPTY_HOME_SCREEN_CARD_CORNER_RADIUS),
        elevation = EMPTY_HOME_SCREEN_CARD_ELEVATION
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.fillMaxWidth( ),
                painter = painterResource(id = R.drawable.ic_empty_list),
                contentDescription = null,
                tint = Color.Black.copy(0.3f)
            )

            Spacer(modifier = Modifier.height(LARGE_PADDING))

            Text(
                text = stringResource(R.string.no_chat_exists),
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(0.4f),
                fontSize = MaterialTheme.typography.body1.fontSize
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyHomeScreenPreview() {
    EmptyHomeScreenContent()
}
