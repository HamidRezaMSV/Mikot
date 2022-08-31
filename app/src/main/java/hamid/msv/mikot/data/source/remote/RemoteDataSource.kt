package hamid.msv.mikot.data.source.remote

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.model.MikotUser

interface RemoteDataSource {
    val signUpResponse : LiveData<Task<AuthResult>>
    val saveNewUserResponse : LiveData<Task<Void>>
    val signInResponse : LiveData<Task<AuthResult>>
    val createNewMessageResponse : LiveData<Task<Void>>
    val updateLastMessageResponse : LiveData<Task<Void>>
    suspend fun signUpUser(email:String , password : String)
    suspend fun saveNewUserInDatabase(user : MikotUser)
    suspend fun signInUser(email:String , password : String)
    suspend fun createNewMessage(message: Message)
    suspend fun getAllUsers() : Map<String, Any>
    suspend fun getAllMessages() : Map<String, Any>
    suspend fun updateChatLastMessage(lastMessage: LastMessage)
    suspend fun getChatsLastMessage() : Map<String, Any>
}