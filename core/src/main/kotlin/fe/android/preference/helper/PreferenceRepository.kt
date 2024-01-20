package fe.android.preference.helper

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

public typealias PreferenceEditAction = SharedPreferences.Editor.() -> Unit
private typealias PrefEditor = SharedPreferences.Editor

public abstract class PreferenceRepository(context: Context) {

    private val preferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    public fun editor(editor: PreferenceEditAction) {
        preferences.edit().apply(editor).apply()
    }

    public fun setStringValueToPreference(preference: BasePreference<*, *>, value: String, editor: PrefEditor?) {
        val mapped = preference as? MappedPreference<*, *>
        return when (if (mapped != null) preference.mappedClazz else preference.clazz) {
            String::class -> unsafeWriteString(preference.key, value, editor)
            Boolean::class -> unsafeWriteBoolean(preference.key, value.toBooleanStrict(), editor)
            Int::class -> unsafeWriteInt(preference.key, value.toInt(), editor)
            Long::class -> unsafeWriteLong(preference.key, value.toLong(), editor)

            else -> Unit
        }
    }

    public fun getAnyAsString(preference: BasePreference<*, *>): String? {
        val mapped = preference as? MappedPreference<*, *>
        val default = (if (mapped != null) preference.defaultMapped else preference.default)
        val clazz = if (mapped != null) preference.mappedClazz else preference.clazz

        return when (clazz) {
            String::class -> unsafeGetString(preference.key, default as String?)
            Boolean::class -> unsafeGetBoolean(preference.key, default as Boolean?).toString()
            Int::class -> unsafeGetInt(preference.key, default as Int?).toString()
            Long::class -> unsafeGetLong(preference.key, default as Long?).toString()
            else -> null
        }
    }


    /**
     * String value operations
     */
    public fun writeString(preference: PreferenceNullable<String>, newState: String?, editor: PrefEditor? = null) {
        unsafeWriteString(preference.key, newState, editor)
    }

    public fun getString(preference: PreferenceNullable<String>): String? {
        return unsafeGetString(preference.key, preference.default)
    }

    public fun getOrWriteInit(preference: InitPreference<String>, editor: PrefEditor? = null): String {
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
    public fun <T : Any> write(preference: MappedPreference<T, String>, newState: T, editor: PrefEditor? = null) {
        unsafeWriteString(preference.key, preference.persist(newState), editor)
    }

    @JvmName("getMappedByString")
    public fun <T : Any> get(preference: MappedPreference<T, String>): T {
        return getValueFromMapped(preference, ::unsafeGetString)
    }

    /**
     * Int value operations
     */
    public fun writeInt(preference: Preference<Int>, newState: Int, editor: PrefEditor? = null) {
        unsafeWriteInt(preference.key, newState, editor)
    }

    public fun getInt(preference: Preference<Int>): Int {
        return unsafeGetInt(preference.key, preference.default)
    }

    /**
     * Type to int value operations
     */
    @JvmName("writeMappedToInt")
    public fun <T : Any> write(preference: MappedPreference<T, Int>, newState: T, editor: PrefEditor? = null) {
        unsafeWriteInt(preference.key, preference.persist(newState), editor)
    }

    @JvmName("getMappedByInt")
    public fun <T : Any> get(preference: MappedPreference<T, Int>): T {
        return getValueFromMapped(preference, ::unsafeGetInt)
    }

    /**
     * Long value operations
     */
    public fun writeLong(preference: Preference<Long>, newState: Long, editor: PrefEditor? = null) {
        unsafeWriteLong(preference.key, newState, editor)
    }

    public fun getLong(preference: Preference<Long>): Long {
        return unsafeGetLong(preference.key, preference.default)
    }

    /**
     * Type to long value operations
     */
    @JvmName("writeMappedToLong")
    public fun <T : Any> write(preference: MappedPreference<T, Long>, newState: T, editor: PrefEditor? = null) {
        unsafeWriteLong(preference.key, preference.persist(newState), editor)
    }

    @JvmName("getMappedByLong")
    public fun <T : Any> get(preference: MappedPreference<T, Long>): T {
        return getValueFromMapped(preference, ::unsafeGetLong)
    }

    /**
     * Boolean value operations
     */
    public fun writeBoolean(preference: Preference<Boolean>, newState: Boolean, editor: PrefEditor? = null) {
        unsafeWriteBoolean(preference.key, newState, editor)
    }

    public fun getBoolean(preference: Preference<Boolean>): Boolean {
        return unsafeGetBoolean(preference.key, preference.default)
    }

    /**
     * Type to boolean value operations
     */
    @JvmName("writeMappedToBoolean")
    public fun <T : Any> write(preference: MappedPreference<T, Boolean>, newState: T, editor: PrefEditor? = null) {
        unsafeWriteBoolean(preference.key, preference.persist(newState), editor)
    }

    @JvmName("getMappedByBoolean")
    public fun <T : Any> get(preference: MappedPreference<T, Boolean>): T {
        return getValueFromMapped(preference, ::unsafeGetBoolean)
    }

    /**
     * Unsafe writes/reads (do not do check type of Property before writing, use with caution!)
     */
    private fun unsafeWriteString(key: String, newState: String?, editor: PrefEditor?) {
        return write(editor) { putString(key, newState) }
    }

    private fun unsafeWriteInt(key: String, newState: Int, editor: PrefEditor?) {
        return write(editor) { putInt(key, newState) }
    }

    private fun unsafeWriteLong(key: String, newState: Long, editor: PrefEditor?) {
        return write(editor) { putLong(key, newState) }
    }

    private fun unsafeWriteBoolean(key: String, newState: Boolean, editor: PrefEditor?) {
        return write(editor) { putBoolean(key, newState) }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    public fun write(editor: PrefEditor?, action: PreferenceEditAction) {
        if (editor != null) action(editor)
        else editor(action)
    }

    private fun unsafeGetString(key: String, default: String?): String? {
        return preferences.getString(key, default)
    }

    private fun unsafeGetInt(key: String, default: Int?): Int {
        return preferences.getInt(key, default!!)
    }

    private fun unsafeGetLong(key: String, default: Long?): Long {
        return preferences.getLong(key, default!!)
    }

    private fun unsafeGetBoolean(key: String, default: Boolean?): Boolean {
        return preferences.getBoolean(key, default!!)
    }

    /**
     * Utils
     */
    private fun <T : Any, M : Any> getValueFromMapped(preference: MappedPreference<T, M>, reader: KeyReader<M?>, ): T {
        val mappedValue = reader(preference.key, preference.defaultMapped)!!
        return preference.read(mappedValue) ?: preference.default
    }
}

public typealias KeyReader<T> = (String, T) -> T
