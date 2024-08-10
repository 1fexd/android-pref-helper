package fe.android.preference.helper

import kotlin.reflect.KClass

public abstract class AbstractPreferenceDefinition {
    protected open fun <T : Preference<*, *>> add(preference: T): T = preference

    protected open fun <T : Preference<*, *>> addMigration(
        preference: T,
        run: (PreferenceRepository) -> Unit,
    ): T = preference

    public open fun runMigrations(repository: PreferenceRepository): Unit = Unit

    protected open fun boolean(key: String, default: Boolean = false): Preference.Boolean {
        return add(Preference.Boolean(key, default))
    }

    public fun Preference.Boolean.migrate(fn: (PreferenceRepository, Boolean) -> Unit): Preference.Boolean {
        return addMigration(this) { fn(it, it.get(this)) }
    }

    public fun Preference.Boolean.migrateTo(fn: (PreferenceRepository, Boolean) -> Boolean): Preference.Boolean {
        return addMigration(this) {
            val current = it.get(this)
            val new = fn(it, current)

            it.put(this, new)
        }
    }

    protected fun string(key: String, default: String? = null): Preference.Nullable<String> {
        return add(Preference.Nullable(key, default, String::class))
    }

    public fun Preference.Nullable<String>.migrate(fn: (PreferenceRepository, String?) -> Unit): Preference.Nullable<String> {
        return addMigration(this) { fn(it, it.get(this)) }
    }

    public fun Preference.Nullable<String>.migrateTo(fn: (PreferenceRepository, String?) -> String?): Preference.Nullable<String> {
        return addMigration(this) {
            val current = it.get(this)
            val new = fn(it, current)

            it.put(this, new)
        }
    }

    protected fun int(key: String, default: Int = 0): Preference.Int {
        return add(Preference.Int(key, default))
    }

    public fun Preference.Int.migrate(fn: (PreferenceRepository, Int) -> Unit): Preference.Int {
        return addMigration(this) { fn(it, it.get(this)) }
    }

    public fun Preference.Int.migrateTo(fn: (PreferenceRepository, Int) -> Int): Preference.Int {
        return addMigration(this) {
            val current = it.get(this)
            val new = fn(it, current)

            it.put(this, new)
        }
    }

    protected fun long(key: String, default: Long = 0L): Preference.Long {
        return add(Preference.Long(key, default))
    }

    public fun Preference.Long.migrate(fn: (PreferenceRepository, Long) -> Unit): Preference.Long {
        return addMigration(this) { fn(it, it.get(this)) }
    }

    public fun Preference.Long.migrateTo(fn: (PreferenceRepository, Long) -> Long): Preference.Long {
        return addMigration(this) {
            val current = it.get(this)
            val new = fn(it, current)

            it.put(this, new)
        }
    }

    protected fun <T : Any, M : Any> mapped(
        key: String,
        default: T,
        mapper: TypeMapper<T, M>,
        t: KClass<T>,
        m: KClass<M>,
    ): Preference.Mapped<T, M> {
        return add(Preference.Mapped(key, default, mapper, t, m))
    }

    @JvmName("migrateMappedBoolean")
    public fun <T : Any> Preference.Mapped<T, Boolean>.migrate(fn: (PreferenceRepository, T) -> Unit): Preference.Mapped<T, Boolean> {
        return addMigration(this) { fn(it, it.get(this)) }
    }

    @JvmName("migrateToMappedBoolean")
    public fun <T : Any> Preference.Mapped<T, Boolean>.migrateTo(fn: (PreferenceRepository, T) -> T): Preference.Mapped<T, Boolean> {
        return addMigration(this) {
            val current = it.get(this)
            val new = fn(it, current)

            it.put(this, new)
        }
    }

    @JvmName("migrateMappedInt")
    public fun <T : Any> Preference.Mapped<T, Int>.migrate(fn: (PreferenceRepository, T) -> Unit): Preference.Mapped<T, Int> {
        return addMigration(this) { fn(it, it.get(this)) }
    }

    @JvmName("migrateToMappedInt")
    public fun <T : Any> Preference.Mapped<T, Int>.migrateTo(fn: (PreferenceRepository, T) -> T): Preference.Mapped<T, Int> {
        return addMigration(this) {
            val current = it.get(this)
            val new = fn(it, current)

            it.put(this, new)
        }
    }

    @JvmName("migrateMappedLong")
    public fun <T : Any> Preference.Mapped<T, Long>.migrate(fn: (PreferenceRepository, T) -> Unit): Preference.Mapped<T, Long> {
        return addMigration(this) { fn(it, it.get(this)) }
    }

    @JvmName("migrateToMappedLong")
    public fun <T : Any> Preference.Mapped<T, Long>.migrateTo(fn: (PreferenceRepository, T) -> T): Preference.Mapped<T, Long> {
        return addMigration(this) {
            val current = it.get(this)
            val new = fn(it, current)

            it.put(this, new)
        }
    }

    @JvmName("migrateMappedString")
    public fun <T : Any> Preference.Mapped<T, String>.migrate(fn: (PreferenceRepository, T) -> Unit): Preference.Mapped<T, String> {
        return addMigration(this) { fn(it, it.get(this)) }
    }

    @JvmName("migrateToMappedString")
    public fun <T : Any> Preference.Mapped<T, String>.migrateTo(fn: (PreferenceRepository, T) -> T): Preference.Mapped<T, String> {
        return addMigration(this) {
            val current = it.get(this)
            val new = fn(it, current)

            it.put(this, new)
        }
    }

    protected fun string(key: String, initial: () -> String): Preference.Init {
        return add(Preference.Init(key, initial))
    }

    public fun Preference.Init.migrate(fn: (PreferenceRepository, String) -> Unit): Preference.Init {
        return addMigration(this) { fn(it, it.getOrPutInit(this)) }
    }
}
