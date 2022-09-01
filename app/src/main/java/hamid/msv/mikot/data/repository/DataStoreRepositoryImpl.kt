package hamid.msv.mikot.data.repository

import hamid.msv.mikot.data.source.cache.DataStoreDataSource
import hamid.msv.mikot.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DataStoreRepositoryImpl
@Inject constructor(private val dataStoreDataSource: DataStoreDataSource) : DataStoreRepository {

    override suspend fun saveOnBoardingState(completed: Boolean) =
        dataStoreDataSource.saveOnBoardingState(completed)

    override fun readOnBoardingState(): Flow<Boolean> =
        dataStoreDataSource.readOnBoardingState()

}