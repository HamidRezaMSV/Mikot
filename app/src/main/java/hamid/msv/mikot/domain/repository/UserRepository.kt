package hamid.msv.mikot.domain.repository

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.MikotUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val signInResponse : LiveData<Task<AuthResult>>

    val signUpResponse : StateFlow<FirebaseResource<String>?>
    val saveNewUserResponse : StateFlow<FirebaseResource<String>?>

    suspend fun signUpUser(email:String , password : String)
    suspend fun saveNewUserInFirebase(user : MikotUser)
    fun signInUser(email:String , password : String): Flow<Task<AuthResult>>
    fun getAllUsers() : Flow<List<MikotUser>>
}