package fe.android.preference.helper

import android.content.Context
import android.content.SharedPreferences

class PreferenceRepository(context: Context, name: String = "preferences") {

    private val preferences by lazy {
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    private fun editor(
        edit: SharedPreferences.Editor.() -> Unit
    ) = preferences.edit().apply(edit).apply()

    /**
     * String value operations
     */
    fun writeString(
        preference: BasePreference.PreferenceNullable<String>,
        newState: String?
    ) = unsafeWriteString(preference.key, newState)

    fun getString(preference: BasePreference.PreferenceNullable<String>) = unsafeGetString(
        preference.key, preference.default
    )

    fun getOrWriteInit(preference: BasePreference.InitPreference<String>): String {
        val value = unsafeGetString(preference.key, null)
        return if (value == null) {
            val initial = preference.initial()
            unsafeWriteString(preference.key, initial)

            return initial
        } else value
    }

    /**
     * Type to string value operations
     */
    @JvmName("writeMappedToString")
    fun <T> write(
        preference: BasePreference.MappedPreference<T, String>,
        newState: T
    ) = unsafeWriteString(preference.key, preference.persist(newState))

    @JvmName("getMappedByString")
    fun <T> get(
        preference: BasePreference.MappedPreference<T, String>,
    ) = getOrDefault(preference, ::unsafeGetString)

    /**
     * Int value operations
     */
    fun writeInt(
        preference: BasePreference.Preference<Int>,
        newState: Int
    ) = unsafeWriteInt(preference.key, newState)

    fun getInt(preference: BasePreference.Preference<Int>) = unsafeGetInt(
        preference.key, preference.default
    )

    /**
     * Type to int value operations
     */
    @JvmName("writeMappedToInt")
    fun <T> write(
        preference: BasePreference.MappedPreference<T, Int>,
        newState: T
    ) = unsafeWriteInt(preference.key, preference.persist(newState))

    @JvmName("getMappedByInt")
    fun <T> get(
        preference: BasePreference.MappedPreference<T, Int>,
    ) = getOrDefault(preference, ::unsafeGetInt)

    /**
     * Long value operations
     */
    fun writeLong(
        preference: BasePreference.Preference<Long>,
        newState: Long
    ) = unsafeWriteLong(preference.key, newState)

    fun getLong(preference: BasePreference.Preference<Long>) = unsafeGetLong(
        preference.key, preference.default
    )

    /**
     * Type to long value operations
     */
    @JvmName("writeMappedToLong")
    fun <T> write(
        preference: BasePreference.MappedPreference<T, Long>,
        newState: T
    ) = unsafeWriteLong(preference.key, preference.persist(newState))

    @JvmName("getMappedByLong")
    fun <T> get(
        preference: BasePreference.MappedPreference<T, Long>,
    ) = getOrDefault(preference, ::unsafeGetLong)

    /**
     * Boolean value operations
     */
    fun writeBoolean(
        preference: BasePreference.Preference<Boolean>,
        newState: Boolean
    ) = unsafeWriteBoolean(preference.key, newState)

    fun getBoolean(preference: BasePreference.Preference<Boolean>) = unsafeGetBoolean(
        preference.key, preference.default
    )

    /**
     * Type to boolean value operations
     */
    @JvmName("writeMappedToBoolean")
    fun <T> write(
        preference: BasePreference.MappedPreference<T, Boolean>,
        newState: T
    ) = unsafeWriteBoolean(preference.key, preference.persist(newState))

    @JvmName("getMappedByBoolean")
    fun <T> get(
        preference: BasePreference.MappedPreference<T, Boolean>,
    ) = getOrDefault(preference, ::unsafeGetBoolean)

    /**
     * Unsafe writes/reads (do not do check type of Property before writing, use with caution!)
     */
    private fun unsafeWriteString(key: String, newState: String?) = editor {
        putString(key, newState)
    }

    private fun unsafeWriteInt(key: String, newState: Int) = editor {
        putInt(key, newState)
    }

    private fun unsafeWriteLong(key: String, newState: Long) = editor {
        putLong(key, newState)
    }

    private fun unsafeWriteBoolean(key: String, newState: Boolean) = editor {
        putBoolean(key, newState)
    }

    private fun unsafeGetString(key: String, default: String?) = preferences.getString(
        key, default
    )

    private fun unsafeGetInt(key: String, default: Int?) = preferences.getInt(
        key, default!!
    )

    private fun unsafeGetLong(key: String, default: Long?) = preferences.getLong(
        key, default!!
    )

    private fun unsafeGetBoolean(key: String, default: Boolean?) = preferences.getBoolean(
        key, default!!
    )

    /**
     * Utils
     */
    private fun <T, M> getOrDefault(
        preference: BasePreference.MappedPreference<T, M>,
        preferenceReader: KeyReader<M?>,
    ): T {
        val mappedValue = preferenceReader(preference.key, preference.defaultMapped)!!
        return preference.read(mappedValue) ?: preference.default
    }
}

typealias KeyReader<T> = (String, T) -> T