package hamid.msv.mikot.presentation.screen.profile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
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

    val currentUser by viewModel.currentUser.collectAsState()
    val connectionState = viewModel.connectionState.collectAsState()

    val context = LocalContext.current

    val saveClicked = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Color.White ,
        topBar = {
            ProfileTopBar(
                connectionState = connectionState,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { saveClicked.value = true }
            )
        },
        content = {
            ProfileScreenContent(
                viewModel = viewModel,
                user = currentUser,
                context = context,
                saveClicked = saveClicked,
                isOnline = connectionState.value
            )
        }
    )

    BackHandler { navController.popBackStack() }

}

@Composable
private fun ProfileScreenContent(
    viewModel: ProfileViewModel,
    user: MikotUser,
    context: Context,
    saveClicked: MutableState<Boolean>,
    isOnline: Boolean
) {

    val fullName = remember{ mutableStateOf(user.fullName!!) }
    val username = remember{ mutableStateOf(user.userName!!) }
    val phoneNumber = remember{ mutableStateOf(user.phoneNumber!!) }

    val launchImagePicker = remember { mutableStateOf(false) }

    // OnSaveClick Logic:
    if (saveClicked.value){
        if (isOnline){
            viewModel.updateCurrentUser(
                fullName = fullName.value,
                username = username.value,
                phone = phoneNumber.value,
                context = context
            )
        }else{
            Toast.makeText(context, context.getString(R.string.connection_failed_try_again), Toast.LENGTH_SHORT).show()
        }

        saveClicked.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = MEDIUM_PADDING)
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user.profileImage != null){
            AsyncImage(
                modifier = Modifier
                    .aspectRatio(1.0f)
                    .clickable { launchImagePicker.value = true }
                    .clip(RoundedCornerShape(CHAT_TOP_BAR_IMAGE_CORNER_RADIUS))
                    .background(Color.White)
                    .border(
                        1.dp,
                        color = Green_Blue,
                        shape = RoundedCornerShape(CHAT_TOP_BAR_IMAGE_CORNER_RADIUS)
                    ),
                model = user.profileImage,
                onLoading = { Log.d("MIKOT_COIL" , "Loading") },
                onError = { Log.d("MIKOT_COIL" , it.result.throwable.message.toString()) },
                placeholder = painterResource(id = R.drawable.img_user),
                fallback = painterResource(id = R.drawable.img_user),
                contentDescription = null,
            )
        }else{
            Image(
                modifier = Modifier
                    .aspectRatio(1.0f)
                    .clickable { launchImagePicker.value = true }
                    .clip(RoundedCornerShape(CHAT_TOP_BAR_IMAGE_CORNER_RADIUS))
                    .border(
                        1.dp,
                        color = Green_Blue,
                        shape = RoundedCornerShape(CHAT_TOP_BAR_IMAGE_CORNER_RADIUS)
                    ),
                painter = painterResource(id = R.drawable.img_user),
                contentDescription = null
            )
        }

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

    ImageSelectorAndCropper(
        viewModel = viewModel,
        isOnline = isOnline,
        launchImagePicker = launchImagePicker
    )

}

@Composable
private fun ProfileTextField(
    label: String,
    text: MutableState<String>,
    hint: Int,
    isPhoneNumber: Boolean = false
) {

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

@Composable
private fun ImageSelectorAndCropper(
    viewModel: ProfileViewModel,
    isOnline: Boolean,
    launchImagePicker: MutableState<Boolean>
){

    val context = LocalContext.current
    var bitmap: Bitmap
    val imageUri = remember { mutableStateOf<Uri?>(null) }

    val showProgressBar by viewModel.showProgressBar.collectAsState()

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()){ cropResult ->
        if (cropResult.isSuccessful){
            imageUri.value = cropResult.uriContent
        }else{
            Log.d("MIKOT_PROFILE" , cropResult.error!!.message.toString())
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
        val cropOptions = CropImageContractOptions(uri, CropImageOptions())
        cropOptions.apply {
            setAspectRatio(1,1)
            setFixAspectRatio(true)
            setGuidelines(CropImageView.Guidelines.ON_TOUCH)
            setImageSource(includeGallery = true,includeCamera = false)
            setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
        }
        imageCropLauncher.launch(cropOptions)
    }

    imageUri.value?.let { uri ->
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
            @Suppress("DEPRECATION")
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }else{
            val source = ImageDecoder.createSource(context.contentResolver,uri)
            bitmap = ImageDecoder.decodeBitmap(source)
        }

        if (isOnline){
            viewModel.updateProfileImage(bitmap,context)
        }else{
            Toast.makeText(context, context.getString(R.string.connection_failed_try_again), Toast.LENGTH_SHORT).show()
        }

        imageUri.value = null
    }

    if (launchImagePicker.value) {
        imagePickerLauncher.launch("image/*")
        launchImagePicker.value = false
    }

    AnimatedVisibility(visible = showProgressBar, enter = fadeIn(), exit = fadeOut()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Box(modifier = Modifier.wrapContentSize()){
                CircularProgressIndicator(color = Green_Blue)
            }
        }
    }

}