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

    @Deprecated(message = "Use ()/invoke()", replaceWith = ReplaceWith("this()"), level = DeprecationLevel.WARNING)
    @Suppress("MemberVisibilityCanBePrivate")
    public val value: NT
        get() = mutableState.value

    public operator fun invoke(): NT {
        return mutableState.value
    }

    public fun forceRefresh() {
        updateState(reader(preference))
    }

    public fun matches(toMatch: NT): Boolean = mutableState.value == toMatch

    @Suppress("MemberVisibilityCanBePrivate")
    public fun updateState(newState: NT) {
        if (mutableState.value != newState) {
            mutableState.value = newState
            writer(preference, newState)
        }
    }

    public operator fun getValue(thisObj: Any?, property: KProperty<*>): NT = mutableState.value
}
