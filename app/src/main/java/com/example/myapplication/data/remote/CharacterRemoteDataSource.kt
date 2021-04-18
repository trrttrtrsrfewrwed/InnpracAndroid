package com.example.myapplication.data.remote

import javax.inject.Inject

class CharacterRemoteDataSource @Inject constructor(
    private val characterService: CharacterService,
    private val quotesService: QuotesService
): BaseDataSource() {

    suspend fun getCharacters() = getResult { characterService.getAllCharacters() }
    suspend fun getCharacter(id: Int) = getResult { characterService.getCharacter(id) }
    suspend fun getQuote(slug: String) = getResult { quotesService.getQuote(slug) }
    suspend fun getAllQuotes(slug: String) = getResult { quotesService.getAllQuotes(slug) }



}