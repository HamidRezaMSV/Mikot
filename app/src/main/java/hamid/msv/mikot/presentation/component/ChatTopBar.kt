package hamid.msv.mikot.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import hamid.msv.mikot.ui.theme.*

@Composable
fun ChatTopBar(name: String) {
    Box(
        modifier = Modifier
            .padding(horizontal = SMALL_PADDING)
            .padding(top = SMALL_PADDING)
            .padding(bottom = EXTRA_SMALL_PADDING)
    ) {
        Card(
            modifier = Modifier.requiredHeight(CHAT_TOP_BAR_HEIGHT),
            shape = RoundedCornerShape(size = CHAT_TOP_BAR_CORNER_RADIUS),
            elevation = CHAT_TOP_BAR_ELEVATION,
            backgroundColor = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = SMALL_PADDING),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = name,
                    color = MaterialTheme.colors.chatTopBarTextColor,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatTopBarPreview() {
    ChatTopBar(name = "Hamid Reza")
}