package hamid.msv.mikot.presentation.screen.login

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import hamid.msv.mikot.R
import hamid.msv.mikot.navigation.Screen
import hamid.msv.mikot.ui.theme.*
import hamid.msv.mikot.util.REGISTER_PLACEHOLDER_ALPHA

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val navigateToHomeScreen = viewModel.navigateToHomeScreen.collectAsState(initial = false)
    val progressVisible = remember { mutableStateOf(false) }

    LoginContent(
        progressVisible,
        onLoginClicked = { email,password ->
            if (viewModel.isInputDataValid(email, password, context)) {
                progressVisible.value = true
                viewModel.signInUser(email, password)
            }
        }
    ) {
        navController.popBackStack()
        navController.navigate(Screen.SignUp.route)
    }

    if (navigateToHomeScreen.value) {
        Toast.makeText(context, context.getString(R.string.welcome), Toast.LENGTH_SHORT).show()
        navController.popBackStack()
        navController.navigate(Screen.Home.route)
        viewModel.finishSignIn()
    }

}

@Composable
fun LoginContent(
    progressVisible: MutableState<Boolean>,
    onLoginClicked: (email: String, password: String) -> Unit,
    onCreateNewAccClicked: () -> Unit
) {

    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(all = LARGE_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            painter = painterResource(id = R.drawable.ic_login),
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(top = LARGE_PADDING),
            text = stringResource(id = R.string.login),
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.h4.fontSize,
            color = MaterialTheme.colors.loginScreenContentColor
        )

        Spacer(modifier = Modifier.height(EXTRA_MEDIUM_PADDING))

        LoginTextField(
            value = email,
            label = R.string.email,
            hint = R.string.login_email_hint,
            isEmail = true
        )
        LoginTextField(
            value = password,
            label = R.string.password,
            hint = R.string.login_pass_hint,
            isPassword = true
        )

        Spacer(modifier = Modifier.weight(1f))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(REGISTER_BUTTON_HEIGHT),
            shape = RoundedCornerShape(size = REGISTER_BUTTON_CORNER_RADIUS),
            elevation = REGISTER_BUTTON_ELEVATION
        ) {
            Button(
                onClick = {
                    onLoginClicked(
                        email.value.trim(),
                        password.value.trim()
                    )
                },
                enabled = !progressVisible.value,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.loginScreenContentColor,
                    contentColor = Color.White,
                    disabledContentColor = LightGray,
                    disabledBackgroundColor = Dark_Red
                )
            ) {
                Text(
                    text = stringResource(id = R.string.login),
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

        TextButton(
            onClick = { onCreateNewAccClicked() }
        ) {
            Text(
                text = stringResource(id = R.string.login_have_not_account),
                fontSize = MaterialTheme.typography.subtitle1.fontSize,
                color = MaterialTheme.colors.loginScreenContentColor
            )
        }
    }
}

@Composable
fun LoginTextField(
    value: MutableState<String>,
    label: Int,
    hint: Int,
    isEmail: Boolean = false,
    isPassword: Boolean = false
) {
    var error by remember { mutableStateOf(false) }
    error = when {
        isPassword -> { value.value.length < 6 }
        else -> { false }
    }

    val keyboardOption = when {
        isEmail -> KeyboardOptions(keyboardType = KeyboardType.Email)
        else -> KeyboardOptions.Default
    }

    val visualTransformation = when {
        isPassword -> PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    val placeHolderColor = if (error) Red.copy(REGISTER_PLACEHOLDER_ALPHA)
    else MaterialTheme.colors.loginScreenContentColor.copy(REGISTER_PLACEHOLDER_ALPHA)

    val errorColor = if (error) Red.copy(ContentAlpha.medium)
    else MaterialTheme.colors.loginScreenContentColor.copy(alpha = 0.7f)

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
            cursorColor = MaterialTheme.colors.loginScreenContentColor,
            focusedBorderColor = MaterialTheme.colors.loginScreenContentColor,
            errorBorderColor = Red,
            unfocusedBorderColor = MaterialTheme.colors.loginScreenContentColor.copy(alpha = 0.7f),
            errorLabelColor = Red,
            placeholderColor = placeHolderColor,
            focusedLabelColor = MaterialTheme.colors.loginScreenContentColor,
            disabledPlaceholderColor = errorColor,
            unfocusedLabelColor = errorColor
        )
    )
}