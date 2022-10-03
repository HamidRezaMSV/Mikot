package hamid.msv.mikot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import hamid.msv.mikot.navigation.SetupNavGraph
import hamid.msv.mikot.ui.theme.MikotTheme

@ExperimentalPagerApi
@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MikotTheme {
                SetupNavGraph()
            }
        }
    }
}