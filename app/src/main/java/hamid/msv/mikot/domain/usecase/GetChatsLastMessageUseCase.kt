package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.MessageRepository
import javax.inject.Inject

class GetChatsLastMessageUseCase @Inject constructor(private val messageRepository: MessageRepository) {

    suspend fun execute() = messageRepository.getChatsLastMessage()

}