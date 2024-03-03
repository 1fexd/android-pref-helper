package fe.android.preference.helper

public typealias Unmapper<M, T> = (M) -> T?
public typealias Mapper<T, M> = (T) -> M

public interface TypeMapper<T, M> {
    public val unmap: Unmapper<M, T>
    public val map: Mapper<T, M>
}

public abstract class OptionTypeMapper<T, M>(key: (T) -> M, options: () -> Array<T>) : TypeMapper<T, M> {
    private val readerOptions by lazy {
        options().associateBy { key(it) }
    }

    override val unmap: Unmapper<M, T> = { readerOptions[it] }
    override val map: Mapper<T, M> = key
}

public abstract class EnumTypeMapper<T : Enum<T>>(values: Array<T>) : TypeMapper<T, Int> {
    override val unmap: Unmapper<Int, T> = { values[it] }
    override val map: Mapper<T, Int> = { it.ordinal }
}
