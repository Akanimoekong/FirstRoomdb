package com.africinnovate.myroomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
public abstract class WordRoomDatabase : RoomDatabase()  {

    abstract fun wordDao() :WordDao

companion object {
    /*Singleton prevents multiple instances of database opening at the
     same time*/
    @Volatile
    private var INSTANCE: WordRoomDatabase? = null

        @InternalCoroutinesApi
        fun getDatabase(
                context: Context,
                scope: CoroutineScope):
                WordRoomDatabase {

            return  INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        WordRoomDatabase::class.java,
                        "word_database"
                )
                        .fallbackToDestructiveMigration()
                        .addCallback(WordDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                instance
            }
        }

    private class WordDatabaseCallback(
            private val scope: CoroutineScope)
        : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {

                }
            }
        }
    }

    suspend fun populateDatabase(wordDao: WordDao) {
        // Start the app with a clean database every time.
        // Not needed if you only populate on creation.
        wordDao.deleteAll()

        var word = Word("Hello")
        wordDao.insert(word)
        word = Word("World!")
        wordDao.insert(word)
    }
    }
}

