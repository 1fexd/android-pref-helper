package fe.android.preference.helper

import kotlin.reflect.KClass

public sealed class Preference<T : Any, NT> protected constructor(
    public val key: String,
    public val default: NT,
    @Suppress("MemberVisibilityCanBePrivate")
    public val clazz: KClass<T>
) {
    internal open val def: Any? = default
    internal open val type: KClass<*> = clazz

    // TODO: Should these be internal (how can they then be accessed from compose-mock?)
    public class Nullable<T : Any> constructor(
        key: String,
        default: T?,
        clazz: KClass<T>
    ) : Preference<T, T?>(key, default, clazz)

    public open class Default<T : Any> constructor(
        key: String,
        default: T,
        clazz: KClass<T>
    ) : Preference<T, T>(key, default, clazz)

    public class Boolean constructor(
        key: String,
        default: kotlin.Boolean
    ) : Default<kotlin.Boolean>(key, default, kotlin.Boolean::class)

    public class Int constructor(
        key: String,
        default: kotlin.Int
    ) : Default<kotlin.Int>(key, default, kotlin.Int::class)

    public class Long constructor(
        key: String,
        default: kotlin.Long
    ) : Default<kotlin.Long>(key, default, kotlin.Long::class)

//    @PublishedApi internal
    public class Mapped<T : Any, M : Any>  constructor(
        key: String, default: T, private val mapper: TypeMapper<T, M>,
        clazz: KClass<T>, public val mappedClazz: KClass<M>,
    ) : Preference<T, T>(key, default, clazz) {
        public val defaultMapped: M = map(default)

        override val def: M = defaultMapped

        public fun unmap(mapped: M): T? = mapper.unmap(mapped)

        @Suppress("MemberVisibilityCanBePrivate")
        public fun map(value: T): M = mapper.map(value)
    }

    public class Init<T : Any> constructor(
        key: String, public val initial: () -> T, clazz: KClass<T>
    ) : Preference<T, T?>(key, null, clazz)

    override fun equals(other: Any?): kotlin.Boolean {
        return (other != null && other::class == this::class) && (other as? Preference<*, *>)?.key == key
    }

    override fun hashCode(): kotlin.Int {
        return key.hashCode()
    }
}
