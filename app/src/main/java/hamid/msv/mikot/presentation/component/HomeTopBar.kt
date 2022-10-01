package hamid.msv.mikot.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import hamid.msv.mikot.R
import hamid.msv.mikot.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun HomeTopBar(connectionState : Boolean) {

    val visible = remember { mutableStateOf(false) }
    val enterAnimation =
        expandVertically(
            animationSpec = tween(durationMillis = 1500),
            expandFrom = Alignment.Bottom
        ) + fadeIn(animationSpec = tween(durationMillis = 2000))

    val topBarTextColor = if (connectionState) Color.Green else Color.Red

    LaunchedEffect(key1 = true) {
        delay(500)
        visible.value = true
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = SMALL_PADDING),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(
                    visible = visible.value,
                    enter = enterAnimation
                ) {
                    Text(
                        modifier = Modifier,
                        text = stringResource(id = R.string.app_name).uppercase(),
                        fontSize = HOME_TOP_BAR_TEXT_SIZE,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = FontFamily.Serif
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTopBarPreview() {
    HomeTopBar(true)
}
