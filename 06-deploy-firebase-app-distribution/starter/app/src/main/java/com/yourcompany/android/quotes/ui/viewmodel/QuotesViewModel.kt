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

package com.yourcompany.android.quotes.ui.viewmodel

import androidx.lifecycle.*
import com.yourcompany.android.quotes.data.Quote
import com.yourcompany.android.quotes.data.QuotesRepository
import kotlinx.coroutines.launch


/**
 * Created by Enzo Lizama Paredes on 7/24/20.
 * Contact: lizama.enzo@gmail.com
 */

class QuotesViewModel(private val repository: QuotesRepository) : ViewModel() {

  private val _dataLoading = MutableLiveData<Boolean>()
  val dataLoading: LiveData<Boolean> = _dataLoading

  fun insertQuote(quote: Quote) {
    _dataLoading.postValue(true)
    viewModelScope.launch {
      repository.insert(quote)
    }
    _dataLoading.postValue(false)
  }

  fun updateQuote(quote: Quote) {
    _dataLoading.postValue(true)
    viewModelScope.launch {
      repository.update(quote)
    }
    _dataLoading.postValue(false)
  }

  fun delete(quote: Quote) {
    _dataLoading.postValue(true)
    viewModelScope.launch {
      repository.delete(quote)
    }
    _dataLoading.postValue(false)
  }

  fun getAllQuotes(): LiveData<List<Quote>>  = liveData {
    _dataLoading.postValue(true)
    repository.getQuotes().collect { quotes ->
      _dataLoading.postValue(false)
      emit(quotes)
    }
  }

}