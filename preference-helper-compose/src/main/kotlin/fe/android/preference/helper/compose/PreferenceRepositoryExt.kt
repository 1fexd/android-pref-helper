package fe.android.preference.helper.compose

import fe.android.preference.helper.*


private typealias StateNullablePreference<NT, T> = RepositoryState<T, NT, PreferenceNullable<T>>
private typealias StateMappedPreference<T, M> = RepositoryState<T, T, MappedPreference<T, M>>
private typealias StatePreference<T> = RepositoryState<T, T, Preference<T>>

public fun PreferenceRepository.getStringState(
    preference: PreferenceNullable<String>,
    stateCache: StateCache = globalStateCache
): StateNullablePreference<String?, String> {
    return getState(preference, ::writeString, ::getString, stateCache)
}

@JvmName("getMappedAsStateByString")
public fun <T : Any> PreferenceRepository.getState(
    preference: MappedPreference<T, String>,
    stateCache: StateCache = globalStateCache
): StateMappedPreference<T, String> {
    return getState(preference, ::write, ::get, stateCache)
}

public fun PreferenceRepository.getIntState(
    preference: Preference<Int>,
    stateCache: StateCache = globalStateCache
): StatePreference<Int> {
    return getState(preference, ::writeInt, ::getInt, stateCache)
}

@JvmName("getMappedAsStateByInt")
public fun <T : Any> PreferenceRepository.getState(
    preference: MappedPreference<T, Int>,
    stateCache: StateCache = globalStateCache
): StateMappedPreference<T, Int> {
    return getState(preference, ::write, ::get, stateCache)
}

public fun PreferenceRepository.getLongState(
    preference: Preference<Long>,
    stateCache: StateCache = globalStateCache
): StatePreference<Long> {
    return getState(preference, ::writeLong, ::getLong, stateCache)
}

@JvmName("getMappedAsStateByLong")
public fun <T : Any> PreferenceRepository.getState(
    preference: MappedPreference<T, Long>,
    stateCache: StateCache = globalStateCache
): StateMappedPreference<T, Long> {
    return getState(preference, ::write, ::get, stateCache)
}

public fun PreferenceRepository.getBooleanState(
    preference: Preference<Boolean>,
    stateCache: StateCache = globalStateCache
): StatePreference<Boolean> {
    return getState(preference, ::writeBoolean, ::getBoolean, stateCache)
}

@JvmName("getMappedAsStateByBoolean")
public fun <T : Any> PreferenceRepository.getState(
    preference: MappedPreference<T, Boolean>,
    stateCache: StateCache = globalStateCache
): StateMappedPreference<T, Boolean> {
    return getState(preference, ::write, ::get, stateCache)
}

private val globalStateCache: StateCache = StateCache()

public fun getGlobalCachedState(key: String): RepositoryState<*, *, *>? {
    return globalStateCache.get(key)
}

public fun closeGlobalStateCache() {
    globalStateCache.close()
}

@Suppress("UNCHECKED_CAST")
private fun <T : Any, NT, P : BasePreference<T, NT>> getState(
    preference: P,
    writer: Writer<P, NT>,
    reader: Reader<P, NT>,
    stateCache: StateCache = globalStateCache
): RepositoryState<T, NT, P> {
    return stateCache.getOrPut(preference, writer, reader) as RepositoryState<T, NT, P>
}

public typealias Writer<P, NT> = (P, NT) -> Unit
public typealias Reader<P, NT> = (P) -> NT
