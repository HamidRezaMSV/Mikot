package hamid.msv.mikot

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import hamid.msv.mikot.domain.model.Contact
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.util.USER_IS_NOT_LOGIN

@HiltAndroidApp
class Application : Application(){
    companion object{
        var currentUserId : String? = null
        var currentUser : MikotUser? = null
        var contactList = mutableListOf<Contact>()

        // should pass receiverId from Chat ui to ChatViewModel to fetch user info
        // but now we pass this id from here :
        var receiverId : String? = null
    }
}