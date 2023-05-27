package fe.android.preference.helper

typealias Reader<M, T> = (M) -> T?
typealias Persister<T, M> = (T) -> M

interface TypeMapper<T, M> {
    val reader: Reader<M, T>
    val persister: Persister<T, M>
}

abstract class OptionTypeMapper<T, M>(key: (T) -> M, options: () -> Array<T>) : TypeMapper<T, M> {
    private val readerOptions by lazy {
        options().associateBy { key(it) }
    }

    override val reader: Reader<M, T> = { readerOptions[it] }
    override val persister: Persister<T, M> = key
}

abstract class EnumTypeMapper<T : Enum<T>>(values: Array<T>) : TypeMapper<T, Int> {
    override val reader: Reader<Int, T> = { values[it] }
    override val persister: Persister<T, Int> = { it.ordinal }
}