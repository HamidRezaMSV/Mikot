package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.DataStoreRepository
import javax.inject.Inject

class SaveOnBoardingUseCase
@Inject constructor(private val dataStoreRepository: DataStoreRepository) {

    suspend fun execute(completed: Boolean) = dataStoreRepository.saveOnBoardingState(completed)

}