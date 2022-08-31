package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserInDatabaseUseCase @Inject constructor(private val userRepository: UserRepository) {

    val response = userRepository.saveNewUserResponse

    suspend fun execute(user: MikotUser) =
        userRepository.saveNewUserInDatabase(user)

}