package fe.android.preference.helper

import kotlin.reflect.KClass

internal class PreferenceDefinitionException(msg: String) : Exception(msg)

public abstract class PreferenceDefinition(
    vararg blacklistedKeys: String
) : AbstractPreferenceDefinition() {

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

    protected override fun <T : Preference<*, *>> add(preference: T): T {
        if (finalized) {
            definitionError("Cannot register preference '${preference.key}' as definition has already been finalized!")
        }

        if (preference.key in blacklistedKeys) {
            definitionError("The key '${preference.key}' is blacklisted and must not be used!")
        }

        if(preference.key in registeredPreferences){
            definitionError("The key '${preference.key}' is already in use for a preference!")
        }

        registeredPreferences[preference.key] = preference
        return preference
    }

    public inline fun <reified T : Any, reified M : Any> mapped(
        key: String,
        default: T,
        mapper: TypeMapper<T, M>,
    ): Preference.Mapped<T, M> {
        return `access$mapped`(key, default, mapper, T::class, M::class)
    }

    @PublishedApi
    internal fun <T : Any, M : Any> `access$mapped`(
        key: String,
        default: T,
        mapper: TypeMapper<T, M>,
        t: KClass<T>,
        m: KClass<M>
    ): Preference.Mapped<T, M> = mapped(key, default, mapper, t, m)

    private fun definitionError(msg: String): Nothing = throw PreferenceDefinitionException(msg)

    protected fun finalize() {
        if (finalized) definitionError("Definition has already been finalized!")
        finalized = true
    }
}
