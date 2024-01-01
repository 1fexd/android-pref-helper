package fe.android.preference.helper

public abstract class Preferences {
    private val registeredPreferences = mutableMapOf<String, BasePreference<*, *>>()

    public val all: Map<String, BasePreference<*, *>>
        get() = registeredPreferences

    public fun getAsKeyValuePairList(repository: PreferenceRepository): List<String> {
        return registeredPreferences.map { (key, value) ->
            "${key}=${repository.getAnyAsString(value)}"
        }
    }

    public fun <T : BasePreference<*, *>> add(preference: T): T {
        if (registeredPreferences[preference.key] != null) error("This key has already been used")
        registeredPreferences[preference.key] = preference

        return preference
    }

    public fun booleanPreference(key: String, default: Boolean = false): Preference<Boolean> {
        return add(Preference(key, default, Boolean::class))
    }

    public fun stringPreference(key: String, default: String? = null): PreferenceNullable<String> {
        return add(PreferenceNullable(key, default, String::class))
    }

    public fun intPreference(key: String, default: Int = 0): Preference<Int> {
        return add(Preference(key, default, Int::class))
    }

    public fun longPreference(key: String, default: Long = 0L): Preference<Long> {
        return add(Preference(key, default, Long::class))
    }

    public inline fun <reified T : Any, reified M : Any> mappedPreference(
        key: String,
        default: T,
        mapper: TypeMapper<T, M>
    ): MappedPreference<T, M> {
        return add(MappedPreference(key, default, mapper, T::class, M::class))
    }

    public inline fun <reified T : Any> stringPreference(key: String, noinline initial: () -> T): InitPreference<T> {
        return add(InitPreference(key, initial, T::class))
    }
}
