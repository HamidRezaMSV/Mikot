package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.domain.repository.MessageRepository
import javax.inject.Inject

class UpdateChatLastMessageUseCase @Inject constructor(private val messageRepository: MessageRepository) {

    val response = messageRepository.updateLastMessageResponse

    suspend fun execute(lastMessage: LastMessage) =
        messageRepository.updateChatLastMessage(lastMessage)

}