package com.yourcompany.android.quotes.ui.composables

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun TopBar(title: String) {
  TopAppBar(
    title = { Text(title) }
  )
}