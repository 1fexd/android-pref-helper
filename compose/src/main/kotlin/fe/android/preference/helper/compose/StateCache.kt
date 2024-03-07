package fe.android.preference.helper.compose

public class StateCache(
    private val map: MutableMap<String, MutablePreferenceState<*, *, *>> = mutableMapOf()
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

    internal fun put(key: String, state: MutablePreferenceState<*, *, *>) {
        map[key] = state
    }

    public fun get(key: String): MutablePreferenceState<*, *, *>? {
        return map[key]
    }

    public fun getAll(): MutableMap<String, MutablePreferenceState<*, *, *>> {
        return map
    }


}
