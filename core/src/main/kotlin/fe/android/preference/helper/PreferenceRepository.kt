package fe.android.preference.helper

import android.content.Context
import android.content.SharedPreferences

public typealias PreferenceEditAction = SharedPreferences.Editor.() -> Unit

public abstract class PreferenceRepository(context: Context, fileName: String = "preferences") : PreferenceEditor() {

    private val preferences by lazy {
        context.getSharedPreferences(context.packageName + "_$fileName", Context.MODE_PRIVATE)
    }

    public fun edit(action: Scope.() -> Unit) {
        withEditor { Scope(this).apply(action) }
    }

    override fun withEditor(action: PreferenceEditAction) {
        preferences.edit().apply(action).apply()
    }

    @OptIn(UnsafePreferenceInteraction::class)
    public fun setStringValueToPreference(preference: Preference<*, *>, value: String) {
        val mapped = preference as? Preference.Mapped<*, *>
        val type = mapped?.mappedClazz ?: preference.type

        when (type) {
            String::class -> unsafePut(preference.key, value)
            Boolean::class -> unsafePut(preference.key, value.toBooleanStrict())
            Int::class -> unsafePut(preference.key, value.toInt())
            Long::class -> unsafePut(preference.key, value.toLong())
        }
    }

    @OptIn(UnsafePreferenceInteraction::class)
    public fun getAnyAsString(preference: Preference<*, *>): String? {
        if (preference is Preference.Mapped<*, *>) {
            @Suppress("UNCHECKED_CAST")
            return when (preference.mappedClazz) {
                String::class -> get(preference as Preference.Mapped<*, String>)
                Boolean::class -> get(preference as Preference.Mapped<*, Boolean>)
                Int::class -> get(preference as Preference.Mapped<*, Int>)
                Long::class -> get(preference as Preference.Mapped<*, Long>)
                else -> null
            }?.toString()
        }

        if (preference is Preference.Init) {
            return unsafeGetString(preference.key, preference.default)
        }

        return when (preference.type) {
            String::class -> unsafeGetString(preference.key, preference.default as String?)
            Boolean::class -> unsafeGetBoolean(preference.key, preference.default as Boolean)
            Int::class -> unsafeGetInt(preference.key, preference.default as Int)
            Long::class -> unsafeGetLong(preference.key, preference.default as Long)
            else -> null
        }?.toString()
    }


    @JvmName("getString")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun get(preference: Preference<String, String?>): String? {
        return unsafeGetString(preference.key, preference.default)
    }

    @OptIn(UnsafePreferenceInteraction::class)
    public fun getOrPutInit(preference: Preference.Init): String {
        val value = unsafeGetString(preference.key, null)
        return if (value == null) {
            val initial = preference.initial()
            unsafePut(preference.key, initial)

            return initial
        } else value
    }

    @JvmName("getMappedByString")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> get(preference: Preference.Mapped<T, String>): T {
        return getValueFromMapped(preference, ::unsafeGetString)
    }

    @JvmName("getInt")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun get(preference: Preference.Default<Int>): Int {
        return unsafeGetInt(preference.key, preference.default)
    }

    @JvmName("getMappedByInt")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> get(preference: Preference.Mapped<T, Int>): T {
        return getValueFromMapped(preference, ::unsafeGetInt)
    }

    @JvmName("getLong")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun get(preference: Preference.Default<Long>): Long {
        return unsafeGetLong(preference.key, preference.default)
    }

    @JvmName("getMappedByLong")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> get(preference: Preference.Mapped<T, Long>): T {
        return getValueFromMapped(preference, ::unsafeGetLong)
    }

    @JvmName("getBoolean")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun get(preference: Preference.Default<Boolean>): Boolean {
        return unsafeGetBoolean(preference.key, preference.default)
    }

    @JvmName("getMappedByBoolean")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> get(preference: Preference.Mapped<T, Boolean>): T {
        return getValueFromMapped(preference, ::unsafeGetBoolean)
    }

    @UnsafePreferenceInteraction
    private fun unsafeGetString(key: String, default: String?): String? {
        return tryUnsafeGet(preferences, default) { pref, def -> pref.getString(key, def) }
    }

    @UnsafePreferenceInteraction
    private fun unsafeGetInt(key: String, default: Int): Int {
        return tryUnsafeGet(preferences, default) { pref, def -> pref.getInt(key, def) }!!
    }

    @UnsafePreferenceInteraction
    private fun unsafeGetLong(key: String, default: Long): Long {
        return tryUnsafeGet(preferences, default) { pref, def -> pref.getLong(key, def) }!!
    }

    @UnsafePreferenceInteraction
    private fun unsafeGetBoolean(key: String, default: Boolean): Boolean {
        return tryUnsafeGet(preferences, default) { pref, def -> pref.getBoolean(key, def) }!!
    }

    private inline fun <T : Any?> tryUnsafeGet(
        preferences: SharedPreferences,
        default: T,
        get: (SharedPreferences, T) -> T?
    ): T? {
        return runCatching { get(preferences, default) }.getOrDefault(default)
    }

    /**
     * Utils
     */
    private inline fun <T : Any, M : Any> getValueFromMapped(
        preference: Preference.Mapped<T, M>,
        get: KeyReader<M>
    ): T {
        val mappedValue = get(preference.key, preference.defaultMapped)!!
        return preference.unmap(mappedValue) ?: preference.default
    }
}

public typealias KeyReader<T> = (String, T) -> T?
