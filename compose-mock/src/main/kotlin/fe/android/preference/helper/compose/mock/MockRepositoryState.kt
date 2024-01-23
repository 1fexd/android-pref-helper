package fe.android.preference.helper.compose.mock

import fe.android.preference.helper.*
import fe.android.preference.helper.compose.*

public object MockRepositoryState {
    public inline fun <reified T : Any> preference(
        value: T,
        key: String = "mock_preference"
    ): RepositoryState<T, T, Preference<T>> {
        val pref = Preference(key, value, T::class)
        return RepositoryState(pref, { _, _ -> }, { it.default })
    }

    public inline fun <reified T : Any> nullablePreference(
        value: T?,
        key: String = "mock_nullable_preference"
    ): RepositoryState<T, T?, PreferenceNullable<T>> {
        val pref = PreferenceNullable(key, value, T::class)
        return RepositoryState(pref, { _, _ -> }, { it.default })
    }

    public inline fun <reified T : Any, reified M : Any> mappedPreference(
        default: T,
        mapper: TypeMapper<T, M>,
        key: String = "mock_mapped_preference"
    ): RepositoryState<T, T, MappedPreference<T, M>> {
        val pref = MappedPreference(key, default, mapper, T::class, M::class)
        return RepositoryState(pref, { _, _ -> }, { it.default })
    }
}


