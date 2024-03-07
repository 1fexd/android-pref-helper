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
    public fun asState(preference: Preference.Default<Int>): MutableIntPreferenceState {
        return createCachedState(preference, ::put, ::get) { pref, put, get ->
            MutableIntPreferenceState(pref, put, get)
        }
    }

    @JvmName("asMappedIntState")
    public fun <T : Any> asState(preference: Preference.Mapped<T, Int>): StateMappedPreference<T, Int> {
        return createCachedState(preference, ::put, this::get)
    }

    @JvmName("asLongState")
    public fun asState(preference: Preference.Default<Long>): MutableLongPreferenceState {
        return createCachedState(preference, ::put, ::get) { pref, put, get ->
            MutableLongPreferenceState(pref, put, get)
        }
    }

    @JvmName("asMappedLongState")
    public fun <T : Any> asState(preference: Preference.Mapped<T, Long>): StateMappedPreference<T, Long> {
        return createCachedState(preference, ::put, ::get)
    }

    @JvmName("asBooleanState")
    public fun asState(preference: Preference.Default<Boolean>): StatePreference<Boolean> {
        return createCachedState(preference, ::put, ::get)
    }

    @JvmName("asMappedBooleanState")
    public fun <T : Any> asState(preference: Preference.Mapped<T, Boolean>): StateMappedPreference<T, Boolean> {
        return createCachedState(preference, ::put, ::get)
    }

    private fun <T : Any, NT, P : Preference<T, NT>, M : MutablePreferenceState<T, NT, P>> createCachedState(
        preference: P,
        put: Put<P, NT>,
        get: Get<P, NT>,
        defaultValue: (P, Put<P, NT>, Get<P, NT>) -> M = { pref, p, g ->
            @Suppress("UNCHECKED_CAST")
            MutablePreferenceState(pref, p, g) as M
        }
    ): M {
        val value = stateCache.get(preference.key)
        val state = if (value == null) {
            val answer = defaultValue(preference, put, get)
            stateCache.put(preference.key, answer)
            answer
        } else {
            value
        }

        @Suppress("UNCHECKED_CAST")
        return state as M
    }
}

public typealias Put<P, NT> = (P, NT) -> Unit
public typealias Get<P, NT> = (P) -> NT
