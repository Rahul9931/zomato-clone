package com.example.zomato

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.zomato.databinding.ActivityLoginBinding
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
import com.google.firebase.ktx.Firebase

class Login_Activity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var googleSignInClient: GoogleSignInClient
    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // initialize
        auth = Firebase.auth
        database= FirebaseDatabase.getInstance().getReference()
        // initialize google signin
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        binding.btnLogin.setOnClickListener {
            email = binding.emailLogin.text.toString().trim()
            password = binding.passwordLogin.text.toString().trim()

            if (!email.isEmpty() && !password.isEmpty()){
                //val userid = FirebaseAuth.getInstance().currentUser!!.uid
                signInAccount(email,password)
            }
            else{
                Toast.makeText(this,"Please fill all fields", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnGoogleSignin.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcer.launch(signIntent)
        }

        binding.dontAcc.setOnClickListener {
            val intent = Intent(this, Signup_activity::class.java)
            startActivity(intent)
        }

    }

    private fun signInAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                result ->
            if (result.isSuccessful){
                val user = auth.currentUser
                Toast.makeText(this, "Successfully SignIn", Toast.LENGTH_SHORT).show()
                updateUi(user)
            }
            else{
                Toast.makeText(this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show()
            }
        }

    }
    // Sign in with google
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
                Toast.makeText(this, "Google SignIn failed", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "${result.resultCode} == ${Activity.RESULT_OK}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}