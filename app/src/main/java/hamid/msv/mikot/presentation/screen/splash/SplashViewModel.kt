package hamid.msv.mikot.presentation.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.domain.usecase.ReadOnBoardingUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
@Inject constructor(readOnBoardingUseCase: ReadOnBoardingUseCase) : ViewModel() {

    private val _onBoardingState = MutableStateFlow(false)
    val onBoardingState: StateFlow<Boolean>
        get() = _onBoardingState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _onBoardingState.value = readOnBoardingUseCase.execute().stateIn(viewModelScope).value
        }
    }

}