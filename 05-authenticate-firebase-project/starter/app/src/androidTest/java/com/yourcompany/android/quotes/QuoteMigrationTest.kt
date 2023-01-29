/*
 * Copyright (c) 2023 Kodeco LLC
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
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.yourcompany.android.quotes.data.MIGRATION_1_2
import com.yourcompany.android.quotes.data.MIGRATION_2_3
import com.yourcompany.android.quotes.data.QuotesDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class QuoteMigrationTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  companion object {
    private const val TEST_DB = "migration-test"
  }

  @get:Rule
  val migrationTestHelper = MigrationTestHelper(
      InstrumentationRegistry.getInstrumentation(),
      QuotesDatabase::class.java.canonicalName,
      FrameworkSQLiteOpenHelperFactory()
  )

  /**
   * Testing all migrations
   */
  @Test
  @Throws(IOException::class)
  fun migrateAll() {
    // Create earliest version of the database.
    migrationTestHelper.createDatabase(TEST_DB, 3).apply {
      close()
    }

    // Open latest version of the database. Room will validate the schema
    // once all migrations execute.
    Room.databaseBuilder(
        InstrumentationRegistry.getInstrumentation().targetContext,
        QuotesDatabase::class.java,
        TEST_DB
    ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).build().apply {
      openHelper.writableDatabase
      close()
    }
  }
}