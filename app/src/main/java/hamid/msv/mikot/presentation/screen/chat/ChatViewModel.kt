package hamid.msv.mikot.presentation.screen.chat

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.Application
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.domain.usecase.GetAllMessagesUseCase
import hamid.msv.mikot.domain.usecase.GetUserByIdUseCase
import hamid.msv.mikot.domain.usecase.SaveAllMessagesUseCase
import hamid.msv.mikot.domain.usecase.SendNewMessageUseCase
import hamid.msv.mikot.util.USER_IS_NOT_LOGIN
import hamid.msv.mikot.util.copyTextToClipBoard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getAllMessagesUseCase: GetAllMessagesUseCase,
    private val sendNewMessageUseCase: SendNewMessageUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val saveAllMessagesUseCase: SaveAllMessagesUseCase
): ViewModel() {

    private val receiverId = Application.receiverId!!
    private val senderId = Application.currentUserId!!

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages : StateFlow<List<Message>> = _messages.asStateFlow()

    private val _receiverUser = MutableStateFlow<MikotUser?>(null)
    val receiverUser = _receiverUser.asStateFlow()

    init {
        fetchReceiverInfo()
        listenForMessages()
        fetchMessagesFromDB()
        editReceivedMessages()
        listenForSendNewMessageResponse()
    }

    fun sendNewMessage(text: String, receiverUser: MikotUser){
        viewModelScope.launch {
            if (senderId != USER_IS_NOT_LOGIN){
                val message = Message(
                    id = UUID.randomUUID().toString(),
                    text = text ,
                    time = System.currentTimeMillis().toString(),
                    senderId = senderId ,
                    receiverId = receiverId,
                    senderUsername = Application.currentUser!!.userName,
                    receiverUsername = receiverUser.userName
                )
                sendNewMessageUseCase.execute(message,senderId,receiverId)
            }else{
                Log.d("MIKOT_CHAT" , "senderId is invalid for sending new message")
            }
        }
    }

    fun copyTextToClipBoard(text: String , context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            context.copyTextToClipBoard(text)
        }
    }

    private fun fetchReceiverInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            getUserByIdUseCase.execute(receiverId).collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            _receiverUser.value = response.data!!
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_CHAT" , response.error.toString())
                        }
                    }
                }
            }
        }
    }

    private fun fetchMessagesFromDB(){
        val path = receiverId + senderId
        viewModelScope.launch(Dispatchers.IO) {
            getAllMessagesUseCase.executeFromDB(path).collectLatest {
                if (it.isNotEmpty()){
                    _messages.value = it.map { roomMessage -> roomMessage.mapToMessage() }
                }
            }
        }
    }

    private fun editReceivedMessages() {
        viewModelScope.launch {
            getAllMessagesUseCase.messagesFromServer.collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            response.data?.let { messageList ->
                                val validList = messageList.map { message ->
                                    if (!message.time!!.contains(":")){
                                        message.time = parseTime(message.time!!.toLong())
                                    }
                                    message
                                }
                                _messages.value = validList
                                saveAllMessagesUseCase.execute(validList.map { message -> message.mapToRoomMessage() })
                            }
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_CHAT" , response.error.toString())
                        }
                    }
                }
            }
        }
    }

    private fun listenForMessages(){
        viewModelScope.launch {
            getAllMessagesUseCase.executeFromServer(senderId+receiverId)
        }
    }

    private fun parseTime(time:Long): String{
        return SimpleDateFormat.getPatternInstance("HH:mm", Locale.ENGLISH).format(Date(time))
    }

    private fun listenForSendNewMessageResponse(){
        viewModelScope.launch {
            sendNewMessageUseCase.sendNewMessageResponse.collect{
                it?.let {
                    when(it){
                        is FirebaseResource.Success -> {
                            Log.d("MIKOT_CHAT" , "send message response is ${it.data}")
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_CHAT" , "send message error is ${it.error}")
                        }
                    }
                }
            }
        }
    }
}