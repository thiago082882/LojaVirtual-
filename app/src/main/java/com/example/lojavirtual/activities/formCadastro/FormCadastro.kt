package com.example.lojavirtual.activities.formCadastro

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lojavirtual.R
import com.example.lojavirtual.databinding.ActivityFormCadastroBinding
import com.example.lojavirtual.model.DB
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

@SuppressLint("StaticFieldLeak")
lateinit var binding :  ActivityFormCadastroBinding
class FormCadastro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar!!.hide()

        val db = DB()



        binding.btCadastrar.setOnClickListener {

            val nome = binding.etNome.text.toString()
            val email = binding.etEmail.text.toString()
            val senha = binding.etSenha.text.toString()


            if(nome.isEmpty() || email.isEmpty() || senha.isEmpty()){
                val snackbar = Snackbar.make(it,"Preencha todos os campos!",Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.setTextColor(Color.WHITE)
                snackbar.show()
            }else{
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener { tarefa->

                    if(tarefa.isSuccessful){
                        db.salvarDadosUsuario(nome)
                        val snackbar = Snackbar.make(it,"Cadastro realizado com sucesso!",Snackbar.LENGTH_SHORT)
                        snackbar.setBackgroundTint(Color.BLUE)
                        snackbar.setTextColor(Color.WHITE)
                        snackbar.show()
                    }
                }.addOnFailureListener { erroCadastro->
                    val mensagemErro = when(erroCadastro){
                        is FirebaseAuthWeakPasswordException -> "Digite uma senha com no mínimo 6 caracteres"
                        is FirebaseAuthUserCollisionException -> "Esta Conta já foi cadastrada"
                        is FirebaseNetworkException -> "Sem conexão com a internet"
                        else -> "Erro ao cadastrar usuário"

                    }
                    val snackbar = Snackbar.make(it,mensagemErro,Snackbar.LENGTH_SHORT)
                    snackbar.setBackgroundTint(Color.RED)
                    snackbar.setTextColor(Color.WHITE)
                    snackbar.show()
                }

            }

        }
    }
}