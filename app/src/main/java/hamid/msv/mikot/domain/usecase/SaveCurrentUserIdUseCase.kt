package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.DataStoreRepository
import javax.inject.Inject

class SaveCurrentUserIdUseCase
@Inject constructor(private val dataStoreRepository: DataStoreRepository) {

    suspend fun execute(uid : String) =
        dataStoreRepository.saveCurrentUserId(uid)

}