package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.model.RoomUser
import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserToDBUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun execute(user: RoomUser) = userRepository.addUserToDB(user)

}