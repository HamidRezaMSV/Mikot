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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hamid.msv.mikot.R
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.ui.theme.*

@Composable
fun ContactScreenItem(user: MikotUser, onItemSelected: (userId: String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(CONTACT_SCREEN_ITEM_HEIGHT)
            .padding(EXTRA_SMALL_PADDING)
            .clickable { onItemSelected(user.id!!) },
        elevation = CONTACT_SCREEN_ITEM_ELEVATION,
        shape = RoundedCornerShape(size = CONTACT_SCREEN_ITEM_CORNER_RADIUS),
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(SMALL_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(CONTACT_SCREEN_ITEM_IMAGE_SIZE)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .border(width = 1.dp, color = Color.Black, shape = CircleShape),
                painter = painterResource(id = R.drawable.img_user),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(SMALL_PADDING))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = user.fullName ?: stringResource(id = R.string.unknown),
                    color = Color.Black,
                    fontSize = CONTACT_SCREEN_ITEM_NAME_TEXT_SIZE,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = user.phoneNumber!!,
                    color = Color.Black.copy(ContentAlpha.medium),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = MaterialTheme.typography.subtitle2.fontSize,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}



@Composable
@Preview(showBackground = true)
fun ContactScreenItemPreview() {
    val user = MikotUser(
        id = "1",
        userName = "Hamid",
        fullName = "Hamid Reza Mousavi",
        phoneNumber = "0937 524 0885",
        bio = "hey, I am here"
    )
    ContactScreenItem(user = user) {}
}