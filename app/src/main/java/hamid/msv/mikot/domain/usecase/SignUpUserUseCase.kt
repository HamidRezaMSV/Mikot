package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    val signUpResponse = userRepository.signUpResponse

    suspend fun execute(email: String, password: String) =
        userRepository.signUpUser(email, password)

}