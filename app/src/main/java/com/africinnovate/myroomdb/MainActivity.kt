package com.africinnovate.myroomdb

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import androidx.lifecycle.observe
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.InternalCoroutinesApi


class MainActivity : AppCompatActivity() {

    private  val newWordActivityRequestCode = 1

    @InternalCoroutinesApi
    private val wordViewModel : WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        wordViewModel.allwords.observe(owner = this) { words ->
            words.let {adapter.submitList(it)}
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    @InternalCoroutinesApi
    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if( requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK){
            intentData?.getStringExtra(NewWordActivity.EXTRA_REPLY)?.let  { reply ->
                val word = Word(reply)
                wordViewModel.insert(word)
            }
         } else {
             Toast.makeText(applicationContext,
                     R.string.empty_not_saved,
                     Toast.LENGTH_LONG)
                     .show()
        }
    }
}