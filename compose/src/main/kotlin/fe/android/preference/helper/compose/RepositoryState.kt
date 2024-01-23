package fe.android.preference.helper.compose


import androidx.compose.runtime.mutableStateOf
import fe.android.preference.helper.BasePreference
import kotlin.reflect.KProperty

public class RepositoryState<T : Any, NT, P : BasePreference<T, NT>>(
    private val preference: P,
    private val writer: (P, NT) -> Unit,
    public val reader: (P) -> NT,
) {
    private val mutableState = mutableStateOf(reader(preference))

    @Deprecated(message = "Use ()/invoke()", replaceWith = ReplaceWith("this()"))
    @Suppress("MemberVisibilityCanBePrivate")
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
                writer(preference, newState)
            }
        }
    }

    public fun forceRefresh() {
        update(reader(preference), write = false)
    }

    @Deprecated(message = "Compare using ==", replaceWith = ReplaceWith("this() == toMatch"))
    public fun matches(toMatch: NT): Boolean = mutableState.value == toMatch

    @Deprecated(message = "Use invoke(newState) instead", replaceWith = ReplaceWith("this(newState)"))
    @Suppress("MemberVisibilityCanBePrivate")
    public fun updateState(newState: NT) {
        invoke(newState)
    }

    public operator fun getValue(thisObj: Any?, property: KProperty<*>): NT = this()
}
