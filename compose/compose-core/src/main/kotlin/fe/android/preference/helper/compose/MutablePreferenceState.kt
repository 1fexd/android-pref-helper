package fe.android.preference.helper.compose


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import fe.android.preference.helper.Preference

public class MutableIntPreferenceState(
    preference: Preference.Default<Int>,
    put: (Preference.Default<Int>, Int) -> Unit,
    get: (Preference.Default<Int>) -> Int,
) : MutablePreferenceState<Int, Int, Preference.Default<Int>>(preference, put, get, mutableIntStateOf(get(preference)))

public class MutableLongPreferenceState(
    preference: Preference.Default<Long>,
    put: (Preference.Default<Long>, Long) -> Unit,
    get: (Preference.Default<Long>) -> Long,
) : MutablePreferenceState<Long, Long, Preference.Default<Long>>(preference, put, get, mutableLongStateOf(get(preference)))

public open class MutablePreferenceState<T : Any, NT, P : Preference<T, NT>>(
    private val preference: P,
    private val put: (P, NT) -> Unit,
    private val get: (P) -> NT,
    private val mutableState: MutableState<NT> = mutableStateOf(get(preference))
) : MutableState<NT> by mutableState {

    override var value: NT
        get() = mutableState.value
        set(value) = update(value)

    public operator fun invoke(): NT {
        return value
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
}
