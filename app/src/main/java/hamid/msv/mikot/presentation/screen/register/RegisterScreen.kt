package hamid.msv.mikot.presentation.screen.register

import android.util.Log
import android.util.Patterns
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import hamid.msv.mikot.R
import hamid.msv.mikot.ui.theme.*
import hamid.msv.mikot.util.PHONE_NUMBER_CHARACTER_COUNT

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel()
) {

//    LaunchedEffect(key1 = true) {
//        Log.d("Hamid_Hamid", "1")
//
//        viewModel.signUpUser(
//            email = "h2.m799@gmail.com",
//            password = "123456789"
//        )
//
//        delay(3000)
//
//        getRegisterResponse(viewModel)
//
//    }

//    click : @Composable () -> Unit
//    RegisterContent {
//        LaunchedEffect(key1 = , block = )
//    }


    RegisterContent(){}
}

@Composable
fun RegisterContent(onRegisterClicked : () -> Unit) {

    val fullName = remember { mutableStateOf("") }
    val userName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = true){
        scrollState.animateScrollTo(
            scrollState.maxValue ,
            tween(durationMillis = 1500 , delayMillis = 1000)
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
            modifier = Modifier.fillMaxWidth() ,
            text = stringResource(id = R.string.register) ,
            textAlign = TextAlign.Center ,
            color = MaterialTheme.colors.registerScreenContentColor ,
            fontWeight = FontWeight.Bold ,
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
            hint = R.string.hint_password ,
            isPassword = true
        )
        RegisterTextField(
            value = confirmPassword,
            label = R.string.confirm_password,
            hint = R.string.hint_confirm_password ,
            isPassword = true
        )
        RegisterTextField(
            value = phoneNumber,
            label = R.string.phoneNumber,
            hint = R.string.hint_phoneNumber ,
            isPhoneNumber = true
        )

        Spacer(modifier = Modifier.height(EXTRA_LARGE_PADDING))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(REGISTER_BUTTON_HEIGHT) ,
            shape = RoundedCornerShape(size = REGISTER_BUTTON_CORNER_RADIUS) ,
            elevation = REGISTER_BUTTON_ELEVATION
        ) {
            Button(
                onClick = { onRegisterClicked() },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.registerButtonBackgroundColor,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(id = R.string.register) ,
                    fontSize = MaterialTheme.typography.h6.fontSize
                )
            }
        }

        Spacer(modifier = Modifier.height(LARGE_PADDING))
    }
}

@Composable
fun RegisterTextField(
    value:MutableState<String>,
    label:Int,
    hint : Int,
    isEmail : Boolean = false,
    isPassword : Boolean = false,
    isPhoneNumber : Boolean = false
) {

    var error by remember { mutableStateOf(false) }
    error = when{
        isEmail -> { Patterns.EMAIL_ADDRESS.matcher(value.value).matches() }
        isPassword -> { value.value.length < 6 }
        isPhoneNumber -> { value.value.length != PHONE_NUMBER_CHARACTER_COUNT }
        else -> { false }
    }

    val keyboardOption = when{
        isEmail ->  KeyboardOptions(keyboardType = KeyboardType.Email)
        isPhoneNumber ->  KeyboardOptions(keyboardType = KeyboardType.Phone)
        else ->  KeyboardOptions.Default
    }

    val visualTransformation = when{
        isPassword ->  PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    val placeHolderColor = if (error) Red.copy(ContentAlpha.medium)
    else MaterialTheme.colors.registerScreenContentColor.copy(ContentAlpha.medium)

    val errorColor = if (error) Red.copy(ContentAlpha.medium)
    else MaterialTheme.colors.registerScreenContentColor.copy(alpha = 0.7f)

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = EXTRA_MEDIUM_PADDING),
        value = value.value,
        onValueChange = { value.value = it } ,
        label = { Text(text = stringResource(id = label))} ,
        placeholder = { Text(text = stringResource(id = hint)) } ,
        singleLine = true ,
        isError = error,
        keyboardOptions = keyboardOption ,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(size = REGISTER_TEXT_FIELD_CORNER_RADIUS) ,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black ,
            backgroundColor = Color.White ,
            cursorColor = MaterialTheme.colors.registerScreenContentColor ,
            focusedBorderColor = MaterialTheme.colors.registerScreenContentColor ,
            errorBorderColor = Red,
            unfocusedBorderColor = MaterialTheme.colors.registerScreenContentColor.copy(alpha = 0.7f),
            errorLabelColor = Red,
            placeholderColor = placeHolderColor,
            focusedLabelColor = MaterialTheme.colors.registerScreenContentColor ,
            disabledPlaceholderColor = errorColor ,
            unfocusedLabelColor = errorColor
        )
    )
}

private fun getRegisterResponse(viewModel: RegisterViewModel) {
    Log.d("Hamid_Hamid", "2")

    var response : Task<AuthResult>? = null

    viewModel.signUpResponse.observeForever {
        response = it
    }

    Log.d("Hamid_Hamid", "3")
    if (response?.isSuccessful == true) {
        Log.d("Hamid_Hamid", "isSuccessful")
    } else {
        Log.d("Hamid_Hamid", "is not Successful")
        Log.d("Hamid_Hamid", response?.exception?.message.toString())
    }
    Log.d("Hamid_Hamid", "4")

    viewModel.signUpResponse.removeObserver {}

    Log.d("Hamid_Hamid", "5")
}