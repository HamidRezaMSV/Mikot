package hamid.msv.mikot.presentation.screen.chat

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import hamid.msv.mikot.util.vibratePhone
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

    val hasReply = remember{ mutableStateOf(false) }
    val repliedMessageText: MutableState<String?> = remember { mutableStateOf(null) }
    val repliedMessageId: MutableState<String?> = remember { mutableStateOf(null) }

    BackHandler { navController.popBackStack() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = {
            ChatScreenContent(
                messages = messages.value,
                context = context,
                onMessageLongClick = { text ->
                    viewModel.copyTextToClipBoard(text,context)
                    Toast.makeText(context, context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
                },
                onReplyMessage = { repliedMsgId,repliedMsgText ->
                    hasReply.value = true
                    repliedMessageText.value = repliedMsgText
                    repliedMessageId.value = repliedMsgId
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
                hasReply = hasReply,
                repliedMessageText = repliedMessageText.value ,
                onSendClicked = { text ->
                    if (hasReply.value){
                        receiverUser.value?.let {
                            Log.d("MIKOT_CHAT" , "send with reply executed")
                            viewModel.sendNewMessage(
                                text = text,
                                receiverUser = it,
                                isReply = true,
                                repliedMessageId = repliedMessageId.value!!
                            )
                        }
                    }else{
                        receiverUser.value?.let { viewModel.sendNewMessage(text = text, receiverUser = it) }
                    }
                }
            )
        }
    )
}

@Composable
@ExperimentalMaterialApi
fun ChatScreenContent(
    messages: List<Message>,
    context: Context,
    onMessageLongClick: (text: String) -> Unit,
    onReplyMessage: (repliedMessageId: String,repliedMessageText: String) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(bottom = 78.dp),
        state = listState,
        reverseLayout = true,
        contentPadding = PaddingValues(bottom = SMALL_PADDING)
    ) {
        items(messages.reversed()) { message ->
            val swipeState = rememberDismissState()

            if (swipeState.isDismissed(DismissDirection.StartToEnd)){
                LaunchedEffect(key1 = true){ swipeState.reset() }
                context.vibratePhone()
                onReplyMessage(message.id!!,message.text!!)
            }

            SwipeToDismiss(
                state = swipeState,
                directions = setOf(DismissDirection.StartToEnd),
                dismissThresholds = { direction -> FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.2f else 0.05f) },
                background = { Surface(color = Color.White) {} },
            ){
                if (message.isReply){
                    var repliedMessageText by remember { mutableStateOf("") }
                    LaunchedEffect(key1 = true){
                        repliedMessageText = messages.first { it.id == message.repliedMessageId }.text.toString()
                    }
                    MessageItem(
                        isMe = message.senderId == Application.currentUserId,
                        text = message.text!!,
                        time = message.time!!,
                        hasReply = true,
                        repliedMessageText = repliedMessageText,
                        repliedMessageId = message.repliedMessageId,
                        onMessageLongClick = { text ->  onMessageLongClick(text) },
                        onRepliedMessageClick = { repliedMessageId ->
                            coroutine.launch {
                                val msg = messages.reversed().first{ it.repliedMessageId == repliedMessageId }
                                val msgIndex = messages.indexOf(msg)
                                listState.animateScrollToItem(index = msgIndex)
                            }
                        }
                    )
                }else{
                    MessageItem(
                        isMe = message.senderId == Application.currentUserId,
                        text = message.text!!,
                        time = message.time!!,
                        onMessageLongClick = { text ->  onMessageLongClick(text) },
                        onRepliedMessageClick = {  }
                    )
                }
            }
        }
    }
}