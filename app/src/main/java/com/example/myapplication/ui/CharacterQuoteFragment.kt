package com.example.myapplication.ui

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.myapplication.data.entities.CharacterDTO
import com.example.myapplication.data.entities.CharacterQuote
import com.example.myapplication.data.entities.GOTCharacter
import com.example.myapplication.databinding.CharacterQuoteFragmentBinding
import com.example.myapplication.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterQuoteFragment : Fragment() {

    private lateinit var binding: CharacterQuoteFragmentBinding
    private val viewModel: CharacterQuoteViewModel by viewModels()
    var character_id: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CharacterQuoteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun start(id: Int) {
        // if no id was provided
        if (arguments?.getInt("id") == null && id < 0) {
            character_id = -1
            binding.progressBar.visibility = View.INVISIBLE
            binding.quoteContainer.visibility = View.INVISIBLE
            binding.defaultFragment.visibility = View.VISIBLE
        } else {
            binding.defaultFragment.visibility = View.GONE
            binding.quoteContainer.visibility = View.VISIBLE
            if (id >= 0) {
                character_id = id
                viewModel.start(id)
            }

            arguments?.getInt("id")?.let { viewModel.start(it) }

            if (arguments?.getInt("id") != null) {
                character_id = arguments?.getInt("id")!!
            }

            setupObservers()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start(character_id)
    }

    private fun setupObservers() {
        viewModel.characterQuote.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    bindCharacter(it.data!!)
                    binding.progressBar.visibility = View.GONE
                    binding.characterCl.visibility = View.VISIBLE
                }

                Resource.Status.ERROR ->
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()

                Resource.Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.characterCl.visibility = View.GONE
                }
            }
        })
    }

    private fun bindCharacter(characterQuote: CharacterQuote) {
        binding.fullName.text = characterQuote.character?.fullName ?: ""
        binding.title.text = characterQuote.character?.title ?: ""

        if (characterQuote.quote.isNullOrEmpty()) {
            binding.quote.text=""
            binding.quoteItem1.visibility=View.INVISIBLE
            binding.quoteItem2.visibility=View.INVISIBLE
        } else {
            binding.quote.text=characterQuote.quote.get(0)
            binding.quoteItem1.visibility=View.VISIBLE
            binding.quoteItem2.visibility=View.VISIBLE
        }
        Glide.with(binding.root)
            .load(characterQuote.character?.imageUrl)
            .transform(CircleCrop())
            .into(binding.image)
    }
}