package hamid.msv.mikot.domain.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import hamid.msv.mikot.domain.model.MikotUser

interface UserRepository {
    val signUpResponse : LiveData<Task<AuthResult>>
    val saveNewUserResponse : LiveData<Task<Void>>
    val signInResponse : LiveData<Task<AuthResult>>
    suspend fun signUpUser(email:String , password : String)
    suspend fun saveNewUserInDatabase(user : MikotUser)
    suspend fun signInUser(email:String , password : String)
    suspend fun getAllUsers() : Map<String, Any>
}