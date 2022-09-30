package hamid.msv.mikot.data.repository

import hamid.msv.mikot.data.source.remote.RemoteDataSource
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl
@Inject constructor(private val remoteDataSource: RemoteDataSource) : UserRepository {

    override val signUpResponse = remoteDataSource.signUpResponse
    override val saveNewUserResponse = remoteDataSource.saveNewUserResponse
    override val signInResponse = remoteDataSource.signInResponse

    override suspend fun signUpUser(email: String, password: String) =
        remoteDataSource.signUpUser(email, password)

    override suspend fun saveNewUserInFirebase(user: MikotUser) =
        remoteDataSource.saveNewUserInFirebase(user)

    override suspend fun signInUser(email: String, password: String)  =
        remoteDataSource.signInUser(email, password)

    override fun getAllUsers() = remoteDataSource.getAllUsers()

    override fun getUserById(id: String) = remoteDataSource.getUserById(id)

}