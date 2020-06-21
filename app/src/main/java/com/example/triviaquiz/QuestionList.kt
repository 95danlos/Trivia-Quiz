package com.example.triviaquiz

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.floor

/**
 * A simple [Fragment] subclass.
 */
class QuestionList : Fragment() {

    private var questions: JSONArray = JSONArray()
    private var nextQuestionNumber: Int = 0
    private var userName = "Anonymous"
    private var userAnswers = JSONArray()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        arguments?.let {
            val safeArgs = QuestionListArgs.fromBundle(it)
            userName = safeArgs.userName
        }

        val queue = Volley.newRequestQueue(activity?.applicationContext)
        val url = "https://opentdb.com/api.php?amount=10&encode=base64"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d("", response.toString())
                setupQuestions(response)
            },
            Response.ErrorListener { error ->
                Log.d("", error.toString())
            }
        )
        queue.add(jsonObjectRequest)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_list, container, false)
    }


    private fun setupQuestions(json: JSONObject) {
        val questionContainer = view?.findViewById<LinearLayout>(R.id.question_container)
        questionContainer?.visibility = LinearLayout.VISIBLE;

        questions = json.get("results") as JSONArray

        for (i in 0 until questions.length()) {
            val question: JSONObject = questions.get(i) as JSONObject
            decodeQuestionFromBase64(question)
            createQuestionOptions(question)
        }
        createButtonEventListeners()
        displayNextQuestion(true)
    }


    private fun createQuestionOptions(question: JSONObject) {
        val incorrectOptions: JSONArray = question.get("incorrect_answers") as JSONArray
        var randomIndex: Int = 0

        if(question.get("type").toString() == "multiple") {
            randomIndex = floor(Math.random() * 3.99).toInt()
        }
        else if(question.get("type").toString() == "boolean") {
            randomIndex = floor(Math.random() * 1.99).toInt()
        }

        val options: JSONArray = JSONArray()

        var j = 0
        var k = 0
        while (j < incorrectOptions.length() + 1) {
            if(j == randomIndex) {
                options.put(question.get("correct_answer"))
            }
            else {
                options.put(incorrectOptions[k])
                k++
            }
            j++
        }
        question.put("options", options)
    }


    private fun createButtonEventListeners() {
        for (i in 1..4) {
            val buttonID = resources.getIdentifier("option_$i", "id", activity?.packageName);
            val button = view?.findViewById<Button>(buttonID)
            button?.setOnClickListener {
                displayNextQuestion(false, button.text.toString())
            }
        }
    }


    private fun displayNextQuestion(isFirstQuestion: Boolean, answer: String = "") {

        if(!isFirstQuestion) {
            userAnswers.put(answer)
        }

        if(nextQuestionNumber == questions.length()) {
            val action = QuestionListDirections.actionQuestionListToResults()
            action.userName = userName
            action.questions = questions.toString()
            action.answers = userAnswers.toString()
            activity?.findNavController(R.id.option_1)?.navigate(action)
        }
        else {
            val question: JSONObject = questions.get(nextQuestionNumber) as JSONObject

            val questionCategory = view?.findViewById<TextView>(R.id.question_category)
            val questionDifficulty = view?.findViewById<TextView>(R.id.question_difficulty)
            val questionText = view?.findViewById<TextView>(R.id.question)

            questionCategory?.text = question.get("category").toString()
            questionDifficulty?.text = question.get("difficulty").toString().capitalize()
            questionText?.text = question.get("question").toString()

            val questionOptions: JSONArray = question.get("options") as JSONArray

            for (i in 0 until questionOptions.length()) {
                val buttonID =
                    resources.getIdentifier("option_${i + 1}", "id", activity?.packageName);
                val button = view?.findViewById<Button>(buttonID)
                if (button != null) {
                    button.text = questionOptions.get(i).toString()
                }
            }

            val button3 = view?.findViewById<Button>(R.id.option_3)
            val button4 = view?.findViewById<Button>(R.id.option_4)

            if (question.get("type").toString() == "boolean") {
                button3?.visibility = Button.GONE
                button4?.visibility = Button.GONE
            } else {
                button3?.visibility = Button.VISIBLE
                button4?.visibility = Button.VISIBLE
            }
            nextQuestionNumber++
        }
    }


    private fun decodeQuestionFromBase64(question: JSONObject) {
        val keys: Iterator<String> = question.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            if (question.get(key) is JSONArray) {
                val questionOptions: JSONArray = question.get(key) as JSONArray
                for (i in 0 until questionOptions.length()) {
                    val decodedText: String = String(android.util.Base64.decode(questionOptions.get(i).toString(), android.util.Base64.DEFAULT))
                    questionOptions.put(i, decodedText)
                }
            }
            else {
                val decodedText: String = String(android.util.Base64.decode(question.get(key).toString(), android.util.Base64.DEFAULT))
                question.put(key.toString(), decodedText)
            }
        }
    }

}
