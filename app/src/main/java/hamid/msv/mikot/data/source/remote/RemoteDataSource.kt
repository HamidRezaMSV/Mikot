package hamid.msv.mikot.data.source.remote

import android.net.Uri
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.model.MikotUser
import kotlinx.coroutines.flow.StateFlow

interface RemoteDataSource {

    val signUpResponse: StateFlow<FirebaseResource<String>?>

    val saveNewUserResponse: StateFlow<FirebaseResource<String>?>

    val signInResponse: StateFlow<FirebaseResource<String>?>

    val messages: StateFlow<FirebaseResource<List<Message>>?>

    val sendNewMessageResponse: StateFlow<FirebaseResource<String>?>

    fun getConnectionState(): StateFlow<FirebaseResource<Boolean>?>

    suspend fun signUpUser(email: String, password: String)

    suspend fun saveNewUserInFirebase(user: MikotUser)

    suspend fun signInUser(email: String, password: String)

    suspend fun signOutUser()

    suspend fun updateCurrentUser(updatedUser: MikotUser): StateFlow<FirebaseResource<String>?>

    fun getAllUsers(): StateFlow<FirebaseResource<List<MikotUser>>?>

    fun getUserById(id: String) : StateFlow<FirebaseResource<MikotUser>?>

    fun listenForMessages(senderId: String, receiverId: String)

    suspend fun sendNewMessage(message: Message, senderId: String, receiverId: String)

    suspend fun getAllLastMessages(currentUserId : String): StateFlow<FirebaseResource<List<LastMessage>>?>

    suspend fun updateProfileImage(uri: Uri,currentUserId: String): StateFlow<FirebaseResource<String>?>

}