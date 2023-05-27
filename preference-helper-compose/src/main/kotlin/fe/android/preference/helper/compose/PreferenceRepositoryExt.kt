package fe.android.preference.helper.compose

import fe.android.preference.helper.BasePreference
import fe.android.preference.helper.PreferenceRepository

fun PreferenceRepository.getStringState(
    preference: BasePreference.PreferenceNullable<String>
) = getState(preference, ::writeString, ::getString)

@JvmName("getMappedAsStateByString")
fun <T> PreferenceRepository.getState(
    preference: BasePreference.MappedPreference<T, String>,
) = getState(preference, ::write, ::get)

fun PreferenceRepository.getIntState(preference: BasePreference.Preference<Int>) = getState(
    preference, ::writeInt, ::getInt
)

@JvmName("getMappedAsStateByInt")
fun <T> PreferenceRepository.getState(
    preference: BasePreference.MappedPreference<T, Int>,
) = getState(preference, ::write, ::get)

fun PreferenceRepository.getLongState(preference: BasePreference.Preference<Long>) = getState(
    preference, ::writeLong, ::getLong
)

@JvmName("getMappedAsStateByLong")
fun <T> PreferenceRepository.getState(
    preference: BasePreference.MappedPreference<T, Long>,
) = getState(preference, ::write, ::get)

fun PreferenceRepository.getBooleanState(preference: BasePreference.Preference<Boolean>) = getState(
    preference, ::writeBoolean, ::getBoolean
)

@JvmName("getMappedAsStateByBoolean")
fun <T> PreferenceRepository.getState(
    preference: BasePreference.MappedPreference<T, Boolean>,
) = getState(preference, ::write, ::get)

private val stateCache = mutableMapOf<String, RepositoryState<*, *, *>>()

@Suppress("UNCHECKED_CAST")
private fun <T, NT, P : BasePreference<T, NT>> getState(
    preference: P,
    writer: (P, NT) -> Unit,
    reader: (P) -> NT,
) = stateCache.getOrPut(preference.key) {
    RepositoryState(preference, writer, reader)
} as RepositoryState<T, NT, P>