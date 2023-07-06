package com.example.todo.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.databinding.TodoItemViewBinding

class TodoAdapter(val taskList:MutableList<TotoData>):RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private var listener : ToDoAdapterClickInterface?=null


    fun setListener(listener:ToDoAdapterClickInterface){

        this.listener=listener
    }
    inner class ViewHolder(val binding: TodoItemViewBinding):RecyclerView.ViewHolder(binding.root){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = TodoItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder){

            with(taskList[position]){

                binding.taskText.text=this.task

                binding.deleteTask.setOnClickListener {

                    listener?.onDeleteBtnClicked(this)
                }

                binding.editTask.setOnClickListener {
                    listener?.onEditTaskBtnClicked(this)
                }
            }
        }
    }

    interface ToDoAdapterClickInterface{

        fun onDeleteBtnClicked(totoData: TotoData)
        fun onEditTaskBtnClicked(totoData: TotoData)
    }
}