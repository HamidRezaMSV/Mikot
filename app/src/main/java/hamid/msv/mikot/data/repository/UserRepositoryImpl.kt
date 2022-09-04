package hamid.msv.mikot.data.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import hamid.msv.mikot.data.source.remote.RemoteDataSource
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl
@Inject constructor(private val remoteDataSource: RemoteDataSource) : UserRepository {

    override val signUpResponse: LiveData<Task<AuthResult>>
        get() = remoteDataSource.signUpResponse
    override val saveNewUserResponse: LiveData<Task<Void>>
        get() = remoteDataSource.saveNewUserResponse
    override val signInResponse: LiveData<Task<AuthResult>>
        get() = remoteDataSource.signInResponse

    override suspend fun signUpUser(email: String, password: String) {
        remoteDataSource.signUpUser(email, password)
    }

    override suspend fun saveNewUserInFirebase(user: MikotUser) {
        remoteDataSource.saveNewUserInFirebase(user)
    }

    override fun signInUser(email: String, password: String): Flow<Task<AuthResult>> =
        remoteDataSource.signInUser(email, password)

    override fun getAllUsers(): Flow<List<MikotUser>> = remoteDataSource.getAllUsers()

}