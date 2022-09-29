package hamid.msv.mikot.presentation.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import hamid.msv.mikot.R
import hamid.msv.mikot.ui.theme.*

@Composable
fun HomeTopBar() {

    val alpha = animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000 , delayMillis = 300)
    )

    Box(
        modifier = Modifier.padding(all = SMALL_PADDING).alpha(alpha.value)
    ){
        Card(
            modifier = Modifier.requiredHeight(HOME_TOP_BAR_HEIGHT),
            backgroundColor = MaterialTheme.colors.homeTopBarBackgroundColor,
            contentColor = MaterialTheme.colors.homeTopBarTextColor,
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
                Text(
                    text = stringResource(id = R.string.app_name).uppercase() ,
                    fontSize = HOME_TOP_BAR_TEXT_SIZE,
                    fontWeight = FontWeight.Bold ,
                    maxLines = 1 ,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily.Serif
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeTopBarPreview() {
    HomeTopBar()
}