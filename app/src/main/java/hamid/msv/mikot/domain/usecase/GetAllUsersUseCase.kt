package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(private val userRepository: UserRepository) {

    fun executeFromServer() = userRepository.getAllUsers()

    fun executeFromDB() = userRepository.getAllUsersFromDB()

}