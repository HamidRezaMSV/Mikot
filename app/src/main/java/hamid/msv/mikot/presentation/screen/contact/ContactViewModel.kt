package hamid.msv.mikot.presentation.screen.contact

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.domain.usecase.GetAllUsersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
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
                                    user.phoneNumber = parsPhoneNumber(user.phoneNumber!!)
                                    user
                                }
                            }
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_CONTACT" , response.error.toString())
                        }
                    }
                }
            }
        }
    }

    private fun parseTime(time:Long): String{
        return SimpleDateFormat.getPatternInstance("yyyy/MM/dd", Locale.ENGLISH).format(Date(time))
    }

    private fun parsPhoneNumber(number: String): String {
        val part1 = number.substring(startIndex = 0, endIndex = 4)
        val part2 = number.substring(startIndex = 4, endIndex = 7)
        val part3 = number.substring(startIndex = 7, endIndex = 11)

        return "$part1 $part2 $part3"
    }
}