package hamid.msv.mikot.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import hamid.msv.mikot.ui.theme.*

@Composable
fun MessageItem(
    isMe: Boolean,
    text: String,
    time: String,
    hasReply: Boolean = false,
    repliedMessageText: String? = null,
    repliedMessageId: String? = null,
    onMessageLongClick: (text: String) -> Unit,
    onRepliedMessageClick: (repliedMessageId:String)-> Unit
) {
    if (isMe) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SMALL_PADDING)
                .padding(top = EXTRA_SMALL_PADDING),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(horizontal = EXTRA_SMALL_PADDING),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = RoundedCornerShape(
                        topStart = MESSAGE_ITEM_CORNER_RADIUS,
                        bottomStart = MESSAGE_ITEM_CORNER_RADIUS,
                        bottomEnd = MESSAGE_ITEM_CORNER_RADIUS
                    ),
                    backgroundColor = LightGray,
                    border = BorderStroke(
                        width = 2.dp,
                        color = Dark_Gray_2
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(SMALL_PADDING)
                            .padding(horizontal = EXTRA_SMALL_PADDING),
                        horizontalAlignment = Alignment.End
                    ) {
                        AnimatedVisibility(visible = hasReply) {
                            Row(
                                modifier = Modifier.clickable { onRepliedMessageClick(repliedMessageId!!) },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ){
                                Box(
                                    modifier = Modifier
                                        .size(width = 3.dp, height = 35.dp)
                                        .padding(vertical = EXTRA_SMALL_PADDING)
                                        .background(Green_Blue)
                                ){}
                                Text(
                                    modifier = Modifier.padding(start = EXTRA_SMALL_PADDING),
                                    text = repliedMessageText!!,
                                    color = Color.Black.copy(alpha = 0.4f),
                                    maxLines = 1 ,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Text(
                            modifier = Modifier
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = { onMessageLongClick(text) })
                                },
                            text = text,
                            color = Color.Black,
                            fontWeight = FontWeight.Normal,
                            fontSize = MaterialTheme.typography.body1.fontSize
                        )
                    }
                }
            }

            Text(
                modifier = Modifier.padding(end = MEDIUM_PADDING),
                text = time,
                color = Dark_Gray_2,
                fontSize = MaterialTheme.typography.caption.fontSize
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SMALL_PADDING)
                .padding(top = EXTRA_SMALL_PADDING),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(horizontal = EXTRA_SMALL_PADDING),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    shape = RoundedCornerShape(
                        topEnd = MESSAGE_ITEM_CORNER_RADIUS,
                        bottomStart = MESSAGE_ITEM_CORNER_RADIUS,
                        bottomEnd = MESSAGE_ITEM_CORNER_RADIUS
                    ),
                    backgroundColor = MaterialTheme.colors.chatItemBackgroundColor,
                    border = BorderStroke(
                        width = 2.dp,
                        color = MaterialTheme.colors.chatItemBorderColor
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(SMALL_PADDING)
                            .padding(horizontal = EXTRA_SMALL_PADDING),
                        horizontalAlignment = Alignment.Start
                    ) {
                        AnimatedVisibility(visible = hasReply) {
                            Row(
                                modifier = Modifier.clickable { onRepliedMessageClick(repliedMessageId!!) },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ){
                                Box(
                                    modifier = Modifier
                                        .size(width = 3.dp, height = 35.dp)
                                        .padding(vertical = EXTRA_SMALL_PADDING)
                                        .background(Green)
                                ){}
                                Text(
                                    modifier = Modifier.padding(start = EXTRA_SMALL_PADDING),
                                    text = repliedMessageText!!,
                                    color = Color.White.copy(alpha = 0.6f),
                                    maxLines = 1 ,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Text(
                            modifier = Modifier.pointerInput(Unit) {
                                detectTapGestures(onLongPress = { onMessageLongClick(text) })
                            },
                            text = text,
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            fontSize = MaterialTheme.typography.body1.fontSize
                        )
                    }
                }
            }

            Text(
                modifier = Modifier.padding(start = MEDIUM_PADDING),
                text = time,
                color = Dark_Gray_2,
                fontSize = MaterialTheme.typography.caption.fontSize
            )
        }
    }

}