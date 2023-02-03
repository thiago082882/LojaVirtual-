package com.example.lojavirtual.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import com.example.lojavirtual.activities.formLogin.FormLogin
import com.example.lojavirtual.databinding.ActivityFormCadastroBinding
import com.example.lojavirtual.databinding.DialogPerfilUsuarioBinding
import com.example.lojavirtual.model.DB
import com.google.firebase.auth.FirebaseAuth

class DialogPerfilUsuario(private val activity: Activity) {

    lateinit var  dialog: AlertDialog
    lateinit var binding: DialogPerfilUsuarioBinding

    fun iniciarPerfilUsuario() {
        val builder = AlertDialog.Builder(activity)
        binding = DialogPerfilUsuarioBinding.inflate(activity.layoutInflater)
        builder.setView(binding.root)
        builder.setCancelable(true)
        dialog = builder.create()
        dialog.show()

    }

    public fun recuperarDadosUsuarioBanco(){

        val nomeUsuario = binding.tvUsuario
        val emailUsuario = binding.tvEmail
        val db = DB()
        db.recuperarDadosUsuarioPerfil(nomeUsuario,emailUsuario)

        binding.btDeslogar.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(activity,FormLogin::class.java)
            activity.startActivity(intent)
            activity.finish()

        }

    }


}