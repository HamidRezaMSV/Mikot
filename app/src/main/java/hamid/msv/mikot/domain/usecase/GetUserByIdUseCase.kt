package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(private val userRepository: UserRepository) {

    fun executeFromServer(id: String) = userRepository.getUserById(id)

    fun executeFromDB(userId: String) = userRepository.getUserByIdFromDB(userId)

}