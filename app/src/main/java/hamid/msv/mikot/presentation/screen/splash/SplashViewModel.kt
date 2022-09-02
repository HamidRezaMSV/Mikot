package hamid.msv.mikot.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.domain.usecase.ReadLoginStateUseCase
import hamid.msv.mikot.domain.usecase.ReadOnBoardingUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject constructor(
    readOnBoardingUseCase: ReadOnBoardingUseCase ,
    readLoginStateUseCase: ReadLoginStateUseCase
) : ViewModel() {

    private val _onBoardingCompleted = MutableStateFlow(false)
    val onBoardingCompleted: StateFlow<Boolean>
        get() = _onBoardingCompleted

    private val _isLogin = MutableStateFlow(false)
    val isLogin : StateFlow<Boolean>
        get() = _isLogin

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _isLogin.value = readLoginStateUseCase.execute().stateIn(viewModelScope).value
            _onBoardingCompleted.value = readOnBoardingUseCase.execute().stateIn(viewModelScope).value
        }
    }

}