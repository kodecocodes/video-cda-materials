package com.yourcompany.android.quotes.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.yourcompany.android.quotes.ui.composables.BackTopBar
import com.yourcompany.android.quotes.ui.composables.InputText
import com.yourcompany.android.quotes.ui.composables.SaveButton
import com.yourcompany.android.quotes.ui.viewmodel.QuotesViewModel

@Composable
fun AddQuoteScreen(navController: NavController, quotesViewModel: QuotesViewModel) {
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      BackTopBar(title = "Add Quote") {
        navController.popBackStack()
      }
    },
    content = {
      AddQuoteForm(quotesViewModel, navController, it)
    }
  )
}

@Composable
fun AddQuoteForm(quotesViewModel: QuotesViewModel, navController: NavController, paddingValues: PaddingValues) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(color = MaterialTheme.colors.background)
      .padding(paddingValues)
  ) {
    InputText(
      label = "Enter quote",
      requestFocus = true,
      onTextChange = { quotesViewModel.quote = it })
    InputText(
      label = "Enter author name",
      onTextChange = { quotesViewModel.personName = it }
    )
    SaveButton(viewModel = quotesViewModel, navController = navController)
  }
}