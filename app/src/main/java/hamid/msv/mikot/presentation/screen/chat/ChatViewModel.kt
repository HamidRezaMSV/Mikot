package hamid.msv.mikot.presentation.screen.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.usecase.GetAllMessagesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getAllMessagesUseCase: GetAllMessagesUseCase
): ViewModel() {

    private val _messages = MutableStateFlow(listOf<Message>())
    val messages : StateFlow<List<Message>>
    get() = _messages

    fun getALlMessages(child:String) = viewModelScope.launch(Dispatchers.IO) {
        getAllMessagesUseCase.execute(child).onEach {
            _messages.value = it
            Log.d("mikot_get" , it.size.toString())
        }.launchIn(viewModelScope)
    }

}