package com.example.learningcenter.model

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import us.google.protobuf.BoolValue

@Entity(tableName = "word")
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val wordTitle: String,
    @ColumnInfo(name = "transcript")
    val wordTranscript: String,
    @ColumnInfo(name = "definition")
    val wordDefinition: String,
    @ColumnInfo(name = "found")
    var wordFound: Int,
    @ColumnInfo(name = "isDone")
    var isDone: Boolean = false
)

data class Title (
    @ColumnInfo(name = "title")
    val wordTitle: String,
    @ColumnInfo(name = "found")
    var wordFound: Int)
data class TitleChart (
    @ColumnInfo(name = "title")
    val wordTitle: String,
    @ColumnInfo(name = "found")
    var wordFound: Int)
data class UpdateWordDone(
    @ColumnInfo(name = "title")
    val wordTitle: String,
    @ColumnInfo(name = "isDone")
    val isDone: Boolean)
