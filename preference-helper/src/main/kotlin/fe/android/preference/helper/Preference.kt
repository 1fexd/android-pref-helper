package fe.android.preference.helper

sealed class BasePreference<T, NT> private constructor(val key: String, val default: NT) {
    class PreferenceNullable<T> private constructor(
        key: String,
        default: T?
    ) : BasePreference<T, T?>(key, default) {
        companion object {
            fun stringPreference(
                key: String,
                default: String? = null
            ) = PreferenceNullable(key, default)
        }
    }

    class Preference<T> private constructor(
        key: String,
        default: T
    ) : BasePreference<T, T>(key, default) {
        companion object {
            fun booleanPreference(
                key: String,
                default: Boolean = false
            ) = Preference(key, default)

            fun intPreference(
                key: String,
                default: Int = 0
            ) = Preference(key, default)

            fun longPreference(
                key: String,
                default: Long = 0L
            ) = Preference(key, default)
        }
    }

    class MappedPreference<T, M> private constructor(
        key: String, default: T, private val mapper: TypeMapper<T, M>
    ) : BasePreference<T, T>(key, default) {
        val defaultMapped = persist(default)
        fun read(mapped: M) = mapper.reader(mapped)
        fun persist(value: T) = mapper.persister(value)

        companion object {
            fun <T, M> mappedPreference(
                key: String,
                default: T,
                mapper: TypeMapper<T, M>
            ) = MappedPreference(key, default, mapper)
        }
    }

    class InitPreference<T> private constructor(
        key: String,
        val initial: () -> T
    ) : BasePreference<T, T?>(key, null) {
        companion object {
            fun <T> stringPreference(
                key: String,
                initial: () -> T
            ) = InitPreference(key, initial)
        }
    }
}