package fe.android.preference.helper

import kotlin.reflect.KClass

sealed class BasePreference<T : Any, NT> private constructor(
    val key: String,
    val default: NT,
    val clazz: KClass<T>
) {
    class PreferenceNullable<T : Any> private constructor(
        key: String,
        default: T?,
        clazz: KClass<T>
    ) : BasePreference<T, T?>(key, default, clazz) {
        companion object {
            fun stringPreference(
                key: String,
                default: String? = null
            ) = PreferenceNullable(key, default, String::class)
        }
    }

    class Preference<T : Any> private constructor(
        key: String,
        default: T,
        clazz: KClass<T>
    ) : BasePreference<T, T>(key, default, clazz) {
        companion object {
            fun booleanPreference(
                key: String,
                default: Boolean = false
            ) = Preference(key, default, Boolean::class)

            fun intPreference(
                key: String,
                default: Int = 0
            ) = Preference(key, default, Int::class)

            fun longPreference(
                key: String,
                default: Long = 0L
            ) = Preference(key, default, Long::class)
        }
    }

    class MappedPreference<T : Any, M : Any>(
        key: String,
        default: T,
        private val mapper: TypeMapper<T, M>,
        clazz: KClass<T>,
        val mappedClazz: KClass<M>,
    ) : BasePreference<T, T>(key, default, clazz) {
        val defaultMapped = persist(default)
        fun read(mapped: M) = mapper.reader(mapped)
        fun persist(value: T) = mapper.persister(value)

        companion object {
            inline fun <reified T : Any, reified M : Any> mappedPreference(
                key: String,
                default: T,
                mapper: TypeMapper<T, M>
            ) = MappedPreference(key, default, mapper, T::class, M::class)
        }
    }

    class InitPreference<T : Any>(
        key: String,
        val initial: () -> T,
        clazz: KClass<T>
    ) : BasePreference<T, T?>(key, null, clazz) {
        companion object {
            inline fun <reified T : Any> stringPreference(
                key: String,
                noinline initial: () -> T
            ) = InitPreference(key, initial, T::class)
        }
    }
}