package hamid.msv.mikot.data.repository

import hamid.msv.mikot.data.source.local.LocalDataSource
import hamid.msv.mikot.data.source.remote.RemoteDataSource
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.domain.model.RoomUser
import hamid.msv.mikot.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl
@Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : UserRepository {

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

    override fun getConnectionState() = remoteDataSource.getConnectionState()

    override suspend fun updateCurrentUser(updatedUser: MikotUser) =
        remoteDataSource.updateCurrentUser(updatedUser)

    override suspend fun addAllUsersToDB(users: List<RoomUser>) =
        localDataSource.addAllUsers(users)

    override fun getAllUsersFromDB() = localDataSource.getAllUsers()

    override suspend fun signOutUser() = remoteDataSource.signOutUser()

    override fun getUserByIdFromDB(userId: String) = localDataSource.getUserById(userId)

    override suspend fun deleteAllUsersFromDB() = localDataSource.deleteAllUsers()

    override suspend fun addUserToDB(user: RoomUser) = localDataSource.addUser(user)
}