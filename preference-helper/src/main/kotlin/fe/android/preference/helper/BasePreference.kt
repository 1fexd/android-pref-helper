package fe.android.preference.helper

import kotlin.reflect.KClass

sealed class BasePreference<T : Any, NT>(
    val key: String,
    val default: NT,
    val clazz: KClass<T>
) {
    class PreferenceNullable<T : Any> (
        key: String,
        default: T?,
        clazz: KClass<T>
    ) : BasePreference<T, T?>(key, default, clazz)

    class Preference<T : Any>(
        key: String,
        default: T,
        clazz: KClass<T>
    ) : BasePreference<T, T>(key, default, clazz)

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
    }

    class InitPreference<T : Any>(
        key: String,
        val initial: () -> T,
        clazz: KClass<T>
    ) : BasePreference<T, T?>(key, null, clazz)
}