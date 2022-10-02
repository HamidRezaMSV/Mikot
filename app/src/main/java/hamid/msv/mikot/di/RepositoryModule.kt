package hamid.msv.mikot.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hamid.msv.mikot.data.repository.DataStoreRepositoryImpl
import hamid.msv.mikot.data.repository.MessageRepositoryImpl
import hamid.msv.mikot.data.repository.UserRepositoryImpl
import hamid.msv.mikot.data.source.cache.DataStoreDataSource
import hamid.msv.mikot.data.source.local.LocalDataSource
import hamid.msv.mikot.data.source.remote.RemoteDataSource
import hamid.msv.mikot.domain.repository.DataStoreRepository
import hamid.msv.mikot.domain.repository.MessageRepository
import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMessageRepository(remoteDataSource: RemoteDataSource,localDataSource: LocalDataSource) : MessageRepository =
        MessageRepositoryImpl(remoteDataSource,localDataSource)

    @Provides
    @Singleton
    fun provideUserRepository(remoteDataSource: RemoteDataSource,localDataSource: LocalDataSource) : UserRepository =
        UserRepositoryImpl(remoteDataSource,localDataSource)

    @Provides
    @Singleton
    fun provideDataStoreRepository(dataStoreDataSource: DataStoreDataSource) : DataStoreRepository =
        DataStoreRepositoryImpl(dataStoreDataSource)

}