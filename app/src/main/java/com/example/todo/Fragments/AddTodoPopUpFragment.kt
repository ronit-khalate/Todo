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
import com.example.todo.utils.TotoData
import com.google.android.material.textfield.TextInputEditText


class AddTodoPopUpFragment : DialogFragment() {

    private lateinit var binding: FragmentAddTodoPopUpBinding
    private lateinit var listener: DialogNextBtnClickListener

    private var todoData: TotoData? = null

    fun setListener(listener: DialogNextBtnClickListener) {

        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentAddTodoPopUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            todoData =
                TotoData(
                    arguments?.getString("taskId").toString(),
                    arguments?.getString("task").toString()
                )

            binding.todoEt.setText(todoData?.task)
        }
        registerEvent()
    }

    companion object {

        const val TAG = "AddTodoPopupFragment"

        @JvmStatic
        fun newInstance(tasId: String, task: String) = AddTodoPopUpFragment().apply {

            arguments = Bundle().apply {

                putString("taskId", tasId)
                putString("task", task)
            }
        }
    }

    private fun registerEvent() {

        binding.todoNextBtn.setOnClickListener {

            val todoTask = binding.todoEt.text.toString()

            if (todoTask.isNotEmpty()) {

                if(todoData==null ){

                    listener.onSaveTask(todoTask,binding.todoEt)
                }
                else{
                    todoData?.task=todoTask
                    listener.onUpdateTask(todoData!!,binding.todoEt)
                }

            } else {
                Toast.makeText(requireContext(), "Please type some task", Toast.LENGTH_SHORT).show()
            }
        }

        binding.todoClose.setOnClickListener {

            dismiss()
        }
    }

    interface DialogNextBtnClickListener {
        fun onSaveTask(todo: String, todoEt: TextInputEditText)
        fun onUpdateTask(todoData: TotoData, todoEt: TextInputEditText)
    }


}