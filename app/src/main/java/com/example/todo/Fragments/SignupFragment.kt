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
import com.example.todo.R
import com.example.todo.databinding.FragmentSignupBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


class SignupFragment : Fragment() {

    private lateinit var auth:FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentSignupBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        init(view)
    }

    private fun init(view: View) {
        navController=Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()
        registerEvents()

    }

    private fun registerEvents(){

        // going to  Sign in
        binding.authTextView.setOnClickListener {

            navController.navigate(R.id.action_signupFragment_to_signinFragment)
        }

        binding.nextBtn.setOnClickListener {

            val email = binding.emailEt.text.toString()
            val pass= binding.passEt.text.toString()
            val verifyPass = binding.rePassEt.text.toString().trim()

            if( email.isNotEmpty() && pass.isNotEmpty() && verifyPass.isNotEmpty()){

                binding.progressBar.visibility=ProgressBar.VISIBLE
                if(pass==verifyPass){
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {

                        if (it.isSuccessful) {

                            binding.progressBar.visibility=ProgressBar.INVISIBLE
                            Toast.makeText(
                                requireContext(),
                                "Registered Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate(R.id.action_signupFragment_to_homeFragment)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                it.exception?.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }


                    }
                }
            }
            else{

                Toast.makeText(requireContext(),"Empty Fields Not Allowed",Toast.LENGTH_SHORT).show()
            }


            }
    }


}