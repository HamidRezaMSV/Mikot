package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.repository.MessageRepository
import javax.inject.Inject

class CreateNewMessageUseCase @Inject constructor(private val messageRepository: MessageRepository) {

    val response = messageRepository.createNewMessageResponse

    suspend fun execute(message: Message, child : String) =
        messageRepository.createNewMessage(message, child)

}