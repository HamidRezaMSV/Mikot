package hamid.msv.mikot.presentation.screen.chat

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import hamid.msv.mikot.data.remote.FirebaseApi.MESSAGE_DATABASE
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.presentation.component.ChatTextField
import hamid.msv.mikot.presentation.component.MessageItem
import hamid.msv.mikot.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

const val receiver = "gQPmtPlZO9PM0S24a8aIRcKS1Pk1"
const val sender = "kcojkPMpdtaEeHhHxWq2WsBXXv93"
const val key1 = sender + receiver
const val key2 = receiver + sender

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ChatScreen(
    navController: NavHostController,
    viewModel: ChatViewModel = hiltViewModel()
) {

    var list by remember { mutableStateOf(listOf<Message>()) }

    LaunchedEffect(key1 = list){
//        this.launch(Dispatchers.IO) {
//            viewModel.a().execute(key1).onEach {  data ->
//                list += data
//                Log.d("mikot_get" , data.size.toString())
//            }.launchIn(this)
//        }
    }


    ChatScreenContent(
        messages = list ,
        onSendClicked = {
            val message = Message(
                id = MESSAGE_DATABASE.child(key1).key,
                text = it,
                time = System.currentTimeMillis().toString(),
                senderId = sender,
                receiverId = receiver
            )
            // Send message :
            MESSAGE_DATABASE.child(key1).push().setValue(message)
            MESSAGE_DATABASE.child(key2).push().setValue(message)
            //viewModel.getALlMessages(key1)
        }
    )



//    var messages by remember { mutableStateOf(listOf<Message>()) }
//
//    MESSAGE_DATABASE.child(key1).addChildEventListener(object : ChildEventListener{
//        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//            val message = snapshot.getValue(Message::class.java) ?: return
//            Log.d("Mikot_Chat" , message.text!!)
//            val buffer = listOf(message)
//            messages = messages + buffer
//        }
//        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
//        override fun onChildRemoved(snapshot: DataSnapshot) {}
//        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
//        override fun onCancelled(error: DatabaseError) {}
//    })

}

@Composable
fun ChatScreenContent(messages:List<Message> , onSendClicked : (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize() ,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) ,
            reverseLayout = true ,
            contentPadding = PaddingValues(bottom = SMALL_PADDING)
        ){
            items(messages.reversed()) { item -> MessageItem(isMe = item.senderId == sender, text = item.text!! , time = item.time!!) }
        }

        Box(
            modifier = Modifier.fillMaxWidth() , contentAlignment = Alignment.Center
        ) {
            ChatTextField(onSendClicked = { onSendClicked(it) })
        }
    }
}