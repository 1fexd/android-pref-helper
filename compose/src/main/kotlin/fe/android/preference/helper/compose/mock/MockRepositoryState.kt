package fe.android.preference.helper.compose.mock

import fe.android.preference.helper.*
import fe.android.preference.helper.compose.*

public object MockRepositoryState {
    public inline fun <reified T : Any> preference(value: T): RepositoryState<T, T, Preference<T>> {
        val pref = Preference("mocked", value, T::class)
        return RepositoryState(pref, { _, _ -> }, { it.default })
    }

    public inline fun <reified T : Any> nullablePreference(value: T?): RepositoryState<T, T?, PreferenceNullable<T>> {
        val pref = PreferenceNullable("mocked", value, T::class)
        return RepositoryState(pref, { _, _ -> }, { it.default })
    }

    public inline fun <reified T : Any, reified M : Any> mappedPreference(
        key: String,
        default: T,
        mapper: TypeMapper<T, M>
    ): RepositoryState<T, T, MappedPreference<T, M>> {
        val pref = MappedPreference(key, default, mapper, T::class, M::class)
        return RepositoryState(pref, { _, _ -> }, { it.default })
    }
}


