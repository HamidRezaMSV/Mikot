package hamid.msv.mikot.presentation.component

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import hamid.msv.mikot.R
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.ui.theme.*

@Composable
fun ChatTopBar(
    user: MikotUser,
    expanded:MutableState<Boolean>,
    isOnline: Boolean,
    onBackClick: () -> Unit,
    onExpandClick: () -> Unit
) {

    val expandIcon = if (expanded.value) Icons.Default.KeyboardArrowUp
    else Icons.Default.KeyboardArrowDown

    val expandIconColor = if (isOnline) Green_Blue else Red

    val enterAnimation = fadeIn(tween(500)) + expandVertically(tween(500))
    val exitAnimation = fadeOut(tween(500)) + shrinkVertically(tween(500))

    Box(
        modifier = Modifier
            .padding(horizontal = SMALL_PADDING)
            .padding(top = SMALL_PADDING)
            .padding(bottom = EXTRA_SMALL_PADDING)
    ) {
        Card(
            shape = RoundedCornerShape(size = CHAT_TOP_BAR_CORNER_RADIUS),
            elevation = CHAT_TOP_BAR_ELEVATION,
            backgroundColor = Color.White
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(CHAT_TOP_BAR_HEIGHT)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = SMALL_PADDING)
                            .padding(top = EXTRA_SMALL_PADDING),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = user.userName ?: "Unknown",
                            color = MaterialTheme.colors.chatTopBarTextColor,
                            fontSize = MaterialTheme.typography.h5.fontSize,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .fillMaxHeight()
                            .align(Alignment.TopStart),
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
                            .aspectRatio(1f)
                            .fillMaxHeight()
                            .align(Alignment.TopEnd),
                        onClick = { onExpandClick() }
                    ) {
                        Icon(
                            imageVector = expandIcon,
                            contentDescription = null,
                            tint = expandIconColor
                        )
                    }
                }
                AnimatedVisibility(
                    visible = expanded.value,
                    enter = enterAnimation ,
                    exit = exitAnimation
                ) {
                    ExpandedChatTopBar(user = user)
                }
            }
        }
    }
}

@Composable
private fun ExpandedChatTopBar(user: MikotUser){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(MEDIUM_PADDING),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user.profileImage != null){
            AsyncImage(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(CHAT_TOP_BAR_IMAGE_CORNER_RADIUS))
                    .border(
                        1.dp,
                        color = Green_Blue,
                        shape = RoundedCornerShape(CHAT_TOP_BAR_IMAGE_CORNER_RADIUS)
                    ),
                model = user.profileImage,
                placeholder = painterResource(id = R.drawable.img_user),
                error = painterResource(id = R.drawable.img_user),
                fallback = painterResource(id = R.drawable.img_user),
                contentDescription = null
            )
        }else{
            Image(
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(CHAT_TOP_BAR_IMAGE_CORNER_RADIUS))
                    .border(
                        1.dp,
                        color = Green_Blue,
                        shape = RoundedCornerShape(CHAT_TOP_BAR_IMAGE_CORNER_RADIUS)
                    ),
                painter = painterResource(id = R.drawable.img_user),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(MEDIUM_PADDING))

        TopBarTextField(label = stringResource(id = R.string.full_name), value = user.fullName ?: "Unknown")
        TopBarTextField(label = stringResource(id = R.string.phoneNumber), value = user.phoneNumber ?: "Unknown")
        TopBarTextField(label = stringResource(id = R.string.email), value = user.email ?: "Unknown")
    }
}

@Composable
private fun TopBarTextField(
    label: String,
    value: String
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = EXTRA_SMALL_PADDING),
        value = value,
        readOnly = true,
        onValueChange = {  },
        label = { Text(text = label) },
        singleLine = true,
        shape = RoundedCornerShape(size = REGISTER_TEXT_FIELD_CORNER_RADIUS),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            backgroundColor = Color.White,
            focusedBorderColor = Green_Blue,
            unfocusedBorderColor = Green_Blue,
            focusedLabelColor = Green_Blue,
            unfocusedLabelColor = Green_Blue
        )
    )
}