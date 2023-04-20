package com.yourcompany.android.quotes.ui.composables

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yourcompany.android.quotes.ui.viewmodel.QuotesViewModel

@Composable
fun SaveButton(viewModel: QuotesViewModel? = null, navController: NavController? = null) {
  val context = LocalContext.current

  Button(
    onClick = {
      if (viewModel?.validateInputs() == true) {
        viewModel.insertQuote()
        navController?.popBackStack()
      } else {
        Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
      }

    },
    modifier = Modifier
      .fillMaxWidth()
      .padding(10.dp),
    shape = RoundedCornerShape(6.dp)
  ) {
    Text(
      text = "Save Item",
      modifier = Modifier
        .padding(6.dp),
    )
  }
}
