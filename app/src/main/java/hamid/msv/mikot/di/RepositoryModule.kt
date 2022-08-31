package hamid.msv.mikot.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hamid.msv.mikot.data.repository.MessageRepositoryImpl
import hamid.msv.mikot.data.repository.UserRepositoryImpl
import hamid.msv.mikot.data.source.remote.RemoteDataSource
import hamid.msv.mikot.domain.repository.MessageRepository
import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMessageRepository(remoteDataSource: RemoteDataSource) : MessageRepository =
        MessageRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideUserRepository(remoteDataSource: RemoteDataSource) : UserRepository =
        UserRepositoryImpl(remoteDataSource)

}