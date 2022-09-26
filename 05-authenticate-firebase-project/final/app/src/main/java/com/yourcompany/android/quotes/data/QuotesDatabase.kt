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

package com.yourcompany.android.quotes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yourcompany.android.quotes.utils.ioThread


/**
 * Created by Enzo Lizama Paredes on 7/23/20.
 * Contact: lizama.enzo@gmail.com
 */

const val DATABASE_VERSION_CODE = 3

@Database(entities = [Quote::class], version = DATABASE_VERSION_CODE, exportSchema = true)
abstract class QuotesDatabase : RoomDatabase() {

  abstract fun quotesDao(): QuotesDao

  companion object {
    private var INSTANCE: QuotesDatabase? = null

    fun getInstance(context: Context): QuotesDatabase? {
      if (INSTANCE == null) {
        synchronized(QuotesDatabase::class) {
          INSTANCE = Room.databaseBuilder(context,
                                          QuotesDatabase::class.java, "quotes_database.db")
              .addMigrations(MIGRATION_1_2)
              .addMigrations(MIGRATION_2_3)
              .allowMainThreadQueries()
              .addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                  super.onCreate(db)
                  ioThread {
                    for (q in PREPOPULATE_DATA) {
                      getInstance(context)!!.quotesDao().insertQuote(q)
                    }
                  }
                }
              })
              .build()
        }
      }
      return INSTANCE
    }


    val PREPOPULATE_DATA = listOf(Quote(1,
        "Any fool can write code that a computer can understand. Good programmers write code that humans can understand.",
        "Martin Fowler", "12/12/2020"),
        Quote(2, "First, solve the problem. Then, write the code.", "John Johnson", "12/12/2020"))
  }

}
