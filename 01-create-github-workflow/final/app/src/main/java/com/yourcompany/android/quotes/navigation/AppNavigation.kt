package com.yourcompany.android.quotes.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.yourcompany.android.quotes.ui.screens.AddQuoteScreen
import com.yourcompany.android.quotes.ui.screens.ListScreen
import com.yourcompany.android.quotes.ui.viewmodel.QuotesViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation(navController: NavHostController, viewModel: QuotesViewModel) {
  AnimatedNavHost(
    navController = navController,
    startDestination = Screens.ListScreen.route,
    enterTransition = { expandIn(animationSpec = tween(800)) },
    exitTransition = { shrinkOut(animationSpec = tween(800)) }
  ) {
    composable(Screens.ListScreen.route,
      enterTransition = {
        if (initialState.destination.route == Screens.QuoteInputScreen.route) slideIntoContainer(
          AnimatedContentScope.SlideDirection.Right,
          animationSpec = tween(600)
        )
        else null
      },
      exitTransition = {
        if (targetState.destination.route == Screens.QuoteInputScreen.route) slideOutOfContainer(
          AnimatedContentScope.SlideDirection.Left,
          animationSpec = tween(600)
        )
        else null
      }
    ) {
      ListScreen(navController, viewModel)
    }

    composable(Screens.QuoteInputScreen.route,
      enterTransition = {
        if (initialState.destination.route == Screens.ListScreen.route) slideIntoContainer(
          AnimatedContentScope.SlideDirection.Left,
          animationSpec = tween(600)
        )
        else null
      },
      exitTransition = {
        if (targetState.destination.route == Screens.ListScreen.route) slideOutOfContainer(
          AnimatedContentScope.SlideDirection.Right,
          animationSpec = tween(600)
        )
        else null
      }
    ) {
      AddQuoteScreen(navController, viewModel)
    }
  }
}