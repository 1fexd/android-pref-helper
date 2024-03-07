package fe.android.preference.helper

internal class PreferenceDefinitionException(msg: String) : Exception(msg)

public abstract class PreferenceDefinition(vararg blacklistedKeys: String) {
    private val blacklistedKeys = mutableSetOf(*blacklistedKeys)
    private val registeredPreferences = mutableMapOf<String, Preference<*, *>>()

    private var finalized = false

    @Suppress("MemberVisibilityCanBePrivate")
    public val all: Map<String, Preference<*, *>>
        get() {
            if (!finalized) definitionError("Preferences must not be read until definition has been finalized!")
            return registeredPreferences
        }

    public fun getAsKeyValuePairList(repository: PreferenceRepository): List<String> {
        return all.map { (key, value) ->
            "${key}=${repository.getAnyAsString(value)}"
        }
    }

    protected fun <T : Preference<*, *>> add(preference: T): T {
        if (finalized) {
            definitionError("Cannot register preference '${preference.key}' as definition has already been finalized!")
        }

        if (preference.key in blacklistedKeys) {
            definitionError("The key '${preference.key}' is blacklisted and must not be used!")
        }

        if (registeredPreferences.containsKey(preference.key)) {
            definitionError("The key '${preference.key}' is already in use for a preference!")
        }

        registeredPreferences[preference.key] = preference
        return preference
    }

    protected fun boolean(key: String, default: Boolean = false): Preference.Boolean {
        return add(Preference.Boolean(key, default))
    }

    @Deprecated(message = "Use simple API", replaceWith = ReplaceWith("boolean(key, default)"))
    protected fun booleanPreference(key: String, default: Boolean = false): Preference.Boolean {
        return boolean(key, default)
    }

    protected fun string(key: String, default: String? = null): Preference.Nullable<String> {
        return add(Preference.Nullable(key, default, String::class))
    }

    @Deprecated(message = "Use simple API", replaceWith = ReplaceWith("string(key, default)"))
    protected fun stringPreference(key: String, default: String? = null): Preference.Nullable<String> {
        return string(key, default)
    }

    protected fun int(key: String, default: Int = 0): Preference.Int {
        return add(Preference.Int(key, default))
    }

    @Deprecated(message = "Use simple API", replaceWith = ReplaceWith("int(key, default)"))
    protected fun intPreference(key: String, default: Int = 0): Preference.Int {
        return int(key, default)
    }

    protected fun long(key: String, default: Long = 0L): Preference.Long {
        return add(Preference.Long(key, default))
    }

    @Deprecated(message = "Use simple API", replaceWith = ReplaceWith("long(key, default)"))
    protected fun longPreference(key: String, default: Long = 0L): Preference.Long {
        return long(key, default)
    }

    protected inline fun <reified T : Any, reified M : Any> mapped(
        key: String,
        default: T,
        mapper: TypeMapper<T, M>
    ): Preference.Mapped<T, M> {
        return add(Preference.Mapped(key, default, mapper, T::class, M::class))
    }

    @Deprecated(message = "Use simple API", replaceWith = ReplaceWith("mapped(key, default)"))
    protected inline fun <reified T : Any, reified M : Any> mappedPreference(
        key: String,
        default: T,
        mapper: TypeMapper<T, M>
    ): Preference.Mapped<T, M> {
        return mapped(key, default, mapper)
    }

    protected fun string(key: String, initial: () -> String): Preference.Init<String> {
        return add(Preference.Init(key, initial, String::class))
    }

    @Deprecated(message = "Use simple API", replaceWith = ReplaceWith("string(key, initial)"))
    protected fun stringPreference(key: String, initial: () -> String): Preference.Init<String> {
        return string(key, initial)
    }

    private fun definitionError(msg: String): Nothing = throw PreferenceDefinitionException(msg)

    protected fun finalize() {
        if (finalized) definitionError("Definition has already been finalized!")
        finalized = true
    }
}
