# android-pref-helper

A wrapper for Android's SharedPreference API providing typed preferences. Also includes support for
custom types not supported by the SharedPreference API by mapping them to a supported type when
reading or writing.

Additionally, helper methods for Jetpack Compose are included which provide the preference
as `MutableState` (which enables composables to re-render when a the preference's value is updated)