package com.kodeco.android.quotes.di

import androidx.room.Room
import com.kodeco.android.quotes.data.QuotesDatabase
import com.kodeco.android.quotes.data.QuotesRepository
import com.kodeco.android.quotes.data.QuotesRepositoryImpl
import com.kodeco.android.quotes.ui.viewmodel.QuotesViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
  single {
    Room.databaseBuilder(
      androidApplication(),
      QuotesDatabase::class.java,
      "Quotes.db"
    ).fallbackToDestructiveMigration()
      .build()
      .quotesDao()
  }
}
val repositoryModule = module {
  single<QuotesRepository> { QuotesRepositoryImpl(get()) }
}

val viewModelModule = module {
  viewModel { QuotesViewModel(get()) }
}

val appModules = listOf(
  dataModule,
  repositoryModule,
  viewModelModule
)