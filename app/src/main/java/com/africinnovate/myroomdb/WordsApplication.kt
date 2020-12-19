package com.africinnovate.myroomdb

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.SupervisorJob

class WordsApplication: Application(){
    val applicationScope = CoroutineScope(SupervisorJob())


    @InternalCoroutinesApi
    val database by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
    @InternalCoroutinesApi
    val repository by lazy { WordRepository(database.wordDao()) }
}