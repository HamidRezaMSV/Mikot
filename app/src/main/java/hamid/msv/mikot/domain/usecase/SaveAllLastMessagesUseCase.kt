package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.model.RoomLastMessage
import hamid.msv.mikot.domain.repository.MessageRepository
import javax.inject.Inject

class SaveAllLastMessagesUseCase @Inject constructor(private val messageRepository: MessageRepository) {

    suspend fun execute(lastMessages: List<RoomLastMessage>) = messageRepository.saveAllLastMessages(lastMessages)

}