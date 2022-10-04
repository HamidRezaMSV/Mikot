package hamid.msv.mikot.presentation.screen.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import hamid.msv.mikot.R
import hamid.msv.mikot.navigation.Screen
import hamid.msv.mikot.ui.theme.*
import hamid.msv.mikot.util.PHONE_NUMBER_CHARACTER_COUNT
import hamid.msv.mikot.util.REGISTER_PLACEHOLDER_ALPHA

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val registeredUserId = viewModel.userId.collectAsState()
    val navigateToHomeScreen = viewModel.navigateToHomeScreen.collectAsState()

    RegisterContent(
        onRegisterClicked = { fName, uName, pass, cPass, email, phone ->
            if (viewModel.isInputDataValid(fName, uName, pass, cPass, email, phone, context)) {
                viewModel.currentUser.apply {
                    this.fullName = fName
                    this.userName = uName
                    this.password = pass
                    this.email = email
                    this.phoneNumber = phone
                    this.createAccountTime = System.currentTimeMillis().toString()
                }
                viewModel.signUpUser(email, pass)
            }
        },
        onLoginClicked = {
            navController.popBackStack()
            navController.navigate(Screen.SignIn.route)
        }
    )

    // Save user in firebase database
    registeredUserId.value?.let {
        viewModel.currentUser.id = it
        viewModel.saveUserInFirebase(viewModel.currentUser)
        viewModel.finishAuthentication()
    }

    if (navigateToHomeScreen.value) {
        navController.popBackStack()
        navController.navigate(Screen.Home.route)
        viewModel.finishRegistering()
    }

}

@Composable
fun RegisterContent(
    onRegisterClicked: (fName: String, uName: String, pass: String, cPass: String, email: String, phone: String) -> Unit,
    onLoginClicked: () -> Unit
) {

    val fullName = remember { mutableStateOf("") }
    val userName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    val progressVisible = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        scrollState.animateScrollTo(
            scrollState.maxValue,
            tween(durationMillis = 1000, delayMillis = 1100)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = LARGE_PADDING)
            .padding(top = LARGE_PADDING)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            painter = painterResource(id = R.drawable.ic_register),
            contentDescription = null
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.register),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.registerScreenContentColor,
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.h4.fontSize
        )

        Spacer(modifier = Modifier.height(EXTRA_LARGE_PADDING))

        RegisterTextField(
            value = fullName,
            label = R.string.full_name,
            hint = R.string.hint_full_name
        )
        RegisterTextField(
            value = userName,
            label = R.string.user_name,
            hint = R.string.hint_user_name
        )
        RegisterTextField(
            value = email,
            label = R.string.email,
            hint = R.string.hint_email,
            isEmail = true
        )
        RegisterTextField(
            value = password,
            label = R.string.password,
            hint = R.string.hint_password,
            isPassword = true
        )
        RegisterTextField(
            value = confirmPassword,
            label = R.string.confirm_password,
            hint = R.string.hint_confirm_password,
            isPassword = true
        )
        RegisterTextField(
            value = phoneNumber,
            label = R.string.phoneNumber,
            hint = R.string.hint_phoneNumber,
            isPhoneNumber = true
        )

        Spacer(modifier = Modifier.height(EXTRA_LARGE_PADDING))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(REGISTER_BUTTON_HEIGHT),
            shape = RoundedCornerShape(size = REGISTER_BUTTON_CORNER_RADIUS),
            elevation = REGISTER_BUTTON_ELEVATION
        ) {
            Button(
                onClick = {
                    progressVisible.value = true
                    onRegisterClicked(
                        fullName.value.trim(),
                        userName.value.trim(),
                        password.value.trim(),
                        confirmPassword.value.trim(),
                        email.value.trim(),
                        phoneNumber.value.trim()
                    )
                },
                enabled = !progressVisible.value,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.registerButtonBackgroundColor,
                    contentColor = Color.White,
                    disabledBackgroundColor = Dark_Green_Blue,
                    disabledContentColor = LightGray
                )
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    fontSize = MaterialTheme.typography.h6.fontSize
                )

                Spacer(modifier = Modifier.width(EXTRA_MEDIUM_PADDING))

                AnimatedVisibility(
                    visible = progressVisible.value
                ) {
                    CircularProgressIndicator(
                        color = Color.LightGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(MEDIUM_PADDING))

        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(REGISTER_BUTTON_HEIGHT),
            onClick = { onLoginClicked() },
            shape = RoundedCornerShape(size = REGISTER_BUTTON_CORNER_RADIUS),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color.White,
                contentColor = MaterialTheme.colors.registerButtonBackgroundColor
            ),
            border = BorderStroke(
                width = 2.dp,
                color = MaterialTheme.colors.registerButtonBackgroundColor
            )
        ) {
            Text(
                text = stringResource(id = R.string.register_have_account),
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            )
        }

        Spacer(modifier = Modifier.height(EXTRA_MEDIUM_PADDING))
    }
}

@Composable
fun RegisterTextField(
    value: MutableState<String>,
    label: Int,
    hint: Int,
    isEmail: Boolean = false,
    isPassword: Boolean = false,
    isPhoneNumber: Boolean = false
) {

    var error by remember { mutableStateOf(false) }
    error = when {
        isPassword -> {
            value.value.length < 6
        }
        isPhoneNumber -> {
            value.value.length != PHONE_NUMBER_CHARACTER_COUNT
        }
        else -> {
            false
        }
    }

    val keyboardOption = when {
        isEmail -> KeyboardOptions(keyboardType = KeyboardType.Email)
        isPhoneNumber -> KeyboardOptions(keyboardType = KeyboardType.Phone)
        else -> KeyboardOptions.Default
    }

    val visualTransformation = when {
        isPassword -> PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    val placeHolderColor = if (error) Red.copy(REGISTER_PLACEHOLDER_ALPHA)
    else MaterialTheme.colors.registerScreenContentColor.copy(REGISTER_PLACEHOLDER_ALPHA)

    val errorColor = if (error) Red.copy(ContentAlpha.medium)
    else MaterialTheme.colors.registerScreenContentColor.copy(alpha = 0.7f)

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = EXTRA_MEDIUM_PADDING),
        value = value.value,
        onValueChange = { value.value = it },
        label = { Text(text = stringResource(id = label)) },
        placeholder = { Text(text = stringResource(id = hint)) },
        singleLine = true,
        isError = error,
        keyboardOptions = keyboardOption,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(size = REGISTER_TEXT_FIELD_CORNER_RADIUS),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            backgroundColor = Color.White,
            cursorColor = MaterialTheme.colors.registerScreenContentColor,
            focusedBorderColor = MaterialTheme.colors.registerScreenContentColor,
            errorBorderColor = Red,
            unfocusedBorderColor = MaterialTheme.colors.registerScreenContentColor.copy(alpha = 0.7f),
            errorLabelColor = Red,
            placeholderColor = placeHolderColor,
            focusedLabelColor = MaterialTheme.colors.registerScreenContentColor,
            disabledPlaceholderColor = errorColor,
            unfocusedLabelColor = errorColor
        )
    )
}