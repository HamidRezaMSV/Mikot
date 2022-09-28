package hamid.msv.mikot.presentation.screen.chat

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.usecase.GetAllMessagesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

const val PRIMARY_KEY = "gQPmtPlZO9PM0S24a8aIRcKS1Pk1kcojkPMpdtaEeHhHxWq2WsBXXv93"

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getAllMessagesUseCase: GetAllMessagesUseCase
): ViewModel() {

    init {
        listenForMessages()
        editReceivedMessages()
    }

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages : StateFlow<List<Message>> = _messages.asStateFlow()

    private fun editReceivedMessages() {
        viewModelScope.launch {
            getAllMessagesUseCase.messages.collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            response.data?.let { messageList ->
                                _messages.value = messageList.map { message ->
                                    message.time = parseTime(message.time!!.toLong())
                                    message
                                }
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
            getAllMessagesUseCase.execute(PRIMARY_KEY)
        }
    }

    private fun parseTime(time:Long): String{
        return SimpleDateFormat.getPatternInstance("HH:mm", Locale.ENGLISH).format(Date(time))
    }

}