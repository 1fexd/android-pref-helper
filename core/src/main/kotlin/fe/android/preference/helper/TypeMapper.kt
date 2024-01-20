package fe.android.preference.helper

public typealias Reader<M, T> = (M) -> T?
public typealias Persister<T, M> = (T) -> M

public interface TypeMapper<T, M> {
    public val reader: Reader<M, T>
    public val persister: Persister<T, M>
}

public abstract class OptionTypeMapper<T, M>(key: (T) -> M, options: () -> Array<T>) : TypeMapper<T, M> {
    private val readerOptions by lazy {
        options().associateBy { key(it) }
    }

    override val reader: Reader<M, T> = { readerOptions[it] }
    override val persister: Persister<T, M> = key
}

public abstract class EnumTypeMapper<T : Enum<T>>(values: Array<T>) : TypeMapper<T, Int> {
    override val reader: Reader<Int, T> = { values[it] }
    override val persister: Persister<T, Int> = { it.ordinal }
}
