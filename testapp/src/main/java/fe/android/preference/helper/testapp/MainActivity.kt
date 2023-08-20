package fe.android.preference.helper.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fe.android.preference.helper.BasePreference.PreferenceNullable.Companion.stringPreference
import fe.android.preference.helper.PreferenceRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preference = stringPreference("test")

        val preferenceRepository = PreferenceRepository(this)
        preferenceRepository.defaultEditor {
            preferenceRepository.writeString(preference, "test", this)
        }

        setContent {}
    }
}
