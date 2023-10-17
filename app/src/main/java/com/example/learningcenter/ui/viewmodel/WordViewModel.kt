package com.example.learningcenter.ui.viewmodel


import android.text.Spannable
import android.text.SpannableString
import android.text.style.TtsSpan
import android.util.Log
import androidx.lifecycle.*
import com.example.learningcenter.data.WordDao
import com.example.learningcenter.model.TitleChart
import com.example.learningcenter.model.Word
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

const val MAX_NO_OF_WORDS = 10
const val SCORE_INCREASE = 20

class WordViewModel(private val wordDao: WordDao) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    private val wordsFlow = searchQuery.flatMapLatest {
        wordDao.getItems(it)
    }

    val allItems: LiveData<List<Word>> = wordsFlow.asLiveData()

    private fun insertWord(word: Word) {
        viewModelScope.launch {
            wordDao.insert(word)
        }
    }

    private fun getNewWordEntry(
        wordTitle: String,
        wordTranscript: String,
        wordDefinition: String,
        wordFound: Int,
        isDone: Boolean
    ): Word {
        return Word(
            wordTitle = wordTitle,
            wordTranscript = wordTranscript,
            wordDefinition = wordDefinition,
            wordFound = wordFound,
            isDone = isDone

        )
    }

    fun addNewWord(
        wordTitle: String,
        wordTranscript: String,
        wordDefinition: String,
        wordFound: Int,
        isDone: Boolean
    ) {
        val newWord = getNewWordEntry(wordTitle, wordTranscript, wordDefinition, wordFound, isDone)
        insertWord(newWord)
    }

    fun isEntryValid(
        wordTitle: String,
        wordTranscript: String,
        wordDefinition: String,
        wordFound: String
    ): Boolean {
        if (wordTitle.isBlank() || wordTranscript.isBlank() || wordDefinition.isBlank() || wordFound.isBlank() || wordFound.toInt() > 20) {
            return false
        }
        return true
    }

    fun retrieveItem(id: Int): LiveData<Word> {
        return wordDao.getItem(id).asLiveData()
    }

    fun updateItem(word: Word) {
        viewModelScope.launch {
            wordDao.update(word)
        }
    }

    fun deleteWord(word: Word) {
        viewModelScope.launch {
            wordDao.delete(word)
        }
    }


    private fun getUpdatedItemEntry(
        wordId: Int,
        wordTitle: String,
        wordTranscript: String,
        wordDefinition: String,
        wordFound: Int,
        isDone: Boolean
    ): Word {
        return Word(
            id = wordId,
            wordTitle = wordTitle,
            wordTranscript = wordTranscript,
            wordDefinition = wordDefinition,
            wordFound = wordFound,
            isDone = isDone
        )
    }

    fun updateItem(
        wordId: Int,
        wordTitle: String,
        wordTranscript: String,
        wordDefinition: String,
        wordFound: Int,
        isDone: Boolean
    ) {
        val updatedItem = getUpdatedItemEntry(
            wordId,
            wordTitle,
            wordTranscript,
            wordDefinition,
            wordFound,
            isDone
        )
        updateItem(updatedItem)
    }


    fun getTitles(): Flow<List<TitleChart>> = wordDao.getWordTitlesGraph()


    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<Spannable> = Transformations.map(_currentScrambledWord) {
        if (it == null) {
            SpannableString("")
        } else {
            val scrambledWord = it.toString()
            val spannable: Spannable = SpannableString(scrambledWord)
            spannable.setSpan(
                TtsSpan.VerbatimBuilder(scrambledWord).build(),
                0,
                scrambledWord.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            spannable
        }
    }

    // List of words used in the game
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    init {
        getNextWord()
    }

    /*
     * Updates currentWord and currentScrambledWord with the next word.
     */
    private fun getNextWord() {
        currentWord = wordDao.getWordTitles().random().wordTitle
        val wordIsDone = wordDao.getWordFound(currentWord)

        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord) || wordIsDone.isDone) {
            getNextWord()
        } else {
            Log.d("Unscramble", "currentWord= $currentWord")
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = _currentWordCount.value?.inc()
            wordsList.add(currentWord)
        }

    }

    /*
     * Re-initializes the game data to restart the game.
     */
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }

    /*
    * Increases the game score if the player’s word is correct.
    */
    private fun increaseScore() {
        _score.value = _score.value?.plus(SCORE_INCREASE)
    }

    /*
    * Returns true if the player word is correct.
    * Increases the score accordingly.
    */
    fun correctWord() {
        val wordCorrect = wordDao.getWordFound(currentWord)
        wordCorrect.wordFound = wordCorrect.wordFound.inc()
        wordCorrect.isDone = wordCorrect.wordFound > 20
        updateItem(
            wordId = wordCorrect.id,
            wordTitle = wordCorrect.wordTitle,
            wordTranscript = wordCorrect.wordTranscript,
            wordDefinition = wordCorrect.wordDefinition,
            wordFound = wordCorrect.wordFound,
            isDone = wordCorrect.isDone
        )

    }

    fun isEnoughWords(): Boolean {
        return (wordDao.getWordTitles().size < 20)
    }
    fun isEnoughWordIsDone(): Boolean {
        return (wordDao.getWordIsDone().size < 20)
    }

    fun isUserWordCorrect(playerWord: String): Boolean {

        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            //println(allConTitle)
            return true
        }
        return false
    }

    /*
    * Returns true if the current word count is less than MAX_NO_OF_WORDS
    */
    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

}

class WordViewModelFactory(private val wordDao: WordDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(wordDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}