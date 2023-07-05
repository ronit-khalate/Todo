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
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.databinding.FragmentSigninBinding
import com.google.firebase.auth.FirebaseAuth


class SigninFragment : Fragment() {

    private lateinit var auth :FirebaseAuth
    private lateinit var navController:NavController
    private lateinit var binding: FragmentSigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSigninBinding.inflate(inflater,container,false)

        return  binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController=Navigation.findNavController(view=view)
        auth=FirebaseAuth.getInstance()
        registerEvents()

    }

    private fun registerEvents(){

        // going to sign up if new user
        binding.authTextView.setOnClickListener {

            navController.navigate(R.id.action_signinFragment_to_signupFragment)
        }





        binding.nextBtn.setOnClickListener {

            val enteredEmail=binding.emailEt.text.toString()
            val enterdPassword=binding.passEt.text.toString()

            if(enteredEmail.isNotEmpty() && enterdPassword.isNotEmpty()){

                binding.progressBar.visibility=ProgressBar.VISIBLE

                auth.signInWithEmailAndPassword(enteredEmail,enterdPassword)
                    .addOnCompleteListener {task ->

                        if(task.isSuccessful){
                            //! Going to home Page
                            binding.progressBar.visibility=ProgressBar.INVISIBLE
                            Toast.makeText(requireContext(),"Logged in successfully",Toast.LENGTH_SHORT).show()
                            navController.navigate(R.id.action_signinFragment_to_homeFragment)

                        }
                        else{

                            Toast.makeText(requireContext(),task.exception?.message.toString(),Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else
            {
                Toast.makeText(requireContext(),"Empty Fields Not Allowed",Toast.LENGTH_SHORT).show()
            }
     }

    }




}