package fe.android.preference.helper

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KClass

public typealias PreferenceEditAction = SharedPreferences.Editor.() -> Unit
private typealias PreferenceEditor = SharedPreferences.Editor

public abstract class PreferenceRepository(context: Context, fileName: String = "preferences") {

    private val preferences by lazy {
        context.getSharedPreferences(context.packageName + "_$fileName", Context.MODE_PRIVATE)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    public fun editor(editor: PreferenceEditAction) {
        preferences.edit().apply(editor).apply()
    }

    @OptIn(UnsafePreferenceInteraction::class)
    public fun setStringValueToPreference(preference: Preference<*, *>, value: String, editor: PreferenceEditor?) {
        return when (preference.type) {
            String::class -> unsafeWriteString(preference.key, value, editor)
            Boolean::class -> unsafeWriteBoolean(preference.key, value.toBooleanStrict(), editor)
            Int::class -> unsafeWriteInt(preference.key, value.toInt(), editor)
            Long::class -> unsafeWriteLong(preference.key, value.toLong(), editor)
            else -> Unit
        }
    }

    @OptIn(UnsafePreferenceInteraction::class)
    public fun getAnyAsString(preference: Preference<*, *>): String? {
        return when (preference.type) {
            String::class -> unsafeGetString(preference.key, preference.def as String?)
            Boolean::class -> unsafeGetBoolean(preference.key, preference.def as Boolean?).toString()
            Int::class -> unsafeGetInt(preference.key, preference.def as Int?).toString()
            Long::class -> unsafeGetLong(preference.key, preference.def as Long?).toString()
            else -> null
        }
    }


    /**
     * String value operations
     */
    @OptIn(UnsafePreferenceInteraction::class)
    public fun writeString(
        preference: Preference.Nullable<String>,
        newState: String?,
        editor: PreferenceEditor? = null
    ) {
        unsafeWriteString(preference.key, newState, editor)
    }

    @OptIn(UnsafePreferenceInteraction::class)
    public fun getString(preference: Preference.Nullable<String>): String? {
        return unsafeGetString(preference.key, preference.default)
    }

    @OptIn(UnsafePreferenceInteraction::class)
    public fun getOrWriteInit(preference: Preference.Init<String>, editor: PreferenceEditor? = null): String {
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
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> write(
        preference: Preference.Mapped<T, String>,
        newState: T,
        editor: PreferenceEditor? = null
    ) {
        unsafeWriteString(preference.key, preference.write(newState), editor)
    }

    @JvmName("getMappedByString")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> get(preference: Preference.Mapped<T, String>): T {
        return getValueFromMapped(preference, ::unsafeGetString)
    }

    /**
     * Int value operations
     */
    @OptIn(UnsafePreferenceInteraction::class)
    public fun writeInt(preference: Preference.Default<Int>, newState: Int, editor: PreferenceEditor? = null) {
        unsafeWriteInt(preference.key, newState, editor)
    }

    @OptIn(UnsafePreferenceInteraction::class)
    public fun getInt(preference: Preference.Default<Int>): Int {
        return unsafeGetInt(preference.key, preference.default)
    }

    /**
     * Type to int value operations
     */
    @JvmName("writeMappedToInt")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> write(preference: Preference.Mapped<T, Int>, newState: T, editor: PreferenceEditor? = null) {
        unsafeWriteInt(preference.key, preference.write(newState), editor)
    }

    @JvmName("getMappedByInt")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> get(preference: Preference.Mapped<T, Int>): T {
        return getValueFromMapped(preference, ::unsafeGetInt)
    }

    /**
     * Long value operations
     */
    @OptIn(UnsafePreferenceInteraction::class)
    public fun writeLong(preference: Preference.Default<Long>, newState: Long, editor: PreferenceEditor? = null) {
        unsafeWriteLong(preference.key, newState, editor)
    }

    @OptIn(UnsafePreferenceInteraction::class)
    public fun getLong(preference: Preference.Default<Long>): Long {
        return unsafeGetLong(preference.key, preference.default)
    }

    /**
     * Type to long value operations
     */
    @JvmName("writeMappedToLong")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> write(preference: Preference.Mapped<T, Long>, newState: T, editor: PreferenceEditor? = null) {
        unsafeWriteLong(preference.key, preference.write(newState), editor)
    }

    @JvmName("getMappedByLong")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> get(preference: Preference.Mapped<T, Long>): T {
        return getValueFromMapped(preference, ::unsafeGetLong)
    }

    /**
     * Boolean value operations
     */
    @OptIn(UnsafePreferenceInteraction::class)
    public fun writeBoolean(
        preference: Preference.Default<Boolean>,
        newState: Boolean,
        editor: PreferenceEditor? = null
    ) {
        unsafeWriteBoolean(preference.key, newState, editor)
    }

    @OptIn(UnsafePreferenceInteraction::class)
    public fun getBoolean(preference: Preference.Default<Boolean>): Boolean {
        return unsafeGetBoolean(preference.key, preference.default)
    }

    /**
     * Type to boolean value operations
     */
    @JvmName("writeMappedToBoolean")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> write(
        preference: Preference.Mapped<T, Boolean>,
        newState: T,
        editor: PreferenceEditor? = null
    ) {
        unsafeWriteBoolean(preference.key, preference.write(newState), editor)
    }

    @JvmName("getMappedByBoolean")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> get(preference: Preference.Mapped<T, Boolean>): T {
        return getValueFromMapped(preference,  ::unsafeGetBoolean)
    }

    /**
     * Unsafe writes/reads (do not do check type of Property before writing, use with caution!)
     */
    @UnsafePreferenceInteraction
    private fun unsafeWriteString(key: String, newState: String?, editor: PreferenceEditor?) {
        return write(editor) { putString(key, newState) }
    }

    @UnsafePreferenceInteraction
    private fun unsafeWriteInt(key: String, newState: Int, editor: PreferenceEditor?) {
        return write(editor) { putInt(key, newState) }
    }

    @UnsafePreferenceInteraction
    private fun unsafeWriteLong(key: String, newState: Long, editor: PreferenceEditor?) {
        return write(editor) { putLong(key, newState) }
    }

    @UnsafePreferenceInteraction
    private fun unsafeWriteBoolean(key: String, newState: Boolean, editor: PreferenceEditor?) {
        return write(editor) { putBoolean(key, newState) }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    public fun write(editor: PreferenceEditor?, action: PreferenceEditAction) {
        if (editor != null) action(editor)
        else editor(action)
    }

    @UnsafePreferenceInteraction
    private fun unsafeGetString(key: String, default: String?): String? {
        return preferences.getString(key, default)
    }

    @UnsafePreferenceInteraction
    private fun unsafeGetInt(key: String, default: Int?): Int {
        return preferences.getInt(key, default!!)
    }

    @UnsafePreferenceInteraction
    private fun unsafeGetLong(key: String, default: Long?): Long {
        return preferences.getLong(key, default!!)
    }

    @UnsafePreferenceInteraction
    private fun unsafeGetBoolean(key: String, default: Boolean?): Boolean {
        return preferences.getBoolean(key, default!!)
    }

    /**
     * Utils
     */
    private fun <T : Any, M : Any> getValueFromMapped(preference: Preference.Mapped<T, M>, reader: KeyReader<M?>): T {
        val mappedValue = reader(preference.key, preference.defaultMapped)!!
        return preference.read(mappedValue) ?: preference.default
    }
}

public typealias KeyReader<T> = (String, T) -> T
