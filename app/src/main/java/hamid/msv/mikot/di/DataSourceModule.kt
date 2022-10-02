package hamid.msv.mikot.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hamid.msv.mikot.data.local.MikotDatabase
import hamid.msv.mikot.data.source.cache.DataStoreDataSource
import hamid.msv.mikot.data.source.cache.DataStoreDataSourceImpl
import hamid.msv.mikot.data.source.local.LocalDataSource
import hamid.msv.mikot.data.source.local.LocalDataSourceImpl
import hamid.msv.mikot.data.source.remote.RemoteDataSource
import hamid.msv.mikot.data.source.remote.RemoteDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(authentication : FirebaseAuth) : RemoteDataSource =
        RemoteDataSourceImpl(authentication)

    @Provides
    @Singleton
    fun provideDataStoreDataSource(@ApplicationContext context: Context) : DataStoreDataSource =
        DataStoreDataSourceImpl(context)

    @Provides
    @Singleton
    fun provideLocalDataSource(mikotDatabase: MikotDatabase) : LocalDataSource =
        LocalDataSourceImpl(mikotDatabase)

}