package fe.android.preference.helper.compose

import android.content.Context
import fe.android.preference.helper.*


public typealias StateNullablePreference<NT, T> = MutablePreferenceState<T, NT, Preference.Nullable<T>>
public typealias StateMappedPreference<T, M> = MutablePreferenceState<T, T, Preference.Mapped<T, M>>
public typealias StatePreference<T> = MutablePreferenceState<T, T, Preference.Default<T>>

public abstract class StatePreferenceRepository(
    context: Context,
    @Suppress("MemberVisibilityCanBePrivate") public val stateCache: StateCache = StateCache()
) : PreferenceRepository(context) {

    @JvmName("asStringState")
    public fun asState(preference: Preference.Nullable<String>): StateNullablePreference<String?, String> {
        return createCachedState(preference, ::put, this::get)
    }

    @JvmName("asMappedStringState")
    public fun <T : Any> asState(preference: Preference.Mapped<T, String>): StateMappedPreference<T, String> {
        return createCachedState(preference, ::put, this::get)
    }

    @JvmName("asIntState")
    public fun asState(preference: Preference.Default<Int>): StatePreference<Int> {
        return createCachedState(preference, ::put, ::getInt)
    }

    @JvmName("asMappedIntState")
    public fun <T : Any> asState(preference: Preference.Mapped<T, Int>): StateMappedPreference<T, Int> {
        return createCachedState(preference, ::put, this::get)
    }

    @JvmName("asLongState")
    public fun asState(preference: Preference.Default<Long>): StatePreference<Long> {
        return createCachedState(preference, ::put, this::get)
    }

    @JvmName("asMappedLongState")
    public fun <T : Any> asState(preference: Preference.Mapped<T, Long>): StateMappedPreference<T, Long> {
        return createCachedState(preference, ::put, this::get)
    }

    @JvmName("asBooleanState")
    public fun asState(preference: Preference.Default<Boolean>): StatePreference<Boolean> {
        return createCachedState(preference, ::put, this::get)
    }

    @JvmName("asMappedBooleanState")
    public fun <T : Any> asState(preference: Preference.Mapped<T, Boolean>): StateMappedPreference<T, Boolean> {
        return createCachedState(preference, ::put, this::get)
    }

    private fun <T : Any, NT, P : Preference<T, NT>> createCachedState(
        preference: P,
        put: Put<P, NT>,
        get: Get<P, NT>,
    ): MutablePreferenceState<T, NT, P> {
        @Suppress("UNCHECKED_CAST")
        return stateCache.getOrPut(preference, put, get) as MutablePreferenceState<T, NT, P>
    }
}

public typealias Put<P, NT> = (P, NT) -> Unit
public typealias Get<P, NT> = (P) -> NT
