package hamid.msv.mikot.domain.repository

import android.net.Uri
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.domain.model.RoomUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {

    val signUpResponse : StateFlow<FirebaseResource<String>?>

    val saveNewUserResponse : StateFlow<FirebaseResource<String>?>

    val signInResponse : StateFlow<FirebaseResource<String>?>

    fun getConnectionState(): StateFlow<FirebaseResource<Boolean>?>

    suspend fun signUpUser(email:String , password : String)

    suspend fun saveNewUserInFirebase(user : MikotUser)

    suspend fun signInUser(email:String , password : String)

    suspend fun signOutUser()

    suspend fun updateCurrentUser(updatedUser: MikotUser): StateFlow<FirebaseResource<String>?>

    suspend fun updateProfileImage(uri: Uri, currentUserId: String): StateFlow<FirebaseResource<String>?>

    fun getAllUsers() : StateFlow<FirebaseResource<List<MikotUser>>?>

    fun getUserById(id: String) : StateFlow<FirebaseResource<MikotUser>?>



    suspend fun addAllUsersToDB(users: List<RoomUser>)

    fun getAllUsersFromDB(): Flow<List<RoomUser>>

    fun getUserByIdFromDB(userId: String): Flow<RoomUser?>

    suspend fun deleteAllUsersFromDB()

    suspend fun addUserToDB(user: RoomUser)

}