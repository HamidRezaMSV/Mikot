package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.repository.MessageRepository
import javax.inject.Inject

class SendNewMessageUseCase @Inject constructor(private val messageRepository: MessageRepository) {

    val sendNewMessageResponse = messageRepository.sendNewMessageResponse

    suspend fun execute(message: Message, senderId: String, receiverId: String) =
        messageRepository.sendNewMessage(message, senderId, receiverId)

}