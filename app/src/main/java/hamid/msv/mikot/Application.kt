package hamid.msv.mikot

import android.app.Application
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Application : Application(){
    companion object{
        var currentUser : FirebaseUser? = null
    }
}