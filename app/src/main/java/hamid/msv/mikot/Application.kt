package hamid.msv.mikot

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import hamid.msv.mikot.util.USER_IS_NOT_LOGIN

@HiltAndroidApp
class Application : Application(){
    companion object{
        var currentUserId : String? = null
    }
}