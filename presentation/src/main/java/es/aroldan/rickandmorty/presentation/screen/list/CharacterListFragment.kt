package es.aroldan.rickandmorty.presentation.screen.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import dagger.hilt.android.AndroidEntryPoint
import es.aroldan.rickandmorty.presentation.R
import es.aroldan.rickandmorty.presentation.databinding.FragmentCharacterListBinding
import es.aroldan.rickandmorty.presentation.model.CharacterView
import es.aroldan.rickandmorty.presentation.model.Event
import es.aroldan.rickandmorty.presentation.screen.list.adapter.CharacterViewAdapter
import es.aroldan.rickandmorty.presentation.util.BaseFragment
import es.aroldan.rickandmorty.presentation.util.toString

@AndroidEntryPoint
class CharacterListFragment : BaseFragment<CharacterListViewModel, FragmentCharacterListBinding>() {

    override fun getLayout(): Int = R.layout.fragment_character_list

    override fun onStart() {
        super.onStart()
        viewModel.onStarted()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.fragmentCharacterListToolbar.toolbar.apply {
            title = getString(R.string.characters)
            addMenuProvider(object: MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu_list, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                    when (menuItem.itemId) {
                        R.id.menu_list_action_show_favourites -> {
                            viewModel.toggleFavouriteFilter()
                            true
                        }
                        else -> {
                            false
                        }
                    }
            })
        }

        binding.fragmentCharacterListCharacters.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            setOnScrollChangeListener { _, _, _, _, _ ->
                val lastItem = (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val items = layoutManager?.itemCount ?: 0
                if (lastItem == items - 1) {
                    viewModel.loadNextPage()
                }
            }

            addOnScrollListener(object: OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        val layoutManager = (layoutManager as LinearLayoutManager)
                        val centerScrollPosition = (layoutManager.findLastCompletelyVisibleItemPosition() + layoutManager.findFirstCompletelyVisibleItemPosition()) / 2
                        viewModel.updateCurrentPosition(centerScrollPosition)
                    }
                }
            })
        }

        collect(viewModel.events, ::handleEvent)
        collect(viewModel.characters, ::handleCharacters)
        collect(viewModel.isLoading, ::handleLoading)
        collect(viewModel.currentPosition, ::handleCurrentPosition)
    }

    private fun handleCurrentPosition(position: Int) {
        (binding.fragmentCharacterListCharacters.layoutManager as LinearLayoutManager)
            .scrollToPosition(position)
    }

    private fun handleEvent(event: Event) {
        when (event) {
            is Event.ShowError -> {
                Toast.makeText(requireContext(), event.definedError.toString(requireContext()), Toast.LENGTH_LONG).show()
            }
            is Event.Navigate -> {
                router.navigate(event.screenNavigation)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleCharacters(characters: List<CharacterView>) {
        if (characters.isNotEmpty()) {
            val itemPosition = (binding.fragmentCharacterListCharacters.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            binding.fragmentCharacterListCharacters.adapter = CharacterViewAdapter(characters) {
                viewModel.characterClicked(it)
            }

            binding.fragmentCharacterListCharacters.scrollToPosition(itemPosition)
            binding.fragmentCharacterListCharacters.visibility = View.VISIBLE
            binding.fragmentCharacterListEmpty.visibility = View.GONE
        }
        else {
            binding.fragmentCharacterListEmpty.visibility = View.VISIBLE
            binding.fragmentCharacterListCharacters.visibility = View.GONE
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.isLoading = isLoading
    }
}
