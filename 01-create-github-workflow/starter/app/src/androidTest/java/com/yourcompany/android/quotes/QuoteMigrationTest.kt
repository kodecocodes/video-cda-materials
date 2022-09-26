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
import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.yourcompany.android.quotes.data.MIGRATION_1_2
import com.yourcompany.android.quotes.data.MIGRATION_2_3

import com.yourcompany.android.quotes.data.QuotesDatabase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * Created by Enzo Lizama Paredes on 7/26/20.
 * Contact: lizama.enzo@gmail.com
 */

@RunWith(AndroidJUnit4::class)
class QuoteMigrationTest {

  private lateinit var database: SupportSQLiteDatabase

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


  @Test
  fun migrate1to2() {
    database = migrationTestHelper.createDatabase(TEST_DB, 1).apply {

      execSQL(
          """
                INSERT INTO quotes VALUES (10, 'Hello', 'Shakespeare', '12/12/21')
                """.trimIndent()
      )
      close()
    }

    database = migrationTestHelper.runMigrationsAndValidate(TEST_DB, 2, true, MIGRATION_1_2)

    val resultCursor = database.query("SELECT * FROM quotes")

    // Let's make sure we can find the  age column, and that it's equal to our default.
    // We can also validate the name is the one we inserted.
    assertTrue(resultCursor.moveToFirst())

    val authorColumnIndex = resultCursor.getColumnIndex("author")
    val textColumnIndex = resultCursor.getColumnIndex("text")

    val authorFromDatabase = resultCursor.getString(authorColumnIndex)
    val textFromDatabase = resultCursor.getString(textColumnIndex)

    assertEquals("Shakespeare", authorFromDatabase)
    assertEquals("Hello", textFromDatabase)
  }

  private val ALL_MIGRATIONS = arrayOf(MIGRATION_1_2, MIGRATION_2_3)

  /**
   * Testing all migrations
   */
  @Test
  @Throws(IOException::class)
  fun migrateAll() {
    // Create earliest version of the database.
    migrationTestHelper.createDatabase(TEST_DB, 1).apply {
      close()
    }

    // Open latest version of the database. Room will validate the schema
    // once all migrations execute.
    Room.databaseBuilder(
        InstrumentationRegistry.getInstrumentation().targetContext,
        QuotesDatabase::class.java,
        TEST_DB
    ).addMigrations(*ALL_MIGRATIONS).build().apply {
      openHelper.writableDatabase
      close()
    }
  }
}