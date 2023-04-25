package es.aroldan.rickandmorty.presentation.model

sealed interface ScreenNavigation {
    object None : ScreenNavigation
    data class Navigate(val deeplink: String, val popBackStack: Boolean = false, val clearStack: Boolean = false) : ScreenNavigation
    data class Back(val arguments: Map<String, Any>? = null) : ScreenNavigation
    data class Url(val url: String) : ScreenNavigation
}
