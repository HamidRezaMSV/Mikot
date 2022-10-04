package hamid.msv.mikot.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import hamid.msv.mikot.R
import hamid.msv.mikot.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun HomeTopBar(connectionState : Boolean, onMenuClick: () -> Unit) {

    var dots by remember { mutableStateOf("") }
    var dotStateToggle by remember { mutableStateOf(true) }

    val topBarTextColor = if (connectionState) Green_Blue else Color.Red

    val topBarText = if (connectionState) stringResource(id = R.string.app_name).uppercase()
    else stringResource(id = R.string.connecting) + dots

    val topBarTextSize = if (connectionState) HOME_TOP_BAR_TEXT_SIZE_CONNECT
    else HOME_TOP_BAR_TEXT_SIZE_DISCONNECT

    if (!connectionState){
        LaunchedEffect(key1 = dotStateToggle) {
            dots = "."
            delay(400)
            dots = ".."
            delay(400)
            dots = "..."
            delay(400)
            dots = ""
            delay(400)
            dotStateToggle = !dotStateToggle
        }
    }

    Box(
        modifier = Modifier.padding(all = SMALL_PADDING),
    ) {
        Card(
            modifier = Modifier.requiredHeight(HOME_TOP_BAR_HEIGHT),
            backgroundColor = MaterialTheme.colors.homeTopBarBackgroundColor,
            contentColor = topBarTextColor,
            shape = RoundedCornerShape(size = HOME_TOP_BAR_CORNER_RADIUS),
            elevation = HOME_TOP_BAR_ELEVATION
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = SMALL_PADDING),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        modifier = Modifier.padding(start = MEDIUM_PADDING),
                        text = topBarText,
                        fontSize = topBarTextSize,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Start
                    )

                }

                IconButton(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxHeight(),
                    onClick = { onMenuClick() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = null,
                        tint = MaterialTheme.colors.contactTopBarIconColor
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeTopBarPreview() {
    HomeTopBar(false){}
}
