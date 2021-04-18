package com.example.myapplication.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.myapplication.data.entities.CharacterDTO
import com.example.myapplication.data.entities.CharacterQuote
import com.example.myapplication.data.repository.CharacterRepository
import com.example.myapplication.utils.Resource



class CharacterQuoteViewModel @ViewModelInject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _id = MutableLiveData<Int>()

    private val _character = _id.switchMap { id ->
        repository.getCharacter(id)
    }

    private fun proxy(character: Resource<CharacterDTO>): LiveData<Pair<Resource<CharacterDTO>, Resource<List<String>>>> {
        if (character.status != Resource.Status.SUCCESS) {
            return  MutableLiveData(Pair(character, Resource.loading(null)))
        }
        return repository.getAllQuotes(character.data!!.slug!!).map{ _quotes -> Pair(character, _quotes) }
    }

    val characterQuote: LiveData<Resource<CharacterQuote>> = _character.switchMap { character ->
        proxy(character).map { Resource.combine(it.first, it.second) { ch, q -> CharacterQuote(ch, q) } }
    }

    fun start(id: Int) {
        _id.value = id
    }

}
