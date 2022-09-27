package hamid.msv.mikot.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.domain.usecase.ReadCurrentUserIdUseCase
import hamid.msv.mikot.domain.usecase.ReadOnBoardingUseCase
import hamid.msv.mikot.util.USER_IS_NOT_LOGIN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject constructor(
    readOnBoardingUseCase: ReadOnBoardingUseCase,
    readCurrentUserIdUseCase: ReadCurrentUserIdUseCase
) : ViewModel() {

    private val _onBoardingCompleted = MutableStateFlow(false)
    val onBoardingCompleted: StateFlow<Boolean> = _onBoardingCompleted.asStateFlow()

    private val _isLogin = MutableStateFlow(false)
    val isLogin : StateFlow<Boolean> = _isLogin.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _isLogin.value = readCurrentUserIdUseCase.execute().stateIn(viewModelScope).value != USER_IS_NOT_LOGIN
            _onBoardingCompleted.value = readOnBoardingUseCase.execute().stateIn(viewModelScope).value
        }
    }

}