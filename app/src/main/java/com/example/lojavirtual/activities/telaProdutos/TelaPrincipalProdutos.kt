package com.example.lojavirtual.activities.telaProdutos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.lojavirtual.R
import com.example.lojavirtual.activities.Pedidos.Pedidos
import com.example.lojavirtual.activities.formLogin.FormLogin
import com.example.lojavirtual.adapter.AdapterProduto
import com.example.lojavirtual.databinding.ActivityTelaPrincipalProdutosBinding
import com.example.lojavirtual.dialog.DialogPerfilUsuario
import com.example.lojavirtual.model.DB
import com.example.lojavirtual.model.Produto
import com.google.firebase.auth.FirebaseAuth

class TelaPrincipalProdutos : AppCompatActivity() {
    lateinit var binding:ActivityTelaPrincipalProdutosBinding
    lateinit var adapterProdutos:AdapterProduto
    var lista_produtos : MutableList<Produto> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaPrincipalProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recycler_produtos = binding.recyclerProdutos
        recycler_produtos.layoutManager = GridLayoutManager(this,2)
        recycler_produtos.setHasFixedSize(true)
        adapterProdutos = AdapterProduto(this,lista_produtos)
        recycler_produtos.adapter = adapterProdutos

       val db = DB()
        db.obterListaDeProdutos(lista_produtos,adapterProdutos)
    }




//    fun itensLista(){
//        val produto1 = Produto(R.drawable.logo,"Sapato de couro","R$128,00")
//        lista_produtos.add(produto1)
//        val produto2 = Produto(R.drawable.logo,"Sapato de couro","R$128,00")
//        lista_produtos.add(produto2)
//        val produto3 = Produto(R.drawable.logo,"Sapato de couro","R$128,00")
//        lista_produtos.add(produto3)
//        val produto4 = Produto(R.drawable.logo,"Sapato de couro","R$128,00")
//        lista_produtos.add(produto4)
//        val produto5 = Produto(R.drawable.logo,"Sapato de couro","R$128,00")
//        lista_produtos.add(produto5)
//        val produto6 = Produto(R.drawable.logo,"Sapato de couro","R$128,00")
//        lista_produtos.add(produto6)
//
//    }

    //call the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }
//menu action

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.perfil -> iniciarDialogPerfilUsuario()
            R.id.pedidos -> iniciarTelaPedidos()
            R.id.deslogar -> deslogarUsuario()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun iniciarDialogPerfilUsuario(){
        val dialogPerfilUsuario = DialogPerfilUsuario(this)
        dialogPerfilUsuario.iniciarPerfilUsuario()
        dialogPerfilUsuario.recuperarDadosUsuarioBanco()
    }

    private fun iniciarTelaPedidos(){
        val intent = Intent(this, Pedidos::class.java)
        startActivity(intent)

    }
    private fun deslogarUsuario(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this,FormLogin::class.java)
        startActivity(intent)
        finish()
    }

}