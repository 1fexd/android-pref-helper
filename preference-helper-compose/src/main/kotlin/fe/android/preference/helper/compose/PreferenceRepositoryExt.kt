package fe.android.preference.helper.compose

import fe.android.preference.helper.BasePreference
import fe.android.preference.helper.PreferenceRepository

class PreferenceRepositoryCompose(private val preferenceRepository: PreferenceRepository) {
    fun getStringState(
        cacheMode: CacheMode = CacheMode.Cached,
        preference: BasePreference.PreferenceNullable<String>
    ) = getState(
        cacheMode,
        preference,
        preferenceRepository::writeString,
        preferenceRepository::getString
    )

    @JvmName("getMappedAsStateByString")
    fun <T> getState(
        cacheMode: CacheMode = CacheMode.Cached,
        preference: BasePreference.MappedPreference<T, String>,
    ) = getState(cacheMode, preference, preferenceRepository::write, preferenceRepository::get)

    fun getIntState(
        cacheMode: CacheMode = CacheMode.Cached,
        preference: BasePreference.Preference<Int>
    ) = getState(
        cacheMode,
        preference,
        preferenceRepository::writeInt,
        preferenceRepository::getInt
    )

    @JvmName("getMappedAsStateByInt")
    fun <T> getState(
        cacheMode: CacheMode = CacheMode.Cached,
        preference: BasePreference.MappedPreference<T, Int>,
    ) = getState(cacheMode, preference, preferenceRepository::write, preferenceRepository::get)

    fun getLongState(
        cacheMode: CacheMode = CacheMode.Cached,
        preference: BasePreference.Preference<Long>
    ) = getState(
        cacheMode,
        preference,
        preferenceRepository::writeLong,
        preferenceRepository::getLong
    )

    @JvmName("getMappedAsStateByLong")
    fun <T> getState(
        cacheMode: CacheMode = CacheMode.Cached,
        preference: BasePreference.MappedPreference<T, Long>,
    ) = getState(cacheMode, preference, preferenceRepository::write, preferenceRepository::get)

    fun getBooleanState(
        cacheMode: CacheMode = CacheMode.Cached,
        preference: BasePreference.Preference<Boolean>
    ) =
        getState(
            cacheMode,
            preference,
            preferenceRepository::writeBoolean,
            preferenceRepository::getBoolean
        )

    @JvmName("getMappedAsStateByBoolean")
    fun <T> getState(
        cacheMode: CacheMode = CacheMode.Cached,
        preference: BasePreference.MappedPreference<T, Boolean>,
    ) = getState(cacheMode, preference, preferenceRepository::write, preferenceRepository::get)

    enum class CacheMode {
        Bypass, Cached
    }

    enum class CacheResult {
        New, Cached
    }

    private val stateCache = mutableMapOf<String, RepositoryState<*, *, *>>()

    fun clearCache() = stateCache.clear()

    @Suppress("UNCHECKED_CAST")
    private fun <T, NT, P : BasePreference<T, NT>> getState(
        cacheMode: CacheMode = CacheMode.Cached,
        preference: P,
        writer: (P, NT) -> Unit,
        reader: (P) -> NT,
    ): RepositoryState<T, NT, P> {
        if (cacheMode == CacheMode.Cached && stateCache.containsKey(preference.key)) {
            return stateCache[preference.key] as RepositoryState<T, NT, P>
        }

        val newState = getNewState(preference, writer, reader)
        stateCache[preference.key] = newState

        return newState
    }

    private fun <T, NT, P : BasePreference<T, NT>> getNewState(
        preference: P,
        writer: (P, NT) -> Unit,
        reader: (P) -> NT,
    ) = RepositoryState(preference, writer, reader)
}

