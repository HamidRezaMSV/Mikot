package hamid.msv.mikot.presentation.screen.chat

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.domain.usecase.GetAllMessagesUseCase
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getAllMessagesUseCase: GetAllMessagesUseCase
): ViewModel() {
}