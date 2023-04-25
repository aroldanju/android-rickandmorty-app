package es.aroldan.rickandmorty.presentation.screen.splash

import dagger.hilt.android.AndroidEntryPoint
import es.aroldan.rickandmorty.presentation.R
import es.aroldan.rickandmorty.presentation.databinding.FragmentSplashBinding
import es.aroldan.rickandmorty.presentation.model.Event
import es.aroldan.rickandmorty.presentation.util.BaseFragment
import es.aroldan.rickandmorty.presentation.util.fadeIn

@AndroidEntryPoint
class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {

    override fun getLayout(): Int = R.layout.fragment_splash

    override fun onStart() {
        super.onStart()

        binding.fragmentSplashTitle.fadeIn(1500) {
            viewModel.animationFinished()
        }

        collect(viewModel.events) { event ->
            when (event) {
                is Event.Navigate -> {
                    router.navigate(event.screenNavigation)
                }
                else -> {}
            }
        }
    }
}
