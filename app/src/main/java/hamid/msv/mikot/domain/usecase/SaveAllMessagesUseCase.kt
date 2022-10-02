package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.model.RoomMessage
import hamid.msv.mikot.domain.repository.MessageRepository
import javax.inject.Inject

class SaveAllMessagesUseCase @Inject constructor(private val messageRepository: MessageRepository){

    suspend fun execute(messages: List<RoomMessage>) = messageRepository.saveAllMessages(messages)

}