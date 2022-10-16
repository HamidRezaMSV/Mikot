package hamid.msv.mikot.presentation.screen.login

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
import hamid.msv.mikot.domain.usecase.SaveCurrentUserIdUseCase
import hamid.msv.mikot.domain.usecase.SignInUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val saveCurrentUserIdUseCase: SaveCurrentUserIdUseCase,
    private val signInUserUseCase: SignInUserUseCase
) : ViewModel() {

    private val _navigateToHomeScreen = MutableSharedFlow<Boolean>()
    val navigateToHomeScreen = _navigateToHomeScreen.asSharedFlow()

    init {
        collectSignInResponse()
    }

    fun signInUser(email: String, password: String) {
        viewModelScope.launch {
            signInUserUseCase.execute(email, password)
        }
    }

    fun finishSignIn(){
        viewModelScope.launch{
            _navigateToHomeScreen.emit(false)
        }
    }

    private fun saveCurrentUserId(uid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            saveCurrentUserIdUseCase.execute(uid)
        }
    }

    private fun collectSignInResponse() {
        viewModelScope.launch {
            signInUserUseCase.signInResponse.collect {
                it?.let {
                    when (it) {
                        is FirebaseResource.Success -> {
                            Log.d("Mikot_login", "login successful")
                            saveCurrentUserId(uid = it.data.toString())
                            Application.currentUserId = it.data.toString()
                            _navigateToHomeScreen.emit(true)
                        }
                        is FirebaseResource.Error -> {
                            Log.d("Mikot_login", it.error.toString())
                        }
                    }
                }
            }
        }
    }

    fun isInputDataValid(email:String,password:String,context: Context) : Boolean{
        val emailValidation = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val passwordValidation = password.length >= 6
        when{
            !emailValidation -> Toast.makeText(context,context.getString(R.string.invalid_email_message), Toast.LENGTH_SHORT).show()
            !passwordValidation -> Toast.makeText(context,context.getString(R.string.invalid_password_message), Toast.LENGTH_SHORT).show()
        }
        return emailValidation && passwordValidation
    }

}