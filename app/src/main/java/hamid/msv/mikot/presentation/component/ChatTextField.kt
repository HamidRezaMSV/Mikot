package hamid.msv.mikot.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import hamid.msv.mikot.R
import hamid.msv.mikot.ui.theme.*
import hamid.msv.mikot.util.CHAT_TEXT_FILED_MAX_LINE

@Composable
fun ChatTextField(
    repliedMessageText: String? = null,
    hasReply: MutableState<Boolean>,
    onSendClicked : (String) -> Unit
) {

    var text by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(EXTRA_SMALL_PADDING)
            .padding(bottom = EXTRA_SMALL_PADDING) ,
        backgroundColor = Color.White ,
        shape = RoundedCornerShape(size = CHAT_TEXT_FIELD_CORNER_RADIUS),
        elevation = CHAT_TEXT_FIELD_ELEVATION
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SMALL_PADDING)
        ) {
            AnimatedVisibility(
                visible = hasReply.value
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = EXTRA_SMALL_PADDING)
                            .padding(bottom = EXTRA_SMALL_PADDING),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.reply_to),
                            color = Green_Blue
                        )
                        Text(
                            modifier = Modifier.weight(1f),
                            text = repliedMessageText!!,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        IconButton(onClick = { hasReply.value = false }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                tint = Green_Blue
                            )
                        }
                    }

                    Divider(
                        modifier = Modifier.fillMaxWidth(0.7f),
                        thickness = 2.dp,
                        color = Color.Black.copy(alpha = 0.3f)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth() ,
                verticalAlignment = Alignment.Bottom ,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .weight(1f),
                    value = text,
                    onValueChange = { text = it } ,
                    placeholder = { Text(text = stringResource(id = R.string.chat_text_field_hint)) },
                    maxLines = CHAT_TEXT_FILED_MAX_LINE ,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black ,
                        backgroundColor = Color.White ,
                        cursorColor = Color.Black ,
                        focusedIndicatorColor = Color.White,
                        placeholderColor = Color.Black.copy(ContentAlpha.medium)
                    )
                )

                Card(
                    modifier = Modifier
                        .padding(bottom = EXTRA_SMALL_PADDING)
                        .padding(end = EXTRA_SMALL_PADDING),
                    shape = CircleShape ,
                    backgroundColor = LightGray ,
                    border = BorderStroke(width = 1.dp , color = Dark_Gray_2) ,
                    contentColor = Color.Black
                ) {
                    IconButton(
                        modifier = Modifier.padding(start = 2.dp),
                        onClick = {
                            onSendClicked(text)
                            hasReply.value = false
                            text = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Send,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}