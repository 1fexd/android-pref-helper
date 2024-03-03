package fe.android.preference.helper

import android.content.SharedPreferences

public sealed class PreferenceEditor {
    @JvmName("putMappedToString")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> put(preference: Preference.Mapped<T, String>, newState: T) {
        unsafePut(preference.key, preference.map(newState))
    }

    @OptIn(UnsafePreferenceInteraction::class)
    public fun put(preference: Preference.Nullable<String>, newState: String?) {
        unsafePut(preference.key, newState)
    }

    @JvmName("putMappedToBoolean")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> put(preference: Preference.Mapped<T, Boolean>, newState: T) {
        unsafePut(preference.key, preference.map(newState))
    }

    @OptIn(UnsafePreferenceInteraction::class)
    public fun put(preference: Preference.Default<Boolean>, newState: Boolean) {
        unsafePut(preference.key, newState)
    }

    @JvmName("putMappedToLong")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> put(preference: Preference.Mapped<T, Long>, newState: T) {
        unsafePut(preference.key, preference.map(newState))
    }

    @OptIn(UnsafePreferenceInteraction::class)
    public fun put(preference: Preference.Default<Long>, newState: Long) {
        unsafePut(preference.key, newState)
    }

    @JvmName("putMappedToInt")
    @OptIn(UnsafePreferenceInteraction::class)
    public fun <T : Any> put(preference: Preference.Mapped<T, Int>, newState: T) {
        unsafePut(preference.key, preference.map(newState))
    }


    @OptIn(UnsafePreferenceInteraction::class)
    public fun put(preference: Preference.Default<Int>, newState: Int) {
        unsafePut(preference.key, newState)
    }

    /**
     * Unsafe writes/reads (do not do check type of Property before writing, use with caution!)
     */
    @UnsafePreferenceInteraction
    protected fun unsafePut(key: String, newState: String?) {
        return withEditor { putString(key, newState) }
    }

    @UnsafePreferenceInteraction
    protected fun unsafePut(key: String, newState: Int) {
        return withEditor { putInt(key, newState) }
    }

    @UnsafePreferenceInteraction
    protected fun unsafePut(key: String, newState: Long) {
        return withEditor { putLong(key, newState) }
    }

    @UnsafePreferenceInteraction
    protected fun unsafePut(key: String, newState: Boolean) {
        return withEditor { putBoolean(key, newState) }
    }

    protected abstract fun withEditor(action: PreferenceEditAction)

    public class Scope(private val editor: SharedPreferences.Editor) : PreferenceEditor() {
        override fun withEditor(action: PreferenceEditAction) {
            editor.apply(action)
        }
    }
}



