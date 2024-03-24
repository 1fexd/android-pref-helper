package fe.android.preference.helper

import kotlin.reflect.KClass

internal class PreferenceDefinitionException(msg: String) : Exception(msg)

public abstract class PreferenceDefinition(
    vararg blacklistedKeys: String,
) : AbstractPreferenceDefinition() {

    private val blacklistedKeys = mutableSetOf(*blacklistedKeys)
    private val registeredPreferences = mutableMapOf<String, Preference<*, *>>()
    private val migratePreferences = mutableMapOf<String, (PreferenceRepository) -> Unit>()

    private var finalized = false

    @Suppress("MemberVisibilityCanBePrivate")
    public val all: Map<String, Preference<*, *>>
        get() {
            if (!finalized) definitionError("Preferences must not be read until definition has been finalized!")
            return registeredPreferences
        }

    private val migrate: MutableMap<String, (PreferenceRepository) -> Unit>
        get() {
            return migratePreferences
        }

    override fun migrate(repository: PreferenceRepository) {
        for ((key, migrateFn) in migrate) {
            if (repository.hasStoredValue(key)) migrateFn(repository)
        }
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

        if (preference.key in registeredPreferences) {
            definitionError("The key '${preference.key}' is already in use for a preference!")
        }

        registeredPreferences[preference.key] = preference
        return preference
    }

    override fun <T : Preference<*, *>> addMigration(preference: T, run: (PreferenceRepository) -> Unit): T {
        if (finalized) {
            definitionError("Cannot migrate '${preference.key}' as definition has already been finalized!")
        }

        if (preference.key in migratePreferences) {
            definitionError("Preference '${preference.key}' already has a migration strategy!")
        }

        migratePreferences[preference.key] = run
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
        m: KClass<M>,
    ): Preference.Mapped<T, M> = mapped(key, default, mapper, t, m)

    private fun definitionError(msg: String): Nothing = throw PreferenceDefinitionException(msg)

    protected fun finalize() {
        if (finalized) definitionError("Definition has already been finalized!")
        finalized = true
    }
}
