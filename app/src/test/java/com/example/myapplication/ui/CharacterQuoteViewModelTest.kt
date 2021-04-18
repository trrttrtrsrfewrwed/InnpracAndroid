package com.example.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.example.myapplication.data.entities.CharacterDTO
import com.example.myapplication.data.repository.CharacterRepository
import com.example.myapplication.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.test.*
import org.mockito.ArgumentMatchers


@ExperimentalCoroutinesApi
class MainCoroutineRule(
    val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispatcher) {
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}

@ExperimentalCoroutinesApi
interface CoroutineTest {

    @get:Rule
    val coroutineRule: MainCoroutineRule

    fun test(
        test: suspend TestCoroutineDispatcher.() -> Unit) = coroutineRule.dispatcher.runBlockingTest { test(coroutineRule.dispatcher) }

}


fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharacterQuoteViewModelTest: CoroutineTest {

    private val character: CharacterDTO = CharacterDTO(1, "test_name", "test_title", "test_slug", "test_url")

    @Mock
    lateinit var characterRepository: CharacterRepository

    @InjectMocks
    private lateinit var viewModel: CharacterQuoteViewModel

    @ExperimentalCoroutinesApi
    @get:Rule
    override val coroutineRule = MainCoroutineRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun start() = test {
        `when`(characterRepository.getCharacter(1)).thenReturn(liveData {
            emit(Resource.success(character))
        })

        `when`(characterRepository.getAllQuotes("test_slug")).thenReturn(liveData {
            emit(Resource.success(listOf("test_sentence")))
        })

        viewModel.start(1)

        viewModel.characterQuote.observeForever {}

        verify(characterRepository, times(1)).getAllQuotes(ArgumentMatchers.anyString())

        assertEquals(viewModel.characterQuote.getOrAwaitValue().data!!.character, character)

        assertEquals(viewModel.characterQuote.getOrAwaitValue().data!!.quote, listOf("test_sentence"))
    }
}