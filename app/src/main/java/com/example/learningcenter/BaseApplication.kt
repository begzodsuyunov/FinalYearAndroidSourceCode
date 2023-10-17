package com.example.learningcenter

import android.app.Application
import com.example.learningcenter.data.WordRoomDatabase

class BaseApplication : Application() {
    val database: WordRoomDatabase by lazy { WordRoomDatabase.getDatabase(this) }

}