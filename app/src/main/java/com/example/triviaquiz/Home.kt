package com.example.triviaquiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import androidx.navigation.Navigation

/**
 * A simple [Fragment] subclass.
 */
class Home : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val button = view?.findViewById<Button>(R.id.button_start);
        button?.setOnClickListener {
            val action = HomeDirections.actionHomeToQuestionList()
            val editText = view?.findViewById<AppCompatEditText>(R.id.input_name)
            val inputName = editText?.text.toString()
            action.userName = inputName
            Navigation.findNavController(button).navigate(action)
        }
    }

}
