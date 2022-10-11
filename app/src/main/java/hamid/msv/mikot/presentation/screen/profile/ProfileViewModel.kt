package hamid.msv.mikot.presentation.screen.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.Application
import hamid.msv.mikot.R
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.usecase.*
import hamid.msv.mikot.util.COMPRESS_QUALITY
import hamid.msv.mikot.util.PHONE_NUMBER_CHARACTER_COUNT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getConnectionStateUseCase: GetConnectionStateUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase,
    private val updateProfileImageUseCase: UpdateProfileImageUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val saveUserToDBUseCase: SaveUserToDBUseCase
) : ViewModel(){

    private val currentUserId = Application.currentUserId!!

    private val _currentUser = MutableStateFlow(Application.currentUser!!)
    val currentUser = _currentUser.asStateFlow()

    private val _connectionState = MutableStateFlow(false)
    val connectionState = _connectionState.asStateFlow()

    private val _showProgressBar = MutableStateFlow(false)
    val showProgressBar = _showProgressBar.asStateFlow()

    init {
        detectConnectionState()
    }

    fun updateProfileImage(bitmap: Bitmap,context: Context){
        _showProgressBar.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val imageUri = getImageUriFromBitmap(bitmap)
            updateProfileImageUseCase.execute(imageUri,currentUserId).collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            withContext(Dispatchers.Main){
                                fetchCurrentUserFromServer()
                                Toast.makeText(context, context.getString(R.string.profile_updated_successfully), Toast.LENGTH_SHORT).show()
                            }
                            Log.d("MIKOT_PROFILE" , "update profile image response is ${response.data}")
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_PROFILE" , response.error.toString())
                        }
                    }
                    _showProgressBar.value = false
                }
            }
        }
    }

    fun updateCurrentUser(fullName: String, username: String, phone: String,context: Context){
        if (isInputDataValid(fullName, username, phone, context)){
            viewModelScope.launch(Dispatchers.IO) {
                Application.currentUser?.let { currentUser ->
                    currentUser.fullName = fullName
                    currentUser.userName = username
                    currentUser.phoneNumber = phone

                    updateCurrentUserUseCase.execute(updatedUser = currentUser).collect{
                        it?.let { response ->
                            when(response){
                                is FirebaseResource.Success -> {
                                    fetchCurrentUserFromServer()
                                    Log.d("MIKOT_PROFILE" , "Update user response is ${response.data}")
                                }
                                is FirebaseResource.Error -> {
                                    Log.d("MIKOT_PROFILE" , response.error.toString())
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val tempFile = File.createTempFile(currentUserId, ".jpeg")
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, bytes)
        val bitmapData = bytes.toByteArray()

        val fileOutPut = FileOutputStream(tempFile)
        fileOutPut.write(bitmapData)
        fileOutPut.flush()
        fileOutPut.close()
        return Uri.fromFile(tempFile)
    }

    private fun isInputDataValid(fullName:String,username:String,phone: String,context: Context) : Boolean{
        val fullNameValidation = fullName.isNotEmpty()
        val usernameValidation = username.isNotEmpty()
        val phoneNumberValidation = phone.length == PHONE_NUMBER_CHARACTER_COUNT
        when{
            !fullNameValidation -> Toast.makeText(context, context.getString(R.string.invalid_name_message), Toast.LENGTH_SHORT).show()
            !usernameValidation -> Toast.makeText(context, context.getString(R.string.invalid_username_message), Toast.LENGTH_SHORT).show()
            !phoneNumberValidation -> Toast.makeText(context, context.getString(R.string.invalid_phone_message), Toast.LENGTH_SHORT).show()
        }

        return fullNameValidation && usernameValidation && phoneNumberValidation
    }

    private fun detectConnectionState() {
        viewModelScope.launch(Dispatchers.IO) {
            getConnectionStateUseCase.execute().collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            response.data?.let { isOnline ->
                                _connectionState.value = isOnline
                                if (isOnline) fetchCurrentUserFromServer()
                            }
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_PROFILE" , response.error.toString())
                        }
                    }
                }
            }
        }
    }

    private fun fetchCurrentUserFromServer(){
        viewModelScope.launch(Dispatchers.IO) {
            getUserByIdUseCase.executeFromServer(currentUserId).collect{
                it?.let { response ->
                    when (response) {
                        is FirebaseResource.Success -> {
                            response.data?.let { mikotUser ->
                                _currentUser.value = mikotUser
                                Application.currentUser = mikotUser
                                saveUserToDBUseCase.execute(mikotUser.mapToRoomUser())
                            }
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_PROFILE" , response.error.toString())
                        }
                    }
                }
            }
        }
    }

}