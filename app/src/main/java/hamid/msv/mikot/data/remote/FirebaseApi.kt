package hamid.msv.mikot.data.remote

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import hamid.msv.mikot.util.FIREBASE_DATABASE_URL
import javax.inject.Inject

@AndroidEntryPoint
object FirebaseApi {

    @Inject lateinit var database: DatabaseReference

    val USER_DATABASE = database.child("users")
    val MESSAGE_DATABASE = database.child("messages")
    val LAST_MESSAGE_DATABASE = database.child("last_message")

}