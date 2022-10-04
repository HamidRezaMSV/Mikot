package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.MessageRepository
import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Inject

class DeleteDBUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository
) {

    suspend fun executeForUserTable() = userRepository.deleteAllUsersFromDB()

    suspend fun executeForMessageTable() = messageRepository.deleteAllMessagesFromDB()

    suspend fun executeForLastMessageTable() = messageRepository.deleteAllLastMessagesFromDB()

}