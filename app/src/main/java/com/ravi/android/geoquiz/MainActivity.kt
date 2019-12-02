package com.ravi.android.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0


class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var cheatButton: Button
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex


        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        cheatButton = findViewById(R.id.cheat_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        prevButton = findViewById(R.id.prev_button)
        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)

        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)

        }

        nextButton.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            updateQuestion()
            toggleAnswerBtns(true)
        }
        cheatButton.setOnClickListener { view: View ->
           val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val options = ActivityOptions
                    .makeClipRevealAnimation(view, 0, 0, view.width, view.height)
                startActivityForResult(intent, REQUEST_CODE_CHEAT, options.toBundle())
            } else {
                startActivityForResult(intent, REQUEST_CODE_CHEAT)
            }
        }

        prevButton.setOnClickListener { view: View ->
            quizViewModel.moveToPrev()
            updateQuestion()
            toggleAnswerBtns(true)
        }

        question_text_view.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            updateQuestion()
        }

        updateQuestion()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if(requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            val quizCheatShown = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
            quizViewModel.questionCheatBank[quizViewModel.currentIndex] = quizCheatShown ?: false
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
        checkNumberOfCheatsUsed()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle?) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState?.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        toggleAnswerBtns(false)
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            //quizViewModel.isCheater -> R.string.judgment_toast
            quizViewModel.questionCheatBank[quizViewModel.currentIndex] -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()
    }

    private fun toggleAnswerBtns(toggle: Boolean){
        trueButton.isEnabled = toggle
        falseButton.isEnabled = toggle
    }

    private fun checkNumberOfCheatsUsed(){
        val cheats = quizViewModel.currentQuestionCheatNumber
        if(cheats >= 3 ){
            cheatButton.isEnabled = false
        }
    }
//
//    private fun totalQuestionsAnswered(){
//        if (questionsAnswered < 4) {
//            questionsAnswered += 1
//        } else {
//            val scorePecent = (currentScore/questionsAnswered) * 100
//            val message = "You scored $scorePecent %"
//            val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
//            toast.show()
//            questionsAnswered = 0
//            currentScore = 0
//
//        }
//    }
}
