package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.DataStoreRepository
import javax.inject.Inject

class ReadLoginStateUseCase
@Inject constructor(private val dataStoreRepository: DataStoreRepository){

    fun execute() = dataStoreRepository.readLoginState()

}