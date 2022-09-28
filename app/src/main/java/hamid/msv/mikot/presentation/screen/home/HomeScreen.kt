package hamid.msv.mikot.presentation.screen.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import hamid.msv.mikot.R
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.navigation.Screen
import hamid.msv.mikot.ui.theme.*

@Composable
fun HomeScreen(
    navController : NavHostController ,
    viewModel : HomeViewModel = hiltViewModel()
) {

    val userList = viewModel.userList.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize() ,
        contentPadding = PaddingValues(EXTRA_SMALL_PADDING)
    ){
        items(userList.value){ user ->
            HomeScreenUserItem(
                user = user,
                onItemSelected = { userId ->
                    Log.d("MIKOT_HOME" , "selected item id : $userId")
                    navController.navigate(Screen.Chat.route)
                }
            )
        }
    }

}

@Composable
fun HomeScreenUserItem(user:MikotUser , onItemSelected : (userId:String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(USER_ITEM_HEIGHT)
            .padding(EXTRA_SMALL_PADDING)
            .clickable { onItemSelected(user.id!!) },
        elevation = USER_ITEM_ELEVATION , 
        shape = RoundedCornerShape(size = USER_ITEM_CORNER_RADIUS) ,
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
                    .size(USER_ITEM_IMAGE_SIZE)
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
                        fontSize = USER_ITEM_NAME_TEXT_SIZE ,
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

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    HomeScreenUserItem(MikotUser()){}
}