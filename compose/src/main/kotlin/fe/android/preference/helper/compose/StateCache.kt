package fe.android.preference.helper.compose

import fe.android.preference.helper.Preference

public class StateCache(
    private val map: MutableMap<String, RepositoryState<*, *, *>> = mutableMapOf()
) : AutoCloseable {

    public fun refresh() {
        map.forEach { (_, state) -> state.forceRefresh() }
    }

    public fun dispose() {
        close()
    }

    public override fun close() {
        map.clear()
    }

    public fun <T : Any, NT, P : Preference<T, NT>> getOrPut(
        preference: P,
        writer: (P, NT) -> Unit,
        reader: (P) -> NT
    ): RepositoryState<*, *, *> {
        return map.getOrPut(preference.key) { RepositoryState(preference, writer, reader) }
    }

    public fun get(key: String): RepositoryState<*, *, *>? {
        return map[key]
    }

    public fun getAll(): MutableMap<String, RepositoryState<*, *, *>> {
        return map
    }
}
