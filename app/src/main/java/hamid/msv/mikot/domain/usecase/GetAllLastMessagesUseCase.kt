package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.MessageRepository
import javax.inject.Inject

class GetAllLastMessagesUseCase @Inject constructor(private val messageRepository: MessageRepository) {

    suspend fun execute(currentUserId: String) = messageRepository.getAllLastMessages(currentUserId)

}