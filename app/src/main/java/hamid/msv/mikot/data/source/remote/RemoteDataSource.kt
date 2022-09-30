package hamid.msv.mikot.data.source.remote

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.model.MikotUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RemoteDataSource {
    val updateLastMessageResponse: LiveData<Task<Void>>

    val messages: StateFlow<FirebaseResource<List<Message>>?>
    val signUpResponse: StateFlow<FirebaseResource<String>?>
    val saveNewUserResponse: StateFlow<FirebaseResource<String>?>
    val signInResponse: StateFlow<FirebaseResource<String>?>
    val sendNewMessageResponse: StateFlow<FirebaseResource<String>?>

    suspend fun signUpUser(email: String, password: String)
    suspend fun listenForMessages(child: String)
    suspend fun saveNewUserInFirebase(user: MikotUser)
    suspend fun signInUser(email: String, password: String)
    fun getAllUsers(): StateFlow<FirebaseResource<List<MikotUser>>?>
    fun getUserById(id: String) : StateFlow<FirebaseResource<MikotUser>?>
    suspend fun sendNewMessage(message: Message, senderId: String, receiverId: String)


    suspend fun updateChatLastMessage(lastMessage: LastMessage)
    suspend fun getChatsLastMessage(): Map<String, Any>
}