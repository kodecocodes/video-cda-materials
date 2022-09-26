/*
 * Copyright (c) 2022 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yourcompany.android.quotes.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourcompany.android.quotes.R
import com.yourcompany.android.quotes.data.Quote
import com.yourcompany.android.quotes.data.QuotesRepositoryImpl
import com.yourcompany.android.quotes.databinding.ActivityMainBinding
import com.yourcompany.android.quotes.ui.viewmodel.QuoteViewModelFactory
import com.yourcompany.android.quotes.ui.viewmodel.QuotesViewModel

/**
 * Main Screen
 */
@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

  private lateinit var quotesViewModel: QuotesViewModel
  private lateinit var quoteAdapter: QuoteAdapter

  private lateinit var activityMainBinding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {

    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(activityMainBinding.root)

    quotesViewModel = ViewModelProvider(
        this,
        QuoteViewModelFactory(QuotesRepositoryImpl(application = application))
    )[QuotesViewModel::class.java]

    quotesViewModel.dataLoading.observe(this) { value ->
      value?.let { show ->
        activityMainBinding.loadingSpinner.visibility = if (show) View.VISIBLE else View.GONE
      }
    }

    quotesViewModel.getAllQuotes().observe(this) {
      quoteAdapter.setQuotes(it)
    }

    quoteAdapter = QuoteAdapter(clickListener = object : ClickListener {
      override fun onClick(quote: Quote) {
        val intent = Intent(this@MainActivity, AddEditActivity::class.java)
        intent.apply {
          putExtra(AddEditActivity.EXTRA_ID, quote.id)
          putExtra(AddEditActivity.EXTRA_AUTHOR, quote.author)
          putExtra(AddEditActivity.EXTRA_TEXT, quote.text)
          putExtra(AddEditActivity.EXTRA_DATE, quote.date)
        }
        startActivityForResult(intent, EDIT_QUOTE_REQUEST_CODE)
      }
    })

    activityMainBinding.quotesRecyclerView.apply {
      layoutManager = LinearLayoutManager(applicationContext)
      adapter = quoteAdapter
    }

    activityMainBinding.addQuoteFloatingButton.setOnClickListener {
      val intent = Intent(this, AddEditActivity::class.java)
      startActivityForResult(intent, ADD_QUOTE_REQUEST_CODE)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
      when (requestCode) {
        ADD_QUOTE_REQUEST_CODE -> {
          val intentData = data!!
          val newNote = Quote(
              text = intentData.getStringExtra(AddEditActivity.EXTRA_TEXT)!!,
              author = intentData.getStringExtra(AddEditActivity.EXTRA_AUTHOR)!!,
              date = intentData.getStringExtra(AddEditActivity.EXTRA_DATE)!!
          )
          quotesViewModel.insertQuote(newNote)
          Toast.makeText(this, "Quote saved!", Toast.LENGTH_SHORT).show()
        }
        EDIT_QUOTE_REQUEST_CODE -> {
          val intentData = data!!
          val updateQuote = Quote(
              id = intentData.getIntExtra(AddEditActivity.EXTRA_ID, -1),
              text = intentData.getStringExtra(AddEditActivity.EXTRA_TEXT)!!,
              author = intentData.getStringExtra(AddEditActivity.EXTRA_AUTHOR)!!,
              date = intentData.getStringExtra(AddEditActivity.EXTRA_DATE)!!
          )
          quotesViewModel.updateQuote(updateQuote)
          Toast.makeText(this, "Quote updated!", Toast.LENGTH_SHORT).show()
        }
        else -> {
          Toast.makeText(this, "Not found!", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  companion object {
    const val ADD_QUOTE_REQUEST_CODE = 1
    const val EDIT_QUOTE_REQUEST_CODE = 2
  }
}
