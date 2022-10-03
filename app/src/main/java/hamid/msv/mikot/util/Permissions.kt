package hamid.msv.mikot.util

import android.Manifest
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import hamid.msv.mikot.R
import hamid.msv.mikot.ui.theme.*

@Composable
@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
fun RequestForReadContactPermission(
    onGranted: @Composable () -> Unit,
) {
    val readContactPermission =
        rememberPermissionState(permission = Manifest.permission.READ_CONTACTS)

    val visible = remember{ mutableStateOf(false) }

    when (readContactPermission.hasPermission) {
        true -> {
            Log.d("MIKOT_PERMISSION", "Read Contact permission GRANTED")
            onGranted()
        }
        false -> {
            Log.d("MIKOT_PERMISSION", "Read Contact permission DENIED")
            visible.value = true
            val text =
                if (readContactPermission.shouldShowRationale)
                    stringResource(id = R.string.contact_permission1)
                else
                    stringResource(id = R.string.contact_permission2)

            RequestPermissionDialog(
                visible = visible,
                text = text,
                title = stringResource(id = R.string.contact_permission),
                permission = readContactPermission,
            )
        }
    }
}

@Composable
@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
fun RequestPermissionDialog(
    visible: MutableState<Boolean>,
    text: String,
    title: String,
    permission: PermissionState
) {
    Dialog(
        onDismissRequest = { visible.value = false },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = { RequestPermissionContent(text = text, title = title, permission = permission) }
    )
}

@Composable
@ExperimentalPermissionsApi
private fun RequestPermissionContent(text: String, title: String, permission: PermissionState) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ){
        Card(
            modifier = Modifier.fillMaxWidth(0.7f),
            shape = RoundedCornerShape(size = PERMISSION_CARD_CORNER_RADIUS),
            elevation = PERMISSION_CARD_ELEVATION,
            backgroundColor = Color.White
        ) {
            Column(
                modifier = Modifier.padding(EXTRA_MEDIUM_PADDING),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    color = Green_Blue,
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(EXTRA_MEDIUM_PADDING))

                Text(
                    text = text,
                    textAlign = TextAlign.Center,
                    color = Color.Black.copy(0.8f),
                    fontWeight = FontWeight.Normal,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize
                )

                Spacer(modifier = Modifier.height(EXTRA_LARGE_PADDING))

                Button(
                    onClick = { permission.launchPermissionRequest() },
                    shape = RoundedCornerShape(size = PERMISSION_BUTTON_CORNER_RADIUS),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Green_Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.request_permission)
                    )
                }
            }
        }
    }
}
