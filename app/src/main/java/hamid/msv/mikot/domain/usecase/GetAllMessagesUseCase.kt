package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.MessageRepository
import javax.inject.Inject

class GetAllMessagesUseCase @Inject constructor(private val messageRepository: MessageRepository) {

    val messages = messageRepository.messages
    suspend fun execute(child : String) = messageRepository.listenForMessages(child)
}