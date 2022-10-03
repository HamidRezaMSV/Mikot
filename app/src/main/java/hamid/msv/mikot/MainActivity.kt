package hamid.msv.mikot

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import hamid.msv.mikot.domain.model.Contact
import hamid.msv.mikot.navigation.SetupNavGraph
import hamid.msv.mikot.ui.theme.MikotTheme

@ExperimentalPagerApi
@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        try {
//            val contacts = getPhoneContacts().distinctBy { it.name }
//            Application.contactList.addAll(contacts)
//        }catch (exception: Exception){
//            Log.d("MIKOT_MAIN_EXCEPTION" , exception.message.toString())
//        }
//
//        Application.contactList.forEach {
//            Log.d("MIKOT_MAIN" , it.toString())
//        }


        setContent {
            MikotTheme {
                SetupNavGraph()
            }
        }
    }
}