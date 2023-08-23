package fe.android.preference.helper

abstract class Preferences {
    private val registeredPreferences = mutableMapOf<String, BasePreference<*, *>>()

    val all: Map<String, BasePreference<*, *>>
        get() = registeredPreferences

    fun getAsKeyValuePairList(
        repository: PreferenceRepository
    ) = registeredPreferences.map { (key, value) ->
        "${key}=${repository.getAnyAsString(value)}"
    }

    protected fun <T : BasePreference<*, *>> add(preference: T): T {
        if (registeredPreferences[preference.key] != null) error("This key has already been used")
        registeredPreferences[preference.key] = preference

        return preference
    }

    protected fun booleanPreference(
        key: String,
        default: Boolean = false
    ) = add(BasePreference.Preference(key, default, Boolean::class))

    protected fun stringPreference(
        key: String,
        default: String? = null
    ) = add(BasePreference.PreferenceNullable(key, default, String::class))

    protected fun intPreference(
        key: String,
        default: Int = 0
    ) = add(BasePreference.Preference(key, default, Int::class))

    protected fun longPreference(
        key: String,
        default: Long = 0L
    ) = add(BasePreference.Preference(key, default, Long::class))

    protected inline fun <reified T : Any, reified M : Any> mappedPreference(
        key: String,
        default: T,
        mapper: TypeMapper<T, M>
    ) = add(BasePreference.MappedPreference(key, default, mapper, T::class, M::class))

    protected inline fun <reified T : Any> stringPreference(
        key: String,
        noinline initial: () -> T
    ) = add(BasePreference.InitPreference(key, initial, T::class))
}
