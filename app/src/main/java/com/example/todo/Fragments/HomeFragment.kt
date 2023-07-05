package com.example.todo.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todo.R
import com.example.todo.databinding.FragmentHomeBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class HomeFragment : Fragment(),AddTodoPopUpFragment.DialogNextBtnClickListener {

    private lateinit var auth:FirebaseAuth
    private lateinit var dbReference :DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding

    private lateinit var popUpFragment :AddTodoPopUpFragment
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

        registerEvent()
    }

    private fun registerEvent() {

        popUpFragment= AddTodoPopUpFragment()

        popUpFragment!!.setListener(this)
        popUpFragment.show(
            childFragmentManager,
            "AddTodoPopUpFragment"
        )

        binding.addNewTaskBtn.setOnClickListener {

            popUpFragment.setListener(this)
            popUpFragment.show(
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

    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {

        dbReference.push().setValue(todo ).addOnCompleteListener{

            if(it.isSuccessful){
                Toast.makeText(requireContext(),"Task saved successfully",Toast.LENGTH_SHORT).show()
                todoEt.text?.clear()

            }else{

                Toast.makeText(requireContext(),it.exception?.message.toString(),Toast.LENGTH_SHORT).show()
            }

            popUpFragment.dismiss()
        }

    }


}