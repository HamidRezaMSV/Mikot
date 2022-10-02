package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.model.RoomUser
import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Inject

class SaveAllUsersUseCase @Inject constructor(private val usersRepository: UserRepository) {

    suspend fun execute(users: List<RoomUser>) = usersRepository.addAllUsersToDB(users)

}