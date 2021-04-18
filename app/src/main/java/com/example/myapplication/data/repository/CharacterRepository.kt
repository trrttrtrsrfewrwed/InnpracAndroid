package com.example.myapplication.data.repository

import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.myapplication.data.entities.*
import com.example.myapplication.data.local.CharacterDao
import com.example.myapplication.data.remote.CharacterRemoteDataSource
import com.example.myapplication.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timerTask

class CharacterRepository @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterDao
) {

    fun getCharacter(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val source = localDataSource.getCharacter(id).map {Resource.success(it)}
        emitSource(source)

        val networkCallResult = remoteDataSource.getCharacter(id)

        if (networkCallResult.status == Resource.Status.SUCCESS) {
            val character : GOTCharacter = networkCallResult.data!!
            var slug: String = character.firstName!!.decapitalize()
            slug = when (slug) {
                "jamie" -> "jaime"
                "brandon" -> "bran"
                "petyr" -> "baelish"
                else -> slug
            }
            localDataSource.insertCharacter(CharacterDTO(character.id, character.fullName, character.title, slug, character.imageUrl))

        } else if (networkCallResult.status == Resource.Status.ERROR) {
            emit(Resource.error(networkCallResult.message!!))
            emitSource(source)
        }
    }

    fun getCharacters() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val source = localDataSource.getAllCharacters().map {Resource.success(it)}

        emitSource(source)


        val networkCallResult = remoteDataSource.getCharacters()

        if (networkCallResult.status == Resource.Status.SUCCESS) {
            val characters : List<GOTCharacter> = networkCallResult.data!!

            val dtoCharacters: List<CharacterDTO> = characters.map {
                val slug: String = it.firstName!!.decapitalize()
                CharacterDTO(it.id, it.fullName, it.title, slug, it.imageUrl)
            }

            localDataSource.insertAllCharacters(dtoCharacters)

        } else if (networkCallResult.status == Resource.Status.ERROR) {
            emit(Resource.error(networkCallResult.message!!))
            emitSource(source)
        }
    }

    fun getAllQuotes(slug: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val source = localDataSource.getAllQuotes(slug).map {Resource.success(it)}

        emitSource(source)

        val networkCallResult = remoteDataSource.getAllQuotes(slug)

        if (networkCallResult.status == Resource.Status.SUCCESS) {
            val quotes : List<String> = if (networkCallResult.data!!.isEmpty()) Collections.emptyList() else networkCallResult.data.get(0).quotes
            val dtoQuotes: List<QuoteDTO> = quotes.map {
                QuoteDTO(slug=slug, sentence=it)
            }
            localDataSource.insertAllQuotes(dtoQuotes)

        } else if (networkCallResult.status == Resource.Status.ERROR) {
            emit(Resource.error(networkCallResult.message!!))
            emitSource(source)
        }
    }
}