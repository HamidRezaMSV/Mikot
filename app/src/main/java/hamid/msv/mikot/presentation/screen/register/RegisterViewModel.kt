package hamid.msv.mikot.presentation.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

//    val signUpResponse : StateFlow<Task<AuthResult>> = signUpUserUseCase.response.value

    val signUpResponse = signUpUserUseCase.response

//    val a = signUpUserUseCase.response.observeForever {}

    fun signUpUser(email:String , password : String){
        viewModelScope.launch(Dispatchers.IO) { signUpUserUseCase.execute(email, password) }
    }

}