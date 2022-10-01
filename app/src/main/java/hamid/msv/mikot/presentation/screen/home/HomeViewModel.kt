package hamid.msv.mikot.presentation.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.Application
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.model.LastMessage
import hamid.msv.mikot.domain.usecase.GetAllLastMessagesUseCase
import hamid.msv.mikot.domain.usecase.GetConnectionStateUseCase
import hamid.msv.mikot.domain.usecase.GetUserByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getConnectionStateUseCase: GetConnectionStateUseCase,
    private val getAllLastMessagesUseCase: GetAllLastMessagesUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
): ViewModel(){

    private val currentUserId = Application.currentUserId!!

    private val _connectionState = MutableStateFlow(false)
    val connectionState = _connectionState.asStateFlow()

    private val _lastMessages = MutableStateFlow<List<LastMessage>>(emptyList())
    val lastMessages = _lastMessages.asStateFlow()

    init {
        detectConnectionState()
        listenForLastMessages()
    }

    private fun detectConnectionState(){
        viewModelScope.launch(Dispatchers.IO) {
            getConnectionStateUseCase.execute().collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            _connectionState.value = response.data!!
                            if (response.data){
                                fetchCurrentUserInfo(currentUserId)
                            }
                            Log.d("MIKOT_HOME" , "Connection State : ${response.data}")
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_HOME" , response.error.toString())
                        }
                    }
                }
            }
        }
    }

    private fun listenForLastMessages(){
        viewModelScope.launch(Dispatchers.IO) {
            getAllLastMessagesUseCase.execute(currentUserId).collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            response.data?.let { data ->
                                _lastMessages.value = data
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

    private fun fetchCurrentUserInfo(currentUserId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getUserByIdUseCase.execute(currentUserId).collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            Application.currentUser = response.data!!
                            Log.d("MIKOT_HOME" , "current user info fetched successfully")
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_HOME" , response.error.toString())
                        }
                    }
                }
            }
        }
    }
}