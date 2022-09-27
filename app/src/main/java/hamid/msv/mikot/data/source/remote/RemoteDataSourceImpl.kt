package hamid.msv.mikot.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import hamid.msv.mikot.data.remote.FirebaseApi.LAST_MESSAGE_DATABASE
import hamid.msv.mikot.data.remote.FirebaseApi.MESSAGE_DATABASE
import hamid.msv.mikot.data.remote.FirebaseApi.USER_DATABASE
import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.util.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val authentication: FirebaseAuth) : RemoteDataSource {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    override val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _signUpResponse = MutableLiveData<Task<AuthResult>>()
    override val signUpResponse: LiveData<Task<AuthResult>>
        get() = _signUpResponse

    private val _saveNewUserResponse = MutableLiveData<Task<Void>>()
    override val saveNewUserResponse: LiveData<Task<Void>>
        get() = _saveNewUserResponse

    private val _signInResponse = MutableLiveData<Task<AuthResult>>()
    override val signInResponse: LiveData<Task<AuthResult>>
        get() = _signInResponse

    private val _createNewMessageResponse = MutableLiveData<Task<Void>>()
    override val createNewMessageResponse: LiveData<Task<Void>>
        get() = _createNewMessageResponse

    private val _updateLastMessageResponse = MutableLiveData<Task<Void>>()
    override val updateLastMessageResponse: LiveData<Task<Void>>
        get() = _updateLastMessageResponse

    override suspend fun signUpUser(email:String , password : String) {
        authentication.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {

                _signUpResponse.postValue(it) }
    }

    override suspend fun saveNewUserInFirebase(user: MikotUser) {
        user.id?.let { id -> USER_DATABASE.child(id).setValue(user).addOnCompleteListener { _saveNewUserResponse.postValue(it) } }
    }

    // Change this for test new settings:
    override fun signInUser(email:String , password : String) : Flow<Task<AuthResult>> = callbackFlow {
        authentication.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            _signInResponse.postValue(it)
            trySend(it).isSuccess
        }
        awaitClose { cancel() }
    }

    override suspend fun createNewMessage(message: Message , child : String) {
        MESSAGE_DATABASE.child(child).push().setValue(message)
            .addOnCompleteListener { _createNewMessageResponse.postValue(it) }
    }

    override suspend fun updateChatLastMessage(lastMessage: LastMessage) {
        LAST_MESSAGE_DATABASE.setValue(lastMessage).addOnCompleteListener {
            _updateLastMessageResponse.postValue(it)
        }
    }

    // Change this for test new settings:
    override fun getAllUsers(): Flow<List<MikotUser>> = callbackFlow {
        val users = mutableListOf<MikotUser>()
        USER_DATABASE.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { item ->
                    val user = item.getValue(MikotUser::class.java)
                    user?.let { users.add(it) }
                }
                trySend(users)
            }

            override fun onCancelled(error: DatabaseError) { TODO("Not yet implemented") }
        })
        awaitClose { cancel() }
    }

    override suspend fun listenForMessages(child : String){
        MESSAGE_DATABASE.child(child).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.map { it.getValue(Message::class.java) ?: return }
               _messages.value = data
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override suspend fun getChatsLastMessage(): Map<String, Any> {
        val result = MutableLiveData<List<LastMessage>>()
        val databaseError = MutableLiveData<DatabaseError>()

        LAST_MESSAGE_DATABASE.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                result.postValue(snapshot.getValue<List<LastMessage>>())
            }

            override fun onCancelled(error: DatabaseError) {
                databaseError.postValue(error)
            }
        })

        return mapOf(
            GET_LAST_MESSAGES_KEY to result,
            DATABASE_ERROR_KEY to databaseError
        )
    }

}