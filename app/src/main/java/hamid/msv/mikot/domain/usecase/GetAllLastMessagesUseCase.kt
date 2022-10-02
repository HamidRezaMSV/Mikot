package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.MessageRepository
import javax.inject.Inject

class GetAllLastMessagesUseCase @Inject constructor(private val messageRepository: MessageRepository) {

    suspend fun executeFromServer(currentUserId: String) =
        messageRepository.getAllLastMessages(currentUserId)

    fun executeFromDB(currentUserId: String) =
        messageRepository.getAllLastMessagesFromDB(currentUserId)

}