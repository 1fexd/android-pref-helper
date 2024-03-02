package fe.android.preference.helper.compose

import android.content.Context
import fe.android.preference.helper.*


public typealias StateNullablePreference<NT, T> = MutablePreferenceState<T, NT, Preference.Nullable<T>>
public typealias StateMappedPreference<T, M> = MutablePreferenceState<T, T, Preference.Mapped<T, M>>
public typealias StatePreference<T> = MutablePreferenceState<T, T, Preference.Default<T>>

public abstract class ComposePreferenceRepository(
    context: Context,
    @Suppress("MemberVisibilityCanBePrivate") public val stateCache: StateCache = StateCache()
) : PreferenceRepository(context) {

    public fun getStringState(preference: Preference.Nullable<String>): StateNullablePreference<String?, String> {
        return getState(preference, ::writeString, ::getString)
    }

    @JvmName("getMappedAsStateByString")
    public fun <T : Any> getState(preference: Preference.Mapped<T, String>): StateMappedPreference<T, String> {
        return getState(preference, ::write, ::get)
    }

    public fun getIntState(preference: Preference.Default<Int>): StatePreference<Int> {
        return getState(preference, ::writeInt, ::getInt)
    }

    @JvmName("getMappedAsStateByInt")
    public fun <T : Any> getState(preference: Preference.Mapped<T, Int>): StateMappedPreference<T, Int> {
        return getState(preference, ::write, ::get)
    }

    public fun getLongState(preference: Preference.Default<Long>): StatePreference<Long> {
        return getState(preference, ::writeLong, ::getLong)
    }

    @JvmName("getMappedAsStateByLong")
    public fun <T : Any> getState(preference: Preference.Mapped<T, Long>): StateMappedPreference<T, Long> {
        return getState(preference, ::write, ::get)
    }

    public fun getBooleanState(preference: Preference.Default<Boolean>): StatePreference<Boolean> {
        return getState(preference, ::writeBoolean, ::getBoolean)
    }

    @JvmName("getMappedAsStateByBoolean")
    public fun <T : Any> getState(preference: Preference.Mapped<T, Boolean>): StateMappedPreference<T, Boolean> {
        return getState(preference, ::write, ::get)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any, NT, P : Preference<T, NT>> getState(
        preference: P,
        writer: Writer<P, NT>,
        reader: Reader<P, NT>,
    ): MutablePreferenceState<T, NT, P> {
        return stateCache.getOrPut(preference, writer, reader) as MutablePreferenceState<T, NT, P>
    }
}

public typealias Writer<P, NT> = (P, NT) -> Unit
public typealias Reader<P, NT> = (P) -> NT
