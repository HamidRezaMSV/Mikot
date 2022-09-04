package hamid.msv.mikot.domain.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import hamid.msv.mikot.domain.model.MikotUser
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val signUpResponse : LiveData<Task<AuthResult>>
    val saveNewUserResponse : LiveData<Task<Void>>
    val signInResponse : LiveData<Task<AuthResult>>
    suspend fun signUpUser(email:String , password : String)
    suspend fun saveNewUserInFirebase(user : MikotUser)
    fun signInUser(email:String , password : String): Flow<Task<AuthResult>>
    fun getAllUsers() : Flow<List<MikotUser>>
}