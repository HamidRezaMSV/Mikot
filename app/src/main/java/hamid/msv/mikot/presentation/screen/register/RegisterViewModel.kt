package hamid.msv.mikot.presentation.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.domain.usecase.SaveCurrentUserIdUseCase
import hamid.msv.mikot.domain.usecase.SaveUserInFirebaseUseCase
import hamid.msv.mikot.domain.usecase.SignUpUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val signUpUserUseCase: SignUpUserUseCase,
    private val saveUserInFirebaseUseCase: SaveUserInFirebaseUseCase,
    private val saveCurrentUserIdUseCase: SaveCurrentUserIdUseCase
): ViewModel() {

    val signUpResponse = signUpUserUseCase.response
    val saveUserInFirebaseResponse = saveUserInFirebaseUseCase.response

    fun signUpUser(email:String , password : String){
        viewModelScope.launch(Dispatchers.IO) { signUpUserUseCase.execute(email, password) }
    }

    fun saveUserInFirebase(user: MikotUser){
        viewModelScope.launch(Dispatchers.IO){ saveUserInFirebaseUseCase.execute(user) }
    }

    fun saveCurrentUserUid(uid : String){
        viewModelScope.launch(Dispatchers.IO) { saveCurrentUserIdUseCase.execute(uid) }
    }

}