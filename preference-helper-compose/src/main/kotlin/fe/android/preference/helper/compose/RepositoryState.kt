package fe.android.preference.helper.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import fe.android.preference.helper.BasePreference
import kotlin.reflect.KProperty

class RepositoryState<T, NT, P : BasePreference<T, NT>>(
    private val preference: P,
    private val writer: (P, NT) -> Unit,
    reader: (P) -> NT,
) {
    private val mutableState = mutableStateOf(reader(preference))
    val value by mutableState

    fun matches(toMatch: NT) = value == toMatch

    fun updateState(newState: NT) {
        if (mutableState.value != newState) {
            mutableState.value = newState
            writer(preference, newState)
        }
    }

    operator fun <T> getValue(thisObj: Any?, property: KProperty<*>): NT = value
}