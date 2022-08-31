package hamid.msv.mikot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import hamid.msv.mikot.navigation.SetupNavGraph
import hamid.msv.mikot.ui.theme.MikotTheme

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