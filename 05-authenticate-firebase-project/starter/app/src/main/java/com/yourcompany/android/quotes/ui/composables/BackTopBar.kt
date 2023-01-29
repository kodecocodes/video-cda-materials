package com.yourcompany.android.quotes.ui.composables

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BackTopBar(title: String, onBackPressed: () -> Unit) {
  TopAppBar(
    title = { Text(title) },
    modifier = Modifier,
    navigationIcon = {
      IconButton(onClick = {
        onBackPressed()
      }) {
        Icon(
          imageVector = Icons.Default.ArrowBack,
          contentDescription = "Back Icon",
        )
      }
    }
  )
}