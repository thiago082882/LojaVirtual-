package com.example.lojavirtual.activities.formLogin

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.lojavirtual.R
import com.example.lojavirtual.activities.formCadastro.FormCadastro
import com.example.lojavirtual.activities.telaProdutos.TelaPrincipalProdutos
import com.example.lojavirtual.databinding.ActivityFormLoginBinding
import com.example.lojavirtual.dialog.DialogCarregando
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class FormLogin : AppCompatActivity() {
    lateinit var  binding : ActivityFormLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        val dialogCarregando = DialogCarregando(this)


  binding.btEntrar.setOnClickListener {view->
      val email = binding.etEmail.text.toString()
      val senha = binding.etSenha.text.toString()

      if(email.isEmpty() || senha.isEmpty()){
          val snackBar = Snackbar.make(view,"Preencha todos os campos",Snackbar.LENGTH_SHORT)
          snackBar.setBackgroundTint(Color.RED)
          snackBar.setTextColor(Color.WHITE)
          snackBar.show()
      }else{
          FirebaseAuth.getInstance().signInWithEmailAndPassword(email,senha).addOnCompleteListener { tarefa->
              if(tarefa.isSuccessful){
                  dialogCarregando.iniciarCarregamentoAlertDialog()
                  Handler(Looper.getMainLooper()).postDelayed({
                      irParaTelaProduto()
                      dialogCarregando.liberarAlertDialog()
                  },3000)
              }
          }.addOnFailureListener {
              val snackBar = Snackbar.make(view,"Erro ao fazer o login!",Snackbar.LENGTH_SHORT)
              snackBar.setBackgroundTint(Color.RED)
              snackBar.setTextColor(Color.WHITE)
              snackBar.show()
          }
      }
  }

        binding.tvTelaCadastro.setOnClickListener {
            val intent = Intent(this,FormCadastro::class.java)
            startActivity(intent)

        }

    }
private fun irParaTelaProduto(){
    val  intent = Intent(this,TelaPrincipalProdutos::class.java)
    startActivity(intent)
    finish()
}
    override fun onStart() {
        super.onStart()
        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        if(usuarioAtual != null){

            irParaTelaProduto()
        }
    }
}