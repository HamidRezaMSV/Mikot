package hamid.msv.mikot.presentation.screen.chat

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun ChatScreen(
    navController: NavHostController,
    receiverId: String?,
    viewModel: ChatViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    if (receiverId == null) {
        Toast.makeText(context, stringResource(id = R.string.null_receiver_id_error), Toast.LENGTH_SHORT).show()
        navController.popBackStack()
    }

    val messages = viewModel.messages.collectAsState()
    val receiverUser = viewModel.receiverUser.collectAsState()

    val topBarExpanded = remember{ mutableStateOf(false) }

    BackHandler {
        navController.popBackStack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = {
            ChatScreenContent(
                messages = messages.value,
                onMessageLongClick = { text ->
                    viewModel.copyTextToClipBoard(text,context)
                    Toast.makeText(context, context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
                },
                onReplyMessage = { repliedMessageId ->
                    // todo : implement logic for reply to specific message
                }
            )
        },
        backgroundColor = Color.White,
        topBar = {
            receiverUser.value?.let {
                ChatTopBar(
                    user = it,
                    expanded = topBarExpanded,
                    onBackClick = { navController.popBackStack() },
                    onExpandClick = { topBarExpanded.value = !topBarExpanded.value }
                )
            }
        },
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
@ExperimentalMaterialApi
fun ChatScreenContent(
    messages: List<Message> ,
    onMessageLongClick: (text:String) -> Unit,
    onReplyMessage: (repliedMessageId:String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp),
        reverseLayout = true,
        contentPadding = PaddingValues(bottom = SMALL_PADDING)
    ) {
        items(messages.reversed()) { message ->
            val swipeState = rememberDismissState()

            if (swipeState.isDismissed(DismissDirection.StartToEnd)){
                LaunchedEffect(key1 = true){ swipeState.reset() }
                onReplyMessage(message.id!!)
                Log.d("MIKOT_CHAT" , "Should reply message with id : ${message.id}")
            }

            SwipeToDismiss(
                state = swipeState,
                directions = setOf(DismissDirection.StartToEnd),
                dismissThresholds = { direction -> FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.2f else 0.05f) },
                background = { Surface(color = Color.White) {} },
            ){
                MessageItem(
                    isMe = message.senderId == Application.currentUserId,
                    text = message.text!!,
                    time = message.time!!,
                    onMessageLongClick = { text ->  onMessageLongClick(text) }
                )
            }
        }
    }
}