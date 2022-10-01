package hamid.msv.mikot.presentation.screen.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.Application
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.usecase.GetUserByIdUseCase
import hamid.msv.mikot.domain.usecase.ReadCurrentUserIdUseCase
import hamid.msv.mikot.domain.usecase.ReadOnBoardingUseCase
import hamid.msv.mikot.util.USER_IS_NOT_LOGIN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject constructor(
    private val readOnBoardingUseCase: ReadOnBoardingUseCase,
    private val readCurrentUserIdUseCase: ReadCurrentUserIdUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : ViewModel() {

    private val _onBoardingCompleted = MutableStateFlow(false)
    val onBoardingCompleted: StateFlow<Boolean> = _onBoardingCompleted.asStateFlow()

    private val _isLogin = MutableStateFlow(false)
    val isLogin : StateFlow<Boolean> = _isLogin.asStateFlow()

    init {
        checkStates()
    }

    private fun checkStates(){
        viewModelScope.launch(Dispatchers.IO) {
            val uid = readCurrentUserIdUseCase.execute().stateIn(viewModelScope).value
            _isLogin.value = uid != USER_IS_NOT_LOGIN
            Application.currentUserId = uid
            _onBoardingCompleted.value = readOnBoardingUseCase.execute().stateIn(viewModelScope).value
            // todo : later should use DataStore to cache current user info
            fetchCurrentUserInfo(currentUserId = uid)
        }
    }

    private fun fetchCurrentUserInfo(currentUserId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserByIdUseCase.execute(currentUserId).collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            Application.currentUser = response.data!!
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_CHAT" , response.error.toString())
                        }
                    }
                }
            }
        }
    }

}