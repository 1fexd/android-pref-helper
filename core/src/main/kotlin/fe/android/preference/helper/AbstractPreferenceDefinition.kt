package fe.android.preference.helper

import kotlin.reflect.KClass

public abstract class AbstractPreferenceDefinition {
    protected open fun <T : Preference<*, *>> add(preference: T): T = preference

    protected open fun <T : Preference<*, *>> addMigration(
        preference: T,
        run: (PreferenceRepository) -> Unit,
    ): T = preference

    public open fun migrate(repository: PreferenceRepository): Unit = Unit

    protected open fun boolean(key: String, default: Boolean = false): Preference.Boolean {
        return add(Preference.Boolean(key, default))
    }

    public fun Preference.Boolean.migrate(fn: (PreferenceRepository, Boolean) -> Unit): Preference.Boolean {
        return addMigration(this) { fn(it, it.get(this)) }
    }

    protected fun string(key: String, default: String? = null): Preference.Nullable<String> {
        return add(Preference.Nullable(key, default, String::class))
    }

    public fun Preference.Nullable<String>.migrate(fn: (PreferenceRepository, String?) -> Unit): Preference.Nullable<String> {
        return addMigration(this) { fn(it, it.get(this)) }
    }

    protected fun int(key: String, default: Int = 0): Preference.Int {
        return add(Preference.Int(key, default))
    }

    public fun Preference.Int.migrate(fn: (PreferenceRepository, Int) -> Unit): Preference.Int {
        return addMigration(this) { fn(it, it.get(this)) }
    }

    protected fun long(key: String, default: Long = 0L): Preference.Long {
        return add(Preference.Long(key, default))
    }

    public fun Preference.Long.migrate(fn: (PreferenceRepository, Long) -> Unit): Preference.Long {
        return addMigration(this) { fn(it, it.get(this)) }
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

    @JvmName("migrateMappedInt")
    public fun <T : Any> Preference.Mapped<T, Int>.migrate(fn: (PreferenceRepository, T) -> Unit): Preference.Mapped<T, Int> {
        return addMigration(this) { fn(it, it.get(this)) }
    }

    @JvmName("migrateMappedLong")
    public fun <T : Any> Preference.Mapped<T, Long>.migrate(fn: (PreferenceRepository, T) -> Unit): Preference.Mapped<T, Long> {
        return addMigration(this) { fn(it, it.get(this)) }
    }

    @JvmName("migrateMappedString")
    public fun <T : Any> Preference.Mapped<T, String>.migrate(fn: (PreferenceRepository, T) -> Unit): Preference.Mapped<T, String> {
        return addMigration(this) { fn(it, it.get(this)) }
    }

    protected fun string(key: String, initial: () -> String): Preference.Init {
        return add(Preference.Init(key, initial))
    }

    public fun Preference.Init.migrate(fn: (PreferenceRepository, String) -> Unit): Preference.Init {
        return addMigration(this) { fn(it, it.getOrPutInit(this)) }
    }
}
