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
import hamid.msv.mikot.domain.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getConnectionStateUseCase: GetConnectionStateUseCase,
    private val getAllLastMessagesUseCase: GetAllLastMessagesUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val saveAllLastMessagesUseCase: SaveAllLastMessagesUseCase,
    private val signOutUserUseCase: SignOutUserUseCase
): ViewModel(){

    private val currentUserId = Application.currentUserId!!

    private val _connectionState = MutableStateFlow(false)
    val connectionState = _connectionState.asStateFlow()

    private val _lastMessages = MutableStateFlow<List<LastMessage>>(emptyList())
    val lastMessages = _lastMessages.asStateFlow()

    init {
        fetchAllLastMessageFromDB()
        detectConnectionState()
        listenForLastMessages()
    }

    fun signOutUser() = viewModelScope.launch(Dispatchers.IO) { signOutUserUseCase.execute() }


    private fun detectConnectionState(){
        viewModelScope.launch(Dispatchers.IO) {
            getConnectionStateUseCase.execute().collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            _connectionState.value = response.data!!
                            if (response.data){
                                fetchCurrentUserInfo(currentUserId)
                            }
                            Log.d("MIKOT_HOME" , "Connection State : ${response.data}")
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
                                _lastMessages.value = validList
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

    private fun fetchCurrentUserInfo(currentUserId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserByIdUseCase.execute(currentUserId).collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            Application.currentUser = response.data!!
                            Log.d("MIKOT_HOME" , "current user info fetched successfully")
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_HOME" , response.error.toString())
                        }
                    }
                }
            }
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
}