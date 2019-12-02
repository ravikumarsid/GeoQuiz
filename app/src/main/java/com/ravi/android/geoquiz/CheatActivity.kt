package com.ravi.android.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.TextView


const val EXTRA_ANSWER_SHOWN = "com.ravi.android.geoquiz.answer_shown"
const val EXTRA_ANSWER_SHOWN_INDEX = "com.ravi.android.geoquiz.answer_index"
private const val EXTRA_ANSWER_IS_TRUE =
    "com.ravi.android.geoquiz.answer_is_true"
private const val ANSWER_SHOWN = "ANSWER_SHOWN"


class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var apiLevelTextView: TextView
    private var answerIsTrue = false
    private var cheatAnswerShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        val isPrevCheatAnswerShown = savedInstanceState?.getBoolean(ANSWER_SHOWN, false) ?: false
        if(isPrevCheatAnswerShown){
            setAnswerShownResult(true)
        }

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiLevelTextView = findViewById(R.id.api_level_text_view)
        apiLevelTextView.text = getString(R.string.api_level, Build.VERSION.SDK_INT)
        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            answerTextView.setText(answerText)
            setAnswerShownResult(true)
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle?) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState?.putBoolean(ANSWER_SHOWN, cheatAnswerShown)

    }

    private fun setAnswerShownResult(isAnswerShown: Boolean){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        cheatAnswerShown = isAnswerShown
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}
