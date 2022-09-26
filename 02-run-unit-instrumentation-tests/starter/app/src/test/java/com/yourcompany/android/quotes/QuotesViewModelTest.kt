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

package com.yourcompany.android.quotes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.yourcompany.android.quotes.data.Quote
import com.yourcompany.android.quotes.data.QuotesRepositoryImpl
import com.yourcompany.android.quotes.ui.viewmodel.QuotesViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.spy
import org.mockito.MockitoAnnotations


/**
 * Created by Enzo Lizama Paredes on 7/25/20.
 * Contact: lizama.enzo@gmail.com
 */

@OptIn(DelicateCoroutinesApi::class)
@ExperimentalCoroutinesApi
class QuotesViewModelTest {

  @Mock
  private lateinit var viewModel: QuotesViewModel

  @Mock
  private lateinit var repositoryImpl: QuotesRepositoryImpl

  @get:Rule
  var instantExecutorRule = InstantTaskExecutorRule()

  private val mainThreadSurrogate = newSingleThreadContext("UI thread")

  private lateinit var isLoadingLiveData: LiveData<Boolean>

  private lateinit var autoCloseable: AutoCloseable

  /**
   * Setup values before init tests
   *
   */
  @Before
  fun setup() {
    autoCloseable = MockitoAnnotations.openMocks(this)
    Dispatchers.setMain(mainThreadSurrogate)

    viewModel = spy(QuotesViewModel(repositoryImpl))
    isLoadingLiveData = viewModel.dataLoading
  }

  /**
   * Test asserting values for [LiveData] items on [QuotesViewModel] to insert [Quote]
   *
   */
  @Test
  fun `Assert loading values are correct fetching quotes`() {
    val testQuote = Quote(id = 1, text = "Hello World!", author = "Ray Wenderlich",
        date = "27/12/1998")
    var isLoading = isLoadingLiveData.value
    isLoading?.let { assertTrue(it) }
    viewModel.insertQuote(testQuote)
    isLoading = isLoadingLiveData.value
    assertNotNull(isLoading)
    isLoading?.let { assertFalse(it) }
  }

  /**
   * Test asserting values for [LiveData] items on [QuotesViewModel] to delete [Quote]
   *
   */
  @Test
  fun `Assert loading values are correct deleting quote`() {
    val testQuote = Quote(id = 1, text = "Hello World!", author = "Ray Wenderlich",
        date = "27/12/1998")
    var isLoading = isLoadingLiveData.value
    isLoading?.let { assertTrue(it) }
    viewModel.delete(testQuote)
    isLoading = isLoadingLiveData.value
    assertNotNull(isLoading)
    isLoading?.let { assertFalse(it) }
  }

  /**
   * Test asserting values for [LiveData] items on [QuotesViewModel] to update [Quote]
   *
   */
  @Test
  fun `Assert loading values are correct updating quote`() {
    val testQuote = Quote(id = 1, text = "Hello World!", author = "Ray Wenderlich",
        date = "27/12/1998")
    var isLoading = isLoadingLiveData.value
    isLoading?.let { assertTrue(it) }
    viewModel.updateQuote(testQuote)
    isLoading = isLoadingLiveData.value
    assertNotNull(isLoading)
    isLoading?.let { assertFalse(it) }
  }

  @After
  fun tearDown() {
    autoCloseable.close()
    Dispatchers.resetMain()
    mainThreadSurrogate.close()
  }
}