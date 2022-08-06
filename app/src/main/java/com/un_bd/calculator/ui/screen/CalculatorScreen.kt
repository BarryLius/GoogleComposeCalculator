package com.un_bd.calculator.ui.screen


import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.un_bd.calculator.R
import com.un_bd.calculator.isWideLayout
import com.un_bd.calculator.ui.theme.CalculatorTheme
import com.un_bd.calculator.ui.theme.GridShapes
import com.un_bd.calculator.widget.AutoSizeText

@Composable
fun CalculatorScreen(isDark: MutableState<Boolean>, viewModel: CalculatorViewModel = viewModel()) {
  val systemUiController = rememberSystemUiController()
  systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = !isDark.value)

  BoxWithConstraints(modifier = Modifier.systemBarsPadding()) {
    TopBar(isDark)
    val isWideLayout by remember { mutableStateOf(isWideLayout()) }
    if (!isWideLayout) {
      Column(modifier = Modifier.fillMaxSize()) {
        Display(modifier = Modifier.weight(1f), viewModel = viewModel)
        Grid(viewModel = viewModel)
      }
    } else {
      Row(modifier = Modifier.fillMaxSize()) {
        Display(modifier = Modifier.weight(1.82f), viewModel = viewModel)
        Grid(modifier = Modifier.weight(1f), viewModel = viewModel)
      }
    }
  }
}

@Composable
fun Display(modifier: Modifier = Modifier, viewModel: CalculatorViewModel) {
  Column(
    modifier = modifier.padding(16.dp),
    horizontalAlignment = Alignment.End,
    verticalArrangement = Arrangement.Bottom
  ) {
    Text(
      text = viewModel.uiState.screenOutResult,
      style = MaterialTheme.typography.h6.copy(
        fontFamily = Font(R.font.pixel).toFontFamily()
      ),
      textAlign = TextAlign.Center,
    )
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = 72.dp),
      contentAlignment = Alignment.BottomEnd
    ) {
      AutoSizeText(
        text = viewModel.uiState.screenOutInput,
        textStyle = MaterialTheme.typography.h2.copy(
          fontFamily = Font(R.font.pixel).toFontFamily()
        )
      )
    }
  }
}

const val GRID_CELL = 4

@Composable
fun Grid(modifier: Modifier = Modifier, viewModel: CalculatorViewModel) {
  val numbers = viewModel.uiState.numbers

  Card(
    modifier = modifier,
    shape = GridShapes,
    elevation = 12.dp
  ) {
    LazyVerticalGrid(
      columns = GridCells.Fixed(GRID_CELL)
    ) {
      numbers.forEachIndexed { index, item ->
        item(span = { GridItemSpan(if (index == 0) 3 else if (index == 16) 2 else 1) }) {
          Item(item, index) {
            viewModel.distance(CalculatorUiAction.Input(it))
          }
        }
      }
    }
  }
}

@Composable
fun Item(text: String = "", index: Int = 0, onClick: (value: String) -> Unit) {
  val color = when (text) {
    "=" -> MaterialTheme.colors.primaryVariant
    "+", "-", "*", "/" -> MaterialTheme.colors.primary
    else -> MaterialTheme.colors.secondary
  }

  val hapticFeedback = LocalHapticFeedback.current
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(color)
      .clip(CircleShape)
      .clickable {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        onClick(text)
      }
      .aspectRatio(if (index == 0) 3 / 1f else if (index == 16) 2 / 1f else 1f),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.h4.copy(
        fontFamily = Font(R.font.pixel).toFontFamily()
      ),
      textAlign = TextAlign.Center,
    )
  }
}

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun TopBar(isDark: MutableState<Boolean>) {
  IconButton(
    onClick = { isDark.value = !isDark.value }
  ) {
    Icon(
      painter = rememberAnimatedVectorPainter(
        animatedImageVector = AnimatedImageVector.animatedVectorResource(id = R.drawable.avd_anim),
        atEnd = isDark.value
      ),
      contentDescription = null,
      modifier = Modifier.size(24.dp)
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalculatorScreenPreview() {
  val isDark = remember { mutableStateOf(false) }
  CalculatorTheme {
    CalculatorScreen(isDark)
  }
}

