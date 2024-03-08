package fe.android.preference.helper.compose.mock

import fe.android.preference.helper.*
import fe.android.preference.helper.compose.*
import kotlin.reflect.KClass

public class MockRepositoryState : AbstractPreferenceDefinition() {

    public fun boolean(
        value: Boolean,
        key: String = "mock_preference"
    ): MutablePreferenceState<Boolean, Boolean, Preference.Default<Boolean>> {
        return MutablePreferenceState(boolean(key, value), { _, _ -> }, { it.default })
    }

    public fun int(
        value: Int,
        key: String = "mock_preference"
    ): MutablePreferenceState<Int, Int, Preference.Default<Int>> {
        return MutableIntPreferenceState(int(key, value), { _, _ -> }, { it.default })
    }

    public fun long(
        value: Long,
        key: String = "mock_preference"
    ): MutablePreferenceState<Long, Long, Preference.Default<Long>> {
        return MutableLongPreferenceState(long(key, value), { _, _ -> }, { it.default })
    }

    public fun string(
        value: String?,
        key: String = "mock_nullable_preference"
    ): MutablePreferenceState<String, String?, Preference.Nullable<String>> {
        return MutablePreferenceState(string(key, value), { _, _ -> }, { it.default })
    }

    public inline fun <reified T : Any, reified M : Any> mapped(
        default: T,
        mapper: TypeMapper<T, M>,
        key: String = "mock_mapped_preference"
    ): MutablePreferenceState<T, T, Preference.Mapped<T, M>> {
        return MutablePreferenceState(
            `access$mapped`(key, default, mapper, T::class, M::class),
            { _, _ -> },
            { it.default })
    }

    @PublishedApi
    internal fun <T : Any, M : Any> `access$mapped`(
        key: String,
        default: T,
        mapper: TypeMapper<T, M>,
        t: KClass<T>,
        m: KClass<M>
    ): Preference.Mapped<T, M> = mapped(key, default, mapper, t, m)
}


