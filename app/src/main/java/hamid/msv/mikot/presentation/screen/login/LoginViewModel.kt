package hamid.msv.mikot.presentation.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.domain.usecase.SaveLoginStateUseCase
import hamid.msv.mikot.domain.usecase.SignInUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val saveLoginStateUseCase: SaveLoginStateUseCase,
    private val signInUserUseCase: SignInUserUseCase
): ViewModel() {

    val loginResponse = signInUserUseCase.response

    fun loginUser(email:String , password : String) =
        viewModelScope.launch(Dispatchers.IO) { signInUserUseCase.execute(email, password) }

    fun saveLoginState(isLogin : Boolean) =
        viewModelScope.launch(Dispatchers.IO) { saveLoginStateUseCase.execute(isLogin) }

}