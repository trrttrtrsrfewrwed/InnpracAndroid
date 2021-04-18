package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.CharactersFragmentBinding
import com.example.myapplication.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment : Fragment(), CharactersAdapter.CharacterItemListener {

    lateinit var binding: CharactersFragmentBinding
    private val viewModel: CharactersViewModel by viewModels()
    private lateinit var adapter: CharactersAdapter
    private var rv: Int = 0
    private var isRvSet: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CharactersFragmentBinding.inflate(inflater, container, false)
        binding.progressBar.visibility = View.VISIBLE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        adapter = CharactersAdapter(this)
        binding.charactersRv.layoutManager = LinearLayoutManager(requireContext())
        binding.charactersRv.adapter = adapter
    }

    fun getRv(): Int {
        var mScrollPosition = 0
        if ( binding.charactersRv.layoutManager != null &&  binding.charactersRv.layoutManager is LinearLayoutManager) {
            mScrollPosition = (binding.charactersRv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        }
        return mScrollPosition
    }

    fun setRv(rv_: Int?) {
        if (rv_ != null) {
            rv = rv_
        }
    }

    private fun setupObservers() {
        viewModel.characters.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    if (!(it.data!!).isNullOrEmpty()) {
                        adapter.setItems(it.data)

                        if (!isRvSet) {
                            if (binding.charactersRv.layoutManager != null && binding.charactersRv.layoutManager is LinearLayoutManager) {
                                (binding.charactersRv.layoutManager as LinearLayoutManager).scrollToPosition(
                                    rv
                                )
                            }
                            isRvSet = true
                        }
                    }
                }
                Resource.Status.ERROR ->
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                Resource.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE
            }
        })
    }

    override fun onClickedCharacter(characterId: Int) {
        val isLandscape: Boolean? = context?.resources?.getBoolean(R.bool.is_landscape)
        if (isLandscape == true) {
            val fragmentManager = parentFragmentManager

            val fragment2 = fragmentManager.findFragmentById(R.id.characterQuoteFragment) as CharacterQuoteFragment?
            fragment2?.start(characterId)
        } else {
            findNavController().navigate(
                R.id.action_charactersFragment_to_characterQuoteFragment,
                bundleOf("id" to characterId)
            )
        }
    }
}
