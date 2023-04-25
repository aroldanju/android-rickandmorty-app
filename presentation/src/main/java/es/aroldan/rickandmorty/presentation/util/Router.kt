package es.aroldan.rickandmorty.presentation.util

import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import es.aroldan.rickandmorty.presentation.model.ScreenNavigation

class Router(private val fragment: Fragment) {

    fun navigate(screenNavigation: ScreenNavigation) {
        when (screenNavigation) {
            is ScreenNavigation.Navigate -> {
                fragment.findNavController().also {
                    if (screenNavigation.popBackStack) {
                        it.popBackStack()
                    }

                    val url = "${Constant.Deeplink.AUTHORITY}://${screenNavigation.deeplink}"

                    it.navigate(
                        NavDeepLinkRequest.Builder.fromUri(url.toUri())
                            .build()
                    )
                }
            }
            is ScreenNavigation.Back -> {
                // TODO
                fragment.findNavController().popBackStack()
            }
            else -> {

            }
        }
    }
}