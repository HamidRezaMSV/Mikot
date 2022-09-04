package hamid.msv.mikot.presentation.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.domain.usecase.GetAllUsersUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase
) : ViewModel() {

    init {
        getAllUsers()
    }

    private val _users = mutableStateOf<List<MikotUser>>(emptyList())
    val users : State<List<MikotUser>>
    get() = _users

    private fun getAllUsers() =
        getAllUsersUseCase.execute().onEach { data -> _users.value = data }.launchIn(viewModelScope)

}