package hamid.msv.mikot.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import hamid.msv.mikot.R
import hamid.msv.mikot.ui.theme.*

@Composable
fun HomeScreenMenu(
    expanded: MutableState<Boolean>,
    onSettingClick: () -> Unit,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.TopEnd)
            .padding(top = HOME_MENU_TOP_PADDING)
            .padding(end = MEDIUM_PADDING)
    ) {
        DropdownMenu(
            modifier = Modifier
                .background(Color.White)
                .clip(RoundedCornerShape(size = HOME_MENU_CORNER_RADIUS)),
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            properties = PopupProperties(focusable = true)
        ) {
            DropdownMenuItem(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    expanded.value = false
                    onProfileClick()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.profile),
                    color = Color.Black.copy(0.9f)
                )
                Spacer(modifier = Modifier.width(LARGE_PADDING))
                Icon(
                    modifier = Modifier.padding(start = 5.dp),
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Black.copy(0.9f)
                )
            }

            DropdownMenuItem(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    expanded.value = false
                    onSettingClick()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.setting),
                    color = Color.Black.copy(0.9f)
                )
                Spacer(modifier = Modifier.width(LARGE_PADDING))
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color.Black.copy(0.9f)
                )
            }

            DropdownMenuItem(
                modifier = Modifier.align(Alignment.End),
                onClick = {
                    expanded.value = false
                    onLogoutClick()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.logout),
                    color = Color.Black.copy(0.9f)
                )
                Spacer(modifier = Modifier.width(LARGE_PADDING))
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null,
                    tint = Color.Black.copy(0.9f)
                )
            }
        }
    }
}