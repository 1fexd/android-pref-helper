package fe.android.preference.helper

import kotlin.reflect.KClass

public abstract class AbstractPreferenceDefinition {
    protected open fun <T : Preference<*, *>> add(preference: T): T = preference

    protected open fun boolean(key: String, default: Boolean = false): Preference.Boolean {
        return add(Preference.Boolean(key, default))
    }

    protected fun string(key: String, default: String? = null): Preference.Nullable<String> {
        return add(Preference.Nullable(key, default, String::class))
    }

    protected fun int(key: String, default: Int = 0): Preference.Int {
        return add(Preference.Int(key, default))
    }

    protected fun long(key: String, default: Long = 0L): Preference.Long {
        return add(Preference.Long(key, default))
    }

    protected fun <T : Any, M : Any> mapped(
        key: String,
        default: T,
        mapper: TypeMapper<T, M>,
        t: KClass<T>,
        m: KClass<M>
    ): Preference.Mapped<T, M> {
        return add(Preference.Mapped(key, default, mapper, t, m))
    }

    protected fun string(key: String, initial: () -> String): Preference.Init {
        return add(Preference.Init(key, initial))
    }
}
