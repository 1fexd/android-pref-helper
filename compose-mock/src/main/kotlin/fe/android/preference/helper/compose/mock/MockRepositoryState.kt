package fe.android.preference.helper.compose.mock

import fe.android.preference.helper.*
import fe.android.preference.helper.compose.*

public object MockRepositoryState  {
    public inline fun <reified T : Any> default(
        value: T,
        key: String = "mock_preference"
    ): MutablePreferenceState<T, T, Preference.Default<T>> {
        val pref = Preference.Default(key, value, T::class)
        return MutablePreferenceState(pref, { _, _ -> }, { it.default })
    }

    public inline fun <reified T : Any> nullable(
        value: T?,
        key: String = "mock_nullable_preference"
    ): MutablePreferenceState<T, T?, Preference.Nullable<T>> {
        val pref = Preference.Nullable(key, value, T::class)
        return MutablePreferenceState(pref, { _, _ -> }, { it.default })
    }

    public inline fun <reified T : Any, reified M : Any> mapped(
        default: T,
        mapper: TypeMapper<T, M>,
        key: String = "mock_mapped_preference"
    ): MutablePreferenceState<T, T, Preference.Mapped<T, M>> {
        val pref = Preference.Mapped(key, default, mapper, T::class, M::class)
        return MutablePreferenceState(pref, { _, _ -> }, { it.default })
    }
}


