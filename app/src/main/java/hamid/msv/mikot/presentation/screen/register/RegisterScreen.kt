package hamid.msv.mikot.presentation.screen.register

import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import hamid.msv.mikot.R
import hamid.msv.mikot.domain.model.MikotUser
import hamid.msv.mikot.ui.theme.*
import hamid.msv.mikot.util.PHONE_NUMBER_CHARACTER_COUNT
import kotlinx.coroutines.delay

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    var executeRegisteringNow by remember { mutableStateOf(false) }

    RegisterContent(
        onRegisterClicked = {
            if (isInputDataValid(fullNameValue,userNameValue,passwordValue,confirmPasswordValue,emailValue,phoneNumberValue,context)){
                executeRegisteringNow = true
            }
        }
    )

    LaunchedEffect(key1 = executeRegisteringNow){
        if (executeRegisteringNow){
            viewModel.signUpUser(emailValue,passwordValue)
            delay(3000)
            executeRegisteringNow = false
        }
    }

    viewModel.signUpResponse.observeAsState().value?.let {
        if (it.isSuccessful){
            Log.d("Hamid_Hamid", "isSuccessful")
            val user = MikotUser(
                id = it.result.user!!.uid ,
                fullName = fullNameValue ,
                userName = userNameValue ,
                password = passwordValue ,
                email = emailValue ,
                createAccountTime = System.currentTimeMillis().toString(),
                phoneNumber = phoneNumberValue
            )
            viewModel.saveUserInFirebase(user)
        }else{
            Log.d("Hamid_Hamid", it.exception?.message.toString())
            Toast.makeText(context,context.getString(R.string.connection_failed),Toast.LENGTH_SHORT).show()
        }
    }

    viewModel.saveUserInFirebaseResponse.observeAsState().value?.let {
        if (it.isSuccessful){
            Log.d("Hamid_Hamid", "database isSuccessful")
        }else{
            Log.d("Hamid_Hamid", "database" + it.exception?.message.toString())
        }
    }

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
            tween(durationMillis = 1000 , delayMillis = 1000)
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
                onClick = {
                    fullNameValue = fullName.value.trim()
                    userNameValue = userName.value.trim()
                    passwordValue = password.value.trim()
                    confirmPasswordValue = confirmPassword.value.trim()
                    emailValue = email.value.trim()
                    phoneNumberValue = phoneNumber.value.trim()
                    onRegisterClicked()
                    },
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

private fun isInputDataValid(
    fullName:String,
    userName:String,
    password:String,
    confirmPassword:String,
    email:String,
    phoneNumber:String,
    context: Context
) : Boolean{
    val emailValidation = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val passwordValidation = password.length >= 6
    val confirmPasswordValidation = password.length >= 6 && confirmPassword == password
    val fullNameValidation = fullName.isNotEmpty()
    val userNameValidation = userName.isNotEmpty()
    val phoneNumberValidation = phoneNumber.isNotEmpty()
    when{
        !emailValidation -> Toast.makeText(context,context.getString(R.string.invalid_email_message), Toast.LENGTH_SHORT).show()
        !passwordValidation -> Toast.makeText(context,context.getString(R.string.invalid_password_message), Toast.LENGTH_SHORT).show()
        !fullNameValidation -> Toast.makeText(context,context.getString(R.string.invalid_name_message), Toast.LENGTH_SHORT).show()
        !userNameValidation -> Toast.makeText(context,context.getString(R.string.invalid_username_message), Toast.LENGTH_SHORT).show()
        !phoneNumberValidation -> Toast.makeText(context,context.getString(R.string.invalid_phone_message), Toast.LENGTH_SHORT).show()
        !confirmPasswordValidation -> Toast.makeText(context,context.getString(R.string.invalid_confirm_password_message), Toast.LENGTH_SHORT).show()
    }
    return emailValidation && passwordValidation && fullNameValidation && userNameValidation && phoneNumberValidation && confirmPasswordValidation
}

var fullNameValue = ""
var userNameValue = ""
var passwordValue = ""
var confirmPasswordValue = ""
var emailValue = ""
var phoneNumberValue = ""