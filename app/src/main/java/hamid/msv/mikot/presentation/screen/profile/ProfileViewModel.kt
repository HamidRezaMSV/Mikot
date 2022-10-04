package hamid.msv.mikot.presentation.screen.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hamid.msv.mikot.domain.model.FirebaseResource
import hamid.msv.mikot.domain.usecase.GetConnectionStateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getConnectionStateUseCase: GetConnectionStateUseCase
) : ViewModel(){

    private val _connectionState = MutableStateFlow(false)
    val connectionState = _connectionState.asStateFlow()


    init {
        detectConnectionState()
    }

    private fun detectConnectionState() {
        viewModelScope.launch(Dispatchers.IO) {
            getConnectionStateUseCase.execute().collect{
                it?.let { response ->
                    when(response){
                        is FirebaseResource.Success -> {
                            _connectionState.value = response.data!!
                        }
                        is FirebaseResource.Error -> {
                            Log.d("MIKOT_PROFILE" , response.error.toString())
                        }
                    }
                }
            }
        }
    }

}