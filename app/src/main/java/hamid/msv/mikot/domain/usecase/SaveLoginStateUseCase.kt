package hamid.msv.mikot.domain.usecase

import hamid.msv.mikot.domain.repository.DataStoreRepository
import javax.inject.Inject

class SaveLoginStateUseCase
@Inject constructor(private val dataStoreRepository: DataStoreRepository) {

    suspend fun execute(isLogin : Boolean) =
        dataStoreRepository.saveLoginState(isLogin)

}