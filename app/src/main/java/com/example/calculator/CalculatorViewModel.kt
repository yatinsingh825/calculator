package com.example.calculator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable

class CalculatorViewModel : ViewModel() {
    private val _equationText = MutableLiveData("")
    val equationText: LiveData<String> = _equationText
    private val _resultText = MutableLiveData("0")
    val resultText: LiveData<String> = _resultText

    fun onButtonClick(btn: String) {
        Log.i("Clicked Button", btn)

        _equationText.value?.let { currentEquation ->
            if (btn == "AC") {
                _equationText.value = ""
                _resultText.value = "0"
                return
            }

            if (btn == "C") {
                if (currentEquation.isNotEmpty()) {
                    _equationText.value = currentEquation.substring(0, currentEquation.length - 1)
                }
                return
            }
        }

        if (btn == "=") {
            try {
                _resultText.value = calculateResult(_equationText.value ?: "")
            } catch (e: Exception) {
                Log.e("CalculatorError", "Error evaluating equation", e)
                _resultText.value = "Error"
            }
            return
        }

        _equationText.value = (_equationText.value ?: "") + btn
    }

    private fun calculateResult(equation: String): String {
        val context: Context = Context.enter()
        context.optimizationLevel = -1
        val scriptable: Scriptable = context.initStandardObjects()
        val finalResult = context.evaluateString(scriptable, equation, "JavaScript", 1, null).toString()
        Context.exit()
        return finalResult
    }
}
