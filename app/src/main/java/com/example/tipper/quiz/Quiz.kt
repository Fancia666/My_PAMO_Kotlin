package com.example.tipper.quiz

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.security.SecureRandom
import java.util.*

class Quiz() : Fragment() {
    private var correctAnswer // correct country for the current flag
            : String? = null
    private var totalGuesses // number of guesses made
            = 0
    private var correctAnswers // number of correct guesses
            = 0
    private var guessRows // number of rows displaying guess Buttons
            = 0
    private var random // used to randomize the quiz
            : SecureRandom? = null
    private var handler // used to delay loading next flag
            : Handler? = null
    private var quizLinearLayout // layout that contains the quiz
            : LinearLayout? = null
    private var questionNumberTextView // shows current question #
            : TextView? = null
    private var healthTextView // displays a question
            : TextView? = null
    private lateinit var guessLinearLayouts // rows of answer Buttons
            : Array<LinearLayout?>
    private var answerTextView // displays correct answer
            : TextView? = null
    var listOfQuestions: MutableMap<String, Map<String, Boolean>> = HashMap()

    //
    // configures the MainActivityFragment when its View is created
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_quiz2, container, false)
        random = SecureRandom()
        handler = Handler()

        // get references to GUI components
        quizLinearLayout = view.findViewById<View>(R.id.quizLinearLayout) as LinearLayout
        questionNumberTextView = view.findViewById<View>(R.id.questionNumberTextView) as TextView
        healthTextView = view.findViewById<View>(R.id.guessHealthTextView) as TextView
        guessLinearLayouts = arrayOfNulls(4)
        guessLinearLayouts[0] = view.findViewById<View>(R.id.row1LinearLayout) as LinearLayout
        guessLinearLayouts[1] = view.findViewById<View>(R.id.row2LinearLayout) as LinearLayout
        guessLinearLayouts[2] = view.findViewById<View>(R.id.row3LinearLayout) as LinearLayout
        guessLinearLayouts[3] = view.findViewById<View>(R.id.row4LinearLayout) as LinearLayout
        answerTextView = view.findViewById<View>(R.id.answerTextView) as TextView

        // configure listeners for the guess Buttons
        for (row: LinearLayout? in guessLinearLayouts) {
            for (column in 0 until row!!.childCount) {
                val button = row.getChildAt(column) as Button
                button.setOnClickListener(guessButtonListener)
            }
        }

        // set questionNumberTextView's text
        questionNumberTextView!!.text = getString(R.string.question, 1, FLAGS_IN_QUIZ)
        return view // return the fragment's view for display
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateGuessRows()
        resetQuiz()
    }

    // update guessRows based on value in SharedPreferences
    fun updateGuessRows() {
        // get the number of guess buttons that should be displayed
        guessRows = 4 / 2

        // hide all quess button LinearLayouts
        for (layout: LinearLayout? in guessLinearLayouts) layout!!.visibility = View.GONE

        // display appropriate guess button LinearLayouts
        for (row in 0 until guessRows) guessLinearLayouts.get(row)!!.visibility = View.VISIBLE
    }

    // set up and start the next quiz
    fun resetQuiz() {
        val assets = requireActivity().assets
        listOfQuestions = HashMap()
        try {
            val `is` = assets.open("quiz.txt")
            val targetReader: Reader = InputStreamReader(`is`)
            val reader: BufferedReader
            reader = BufferedReader(targetReader)
            var line = reader.readLine()
            var row = 1
            var question = ""
            var answers: MutableMap<String, Boolean> = HashMap()
            while (line != null) {
                if (row == 1) {
                    question = line
                }
                if (row > 1 && row <= 5) {
                    answers[line.replace("- ", "")
                        .replace("<-", "")] = line.contains("<-")
                }
                if (row > 4) {
                    listOfQuestions[question] = answers
                    question = ""
                    answers = HashMap()
                    row = 0
                }
                // read next line
                line = reader.readLine()
                row++
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        correctAnswers = 0 // reset the number of correct answers made
        totalGuesses = 0 // reset the total number of guesses the user made
        loadNextQuestion() // start the quiz by loading the first flag
    }

    // after the user guesses a correct flag, load the next flag
    private fun loadNextQuestion() {
        println(listOfQuestions)
        val question = listOfQuestions.keys.stream().findAny()
        println(question.get())
        val answers = (listOfQuestions[question.get()])!!
        val allAnswers: MutableList<String?> = ArrayList()
        for (entry: Map.Entry<String, Boolean> in answers.entries) {
            println(entry.key + ":" + entry.value)
            allAnswers.add(entry.key)
            if (entry.value) {
                correctAnswer = entry.key
            }
        }
        println(answers)
        println(correctAnswer)

        // get file name of the next flag and remove it from the list
        answerTextView!!.text = "" // clear answerTextView

        // display current question number
        questionNumberTextView!!.text = getString(
            R.string.question, (correctAnswers + 1), FLAGS_IN_QUIZ
        )
        healthTextView!!.text = question.get()
        val ddd = listOfQuestions.remove(question.get(), answers)
        println(ddd)
        println(listOfQuestions)
        Collections.shuffle(allAnswers) // shuffle file names

        // put the correct answer at the end of fileNameList
        val correct = allAnswers.indexOf(correctAnswer)
        allAnswers.add(allAnswers.removeAt(correct))

        // add 2, 4, 6 or 8 guess Buttons based on the value of guessRows
        for (row in 0 until guessRows) {
            // place Buttons in currentTableRow
            for (column in 0 until guessLinearLayouts[row]!!.childCount) {
                // get reference to Button to configure
                val newGuessButton = guessLinearLayouts[row]!!.getChildAt(column) as Button
                newGuessButton.isEnabled = true

                // get country name and set it as newGuessButton's text
                val filename = allAnswers[(row * 2) + column]
                newGuessButton.text = filename
            }
        }
    }

    // animates the entire quizLinearLayout on or off screen
    private fun animate(animateOut: Boolean) {
        // prevent animation into the the UI for the first flag
        if (correctAnswers == 0) return

        // calculate center x and center y
        val centerX = (quizLinearLayout!!.left +
                quizLinearLayout!!.right) / 2 // calculate center x
        val centerY = (quizLinearLayout!!.top +
                quizLinearLayout!!.bottom) / 2 // calculate center y

        // calculate animation radius
        val radius = Math.max(
            quizLinearLayout!!.width,
            quizLinearLayout!!.height
        )
        val animator: Animator

        // if the quizLinearLayout should animate out rather than in
        if (animateOut) {
            // create circular reveal animation
            animator = ViewAnimationUtils.createCircularReveal(
                quizLinearLayout, centerX, centerY, radius.toFloat(), 0f
            )
            animator.addListener(
                object : AnimatorListenerAdapter() {
                    // called when the animation finishes
                    override fun onAnimationEnd(animation: Animator) {
                        loadNextQuestion()
                    }
                }
            )
        } else { // if the quizLinearLayout should animate in
            animator = ViewAnimationUtils.createCircularReveal(
                quizLinearLayout, centerX, centerY, 0f, radius.toFloat()
            )
        }
        animator.duration = 500 // set animation duration to 500 ms
        animator.start() // start the animation
    }

    // called when a guess Button is touched
    private val guessButtonListener: View.OnClickListener = object : View.OnClickListener {
        override fun onClick(v: View) {
            val guessButton = (v as Button)
            val guess = guessButton.text.toString()
            val answer = correctAnswer
            ++totalGuesses // increment number of guesses the user has made
            if ((guess == answer)) { // if the guess is correct
                ++correctAnswers // increment the number of correct answers

                // display correct answer in green text
                answerTextView!!.text = "$answer!"
                answerTextView!!.setTextColor(
                    resources.getColor(
                        R.color.purple_200,
                        context!!.theme
                    )
                )
                disableButtons() // disable all guess Buttons

                // if the user has correctly identified FLAGS_IN_QUIZ flags
                if (correctAnswers == FLAGS_IN_QUIZ) {
                    onButtonShowPopupWindowClick()
                } else { // answer is correct but quiz is not over
                    // load the next flag after a 2-second delay
                    handler!!.postDelayed(
                        Runnable {
                            animate(true) // animate the flag off the screen
                        }, 2000
                    ) // 2000 milliseconds for 2-second delay
                }
            } else { // answer was incorrect

                // display "Incorrect!" in red
                answerTextView!!.setText(R.string.incorrect_answer)
                answerTextView!!.setTextColor(
                    resources.getColor(
                        R.color.bmi_button, context!!.theme
                    )
                )
                guessButton.isEnabled = false // disable incorrect answer
            }
        }
    }

    fun onButtonShowPopupWindowClick() {
        // inflate the layout of the popup window
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.activity_popup, null)

        // create the popup window
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true // lets taps outside the popup also dismiss it
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        val popupTextView = popupView.findViewById<View>(R.id.popupTextView) as TextView
        popupTextView.text = getString(
            R.string.results,
            totalGuesses,
            (1000 / totalGuesses.toDouble())
        )
        val popupButton = popupView.findViewById<View>(R.id.popupButton) as Button
        popupButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                resetQuiz()
                popupWindow.dismiss()
            }
        })

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    // utility method that disables all answer Buttons
    private fun disableButtons() {
        for (row in 0 until guessRows) {
            val guessRow = guessLinearLayouts[row]
            for (i in 0 until guessRow!!.childCount) guessRow.getChildAt(i).isEnabled = false
        }
    }

    companion object {
        private val TAG = "FlagQuiz Activity"
        private val FLAGS_IN_QUIZ = 2
    }
}