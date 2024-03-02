package fe.android.preference.helper.compose

import fe.android.preference.helper.Preference

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

    public fun <T : Any, NT, P : Preference<T, NT>> getOrPut(
        preference: P,
        writer: (P, NT) -> Unit,
        reader: (P) -> NT
    ): MutablePreferenceState<*, *, *> {
        return map.getOrPut(preference.key) { MutablePreferenceState(preference, writer, reader) }
    }

    public fun get(key: String): MutablePreferenceState<*, *, *>? {
        return map[key]
    }

    public fun getAll(): MutableMap<String, MutablePreferenceState<*, *, *>> {
        return map
    }
}
