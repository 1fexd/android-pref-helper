package fe.android.preference.helper

import kotlin.reflect.KClass


public sealed class Preference<T : Any, NT>(
    public val key: String,
    public val default: NT,
    @Suppress("MemberVisibilityCanBePrivate")
    public val clazz: KClass<T>
) {
    internal open val def: Any? = default
    internal open val type: KClass<*> = clazz

    public class Nullable<T : Any>(key: String, default: T?, clazz: KClass<T>) : Preference<T, T?>(key, default, clazz)

    public class Default<T : Any>(key: String, default: T, clazz: KClass<T>) : Preference<T, T>(key, default, clazz)

    public class Mapped<T : Any, M : Any>(
        key: String, default: T, private val mapper: TypeMapper<T, M>,
        clazz: KClass<T>, public val mappedClazz: KClass<M>,
    ) : Preference<T, T>(key, default, clazz) {
        public val defaultMapped: M = write(default)

        override val def: M = defaultMapped

        public fun read(mapped: M): T? = mapper.reader(mapped)

        @Suppress("MemberVisibilityCanBePrivate")
        public fun write(value: T): M = mapper.writer(value)
    }

    public class Init<T : Any>(
        key: String, public val initial: () -> T, clazz: KClass<T>
    ) : Preference<T, T?>(key, null, clazz)

    override fun equals(other: Any?): Boolean {
        return (other != null && other::class == this::class) && (other as? Preference<*, *>)?.key == key
    }

    override fun hashCode(): Int {
        return key.hashCode()
    }
}
