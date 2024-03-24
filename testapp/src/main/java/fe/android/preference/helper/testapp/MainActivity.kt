package fe.android.preference.helper.testapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
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


        val init = string("tet") {
            "yeeeeeeeeet"
        }

        init {
            finalize()
        }

        init {
//            this.add()
        }

        init {
//            Preference.Init<Int>("test", { 0 }, Int::class)
        }

//        val test = this.
    }

    class Test2(context: Context) : StatePreferenceRepository(context) {
        val testState = asState(TestPreferenceDefinition.test)
        val initState = getOrPutInit(TestPreferenceDefinition.init)

        init {

//            TestPreferenceDefinition.migrate(this)

//            TestPreferenceDefinition.migrate.forEach { (key, pref) ->
//
////                isPreferenceStored(key)
//            }
        }

        fun test() {
//            isPreferenceStored(Test.test)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preferenceRepository = Test2(this)
//        println(preferenceRepository.initState)

        val test = preferenceRepository.asState(TestPreferenceDefinition.int)
//        preferenceRepository.unsafePut()
//        println(test.value)
        println(test())

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

        println(preferenceRepository.getAnyAsString(TestPreferenceDefinition.test))
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
