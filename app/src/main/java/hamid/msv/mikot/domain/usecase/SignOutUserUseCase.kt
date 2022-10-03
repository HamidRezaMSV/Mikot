package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Inject

class SignOutUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun execute() = userRepository.signOutUser()

}