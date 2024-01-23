package fe.android.preference.helper

import kotlin.reflect.KClass

public sealed class BasePreference<T : Any, NT>(
    public val key: String,
    public val default: NT,
    public val clazz: KClass<T>
)

public class PreferenceNullable<T : Any>(
    key: String,
    default: T?,
    clazz: KClass<T>
) : BasePreference<T, T?>(key, default, clazz)

public class Preference<T : Any>(
    key: String,
    default: T,
    clazz: KClass<T>
) : BasePreference<T, T>(key, default, clazz)

public class MappedPreference<T : Any, M : Any>(
    key: String,
    default: T,
    private val mapper: TypeMapper<T, M>,
    clazz: KClass<T>,
    public val mappedClazz: KClass<M>,
) : BasePreference<T, T>(key, default, clazz) {
    public val defaultMapped: M = persist(default)
    public fun read(mapped: M): T? = mapper.reader(mapped)
    @Suppress("MemberVisibilityCanBePrivate")
    public fun persist(value: T): M = mapper.writer(value)
}

public class InitPreference<T : Any>(
    key: String,
    public val initial: () -> T,
    clazz: KClass<T>
) : BasePreference<T, T?>(key, null, clazz)
