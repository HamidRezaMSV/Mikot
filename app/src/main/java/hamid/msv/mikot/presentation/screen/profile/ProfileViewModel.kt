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
import hamid.msv.mikot.data.remote.FirebaseApi
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.usecase.GetConnectionStateUseCase
import hamid.msv.mikot.domain.usecase.UpdateCurrentUserUseCase
import hamid.msv.mikot.util.COMPRESS_QUALITY
import hamid.msv.mikot.util.PHONE_NUMBER_CHARACTER_COUNT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getConnectionStateUseCase: GetConnectionStateUseCase,
    private val updateCurrentUserUseCase: UpdateCurrentUserUseCase
) : ViewModel(){

    private val currentUserId = Application.currentUserId!!

    private val _connectionState = MutableStateFlow(false)
    val connectionState = _connectionState.asStateFlow()

    init {
        detectConnectionState()
    }

    fun updateProfileImage(bitmap: Bitmap){
        Log.d("MIKOT_PROFILE" , "Method execute")
        val imageUri = getImageUriFromBitmap(bitmap)
        val path = FirebaseApi.PROFILE_IMAGE_STORAGE.child(currentUserId)
        path.putFile(imageUri).addOnCompleteListener {
            if (it.isSuccessful){
                path.downloadUrl.addOnCompleteListener { result ->
                    Log.d("MIKOT_PROFILE" , result.result.toString())
                }
            }else{
                Log.d("MIKOT_PROFILE" , it.exception!!.message.toString())
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
                            _connectionState.value = response.data!!
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