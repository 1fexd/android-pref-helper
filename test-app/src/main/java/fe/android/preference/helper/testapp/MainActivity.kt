package fe.android.preference.helper.testapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fe.android.preference.helper.OptionTypeMapper
import fe.android.preference.helper.PreferenceDefinition
import fe.android.preference.helper.compose.StatePreferenceRepository

class MainActivity : ComponentActivity() {

    sealed class BrowserMode(val value: String) {
        object None : BrowserMode("none")
        object AlwaysAsk : BrowserMode("always_ask")
        object SelectedBrowser : BrowserMode("browser")
        object Whitelisted : BrowserMode("whitelisted")

        companion object Companion : OptionTypeMapper<BrowserMode, String>(
            { it.value }, { arrayOf(None, AlwaysAsk, SelectedBrowser, Whitelisted) }
        )

        override fun toString(): String {
            return value
        }
    }

    object TestPreferenceDefinition : PreferenceDefinition() {
        val test = mapped("key", BrowserMode.SelectedBrowser, BrowserMode.Companion)
        val int = int("testint").migrate { repo, value ->
            if (!repo.hasStoredValue(test)) {

            }
        }

        val test123 = long(".________________________.", 1111).migrateTo { preferenceRepository, current ->
            current + 1000
        }

        val init = string("tet") {
            "yeeeeeeeeet"
        }

        val counter = int("counter")

        init {
            finalize()
        }
    }

    class Test2(context: Context) : StatePreferenceRepository(context) {
//        val testState = asState(TestPreferenceDefinition.test)
        val initState = getOrPutInit(TestPreferenceDefinition.init)
        val test123 = asState(TestPreferenceDefinition.test123)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferenceRepository = Test2(this)

        setContent {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(all = 10.dp)
            ) {
                var counter by remember { preferenceRepository.asState(TestPreferenceDefinition.counter) }

                Column {
                    Text(text = "$counter")

                    Row {
                        Button(onClick = {
                            counter--
                        }) {
                            Text(text = "-")
                        }

                        Button(onClick = {
                            counter++
                        }) {
                            Text(text = "+")
                        }
                    }
                }
            }
        }
//        TestPreferenceDefinition.runMigrations(preferenceRepository)
//        println(preferenceRepository.initState)s

//        val test = preferenceRepository.asState(TestPreferenceDefinition.int)
//        val test123 = preferenceRepository.asState(TestPreferenceDefinition.test123)
//        preferenceRepository.unsafePut()
//        println(test.value)
//        println(test())
//        Log.d("MigrateTo(test123)", "current=${test123()}")
//        Test.i
//        Test.intPreference("")

//        test.updateState(2)
//        test(2)

//        preferenceRepository.edit {
//
//        }

//        preferenceRepository.editor { pref ->
//
////            pref.writeInt(Test.int, 1, this)
//        }

//        println(preferenceRepository.getAnyAsString(TestPreferenceDefinition.test))
//        )
//        preferenceRepository.edit {
//            put(Test.int, 10)
//        }
//
//        val hi = test() == 3
//        println(hi)
//        val x by test

//        val pref = Test.int


//        val test123 = preferenceRepository.get(Test.int)

//        val x = test

//        Preference.PB()
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
