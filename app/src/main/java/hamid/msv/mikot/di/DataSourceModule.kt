package hamid.msv.mikot.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

}