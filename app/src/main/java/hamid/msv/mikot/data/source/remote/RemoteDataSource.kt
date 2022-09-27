package hamid.msv.mikot.data.source.remote

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.model.MikotUser
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    val signUpResponse : LiveData<Task<AuthResult>>
    val saveNewUserResponse : LiveData<Task<Void>>
    val signInResponse : LiveData<Task<AuthResult>>
    val createNewMessageResponse : LiveData<Task<Void>>
    val updateLastMessageResponse : LiveData<Task<Void>>
    suspend fun signUpUser(email:String , password : String)
    suspend fun saveNewUserInFirebase(user : MikotUser)
    // Changed
    fun signInUser(email:String , password : String) : Flow<Task<AuthResult>>
    suspend fun createNewMessage(message: Message,child : String)
    // Changed
    fun getAllUsers() : Flow<List<MikotUser>>
    // Changed
    suspend fun getAllMessages(child : String) : Flow<List<Message>>
    suspend fun updateChatLastMessage(lastMessage: LastMessage)
    suspend fun getChatsLastMessage() : Map<String, Any>
}