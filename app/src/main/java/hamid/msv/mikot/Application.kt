package hamid.msv.mikot

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import hamid.msv.mikot.util.USER_IS_NOT_LOGIN

@HiltAndroidApp
class Application : Application(){
    companion object{
        var currentUserId : String? = null
        // should pass receiverId from Chat ui to ChatViewModel to fetch user info
        // but now we pass this id from here :
        var receiverId : String? = null
    }
}