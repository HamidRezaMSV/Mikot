package hamid.msv.mikot.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val LightGray = Color(0xFFD8D8D8)
val DarkGray = Color(0xFF2A2A2A)
val Dark_Gray_2 = Color(0xFF4B4A4A)


val Green = Color(0xFF4CAF50)
val Blue = Color(0xFF2196F3)
val Red= Color(0xFFF75A28)
val Green_Blue = Color(0xFF009688)
val Dark_Green_Blue = Color(0xFF015F56)

val Colors.splashAppNameColor
    @Composable get() = if (isLight) Purple500 else Purple500

val Colors.welcomeScreenBackgroundColor
    @Composable get() = if (isLight) Color.White else Color.White

val Colors.activeWelcomeIndicator1Color
    @Composable get() = if (isLight) Green else Green

val Colors.activeWelcomeIndicator2Color
    @Composable get() = if (isLight) Blue else Blue

val Colors.activeWelcomeIndicator3Color
    @Composable get() = if (isLight) Red else Red

val Colors.inActiveWelcomeIndicatorColor
    @Composable get() = if (isLight) LightGray else LightGray

val Colors.welcomeDescriptionColor
    @Composable get() = if (isLight) DarkGray.copy(alpha = 0.5f) else DarkGray.copy(alpha = 0.5f)

val Colors.finishButtonBackgroundColor
    @Composable get() = if (isLight) Red else Red

val Colors.registerScreenContentColor
    @Composable get() = if (isLight) Green else Green

val Colors.registerButtonBackgroundColor
    @Composable get() = if (isLight) Green else Green

val Colors.loginScreenContentColor
    @Composable get() = if (isLight) Red else Red

val Colors.chatItemBackgroundColor
    @Composable get() = if (isLight) Green_Blue else Green_Blue

val Colors.chatItemBorderColor
    @Composable get() = if (isLight) Dark_Green_Blue else Dark_Green_Blue


