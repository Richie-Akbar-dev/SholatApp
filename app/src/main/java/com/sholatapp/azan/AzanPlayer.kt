package com.sholatapp.azan
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.sholatapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Handles playing bundled and custom azan audio.
 * Supports 3 built-in azan sounds: Makkah, Madinah, Mishary Alafasy.
 */
class AzanPlayer(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "azan_prefs"
        private const val KEY_SELECTED_AZAN = "selected_azan"
        private const val KEY_CUSTOM_AZAN = "custom_azan_path"
        private const val KEY_AZAN_READY = "azan_ready"

        /**
         * Azan options available in the app.
         * resourceId: 0 means it's a custom azan (path stored in prefs).
         */
        data class AzanOption(
            val id: String,
            val name: String,
            val description: String,
            val resourceId: Int
        )

        val ALL_AZAN_OPTIONS = listOf(
            AzanOption(
                id = "makkah",
                name = "Makkah",
                description = "Adzan Masjidil Haram, Mekkah",
                resourceId = R.raw.azan_makkah
            ),
            AzanOption(
                id = "madinah",
                name = "Madinah",
                description = "Adzan Masjid Nabawi, Madinah",
                resourceId = R.raw.azan_madinah
            ),
            AzanOption(
                id = "mishary",
                name = "Mishary Alafasy",
                description = "Adzan oleh Mishary Rashid Alafasy",
                resourceId = R.raw.azan_mishary
            )
        )

        /**
         * Get the default azan option.
         */
        val DEFAULT_AZAN = ALL_AZAN_OPTIONS[0] // Makkah
    }

    private var mediaPlayer: MediaPlayer? = null

    /**
     * Get the currently selected azan option ID.
     */
    fun getSelectedAzanId(): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_SELECTED_AZAN, DEFAULT_AZAN.id) ?: DEFAULT_AZAN.id
    }

    /**
     * Get the AzanOption object for the currently selected azan.
     */
    fun getSelectedAzanOption(): AzanOption {
        val id = getSelectedAzanId()
        return ALL_AZAN_OPTIONS.find { it.id == id } ?: DEFAULT_AZAN
    }

    /**
     * Set the selected azan by ID and save to preferences.
     */
    fun setSelectedAzan(azanId: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_SELECTED_AZAN, azanId)
            .putBoolean(KEY_AZAN_READY, true)
            .apply()
    }

    /**
     * Check if an azan has been selected (for first-run flow).
     */
    fun hasSelectedAzan(): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.contains(KEY_SELECTED_AZAN) || prefs.getBoolean(KEY_AZAN_READY, false)
    }

    /**
     * Check if azan audio is available.
     */
    fun isAzanAvailable(): Boolean {
        if (getCustomAzanFile()?.exists() == true) return true
        return getSelectedAzanOption().resourceId > 0
    }

    /**
     * Play the azan audio. Uses selected bundled azan, or custom if set.
     */
    fun playAzan() {
        try {
            // Try custom azan first
            val customFile = getCustomAzanFile()
            if (customFile != null && customFile.exists()) {
                playFile(customFile)
                return
            }

            // Play selected bundled azan
            val option = getSelectedAzanOption()
            if (option.resourceId > 0) {
                playResource(option.resourceId)
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Play a specific azan option (for preview in AzanPickerScreen).
     */
    fun playPreview(azanId: String) {
        stopAzan()
        val option = ALL_AZAN_OPTIONS.find { it.id == azanId } ?: return
        if (option.resourceId > 0) {
            playResource(option.resourceId)
        }
    }

    /**
     * Stop playing azan.
     */
    fun stopAzan() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Check if azan is currently playing.
     */
    fun isPlaying(): Boolean {
        return try {
            mediaPlayer?.isPlaying == true
        } catch (e: Exception) {
            false
        }
    }

    // --- Custom azan support ---

    fun setCustomAzanPath(path: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_CUSTOM_AZAN, path)
            .putBoolean(KEY_AZAN_READY, true)
            .apply()
    }

    fun getCustomAzanFile(): File? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val path = prefs.getString(KEY_CUSTOM_AZAN, null) ?: return null
        val file = File(path)
        return if (file.exists()) file else null
    }

    fun clearCustomAzan() {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_CUSTOM_AZAN)
            .apply()
    }

    // --- Private helpers ---

    private fun playResource(resourceId: Int) {
        stopAzan()
        try {
            val afd = context.resources.openRawResourceFd(resourceId)
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                prepare()
                setOnCompletionListener {
                    release()
                    mediaPlayer = null
                }
                start()
            }
            afd.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun playFile(file: File) {
        stopAzan()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            setDataSource(file.absolutePath)
            prepare()
            setOnCompletionListener {
                release()
                mediaPlayer = null
            }
            start()
        }
    }
}
