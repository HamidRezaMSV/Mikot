package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Inject

class GetConnectionStateUseCase @Inject constructor(private val userRepository: UserRepository) {

    fun execute() = userRepository.getConnectionState()
}