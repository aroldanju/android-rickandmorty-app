package es.aroldan.rickandmorty.presentation.screen.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import es.aroldan.rickandmorty.presentation.R
import es.aroldan.rickandmorty.presentation.databinding.FragmentCharacterDetailBinding
import es.aroldan.rickandmorty.presentation.model.CharacterView
import es.aroldan.rickandmorty.presentation.model.Event
import es.aroldan.rickandmorty.presentation.model.LocationView
import es.aroldan.rickandmorty.presentation.model.UiState
import es.aroldan.rickandmorty.presentation.util.BaseFragment
import es.aroldan.rickandmorty.presentation.util.toString

@AndroidEntryPoint
class CharacterDetailFragment: BaseFragment<CharacterDetailViewModel, FragmentCharacterDetailBinding>() {

    override fun getLayout(): Int = R.layout.fragment_character_detail

    override fun onStart() {
        super.onStart()
        viewModel.started()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collect(viewModel.events, ::handleEvent)
        collect(viewModel.uiState, ::handleUiState)

        binding.fragmentCharacterDetailFav.setOnClickListener {
            viewModel.toggleFavourite()
        }
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.Navigate -> {
                router.navigate(event.screenNavigation)
            }
            is Event.ShowError -> {
                Toast.makeText(requireContext(), event.definedError.toString(requireContext()), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleUiState(uiState: UiState<Pair<CharacterView, LocationView?>>) {
        when (uiState) {
            is UiState.Loading -> {
                with (binding) {
                    fragmentCharacterDetailProgress.visibility = View.VISIBLE
                    fragmentCharacterDetailName.visibility = View.GONE
                    fragmentCharacterDetailError.visibility = View.GONE
                }
            }
            is UiState.Error -> {
                binding.fragmentCharacterDetailProgress.visibility = View.GONE
                binding.fragmentCharacterDetailName.visibility = View.GONE
                binding.fragmentCharacterDetailError.apply {
                    visibility = View.VISIBLE
                    text = uiState.definedError.toString(requireContext())
                }
            }
            is UiState.Content -> {
                binding.fragmentCharacterDetailToolbar.toolbar.title = uiState.data.first.name

                binding.character = uiState.data.first
                binding.location = uiState.data.second

                binding.fragmentCharacterDetailProgress.visibility = View.GONE
                binding.fragmentCharacterDetailName.visibility = View.VISIBLE
                binding.fragmentCharacterDetailError.visibility = View.GONE
            }
            else -> {

            }
        }
    }
}
