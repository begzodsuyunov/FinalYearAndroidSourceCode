package com.example.learningcenter.data

import androidx.room.*
import com.example.learningcenter.model.Title
import com.example.learningcenter.model.TitleChart
import com.example.learningcenter.model.Word
import kotlinx.coroutines.flow.Flow


@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(words: Word)

    @Update
    suspend fun update(words: Word)

    @Delete
    suspend fun delete(words: Word)

    @Query("select * from word where id = :id")
    fun getItem(id: Int): Flow<Word>

    @Query("select * from word where title LIKE '%' || :searchQuery || '%' order by isDone = 0")
    fun getItems(searchQuery: String): Flow<List<Word>>

    @Query("select title, found from word")
    fun getWordTitles(): List<Title>

    @Query("select * from word where title = :title")
    fun getWordFound(title: String): Word

    @Query("select found, title from word")
    fun getWordTitlesGraph(): Flow<List<TitleChart>>

    @Query("select * from word where isDone = 0")
    fun getWordIsDone(): List<Word>



}