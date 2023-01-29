package com.yourcompany.android.quotes.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yourcompany.android.quotes.data.Quote
import com.yourcompany.android.quotes.navigation.Screens
import com.yourcompany.android.quotes.ui.composables.QuoteItemRow
import com.yourcompany.android.quotes.ui.composables.TopBar
import com.yourcompany.android.quotes.ui.viewmodel.QuotesViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ListScreen(navController: NavController, quotesViewModel: QuotesViewModel) {
  val keyboardController = LocalSoftwareKeyboardController.current
  keyboardController?.hide()

  quotesViewModel.getAllQuotes()
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopBar(title = "Quotes")
    },
    content = {
      val items by quotesViewModel.items.observeAsState()
      items?.let {
        ItemList(quotes = it)
      }
    },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        modifier = Modifier
          .padding(16.dp),
        onClick = {
          navController.navigate(Screens.QuoteInputScreen.route)
        },
        icon = {
          Icon(
            Icons.Filled.Add,
            contentDescription = "Create"
          )
        },
        text = { Text("Create") }
      )
    }
  )
}

@Composable
fun ItemList(quotes: List<Quote>) {
  /**
   * Since the LazyColumn is the main container of the UI, set
   * its background to the `background` color of the theme
   */
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colors.background)
      .padding(top = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    items(quotes) { quote ->
      QuoteItemRow(quote)
    }
  }
}