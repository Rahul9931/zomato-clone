package com.example.zomato

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.zomato.databinding.ActivitySignupBinding
import com.example.zomato.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Signup_activity : AppCompatActivity() {
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // initialize
        auth = Firebase.auth
        database = Firebase.database.reference
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions)

        binding.alreadyAcc.setOnClickListener {
            val intent = Intent(this, Login_Activity::class.java)
            startActivity(intent)
        }

        binding.btnCreateAccount.setOnClickListener {
            name = binding.nameSignup.text.toString().trim()
            email = binding.emailSignup.text.toString().trim()
            password = binding.passwordSignup.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please Fill All Field", Toast.LENGTH_SHORT).show()
            } else {
                createAccount(email, password)
            }
        }

        binding.btnGoogleSignup.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcer.launch(signIntent)
        }
    }

    // Sign up with google
    private val launcer = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account : GoogleSignInAccount = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener {authtask ->
                    if (authtask.isSuccessful){
                        Toast.makeText(this, "Successful SignIn with Google", Toast.LENGTH_SHORT).show()
                        updateUi(authtask.result?.user)
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Google SignIn failed", Toast.LENGTH_SHORT).show()

                    }
                }
            }
            else{
                Toast.makeText(this, "Google SignIn failed2", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "${result.resultCode} == ${Activity.RESULT_OK}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                saveUserData()
                Toast.makeText(this, "Account Created Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "${task.exception}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun saveUserData() {
        name = binding.nameSignup.text.toString().trim()
        email = binding.emailSignup.text.toString().trim()
        password = binding.passwordSignup.text.toString().trim()
        val user = UserModel(name, email, password)
        val userid = FirebaseAuth.getInstance().currentUser!!.uid

        database.child("User").child(userid).setValue(user)
    }
    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}