package fe.android.preference.helper.testapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import fe.android.preference.helper.OptionTypeMapper
import fe.android.preference.helper.Preferences
import fe.android.preference.helper.compose.ComposePreferenceRepository

class MainActivity : ComponentActivity() {

    sealed class BrowserMode(val value: String) {
        object None : BrowserMode("none")
        object AlwaysAsk : BrowserMode("always_ask")
        object SelectedBrowser : BrowserMode("browser")
        object Whitelisted : BrowserMode("whitelisted")

        companion object Companion : OptionTypeMapper<BrowserMode, String>(
            { it.value }, { arrayOf(None, AlwaysAsk, SelectedBrowser, Whitelisted) }
        )
    }

    object Test : Preferences() {
        val test = mappedPreference("key", BrowserMode.SelectedBrowser, BrowserMode.Companion)
        val int = intPreference("testint")
    }

    class Test2(context: Context) : ComposePreferenceRepository(context) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferenceRepository = Test2(this)

        val test = preferenceRepository.getIntState(Test.int)
        println(test.value)
        println(test())

//        test.updateState(2)
        test(2)

        val hi = test() == 3
        println(hi)
        val x by test
//        val current = test.value

//        val cur = test()


//        Log.d("test", preferenceRepository.getAnyAsString(Test.test))
//        registerPreference {
//            val key = stringPreference("value")
//        }


//        val key = LinkSheetPreferences.test


////        preferenceRepository.getStringState()
//    //
//    //        val str = preferenceRepository.getAsString(key)
//
//        preferenceRepository.editor {
//            preferenceRepository.writeString(preference, "new value", this)
//            preferenceRepository.writeString(preference, "new value2", this)
//        }

    }
}
