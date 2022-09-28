package hamid.msv.mikot.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import hamid.msv.mikot.data.remote.FirebaseApi.LAST_MESSAGE_DATABASE
import hamid.msv.mikot.data.remote.FirebaseApi.MESSAGE_DATABASE
import hamid.msv.mikot.data.remote.FirebaseApi.USER_DATABASE
import hamid.msv.mikot.domain.model.*
import hamid.msv.mikot.util.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val authentication: FirebaseAuth) : RemoteDataSource {

    private val _messages = MutableStateFlow<FirebaseResource<List<Message>>?>(null)
    override val messages: StateFlow<FirebaseResource<List<Message>>?> = _messages.asStateFlow()

    private val _signUpResponse = MutableStateFlow<FirebaseResource<String>?>(null)
    override val signUpResponse: StateFlow<FirebaseResource<String>?> = _signUpResponse.asStateFlow()

    private val _saveNewUserResponse = MutableStateFlow<FirebaseResource<String>?>(null)
    override val saveNewUserResponse = _saveNewUserResponse.asStateFlow()

    private val _signInResponse = MutableStateFlow<FirebaseResource<String>?>(null)
    override val signInResponse = _signInResponse.asStateFlow()


    private val _createNewMessageResponse = MutableLiveData<Task<Void>>()
    override val createNewMessageResponse: LiveData<Task<Void>>
        get() = _createNewMessageResponse

    private val _updateLastMessageResponse = MutableLiveData<Task<Void>>()
    override val updateLastMessageResponse: LiveData<Task<Void>>
        get() = _updateLastMessageResponse


    override suspend fun signUpUser(email:String, password : String) {
        authentication.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    _signUpResponse.value = FirebaseResource.Success(data = it.result.user?.uid.toString())
                }else{
                    _signUpResponse.value = FirebaseResource.Error(error = it.exception?.message.toString())
                }
            }
    }

    override suspend fun saveNewUserInFirebase(user: MikotUser) {
        USER_DATABASE.child(user.id!!).setValue(user).addOnCompleteListener{
            if (it.isSuccessful){
                _saveNewUserResponse.value = FirebaseResource.Success(data = "OK")
            }else{
                _saveNewUserResponse.value = FirebaseResource.Error(it.exception?.message.toString())
            }
        }
    }

    override suspend fun signInUser(email:String , password : String) {
        authentication.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    _signInResponse.value = FirebaseResource.Success(data = it.result.user!!.uid)
                }else{
                    _signInResponse.value = FirebaseResource.Error(error = it.exception?.message.toString())
                }
            }
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

    override fun getAllUsers(): StateFlow<FirebaseResource<List<MikotUser>>?> {
        val response = MutableStateFlow<FirebaseResource<List<MikotUser>>?>(null)
        USER_DATABASE.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.map { it.getValue(MikotUser::class.java) ?: return }
                response.value = FirebaseResource.Success(data = data)
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = FirebaseResource.Error(error = error.message)
            }
        })
        return response.asStateFlow()
    }

    override suspend fun listenForMessages(child : String){
        MESSAGE_DATABASE.child(child).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.map { it.getValue(Message::class.java) ?: return }
               _messages.value = FirebaseResource.Success(data = data)
            }

            override fun onCancelled(error: DatabaseError) {
                _messages.value = FirebaseResource.Error(error = error.message)
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