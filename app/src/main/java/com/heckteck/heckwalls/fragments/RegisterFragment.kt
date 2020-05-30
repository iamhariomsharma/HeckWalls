package com.heckteck.heckwalls.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.heckteck.heckwalls.R
import kotlinx.android.synthetic.main.fragment_register.*

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        if (firebaseAuth.currentUser == null) {
            registerText.text = "Creating New Account"

            firebaseAuth.signInAnonymously().addOnCompleteListener {
                if (it.isSuccessful) {
                    registerText.text = "Account Created, Logging in"
                    navController?.navigate(R.id.action_registerFragment_to_homeFragment)
                } else {
                    registerText.text = "Error: ${it.exception?.message}"
                }
            }
        } else {
            navController?.navigate(R.id.action_registerFragment_to_homeFragment)
        }
    }

}
