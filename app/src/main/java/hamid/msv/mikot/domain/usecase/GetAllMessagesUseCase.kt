package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.MessageRepository
import javax.inject.Inject

class GetAllMessagesUseCase @Inject constructor(private val messageRepository: MessageRepository) {

    val messagesFromServer = messageRepository.messages

    fun executeFromServer(child : String) = messageRepository.listenForMessages(child)

    fun executeFromDB(currentUserId: String) = messageRepository.getAllMessagesFromDB(currentUserId)
}