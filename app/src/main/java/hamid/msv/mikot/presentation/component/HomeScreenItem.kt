package hamid.msv.mikot.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import hamid.msv.mikot.R
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.ui.theme.*

@Composable
fun HomeScreenItem(user: MikotUser, onItemSelected : (userId:String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(HOME_SCREEN_ITEM_HEIGHT)
            .padding(EXTRA_SMALL_PADDING)
            .clickable { onItemSelected(user.id!!) },
        elevation = HOME_SCREEN_ITEM_ELEVATION ,
        shape = RoundedCornerShape(size = HOME_SCREEN_ITEM_CORNER_RADIUS) ,
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(SMALL_PADDING) ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(HOME_SCREEN_ITEM_IMAGE_SIZE)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .border(width = 1.dp, color = Color.Black, shape = CircleShape),
                painter = painterResource(id = R.drawable.img_user),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(SMALL_PADDING))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = EXTRA_SMALL_PADDING) ,
                    horizontalArrangement = Arrangement.SpaceBetween ,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = user.userName ?: stringResource(id = R.string.unknown) ,
                        color = Color.Black ,
                        fontSize = HOME_SCREEN_ITEM_NAME_TEXT_SIZE ,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        modifier = Modifier.padding(end = EXTRA_SMALL_PADDING) ,
                        text = user.createAccountTime ?: "00:00" ,
                        color = Color.Black.copy(ContentAlpha.medium) ,
                        fontSize = MaterialTheme.typography.body2.fontSize
                    )
                }

                Text(
                    modifier = Modifier.weight(1f) ,
                    text = user.email ?: "example@gmail.com" ,
                    color = Color.Black.copy(ContentAlpha.medium) ,
                    fontWeight = FontWeight.SemiBold ,
                    fontSize = MaterialTheme.typography.subtitle2.fontSize ,
                    maxLines = 2 ,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}