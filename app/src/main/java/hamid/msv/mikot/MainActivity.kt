package hamid.msv.mikot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import hamid.msv.mikot.ui.theme.MikotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MikotTheme {

            }
        }
    }
}