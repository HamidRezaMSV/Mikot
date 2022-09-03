package hamid.msv.mikot.presentation.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.domain.usecase.SaveLoginStateUseCase
import hamid.msv.mikot.domain.usecase.SaveUserInDatabaseUseCase
import hamid.msv.mikot.domain.usecase.SignUpUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val signUpUserUseCase: SignUpUserUseCase,
    private val saveUserInDatabaseUseCase: SaveUserInDatabaseUseCase ,
    private val saveLoginStateUseCase: SaveLoginStateUseCase
): ViewModel() {

    val signUpResponse = signUpUserUseCase.response
    val saveUserInFirebaseResponse = saveUserInDatabaseUseCase.response

    fun signUpUser(email:String , password : String){
        viewModelScope.launch(Dispatchers.IO) { signUpUserUseCase.execute(email, password) }
    }

    fun saveUserInFirebase(user: MikotUser){
        viewModelScope.launch{ saveUserInDatabaseUseCase.execute(user) }
    }

    fun saveLoginState(isLogin:Boolean){
        viewModelScope.launch { saveLoginStateUseCase.execute(isLogin) }
    }

}

//    val signUpResponse : StateFlow<Task<AuthResult>> = signUpUserUseCase.response.value
//    val a = signUpUserUseCase.response.observeForever {}