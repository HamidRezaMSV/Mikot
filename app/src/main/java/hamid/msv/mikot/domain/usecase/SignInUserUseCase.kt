package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Inject

class SignInUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    val response = userRepository.signInResponse

    suspend fun execute(email: String, password: String) =
        userRepository.signInUser(email, password)

}