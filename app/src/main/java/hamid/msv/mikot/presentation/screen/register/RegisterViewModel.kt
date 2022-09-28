package hamid.msv.mikot.presentation.screen.register

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.Application
import hamid.msv.mikot.R
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.domain.usecase.SaveCurrentUserIdUseCase
import hamid.msv.mikot.domain.usecase.SaveUserInFirebaseUseCase
import hamid.msv.mikot.domain.usecase.SignUpUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val signUpUserUseCase: SignUpUserUseCase,
    private val saveUserInFirebaseUseCase: SaveUserInFirebaseUseCase,
    private val saveCurrentUserIdUseCase: SaveCurrentUserIdUseCase
): ViewModel() {

    val currentUser = MikotUser()

    init {
        collectSignUpResponse()
        collectSaveUserInFirebaseResponse()
    }

    private val _userId = MutableStateFlow<String?>(null)
    val userId = _userId.asStateFlow()

    private val _navigateToHomeScreen = MutableStateFlow(false)
    val navigateToHomeScreen = _navigateToHomeScreen.asStateFlow()

    fun signUpUser(email:String , password : String){
        viewModelScope.launch(Dispatchers.IO) { signUpUserUseCase.execute(email, password) }
    }

    fun saveUserInFirebase(user: MikotUser){
        viewModelScope.launch(Dispatchers.IO){ saveUserInFirebaseUseCase.execute(user) }
    }

    private fun saveCurrentUserUid(uid : String){
        viewModelScope.launch(Dispatchers.IO) { saveCurrentUserIdUseCase.execute(uid) }
    }

    fun finishAuthentication() {
        _userId.value = null
    }

    fun finishRegistering(){
        _navigateToHomeScreen.value = false
    }

    fun isInputDataValid(
        fullName: String, userName: String, password: String, confirmPassword: String,
        email: String, phoneNumber: String, context: Context)
    : Boolean {
        val emailValidation = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val passwordValidation = password.length >= 6
        val confirmPasswordValidation = password.length >= 6 && confirmPassword == password
        val fullNameValidation = fullName.isNotEmpty()
        val userNameValidation = userName.isNotEmpty()
        val phoneNumberValidation = phoneNumber.isNotEmpty()
        when {
            !emailValidation -> Toast.makeText(
                context,
                context.getString(R.string.invalid_email_message),
                Toast.LENGTH_SHORT
            ).show()
            !passwordValidation -> Toast.makeText(
                context,
                context.getString(R.string.invalid_password_message),
                Toast.LENGTH_SHORT
            ).show()
            !fullNameValidation -> Toast.makeText(
                context,
                context.getString(R.string.invalid_name_message),
                Toast.LENGTH_SHORT
            ).show()
            !userNameValidation -> Toast.makeText(
                context,
                context.getString(R.string.invalid_username_message),
                Toast.LENGTH_SHORT
            ).show()
            !phoneNumberValidation -> Toast.makeText(
                context,
                context.getString(R.string.invalid_phone_message),
                Toast.LENGTH_SHORT
            ).show()
            !confirmPasswordValidation -> Toast.makeText(
                context,
                context.getString(R.string.invalid_confirm_password_message),
                Toast.LENGTH_SHORT
            ).show()
        }
        return emailValidation &&
                passwordValidation &&
                fullNameValidation &&
                userNameValidation &&
                phoneNumberValidation &&
                confirmPasswordValidation
    }

    private fun collectSignUpResponse(){
        viewModelScope.launch {
            signUpUserUseCase.signUpResponse.collect{
                it?.let {
                    when(it){
                        is FirebaseResource.Success -> {
                            Log.d("Mikot_Register", "isSuccessful")
                            _userId.value = it.data
                            Application.currentUserId = it.data!!
                        }
                        is FirebaseResource.Error -> {
                            Log.d("Mikot_Register", it.error!!)
                        }
                    }
                }
            }
        }
    }

    private fun collectSaveUserInFirebaseResponse(){
        viewModelScope.launch {
            saveUserInFirebaseUseCase.saveNewUserResponse.collect{
                it?.let {
                    when(it){
                        is FirebaseResource.Success -> {
                            Log.d("Mikot_Register", "database isSuccessful")
                            saveCurrentUserUid(uid = Application.currentUserId)
                            _navigateToHomeScreen.value = true
                        }
                        is FirebaseResource.Error -> {
                            Log.d("Mikot_Register", "database" + it.error)
                        }
                    }
                }
            }
        }
    }

}