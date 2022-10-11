package hamid.msv.mikot.data.remote

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import hamid.msv.mikot.util.FIREBASE_DATABASE_URL
import hamid.msv.mikot.util.FIREBASE_STORAGE_URL

object FirebaseApi {

    private val database: DatabaseReference =
        FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL).reference

    private val storage: StorageReference =
        FirebaseStorage.getInstance(FIREBASE_STORAGE_URL).reference

    val USER_DATABASE = database.child("users")
    val MESSAGE_DATABASE = database.child("messages")
    val LAST_MESSAGE_DATABASE = database.child("last_message")

    val PROFILE_IMAGE_STORAGE = storage.child("profile_images/")

}