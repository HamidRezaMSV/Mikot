package hamid.msv.mikot.presentation.screen.profile

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import hamid.msv.mikot.Application
import hamid.msv.mikot.R
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.presentation.component.ProfileTopBar
import hamid.msv.mikot.ui.theme.*
import hamid.msv.mikot.util.PHONE_NUMBER_CHARACTER_COUNT
import hamid.msv.mikot.util.REGISTER_PLACEHOLDER_ALPHA

@Composable
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val currentUser = Application.currentUser!!
    val connectionState = viewModel.connectionState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color.White ,
        topBar = {
            ProfileTopBar(
                connectionState = connectionState,
                onBackClick = { navController.popBackStack() },
                onSaveClick = {  }
            )
        },
        content = { ProfileScreenContent(user = currentUser) }
    )

    BackHandler { navController.popBackStack() }

}

@Composable
private fun ProfileScreenContent(user: MikotUser) {

    val image = if (user.profileImage != null) painterResource(id = R.drawable.img_user)
    else painterResource(id = R.drawable.img_user)

    val fullName = remember{ mutableStateOf(user.fullName!!) }
    val username = remember{ mutableStateOf(user.userName!!) }
    val phoneNumber = remember{ mutableStateOf(user.phoneNumber!!) }
    val bio = remember{ mutableStateOf(user.bio) }
    val profileImage = remember{ mutableStateOf(user.profileImage) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = MEDIUM_PADDING)
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(CHAT_TOP_BAR_IMAGE_CORNER_RADIUS))
                .border(
                    1.dp,
                    color = Green_Blue,
                    shape = RoundedCornerShape(CHAT_TOP_BAR_IMAGE_CORNER_RADIUS)
                ),
            painter = image,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(MEDIUM_PADDING))

        ProfileTextField(
            label = stringResource(id = R.string.full_name),
            text = fullName,
            hint = R.string.hint_full_name
        )
        ProfileTextField(
            label = stringResource(id = R.string.user_name),
            text = username,
            hint = R.string.hint_user_name
        )
        ProfileTextField(
            label = stringResource(id = R.string.phoneNumber),
            text = phoneNumber,
            hint = R.string.hint_phoneNumber,
            isPhoneNumber = true
        )
    }
}

@Composable
private fun ProfileTextField(label: String, text: MutableState<String>, hint: Int, isPhoneNumber: Boolean = false) {

    var error by remember { mutableStateOf(false) }

    error = when {
        isPhoneNumber -> { text.value.length != PHONE_NUMBER_CHARACTER_COUNT }
        else -> { false }
    }

    val keyboardOption = when {
        isPhoneNumber -> KeyboardOptions(keyboardType = KeyboardType.Phone)
        else -> KeyboardOptions.Default
    }

    val placeHolderColor = if (error) Red.copy(REGISTER_PLACEHOLDER_ALPHA)
    else Green_Blue.copy(REGISTER_PLACEHOLDER_ALPHA)

    val errorColor = if (error) Red.copy(ContentAlpha.medium)
    else Green_Blue.copy(alpha = 0.7f)

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = EXTRA_SMALL_PADDING),
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text(text = label) },
        placeholder = { Text(text = stringResource(id = hint)) },
        singleLine = true,
        isError = error,
        keyboardOptions = keyboardOption,
        shape = RoundedCornerShape(size = REGISTER_TEXT_FIELD_CORNER_RADIUS),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            backgroundColor = Color.White,
            focusedBorderColor = Green_Blue,
            unfocusedBorderColor = Green_Blue,
            focusedLabelColor = Green_Blue,
            cursorColor = Green_Blue,
            errorBorderColor = Red,
            errorLabelColor = Red,
            placeholderColor = placeHolderColor,
            disabledPlaceholderColor = errorColor,
            unfocusedLabelColor = errorColor
        )
    )
}