package com.un_bd.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.un_bd.calculator.ui.screen.CalculatorScreen
import com.un_bd.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    super.onCreate(savedInstanceState)
    setContent {
      val isDark = remember { mutableStateOf(false) }
      CalculatorTheme(darkTheme = isDark.value) {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
          CalculatorScreen(isDark)
        }
      }
    }
  }
}

val WIDE_LAYOUT_MIN_WIDTH = 600.dp

fun BoxWithConstraintsScope.isWideLayout() = maxWidth >= WIDE_LAYOUT_MIN_WIDTH

@Composable
fun Greeting(name: String) {
  Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  CalculatorTheme {
    Greeting("Android")
  }
}