package com.yourcompany.android.quotes.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourcompany.android.quotes.data.Quote

@Composable
fun QuoteItemRow(item: Quote) {

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    shape = RoundedCornerShape(4.dp),
    backgroundColor = MaterialTheme.colors.surface,
    elevation = 4.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Text(
        text = item.text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.padding(top = 10.dp))
      Text(text = item.author)
    }
  }
}