package hamid.msv.mikot.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hamid.msv.mikot.ui.theme.*

@Composable
fun MessageItem(isMe: Boolean,text:String,time:String) {
    SelectionContainer {
        if (isMe) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = SMALL_PADDING)
                    .padding(top = EXTRA_SMALL_PADDING),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = EXTRA_SMALL_PADDING)
                        .padding(horizontal = EXTRA_SMALL_PADDING),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = RoundedCornerShape(
                            topStart = MESSAGE_ITEM_CORNER_RADIUS ,
                            bottomStart = MESSAGE_ITEM_CORNER_RADIUS ,
                            bottomEnd = MESSAGE_ITEM_CORNER_RADIUS
                        ),
                        backgroundColor = LightGray,
                        border = BorderStroke(
                            width = 2.dp,
                            color = Dark_Gray_2
                        )
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(SMALL_PADDING)
                                .padding(horizontal = EXTRA_SMALL_PADDING),
                            text = text,
                            color = Color.Black,
                            fontWeight = FontWeight.Normal,
                            fontSize = MaterialTheme.typography.body1.fontSize
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(end = MEDIUM_PADDING) ,
                    text = time ,
                    color = Dark_Gray_2 ,
                    fontSize = MaterialTheme.typography.caption.fontSize
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = SMALL_PADDING)
                    .padding(top = EXTRA_SMALL_PADDING),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = EXTRA_SMALL_PADDING)
                        .padding(horizontal = EXTRA_SMALL_PADDING),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = RoundedCornerShape(
                            topEnd = MESSAGE_ITEM_CORNER_RADIUS ,
                            bottomStart = MESSAGE_ITEM_CORNER_RADIUS ,
                            bottomEnd = MESSAGE_ITEM_CORNER_RADIUS
                        ),
                        backgroundColor = MaterialTheme.colors.chatItemBackgroundColor,
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colors.chatItemBorderColor
                        )
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = SMALL_PADDING , horizontal = MEDIUM_PADDING),
                            text = text,
                            color = Color.White,
                            fontWeight = FontWeight.Normal,
                            fontSize = MaterialTheme.typography.body1.fontSize
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(start = MEDIUM_PADDING) ,
                    text = time ,
                    color = Dark_Gray_2 ,
                    fontSize = MaterialTheme.typography.caption.fontSize
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MessageItem1() {
    MessageItem(
        isMe = true,
        text = "Salam",
        time = "02:20"
    )
}

@Composable
@Preview(showBackground = true)
fun MessageItem2() {
    MessageItem(
        isMe = false,
        text = "haj Hamid",
        time = "02:20"
    )
}