package hamid.msv.mikot.presentation.screen.chat

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import hamid.msv.mikot.Application
import hamid.msv.mikot.R
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.presentation.component.ChatTextField
import hamid.msv.mikot.presentation.component.ChatTopBar
import hamid.msv.mikot.presentation.component.MessageItem
import hamid.msv.mikot.ui.theme.SMALL_PADDING

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatScreen(
    navController: NavHostController,
    receiverId: String?,
    viewModel: ChatViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    if (receiverId == null) {
        Toast.makeText(
            context,
            stringResource(id = R.string.null_receiver_id_error),
            Toast.LENGTH_SHORT
        ).show()
        navController.popBackStack()
    }

    val messages = viewModel.messages.collectAsState()
    val receiverUser = viewModel.receiverUser.collectAsState()

    BackHandler {
        navController.popBackStack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = {
            ChatScreenContent(
                messages = messages.value,
                onMessageClick = { text -> viewModel.copyTextToClipBoard(text,context) }
            )
        },
        backgroundColor = Color.White,
        topBar = { receiverUser.value?.let { ChatTopBar(user = it) } },
        bottomBar = {
            ChatTextField(
                onSendClicked = { text ->
                    receiverUser.value?.let { viewModel.sendNewMessage(text = text, receiverUser = it) }
                }
            )
        }
    )
}

@Composable
fun ChatScreenContent(messages: List<Message> , onMessageClick: (text:String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            reverseLayout = true,
            contentPadding = PaddingValues(bottom = SMALL_PADDING)
        ) {
            items(messages.reversed()) { message ->
                MessageItem(
                    isMe = message.senderId == Application.currentUserId,
                    text = message.text!!,
                    time = message.time!!,
                    onMessageClick = { text ->  onMessageClick(text) }
                )
            }
        }
    }
}