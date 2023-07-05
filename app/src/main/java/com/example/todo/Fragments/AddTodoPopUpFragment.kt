package com.example.todo.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.todo.R
import com.example.todo.databinding.FragmentAddTodoPopUpBinding
import com.google.android.material.textfield.TextInputEditText


class AddTodoPopUpFragment : DialogFragment() {

    private lateinit var binding: FragmentAddTodoPopUpBinding
    private lateinit var  listener :DialogNextBtnClickListener

    fun setListener(listener: DialogNextBtnClickListener){

        this.listener=listener
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding= FragmentAddTodoPopUpBinding.inflate(inflater,container,false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvent()
    }

    private fun registerEvent() {

        binding.todoNextBtn.setOnClickListener {

            val todoTask=binding.todoEt.text.toString()

            if(todoTask.isNotEmpty()){

                listener.onSaveTask(todoTask,binding.todoEt)
            }
            else{
                Toast.makeText(requireContext(),"Please type some task",Toast.LENGTH_SHORT).show()
            }
        }

        binding.todoClose.setOnClickListener {

            dismiss()
        }
    }

    interface  DialogNextBtnClickListener{
        fun onSaveTask(todo :String , todoEt:TextInputEditText)
    }


}