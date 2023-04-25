package es.aroldan.rickandmorty.presentation.screen.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.aroldan.rickandmorty.presentation.databinding.ViewCharacterBinding
import es.aroldan.rickandmorty.presentation.model.CharacterView

class CharacterViewAdapter(private val items: List<CharacterView>,
                           private val characterClickListener: (character: CharacterView) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as CharacterViewHolder).bind(items[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CharacterViewHolder(
            ViewCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            characterClickListener
        )

    inner class CharacterViewHolder(private val binding: ViewCharacterBinding,
                                    private val characterClickListener: (characterView: CharacterView) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(characterView: CharacterView) {
            with (binding) {
                character = characterView

                executePendingBindings()

                root.setOnClickListener {
                    characterClickListener(characterView)
                }
            }
        }
    }
}