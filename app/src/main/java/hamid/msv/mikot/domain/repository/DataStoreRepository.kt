package hamid.msv.mikot.domain.repository

import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveOnBoardingState(completed:Boolean)
    fun readOnBoardingState() : Flow<Boolean>
    suspend fun saveLoginState(isLogin : Boolean)
    fun readLoginState() : Flow<Boolean>
}