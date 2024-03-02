package fe.android.preference.helper

public abstract class PreferenceDefinition(
    private val blacklistedKeys: MutableSet<String> = mutableSetOf()
) {
    private val registeredPreferences = mutableMapOf<String, Preference<*, *>>()

    public val all: Map<String, Preference<*, *>>
        get() = registeredPreferences

    public fun getAsKeyValuePairList(repository: PreferenceRepository): List<String> {
        return registeredPreferences.map { (key, value) ->
            "${key}=${repository.getAnyAsString(value)}"
        }
    }

    public fun addToBlacklist(vararg keys: String) {
        blacklistedKeys.addAll(keys)
    }

    public fun <T : Preference<*, *>> add(preference: T): T {
        if (preference.key in blacklistedKeys) {
            throw Exception("The key '${preference.key}' is blacklisted and must not be used!")
        }

        if (registeredPreferences.containsKey(preference.key)) {
            throw Exception("The key '${preference.key}' is already in use for a preference!")
        }

        registeredPreferences[preference.key] = preference
        return preference
    }

    public fun booleanPreference(key: String, default: Boolean = false): Preference.Boolean {
        return add(Preference.Boolean(key, default))
    }

    public fun stringPreference(key: String, default: String? = null): Preference.Nullable<String> {
        return add(Preference.Nullable(key, default, String::class))
    }

    public fun intPreference(key: String, default: Int = 0): Preference.Int {
        return add(Preference.Int(key, default))
    }

    public fun longPreference(key: String, default: Long = 0L): Preference.Long {
        return add(Preference.Long(key, default))
    }

    public inline fun <reified T : Any, reified M : Any> mappedPreference(
        key: String,
        default: T,
        mapper: TypeMapper<T, M>
    ): Preference.Mapped<T, M> {
        return add(Preference.Mapped(key, default, mapper, T::class, M::class))
    }

    public fun stringPreference(key: String, initial: () -> String): Preference.Init<String> {
        return add(Preference.Init(key, initial, String::class))
    }
}
