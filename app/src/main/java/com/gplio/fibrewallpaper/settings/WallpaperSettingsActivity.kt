@file:Suppress(
    "OVERRIDE_DEPRECATION", "DEPRECATION", "ExportedPreferenceActivity"
) // TODO Look for alternative that is displayed in the wallpaper settings, a regular activity + androidx.preferences appears to not work.

package com.gplio.fibrewallpaper.settings

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import android.widget.Toast
import com.gplio.fibrewallpaper.R
import com.gplio.fibrewallpaper.lib.graphics.ShaderType
import com.gplio.fibrewallpaper.values.PREFERENCE_FRAGMENT_SHADER
import com.gplio.fibrewallpaper.values.PREFERENCE_VERTEX_SHADER

class WallpaperSettingsActivity : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

        val vertexShaderPreference = preferenceScreen.findPreference(PREFERENCE_VERTEX_SHADER)
        val fragmentShaderPreference = preferenceScreen.findPreference(PREFERENCE_FRAGMENT_SHADER)

        vertexShaderPreference.onPreferenceChangeListener = createPreferenceChangeValidator(
            ShaderType.Vertex
        )
        fragmentShaderPreference.onPreferenceChangeListener = createPreferenceChangeValidator(
            ShaderType.Fragment
        )
    }

    @Suppress("UNUSED_ANONYMOUS_PARAMETER") // TODO implement validation
    private fun createPreferenceChangeValidator(shaderType: ShaderType) =
        Preference.OnPreferenceChangeListener { preference, newValue ->

            val errorString = when (shaderType) {
                ShaderType.Vertex -> R.string.validation_invalid_fragment_shader
                ShaderType.Fragment -> R.string.validation_invalid_vertex_shader
            }
            Toast.makeText(
                this@WallpaperSettingsActivity.applicationContext,
                errorString,
                Toast.LENGTH_SHORT
            ).show()
            true
        }
}