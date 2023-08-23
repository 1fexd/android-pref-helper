package fe.android.preference.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

typealias PreferenceEditAction = SharedPreferences.Editor.() -> Unit

abstract class PreferenceRepository(context: Context, name: String? = "preferences") {

    private val preferences by lazy {
        if (name == null) PreferenceManager.getDefaultSharedPreferences(context)
        else context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun editor(editor: PreferenceEditAction) = preferences.edit().apply(editor).apply()

    fun setStringValueToPreference(
        preference: BasePreference<*, *>,
        value: String,
        editor: SharedPreferences.Editor?
    ) {
        val mapped = preference as? BasePreference.MappedPreference<*, *>
        return when (if (mapped != null) preference.mappedClazz else preference.clazz) {
            String::class -> unsafeWriteString(preference.key, value, editor)
            Boolean::class -> unsafeWriteBoolean(preference.key, value.toBooleanStrict(), editor)
            Int::class -> unsafeWriteInt(preference.key, value.toInt(), editor)
            Long::class -> unsafeWriteLong(preference.key, value.toLong(), editor)

            else -> Unit
        }
    }

    fun getAnyAsString(preference: BasePreference<*, *>): String? {
        val mapped = preference as? BasePreference.MappedPreference<*, *>
        return when (if (mapped != null) preference.mappedClazz else preference.clazz) {
            String::class -> {
//                if (mapped != null) getValueFromMapped(
//                    preference as BasePreference.MappedPreference<*, String>,
//                    ::unsafeGetString
//                ).toString()
                unsafeGetString(preference.key, (if(mapped != null) preference.defaultMapped else preference.default) as String?)
            }

            Boolean::class -> {
//                if (mapped != null) getValueFromMapped(
//                    preference as BasePreference.MappedPreference<*, Boolean>,
//                    ::unsafeGetBoolean
//                ).toString()
                unsafeGetBoolean(preference.key, (if(mapped != null) preference.defaultMapped else preference.default) as Boolean?).toString()
            }

            Int::class -> {
//                if (mapped != null) getValueFromMapped(
//                    preference as BasePreference.MappedPreference<*, Int>,
//                    ::unsafeGetInt
//                ).toString()
                unsafeGetInt(preference.key, (if(mapped != null) preference.defaultMapped else preference.default) as Int?).toString()
            }

            Long::class -> {
//                if (mapped != null) getValueFromMapped(
//                    preference as BasePreference.MappedPreference<*, Long>,
//                    ::unsafeGetLong
//                ).toString()
                unsafeGetLong(preference.key, (if(mapped != null) preference.defaultMapped else preference.default) as Long?).toString()
            }

            else -> null
        }
    }


    /**
     * String value operations
     */
    fun writeString(
        preference: BasePreference.PreferenceNullable<String>,
        newState: String?,
        editor: SharedPreferences.Editor? = null
    ) = unsafeWriteString(preference.key, newState, editor)

    fun getString(preference: BasePreference.PreferenceNullable<String>) = unsafeGetString(
        preference.key, preference.default
    )

    fun getOrWriteInit(
        preference: BasePreference.InitPreference<String>,
        editor: SharedPreferences.Editor? = null
    ): String {
        val value = unsafeGetString(preference.key, null)
        return if (value == null) {
            val initial = preference.initial()
            unsafeWriteString(preference.key, initial, editor)

            return initial
        } else value
    }

    /**
     * Type to string value operations
     */
    @JvmName("writeMappedToString")
    fun <T : Any> write(
        preference: BasePreference.MappedPreference<T, String>,
        newState: T,
        editor: SharedPreferences.Editor? = null
    ) = unsafeWriteString(preference.key, preference.persist(newState), editor)

    @JvmName("getMappedByString")
    fun <T : Any> get(
        preference: BasePreference.MappedPreference<T, String>,
    ) = getValueFromMapped(preference, ::unsafeGetString)

    /**
     * Int value operations
     */
    fun writeInt(
        preference: BasePreference.Preference<Int>,
        newState: Int,
        editor: SharedPreferences.Editor? = null
    ) = unsafeWriteInt(preference.key, newState, editor)

    fun getInt(preference: BasePreference.Preference<Int>) = unsafeGetInt(
        preference.key, preference.default
    )

    /**
     * Type to int value operations
     */
    @JvmName("writeMappedToInt")
    fun <T : Any> write(
        preference: BasePreference.MappedPreference<T, Int>,
        newState: T,
        editor: SharedPreferences.Editor? = null
    ) = unsafeWriteInt(preference.key, preference.persist(newState), editor)

    @JvmName("getMappedByInt")
    fun <T : Any> get(
        preference: BasePreference.MappedPreference<T, Int>,
    ) = getValueFromMapped(preference, ::unsafeGetInt)

    /**
     * Long value operations
     */
    fun writeLong(
        preference: BasePreference.Preference<Long>,
        newState: Long,
        editor: SharedPreferences.Editor? = null
    ) = unsafeWriteLong(preference.key, newState, editor)

    fun getLong(preference: BasePreference.Preference<Long>) = unsafeGetLong(
        preference.key, preference.default
    )

    /**
     * Type to long value operations
     */
    @JvmName("writeMappedToLong")
    fun <T : Any> write(
        preference: BasePreference.MappedPreference<T, Long>,
        newState: T,
        editor: SharedPreferences.Editor? = null
    ) = unsafeWriteLong(preference.key, preference.persist(newState), editor)

    @JvmName("getMappedByLong")
    fun <T : Any> get(
        preference: BasePreference.MappedPreference<T, Long>,
    ) = getValueFromMapped(preference, ::unsafeGetLong)

    /**
     * Boolean value operations
     */
    fun writeBoolean(
        preference: BasePreference.Preference<Boolean>,
        newState: Boolean,
        editor: SharedPreferences.Editor? = null
    ) = unsafeWriteBoolean(preference.key, newState, editor)

    fun getBoolean(preference: BasePreference.Preference<Boolean>) = unsafeGetBoolean(
        preference.key, preference.default
    )

    /**
     * Type to boolean value operations
     */
    @JvmName("writeMappedToBoolean")
    fun <T : Any> write(
        preference: BasePreference.MappedPreference<T, Boolean>,
        newState: T,
        editor: SharedPreferences.Editor? = null
    ) = unsafeWriteBoolean(preference.key, preference.persist(newState), editor)

    @JvmName("getMappedByBoolean")
    fun <T : Any> get(
        preference: BasePreference.MappedPreference<T, Boolean>,
    ) = getValueFromMapped(preference, ::unsafeGetBoolean)

    /**
     * Unsafe writes/reads (do not do check type of Property before writing, use with caution!)
     */
    private fun unsafeWriteString(
        key: String,
        newState: String?,
        editor: SharedPreferences.Editor?
    ) = write(editor) { putString(key, newState) }

    private fun unsafeWriteInt(
        key: String,
        newState: Int,
        editor: SharedPreferences.Editor?
    ) = write(editor) { putInt(key, newState) }

    private fun unsafeWriteLong(
        key: String,
        newState: Long,
        editor: SharedPreferences.Editor?
    ) = write(editor) { putLong(key, newState) }

    private fun unsafeWriteBoolean(
        key: String,
        newState: Boolean,
        editor: SharedPreferences.Editor?
    ) = write(editor) { putBoolean(key, newState) }

    private fun write(editor: SharedPreferences.Editor?, action: PreferenceEditAction) {
        if (editor != null) action(editor)
        else editor(action)
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
    private fun <T : Any, M : Any> getValueFromMapped(
        preference: BasePreference.MappedPreference<T, M>,
        preferenceReader: KeyReader<M?>,
    ): T {
        val mappedValue = preferenceReader(preference.key, preference.defaultMapped)!!
        return preference.read(mappedValue) ?: preference.default
    }
}

typealias KeyReader<T> = (String, T) -> T