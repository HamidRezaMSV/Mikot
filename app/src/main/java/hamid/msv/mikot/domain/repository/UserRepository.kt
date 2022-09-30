package hamid.msv.mikot.domain.repository

import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.MikotUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {

    val signUpResponse : StateFlow<FirebaseResource<String>?>
    val saveNewUserResponse : StateFlow<FirebaseResource<String>?>
    val signInResponse : StateFlow<FirebaseResource<String>?>

    suspend fun signUpUser(email:String , password : String)
    suspend fun saveNewUserInFirebase(user : MikotUser)
    suspend fun signInUser(email:String , password : String)
    fun getAllUsers() : StateFlow<FirebaseResource<List<MikotUser>>?>
    fun getUserById(id: String) : StateFlow<FirebaseResource<MikotUser>?>
}