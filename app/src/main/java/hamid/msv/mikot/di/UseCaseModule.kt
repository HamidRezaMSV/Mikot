package hamid.msv.mikot.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hamid.msv.mikot.domain.repository.DataStoreRepository
import hamid.msv.mikot.domain.repository.MessageRepository
import hamid.msv.mikot.domain.repository.UserRepository
import hamid.msv.mikot.domain.usecase.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideSendNewMessageUseCase(messageRepository: MessageRepository) =
        SendNewMessageUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideGetAllMessagesUseCase(messageRepository: MessageRepository) =
        GetAllMessagesUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideGetAllUsersUseCase(userRepository: UserRepository) =
        GetAllUsersUseCase(userRepository)

    @Provides
    @Singleton
    fun provideGetAllLastMessagesUseCase(messageRepository: MessageRepository) =
        GetAllLastMessagesUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideSaveUserInFirebaseUseCase(userRepository: UserRepository) =
        SaveUserInFirebaseUseCase(userRepository)

    @Provides
    @Singleton
    fun provideSignInUserUseCase(userRepository: UserRepository) =
        SignInUserUseCase(userRepository)

    @Provides
    @Singleton
    fun provideSignUpUserUseCase(userRepository: UserRepository) =
        SignUpUserUseCase(userRepository)

    @Provides
    @Singleton
    fun provideReadOnBoardingUseCase(dataStoreRepository: DataStoreRepository) =
        ReadOnBoardingUseCase(dataStoreRepository)

    @Provides
    @Singleton
    fun provideSaveOnBoardingUseCase(dataStoreRepository: DataStoreRepository) =
        SaveOnBoardingUseCase(dataStoreRepository)

    @Provides
    @Singleton
    fun provideSaveCurrentUserIdUseCase(dataStoreRepository: DataStoreRepository) =
        SaveCurrentUserIdUseCase(dataStoreRepository)

    @Provides
    @Singleton
    fun provideReadCurrentUserIdUseCase(dataStoreRepository: DataStoreRepository) =
        ReadCurrentUserIdUseCase(dataStoreRepository)

    @Provides
    @Singleton
    fun provideGetUserByIdUseCase(userRepository: UserRepository) =
        GetUserByIdUseCase(userRepository)

    @Provides
    @Singleton
    fun provideGetConnectionStateUseCase(userRepository: UserRepository) =
        GetConnectionStateUseCase(userRepository)

    @Provides
    @Singleton
    fun provideSaveAllMessagesUseCase(messageRepository: MessageRepository) =
        SaveAllMessagesUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideSaveAllLastMessageUseCase(messageRepository: MessageRepository) =
        SaveAllLastMessagesUseCase(messageRepository)

    @Provides
    @Singleton
    fun provideSignOutUserUseCase(userRepository: UserRepository) =
        SignOutUserUseCase(userRepository)

    @Provides
    @Singleton
    fun provideDeleteDBUseCase(userRepository: UserRepository,messageRepository: MessageRepository) =
        DeleteDBUseCase(userRepository,messageRepository)

    @Provides
    @Singleton
    fun provideSaveUserToDBUseCase(userRepository: UserRepository) =
        SaveUserToDBUseCase(userRepository)

    @Provides
    @Singleton
    fun provideUpdateCurrentUserUseCase(userRepository: UserRepository) =
        UpdateCurrentUserUseCase(userRepository)

    @Provides
    @Singleton
    fun provideUpdateProfileImageUseCase(userRepository: UserRepository) =
        UpdateProfileImageUseCase(userRepository)

}