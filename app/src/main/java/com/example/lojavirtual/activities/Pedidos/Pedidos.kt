package com.example.lojavirtual.activities.Pedidos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lojavirtual.R
import com.example.lojavirtual.adapter.AdapterPedidos
import com.example.lojavirtual.databinding.ActivityPedidosBinding
import com.example.lojavirtual.model.DB
import com.example.lojavirtual.model.Pedido

class Pedidos : AppCompatActivity() {
    lateinit var binding: ActivityPedidosBinding
    lateinit var adapterPedidos:AdapterPedidos
    var lista_pedidos : MutableList<Pedido>  = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val recycler_pedidos = binding.recyclerPedidos
        recycler_pedidos.layoutManager = LinearLayoutManager(this)
        recycler_pedidos.setHasFixedSize(true)
        adapterPedidos = AdapterPedidos(this, lista_pedidos)
        recycler_pedidos.adapter = adapterPedidos

        val db = DB()
        db.obterListaPedidos(lista_pedidos,adapterPedidos)
    }
}