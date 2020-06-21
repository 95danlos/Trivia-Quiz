package com.example.triviaquiz

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import org.json.JSONArray
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class Results : Fragment() {

    private var userName: String = "Anonymous"
    private var questions: JSONArray = JSONArray()
    private var answers: JSONArray = JSONArray()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            val safeArgs = ResultsArgs.fromBundle(it)
            userName = safeArgs.userName
            questions = JSONArray(safeArgs.questions)
            answers = JSONArray(safeArgs.answers)
        }

        val answersView = view?.findViewById<LinearLayout>(R.id.answers)

        val headerText: TextView = TextView(context)
        val headerTextparams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        headerTextparams.setMargins(20)
        headerTextparams.gravity = Gravity.CENTER_HORIZONTAL
        headerText.layoutParams = headerTextparams

        headerText.text = "Score"
        headerText.textSize = 30.0F
        headerText.typeface = Typeface.createFromAsset(
            context?.assets,
            "font/londrina_solid_light.ttf")
        answersView?.addView(headerText)


        val scoreText: TextView = TextView(context)
        val scoreTextparams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)

        scoreTextparams.setMargins(0, 0, 0, 20)
        scoreTextparams.gravity = Gravity.CENTER_HORIZONTAL
        scoreText.layoutParams = scoreTextparams

        var score: Int = 0
        for (i in 0 until answers.length()) {
            if(answers.get(i).toString() == (questions.get(i) as JSONObject).get("correct_answer").toString()) {
                score++
            }
        }

        scoreText.text = score.toString() + " / " + questions.length().toString()
        scoreText.textSize = 20.0F
        scoreText.typeface = Typeface.createFromAsset(
            context?.assets,
            "font/londrina_solid_light.ttf")
        answersView?.addView(scoreText)


        for (i in 0 until answers.length()) {
            val answerContainer: LinearLayout = LinearLayout(context)
            val answerContainerparams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

            answerContainerparams.setMargins(20)
            answerContainer.layoutParams = answerContainerparams
            answerContainer.orientation = LinearLayout.VERTICAL

            answersView?.addView(answerContainer)

            var textColor = Color.RED
            if(answers.get(i).toString() == (questions.get(i) as JSONObject).get("correct_answer").toString()) {
                textColor = Color.parseColor("#006400")
            }


            val questionText: TextView = TextView(context)
            val questionTextparams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)

            questionTextparams.setMargins(0, 25, 0 ,25)
            questionText.layoutParams = questionTextparams

            questionText.text = (questions.get(i) as JSONObject).get("question").toString()
            questionText.textSize = 16.0F
            questionText.setTypeface(null, Typeface.BOLD)
            questionText.typeface = Typeface.createFromAsset(
                context?.assets,
                "font/londrina_solid_black.ttf")
            answerContainer?.addView(questionText)


            val userAnswerText: TextView = TextView(context)
            val userAnswerTextparams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

            userAnswerTextparams.setMargins(0, 20, 0 , 10)
            userAnswerText.layoutParams = userAnswerTextparams

            userAnswerText.text = answers.get(i).toString()
            userAnswerText.textSize = 16.0F
            userAnswerText.typeface = Typeface.createFromAsset(
                context?.assets,
                "font/londrina_solid_light.ttf")
            userAnswerText.setTextColor(textColor)
            answerContainer?.addView(userAnswerText)


            val correctAnswerText: TextView = TextView(context)
            correctAnswerText.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

            correctAnswerText.text = (questions.get(i) as JSONObject).get("correct_answer").toString()
            correctAnswerText.textSize = 16.0F
            correctAnswerText.typeface = Typeface.createFromAsset(
                context?.assets,
                "font/londrina_solid_light.ttf")
            answerContainer?.addView(correctAnswerText)
        }
    }

}
