package hamid.msv.mikot.data.source.cache

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import hamid.msv.mikot.util.ON_BOARDING_PREFERENCES_KEY
import hamid.msv.mikot.util.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreDataSourceImpl(context: Context) : DataStoreDataSource {

    private object PreferencesKey{
        val onBoardingKey = booleanPreferencesKey(ON_BOARDING_PREFERENCES_KEY)
    }

    private val dataStore = context.dataStore

    override suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { it[PreferencesKey.onBoardingKey] = completed }
    }

    override fun readOnBoardingState(): Flow<Boolean> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) emit(emptyPreferences())
                else throw exception
            }
            .map {
                val onBoardingState = it[PreferencesKey.onBoardingKey] ?: false
                onBoardingState
            }
    }

}