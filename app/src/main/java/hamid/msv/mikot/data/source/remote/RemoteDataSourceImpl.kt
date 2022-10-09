package hamid.msv.mikot.data.source.remote

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import hamid.msv.mikot.data.remote.FirebaseApi.LAST_MESSAGE_DATABASE
import hamid.msv.mikot.data.remote.FirebaseApi.MESSAGE_DATABASE
import hamid.msv.mikot.data.remote.FirebaseApi.USER_DATABASE
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.domain.model.Message
import hamid.msv.mikot.domain.model.MikotUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _sendNewMessageResponse = MutableStateFlow<FirebaseResource<String>?>(null)
    override val sendNewMessageResponse = _sendNewMessageResponse.asStateFlow()

    override fun getConnectionState(): StateFlow<FirebaseResource<Boolean>?> {
        val response = MutableStateFlow<FirebaseResource<Boolean>?>(null)
        Firebase.database.getReference(".info/connected")
            .addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                response.value = FirebaseResource.Success(data = connected)
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = FirebaseResource.Error(error = error.message)
            }
        })

        return response.asStateFlow()
    }

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

    override suspend fun sendNewMessage(message: Message, senderId: String, receiverId: String) {
        val msg1 = Message(
            id = message.id,
            text = message.text,
            time = message.time,
            senderId = message.senderId,
            senderUsername = message.senderUsername,
            receiverId = message.receiverId,
            receiverUsername = message.receiverUsername,
            isEdited = message.isEdited,
            editTime = message.editTime,
            isReply = message.isReply,
            repliedMessageId = message.repliedMessageId,
            key = senderId + receiverId
        )
        val msg2 = Message(
            id = message.id,
            text = message.text,
            time = message.time,
            senderId = message.senderId,
            senderUsername = message.senderUsername,
            receiverId = message.receiverId,
            receiverUsername = message.receiverUsername,
            isEdited = message.isEdited,
            editTime = message.editTime,
            isReply = message.isReply,
            repliedMessageId = message.repliedMessageId,
            key = receiverId + senderId
        )

        MESSAGE_DATABASE.child(senderId).child(receiverId).push().setValue(msg1)
            .addOnCompleteListener { response1 ->
                if (response1.isSuccessful){
                    MESSAGE_DATABASE.child(receiverId).child(senderId).push().setValue(msg2)
                        .addOnCompleteListener { response2 ->
                            if (response2.isSuccessful){
                                LAST_MESSAGE_DATABASE.child(senderId+receiverId).setValue(msg1.mapToLastMessage())
                                    .addOnCompleteListener { response3 ->
                                        if (response3.isSuccessful){
                                            LAST_MESSAGE_DATABASE.child(receiverId+senderId).setValue(msg2.mapToLastMessage())
                                                .addOnCompleteListener { response4 ->
                                                    if (response4.isSuccessful){
                                                        _sendNewMessageResponse.value = FirebaseResource.Success(data = "OK")
                                                    }else{
                                                        _sendNewMessageResponse.value = FirebaseResource.Error(error = response4.exception?.message.toString())
                                                    }
                                                }
                                        }else{
                                            _sendNewMessageResponse.value = FirebaseResource.Error(error = response3.exception?.message.toString())
                                        }
                                    }
                            }else{
                                _sendNewMessageResponse.value = FirebaseResource.Error(error = response2.exception?.message.toString())
                            }
                        }
                }else{
                    _sendNewMessageResponse.value = FirebaseResource.Error(error = response1.exception?.message.toString())
                }
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

    override fun getUserById(id: String): StateFlow<FirebaseResource<MikotUser>?> {
        val response = MutableStateFlow<FirebaseResource<MikotUser>?>(null)
        USER_DATABASE.child(id).get().addOnCompleteListener {
            if (it.isSuccessful){
                val user = it.result.getValue(MikotUser::class.java)
                if (user != null){
                    response.value = FirebaseResource.Success(data = user)
                }else{
                    response.value = FirebaseResource.Error(error = "Received user is null")
                }
            }else{
                response.value = FirebaseResource.Error(error = it.exception?.message.toString())
            }
        }

        return response.asStateFlow()
    }

    override fun listenForMessages(senderId: String, receiverId: String){
        MESSAGE_DATABASE.child(senderId).child(receiverId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.map { it.getValue(Message::class.java) ?: return }
                data.forEach { Log.d("test_test" , it.key.toString()) }
               _messages.value = FirebaseResource.Success(data = data)
            }

            override fun onCancelled(error: DatabaseError) {
                _messages.value = FirebaseResource.Error(error = error.message)
            }
        })
    }

    override suspend fun getAllLastMessages(currentUserId : String): StateFlow<FirebaseResource<List<LastMessage>>?> {
        val response = MutableStateFlow<FirebaseResource<List<LastMessage>>?>(null)
        LAST_MESSAGE_DATABASE.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<LastMessage>()
                snapshot.children.forEach {
                    if (it.key.toString().startsWith(currentUserId)){
                        val item = it.getValue(LastMessage::class.java) ?: return
                        list.add(item)
                    }
                }
                response.value = FirebaseResource.Success(data = list.toList())
            }

            override fun onCancelled(error: DatabaseError) {
                response.value = FirebaseResource.Error(error = error.message)
            }
        })

        return response.asStateFlow()
    }

    override suspend fun signOutUser() { authentication.signOut() }

    override suspend fun updateCurrentUser(updatedUser: MikotUser): StateFlow<FirebaseResource<String>?> {
        val response = MutableStateFlow<FirebaseResource<String>?>(null)
        USER_DATABASE.child(updatedUser.id!!).setValue(updatedUser).addOnCompleteListener {
            if (it.isSuccessful){
                response.value = FirebaseResource.Success(data = "OK")
            }else{
                response.value = FirebaseResource.Error(error = it.exception!!.message.toString())
            }
        }

        return response.asStateFlow()
    }

}