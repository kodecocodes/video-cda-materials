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
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yourcompany.android.quotes.databinding.ActivityAddEditBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AddEditActivity : AppCompatActivity() {

  private lateinit var addEditBinding: ActivityAddEditBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addEditBinding = ActivityAddEditBinding.inflate(layoutInflater)
    setContentView(addEditBinding.root)

    if (intent.hasExtra(EXTRA_ID)) {
      title = "Edit quote"
      addEditBinding.editTextQuoteText.setText(intent.getStringExtra(EXTRA_TEXT))
      addEditBinding.editTextQuoteAuthor.setText(intent.getStringExtra(EXTRA_AUTHOR))
      addEditBinding.editTextQuoteDate.setText(intent.getStringExtra(EXTRA_DATE))
    } else {
      title = "Add new quote"
    }
    addEditBinding.buttonSaveQuote.setOnClickListener {
      saveQuote()
    }

    initUI()
  }

  private fun initUI() {
    val calendar = Calendar.getInstance()
    val date = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
      calendar.set(Calendar.YEAR, year)
      calendar.set(Calendar.MONTH, monthOfYear)
      calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
      val dateFormat = "dd/MM/yyyy"
      val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.US)
      addEditBinding.editTextQuoteDate.setText(simpleDateFormat.format(calendar.time))
    }

    addEditBinding.editTextQuoteDate.setOnClickListener {
      DatePickerDialog(this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
          calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
  }


  private fun saveQuote() {
    if (addEditBinding.editTextQuoteText.text.toString().trim().isBlank() ||
        addEditBinding.editTextQuoteAuthor.text.toString().trim().isBlank() ||
        addEditBinding.editTextQuoteDate.text.toString().trim().isBlank()) {
      Toast.makeText(this, "Quote is empty! Please fill the missing fields", Toast.LENGTH_SHORT)
          .show()
      return
    }

    val data = Intent().apply {
      putExtra(EXTRA_TEXT, addEditBinding.editTextQuoteText.text.toString())
      putExtra(EXTRA_AUTHOR, addEditBinding.editTextQuoteAuthor.text.toString())
      putExtra(EXTRA_DATE, addEditBinding.editTextQuoteDate.text.toString())
      if (intent.getIntExtra(EXTRA_ID, -1) != -1) {
        putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, -1))
      }
    }

    setResult(Activity.RESULT_OK, data)
    finish()
  }

  companion object {
    const val EXTRA_ID = "QUOTE_ID"
    const val EXTRA_TEXT = "QUOTE_TEXT"
    const val EXTRA_AUTHOR = "QUOTE_AUTHOR"
    const val EXTRA_DATE = "QUOTE_DATE"
  }
}