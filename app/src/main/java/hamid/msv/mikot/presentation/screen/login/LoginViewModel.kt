package hamid.msv.mikot.presentation.screen.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.Application
import hamid.msv.mikot.domain.model.FirebaseResponse
import hamid.msv.mikot.domain.usecase.SaveLoginStateUseCase
import hamid.msv.mikot.domain.usecase.SignInUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val saveLoginStateUseCase: SaveLoginStateUseCase,
    private val signInUserUseCase: SignInUserUseCase
): ViewModel() {

    val loginResponse = mutableStateOf(FirebaseResponse.END)

    fun loginUser(email:String , password : String)  {
        signInUserUseCase.execute(email, password)
            .onEach {
            if (it.isSuccessful){
                saveLoginState()
                Application.currentUser = it.result.user
                loginResponse.value = FirebaseResponse.SUCCESSFUL
            }else{
                Log.d("Mikot_login", it.exception?.message.toString())
                loginResponse.value = FirebaseResponse.FAILED
            }
        }.launchIn(viewModelScope)
    }

    private fun saveLoginState() = viewModelScope.launch(Dispatchers.IO) { saveLoginStateUseCase.execute(true) }

}