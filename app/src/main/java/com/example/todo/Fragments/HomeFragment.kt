package com.example.todo.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo.R
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.utils.TodoAdapter
import com.example.todo.utils.TotoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment()
    ,AddTodoPopUpFragment.DialogNextBtnClickListener
    ,TodoAdapter.ToDoAdapterClickInterface{

    private lateinit var auth:FirebaseAuth
    private lateinit var dbReference :DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding

    private lateinit var adapder :TodoAdapter

    private  var popUpFragment :AddTodoPopUpFragment?=null
    private lateinit var taskList: MutableList<TotoData>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding= FragmentHomeBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        getDataFromFirebase()
        registerEvent()
    }

    private fun registerEvent() {



        binding.addNewTaskBtn.setOnClickListener {

            if(popUpFragment!=null){
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
            }
            popUpFragment= AddTodoPopUpFragment()
            popUpFragment?.setListener(this)
            popUpFragment?.show(
                childFragmentManager,
                "AddTodoPopUpFragment"
            )
        }
    }

    private fun init(view: View) {

        navController=Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()
        dbReference=FirebaseDatabase.getInstance().reference.child("Tasks")
            .child(auth.currentUser?.uid.toString())


        binding.rv.setHasFixedSize(true)
        binding.rv.layoutManager=LinearLayoutManager(context)
        taskList= mutableListOf()
        adapder= TodoAdapter(taskList)
        adapder.setListener(this)

        binding.rv.adapter=adapder

    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {

        dbReference.push().setValue(todo ).addOnCompleteListener{

            if(it.isSuccessful){
                Toast.makeText(requireContext(),"Task saved successfully",Toast.LENGTH_SHORT).show()
                todoEt.text?.clear()

            }else{

                Toast.makeText(requireContext(),it.exception?.message.toString(),Toast.LENGTH_SHORT).show()
            }


            popUpFragment?.dismiss()
        }

    }

    override fun onUpdateTask(todoData: TotoData, todoEt: TextInputEditText) {
        val map = HashMap<String,Any>()

        map[todoData.taskID]=todoData.task

        dbReference.updateChildren(map).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(requireContext(),"Task Updated Successfully",Toast.LENGTH_SHORT).show()
                todoEt.text=null
            }else{
                Toast.makeText(requireContext(),it.exception?.message,Toast.LENGTH_SHORT).show()

            }
            todoEt.text=null
            popUpFragment?.dismiss()
        }
    }

    override fun onDeleteBtnClicked(totoData: TotoData) {

        dbReference.child(totoData.taskID).removeValue().addOnCompleteListener{

            if(it.isSuccessful){

                Toast.makeText(requireContext(),"Task Deleted Successfully",Toast.LENGTH_SHORT).show()

            }else{
                Toast.makeText(requireContext(),it.exception?.message.toString(),Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onEditTaskBtnClicked(totoData: TotoData) {

        if (popUpFragment != null) {
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
        }

        popUpFragment = AddTodoPopUpFragment.newInstance(totoData.taskID, totoData.task)

        popUpFragment?.setListener(this)
        popUpFragment?.show(childFragmentManager, AddTodoPopUpFragment.TAG)

    }
    fun getDataFromFirebase() {

        binding.rvProgressBar.visibility=ProgressBar.VISIBLE
        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                taskList.clear()

                snapshot.children.forEach {
                    val task = it.key?.let { key ->

                        TotoData(taskID = key, task = it.value.toString())
                    }

                    if (task != null) {
                        taskList.add(task)
                    }
                }

                adapder.notifyDataSetChanged()

                binding.rvProgressBar.visibility=ProgressBar.INVISIBLE

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }


        })
    }



}