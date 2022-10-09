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
import hamid.msv.mikot.domain.usecase.*
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
    private val saveAllMessagesUseCase: SaveAllMessagesUseCase,
    private val getConnectionStateUseCase: GetConnectionStateUseCase,
    private val saveUserToDBUseCase: SaveUserToDBUseCase
): ViewModel() {

    private val receiverId = Application.receiverId!!
    private val senderId = Application.currentUserId!!

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages : StateFlow<List<Message>> = _messages.asStateFlow()

    private val _receiverUser = MutableStateFlow<MikotUser?>(null)
    val receiverUser = _receiverUser.asStateFlow()

    private val _connectionState = MutableStateFlow(false)
    val connectionState = _connectionState.asStateFlow()

    private val _receiverUserConnectionState = MutableStateFlow(false)
    val receiverUserConnectionState = _receiverUserConnectionState.asStateFlow()

    init {
        fetchReceiverInfoFromServer()
        detectConnectionState()
        fetchReceiverInfoFromDB()
        listenForMessages()
        fetchMessagesFromDB()
        formatReceivedMessages()
        listenForSendNewMessageResponse()
    }

    fun sendNewMessage(text: String, receiverUser: MikotUser,isReply: Boolean = false , repliedMessageId: String? = null){
        viewModelScope.launch(Dispatchers.IO) {
            if (senderId != USER_IS_NOT_LOGIN){
                val message = Message(
                    id = UUID.randomUUID().toString(),
                    text = text ,
                    time = System.currentTimeMillis().toString(),
                    senderId = senderId ,
                    receiverId = receiverId,
                    senderUsername = Application.currentUser!!.userName,
                    receiverUsername = receiverUser.userName,
                    isReply = isReply,
                    repliedMessageId = repliedMessageId
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

    private fun fetchReceiverInfoFromServer(){
        viewModelScope.launch(Dispatchers.IO) {
            getUserByIdUseCase.executeFromServer(receiverId).collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            response.data?.let { user ->
                                _receiverUser.value = user
                                _receiverUserConnectionState.value = user.isOnline
                                saveUserToDBUseCase.execute(user.mapToRoomUser())
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

    private fun fetchReceiverInfoFromDB(){
        viewModelScope.launch(Dispatchers.IO) {
            getUserByIdUseCase.executeFromDB(userId = receiverId).collect{
                it?.let { roomUser ->
                    _receiverUser.value = roomUser.mapToMikotUser()
                }
            }
        }
    }

    private fun fetchMessagesFromDB(){
        val path = senderId + receiverId
        viewModelScope.launch(Dispatchers.IO) {
            getAllMessagesUseCase.executeFromDB(path).collectLatest {
                if (it.isNotEmpty()){
                    _messages.value = it.map { roomMessage -> roomMessage.mapToMessage() }
                }
            }
        }
    }

    private fun formatReceivedMessages() {
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
                                    Log.d("MIKOT_HAMID" , message.key.toString())
                                    message
                                }
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
            getAllMessagesUseCase.executeFromServer(senderId,receiverId)
        }
    }

    private fun detectConnectionState(){
        viewModelScope.launch(Dispatchers.IO) {
            getConnectionStateUseCase.execute().collectLatest {
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            _connectionState.value = response.data!!
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_CHAT" , response.error.toString())
                        }
                    }
                }
            }
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

    override fun onCleared() {
        super.onCleared()
        _receiverUser.value = null
        _messages.value = emptyList()
    }

}