package fe.android.preference.helper.compose


import androidx.compose.runtime.mutableStateOf
import fe.android.preference.helper.Preference
import kotlin.reflect.KProperty

public class MutablePreferenceState<T : Any, NT, P : Preference<T, NT>>(
    private val preference: P,
    private val put: (P, NT) -> Unit,
    public val get: (P) -> NT,
) {
    private val mutableState = mutableStateOf(get(preference))
    public val value: NT
        get() = this()

    public operator fun invoke(): NT {
        return mutableState.value
    }

    public operator fun invoke(newState: NT) {
        update(newState)
    }

    private fun update(newState: NT, write: Boolean = true) {
        if (mutableState.value != newState) {
            mutableState.value = newState
            if (write) {
                put(preference, newState)
            }
        }
    }

    public fun forceRefresh() {
        update(get(preference), write = false)
    }

    public operator fun getValue(thisObj: Any?, property: KProperty<*>): NT = this()
}
