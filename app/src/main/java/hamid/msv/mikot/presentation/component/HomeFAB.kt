package hamid.msv.mikot.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import hamid.msv.mikot.ui.theme.*

@Composable
fun HomeFAB(onClick: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier.size(HOME_FAB_SIZE),
        onClick = { onClick() },
        shape = RoundedCornerShape(size = HOME_FAB_CORNER_RADIUS),
        backgroundColor = MaterialTheme.colors.homeFABBackgroundColor,
        contentColor = MaterialTheme.colors.homeFABContentColor,
        content = {
            Icon(
                modifier = Modifier.fillMaxSize(0.65f),
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        }
    )
}

@Preview
@Composable
fun HomeFABPreview() {
    HomeFAB {}
}