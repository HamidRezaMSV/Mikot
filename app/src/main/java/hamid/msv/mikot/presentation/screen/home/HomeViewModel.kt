package hamid.msv.mikot.presentation.screen.home

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.Application
import hamid.msv.mikot.domain.model.Contact
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.domain.usecase.*
import hamid.msv.mikot.util.USER_IS_NOT_LOGIN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getConnectionStateUseCase: GetConnectionStateUseCase,
    private val getAllLastMessagesUseCase: GetAllLastMessagesUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val saveAllLastMessagesUseCase: SaveAllLastMessagesUseCase,
    private val signOutUserUseCase: SignOutUserUseCase,
    private val saveCurrentUserIdUseCase: SaveCurrentUserIdUseCase,
    private val deleteDBUseCase: DeleteDBUseCase,
    private val saveUserToDBUseCase: SaveUserToDBUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase
): ViewModel(){

    private val currentUserId = Application.currentUserId!!

    private val _connectionState = MutableStateFlow(false)
    val connectionState = _connectionState.asStateFlow()

    private val _lastMessages = MutableStateFlow<List<LastMessage>>(emptyList())
    val lastMessages = _lastMessages.asStateFlow()

    init {
        fetchCurrentUserFromDB()
        fetchAllLastMessageFromDB()
        detectConnectionState()
        listenForLastMessages()
    }

    fun signOutUser(){
        viewModelScope.launch(Dispatchers.IO) { signOutUserUseCase.execute() }
        viewModelScope.launch(Dispatchers.IO) { deleteDBUseCase.executeForUserTable() }
        viewModelScope.launch(Dispatchers.IO) { deleteDBUseCase.executeForMessageTable() }
        viewModelScope.launch(Dispatchers.IO) { deleteDBUseCase.executeForLastMessageTable() }
        viewModelScope.launch(Dispatchers.IO) { saveCurrentUserIdUseCase.execute(uid = USER_IS_NOT_LOGIN) }
        Application.apply {
            currentUser = null
            currentUserId = null
        }
    }

    private fun detectConnectionState(){
        viewModelScope.launch(Dispatchers.IO) {
            getConnectionStateUseCase.execute().collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            response.data?.let { isOnline ->
                                _connectionState.value = isOnline
                                if (isOnline){ fetchCurrentUserFromServer(currentUserId) }
                                Application.currentUser?.let { currentUser ->
                                    currentUser.isOnline = isOnline
                                    sendUserConnectionStateToServer(updatedUser = currentUser)
                                }
                            }
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_HOME" , response.error.toString())
                        }
                    }
                }
            }
        }
    }

    private fun sendUserConnectionStateToServer(updatedUser: MikotUser){
        viewModelScope.launch(Dispatchers.IO) {
            updateCurrentUserUseCase.execute(updatedUser).collectLatest {
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            Log.d("MIKOT_HOME" , "Update user response is ${response.data}")
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_HOME" , response.error.toString())
                        }
                    }
                }
            }
        }
    }

    private fun listenForLastMessages(){
        viewModelScope.launch(Dispatchers.IO) {
            getAllLastMessagesUseCase.executeFromServer(currentUserId).collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            response.data?.let { data ->
                                val validList = data
                                    .sortedByDescending { lastMessage -> lastMessage.time!!.toLong() }
                                    .map { lastMessage ->
                                        if (!lastMessage.time.toString().contains(":")){
                                            lastMessage.time = parseTime(lastMessage.time!!.toLong())
                                        }
                                        lastMessage
                                    }
                                saveAllLastMessagesUseCase.execute(validList.map { lastMessage-> lastMessage.mapToRoomLastMessage() })
                            }
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_HOME" , response.error.toString())
                        }
                    }
                }
            }
        }
    }

    private fun fetchCurrentUserFromServer(currentUserId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserByIdUseCase.executeFromServer(currentUserId).collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            response.data?.let { mikotUser ->
                                Application.currentUser = mikotUser
                                Application.currentUser!!.isOnline = connectionState.value
                                sendUserConnectionStateToServer(updatedUser = Application.currentUser!!)
                                saveCurrentUserInDB(mikotUser)
                            }
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_HOME" , response.error.toString())
                        }
                    }
                }
            }
        }
    }

    private fun saveCurrentUserInDB(user: MikotUser){
        viewModelScope.launch(Dispatchers.IO) {
            saveUserToDBUseCase.execute(user.mapToRoomUser())
        }
    }

    private fun fetchAllLastMessageFromDB(){
        viewModelScope.launch(Dispatchers.IO) {
            getAllLastMessagesUseCase.executeFromDB(currentUserId).collect{
                if (it.isNotEmpty()){
                    _lastMessages.value = it.map { roomLastMessage -> roomLastMessage.mapToLastMessage() }
                }
            }
        }
    }

    private fun fetchCurrentUserFromDB(){
        viewModelScope.launch(Dispatchers.IO) {
            getUserByIdUseCase.executeFromDB(currentUserId).collect{
                it?.let { roomUser ->
                    Application.currentUser = roomUser.mapToMikotUser()
                }
            }
        }
    }

    private fun parseTime(time:Long): String{
        return SimpleDateFormat.getPatternInstance("yyyy/MM/dd HH:mm", Locale.ENGLISH).format(Date(time))
    }

    fun getPhoneContacts(context: Context): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val cursor = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            if (it.count > 0) {
                while (it.moveToNext()) {
                    val id =
                        it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NAME_RAW_CONTACT_ID))
                    val name =
                        it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val number =
                        it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    val contact = Contact(id, name, number)
                    contactList.add(contact)
                }
            }
            cursor.close()
        }
        return formatContactNumber(contactList)
    }

    private fun formatContactNumber(contacts: List<Contact>): List<Contact>{
        if (contacts.isNotEmpty()){
            contacts.forEach { contact ->
                if (contact.number.startsWith("+98")){
                    contact.number = contact.number.replace(oldValue = "+98", newValue = "0")
                }
                if (contact.number.contains(" ")){
                    contact.number = contact.number.replace(oldValue = " ", newValue = "")
                }
            }
        }
        return contacts
    }

    override fun onCleared() {
        super.onCleared()
        Application.currentUser?.let {
            it.isOnline = false
            sendUserConnectionStateToServer(updatedUser = it)
        }
    }

}