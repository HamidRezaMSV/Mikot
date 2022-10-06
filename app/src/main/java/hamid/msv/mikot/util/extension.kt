package hamid.msv.mikot.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

fun Context.copyTextToClipBoard(text: String) {
    val clipBoardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("MESSAGE", text)
    clipBoardManager.setPrimaryClip(clipData)
}

fun Context.vibratePhone() {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
        (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
    else
        @Suppress("DEPRECATION") getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    val vibrationEffect = VibrationEffect.createOneShot(VIBRATION_DURATION, VibrationEffect.DEFAULT_AMPLITUDE)
    vibrator.vibrate(vibrationEffect)
}