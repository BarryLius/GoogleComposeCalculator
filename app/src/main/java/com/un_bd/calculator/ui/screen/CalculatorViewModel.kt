package com.un_bd.calculator.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import javax.script.ScriptEngineManager

class CalculatorViewModel : ViewModel() {

  var uiState by mutableStateOf(CalculatorUiState())

  fun distance(uiAction: CalculatorUiAction) {
    when (uiAction) {
      is CalculatorUiAction.Input -> input(uiAction.value)
    }
  }

  private fun input(value: String) {
    val screenOutInput = uiState.screenOutInput

    if (uiState.clickEqual) {
      uiState = uiState.copy(screenOutInput = "0", screenOutResult = "", clickEqual = false)
    }

    uiState = uiState.copy(
      screenOutInput = when (value) {
        "AC" -> "0"
        "." -> {
          if (screenOutInput.contains(".")) {
            uiState.screenOutInput
          } else {
            uiState.screenOutInput.plus(value)
          }
        }
        "0" -> {
          if (screenOutInput == "0") {
            uiState.screenOutInput
          } else {
            uiState.screenOutInput.plus(value)
          }
        }
        "1", "2", "3", "4", "5", "6", "7", "8", "9" -> {
          if (screenOutInput == "0") {
            uiState = uiState.copy(screenOutInput = "")
          }
          uiState.screenOutInput.plus(value)
        }
        else -> uiState.screenOutInput
      },
      screenOutResult = when (value) {
        "AC" -> ""
        "+", "-", "*", "/", "=" -> {
          uiState.screenOutResult + uiState.screenOutInput + "\t$value\t"
        }
        else -> {
          uiState.screenOutResult
        }
      }
    )

    when (value) {
      "+", "-", "*", "/" -> {
        uiState = uiState.copy(screenOutInput = "0")
      }
      "=" -> {
        uiState = uiState.copy(screenOutInput = "0")
        val outResult = uiState.screenOutResult.replace("=", "")
        val engine = ScriptEngineManager().getEngineByName("rhino")
        val results = engine.eval(outResult).toString()
        uiState =
          uiState.copy(screenOutResult = uiState.screenOutResult.plus(results), clickEqual = true)
      }
    }
  }
}

data class CalculatorUiState(
  val screenOutInput: String = "0",
  val screenOutResult: String = "",
  val numbers: List<String> = listOf(
    "AC", "+",
    "1", "2", "3", "-",
    "4", "5", "6", "*",
    "7", "8", "9", "/",
    "0", ".", "="
  ),
  val clickEqual: Boolean = false,
)

sealed class CalculatorUiAction {
  class Input(val value: String) : CalculatorUiAction()
}

