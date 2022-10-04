package hamid.msv.mikot.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import hamid.msv.mikot.R
import hamid.msv.mikot.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ProfileTopBar(
    connectionState: State<Boolean>,
    onBackClick: () -> Unit,
    onSaveClick: ()-> Unit
) {

    var dots by remember { mutableStateOf("   ") }
    var dotStateToggle by remember { mutableStateOf(true) }

    val topBarTextColor = if (connectionState.value) Green_Blue else Color.Red
    val topBarText = if (connectionState.value) stringResource(id = R.string.profile).uppercase()
    else stringResource(id = R.string.connecting) + dots

    val topBarTextSize = if (connectionState.value) CONTACT_TOP_BAR_TEXT_SIZE_CONNECT
    else HOME_TOP_BAR_TEXT_SIZE_DISCONNECT

    if (!connectionState.value){
        LaunchedEffect(key1 = dotStateToggle) {
            dots = ".  "
            delay(400)
            dots = ".. "
            delay(400)
            dots = "..."
            delay(400)
            dots = "   "
            delay(400)
            dotStateToggle = !dotStateToggle
        }
    }

    Box(
        modifier = Modifier.padding(all = SMALL_PADDING),
    ){
        Card(
            modifier = Modifier.requiredHeight(HOME_TOP_BAR_HEIGHT),
            backgroundColor = MaterialTheme.colors.homeTopBarBackgroundColor,
            contentColor = topBarTextColor,
            shape = RoundedCornerShape(size = HOME_TOP_BAR_CORNER_RADIUS),
            elevation = HOME_TOP_BAR_ELEVATION
        ){
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = SMALL_PADDING),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        modifier = Modifier,
                        text = topBarText,
                        fontSize = topBarTextSize,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center
                    )
                }

                IconButton(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxHeight()
                        .align(Alignment.CenterStart),
                    onClick = { onBackClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colors.contactTopBarIconColor
                    )
                }

                IconButton(
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    onClick = { onSaveClick() }
                ) {
                    Image(
                        modifier = Modifier
                            .requiredHeight(HOME_TOP_BAR_HEIGHT)
                            .padding(vertical = MEDIUM_PADDING)
                            .padding(end = PROFILE_TOP_BAR_END_PADDING)
                            .aspectRatio(1f),
                        painter = painterResource(id = R.drawable.ic_save),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.contactTopBarIconColor)
                    )
                }
            }
        }
    }

}