package es.aroldan.rickandmorty.presentation.util

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<V: ViewModel, B: ViewDataBinding> : Fragment() {

    protected val viewModel by lazy { getViewModel<V>() }

    private var _binding: B? = null
    protected val binding get() = _binding!!

    protected val router by lazy { Router(this) }

    @LayoutRes
    abstract fun getLayout(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        DataBindingUtil.inflate<B>(inflater, getLayout(), container, false).also {
            _binding = it
            binding.executePendingBindings()
            binding.lifecycleOwner = viewLifecycleOwner
        }.root

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun <T> collect(flow: Flow<T>, block: (T) -> Unit) =
        with (viewLifecycleOwner) {
            lifecycleScope.launchWhenStarted {
                flow.onEach {
                    block(it)
                }.collect()
            }
        }
}
