package hamid.msv.mikot.presentation.screen.home

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.domain.usecase.GetAllUsersUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase
) : ViewModel() {

    init {
        listenForAllUsers()
    }

    private val _userList = MutableStateFlow<List<MikotUser>>(emptyList())
    val userList = _userList.asStateFlow()

    private fun listenForAllUsers(){
        viewModelScope.launch {
            getAllUsersUseCase.execute().collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            response.data?.let { data ->
                                _userList.value = data.map { user ->
                                    user.createAccountTime = parseTime(user.createAccountTime!!.toLong())
                                    user
                                }
                            }
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_HOME" , response.error.toString())
                        }
                    }
                }
            }
        }
    }

    private fun parseTime(time:Long): String{
        return SimpleDateFormat.getPatternInstance("yyyy/MM/dd", Locale.ENGLISH).format(Date(time))
    }
}