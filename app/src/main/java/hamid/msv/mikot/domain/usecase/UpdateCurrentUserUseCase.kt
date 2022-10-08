package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Inject

class UpdateCurrentUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun execute(updatedUser: MikotUser) = userRepository.updateCurrentUser(updatedUser)

}