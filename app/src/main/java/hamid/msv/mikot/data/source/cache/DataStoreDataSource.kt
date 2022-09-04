package hamid.msv.mikot.data.source.cache

import kotlinx.coroutines.flow.Flow

interface DataStoreDataSource {
    suspend fun saveOnBoardingState(completed:Boolean)
    fun readOnBoardingState() : Flow<Boolean>
    suspend fun saveCurrentUserId(uid : String)
    fun readCurrentUserId() : Flow<String>
}