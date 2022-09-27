package hamid.msv.mikot.presentation.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.usecase.GetAllMessagesUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PRIMARY_KEY = "kcojkPMpdtaEeHhHxWq2WsBXXv93gQPmtPlZO9PM0S24a8aIRcKS1Pk1"

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getAllMessagesUseCase: GetAllMessagesUseCase
): ViewModel() {

    val messages : StateFlow<List<Message>> = getAllMessagesUseCase.messages

    init {
        viewModelScope.launch {
            getAllMessagesUseCase.execute(PRIMARY_KEY)
        }
    }

}