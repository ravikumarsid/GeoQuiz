package com.ravi.android.geoquiz

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))

    var questionCheatBank: BooleanArray = BooleanArray(questionBank.size){false}

    private var currentScore = 0
    private var questionsAnswered = 0
    var isCheater = false

    var currentIndex: Int = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val currentQuestionCheatStatus: Boolean
        get() = questionCheatBank[currentIndex]

    val currentQuestionCheatNumber: Int
        get() = questionCheatBank.count {it}

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        if (currentIndex > 0){
            currentIndex = (currentIndex - 1) % questionBank.size
        }
    }

}