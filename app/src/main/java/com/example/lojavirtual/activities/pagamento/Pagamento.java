package com.example.lojavirtual.activities.pagamento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lojavirtual.InterfaceMercadoPago.ComunicacaoServidorMP;
import com.example.lojavirtual.R;
import com.example.lojavirtual.databinding.ActivityPagamentoBinding;
import com.example.lojavirtual.model.DB;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mercadopago.android.px.configuration.AdvancedConfiguration;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.model.Payment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Pagamento extends AppCompatActivity {

    ActivityPagamentoBinding binding;
    private String tamanho_calcado;
    private String nome;
    private String preco;
    private final String PUBLIC_KEY = "TEST-f776dc28-1297-4751-ac48-5af14bda6ed3";
    private final String ACCESS_TOKEN = "TEST-4004213372072441-012812-1c1088a6880b1579a778bfb46afd163b-348750320";
    DB db = new DB();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPagamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tamanho_calcado = getIntent().getExtras().getString("tamanho_calcado");
        nome = getIntent().getExtras().getString("nome");
        preco = getIntent().getExtras().getString("preco");



        binding.btFazerPagamento.setOnClickListener(view -> {

            String bairro = binding.editBairro.getText().toString();
            String rua_numero = binding.editRuaNumero.getText().toString();
            String cidade_estado = binding.editCidadeEstado.getText().toString();
            String celular = binding.editCelular.getText().toString();
            if (bairro.isEmpty() || rua_numero.isEmpty() || cidade_estado.isEmpty() || celular.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, "Preencha todos os campos!", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.setTextColor(Color.WHITE);
                snackbar.show();
            } else {
                criarJsonObject();
            }
        });

    }

    private void criarJsonObject() {

        JsonObject dados = new JsonObject();

        //Primeiro Item

        JsonArray item_lista = new JsonArray();
        JsonObject item;

        //Segundo Item

        JsonObject email = new JsonObject();


        //Terceiro Item - Excluir formas de Pagamentos - nesse caso o Boleto

        /*

        JsonObject excluir_tipo_pagamento = new JsonObject();
        JsonArray ids = new JsonArray();
        JsonObject removerBoleto = new JsonObject();
         */

        item = new JsonObject();
        item.addProperty("title", nome);
        item.addProperty("quantity", 1);
        item.addProperty("currency_id", "BRL");
        item.addProperty("unit_price", Double.parseDouble(preco));
        item_lista.add(item);
        dados.add("items", item_lista);

//        Log.d("JSON",item_lista.toString());

        String emailUsuario = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        email.addProperty("email", emailUsuario);
        dados.add("payer", email);


        /*
        removerBoleto.addroperty("id","ticket");
        ids.add(removerBoleto);
        excluir_tipo_pagamento.add("excluded_payment_types",ids);
        excluir_tipo_pagamento.addProperty("installments",2);
        dados.add("payment_methods",excluir_tipo_pagamento);
         */

        Log.d("JSON", dados.toString());

        criarPreferenciaPagamento(dados);


    }

    private void criarPreferenciaPagamento(JsonObject dados) {

        String site = "https://api.mercadopago.com";
        String url = "/checkout/preferences?access_token="+ ACCESS_TOKEN;
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(site)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ComunicacaoServidorMP conexao_pagamento = retrofit.create(ComunicacaoServidorMP.class);
        Call<JsonObject> request = conexao_pagamento.enviar_pagamento(url,dados);
        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
               String preferenceId = response.body().get("id").getAsString();
               criarPagamento(preferenceId);

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void criarPagamento(String preferenceId) {
        final AdvancedConfiguration advancedConfiguration =
                new AdvancedConfiguration.Builder().setBankDealsEnabled(false).build();
        new MercadoPagoCheckout
                .Builder(PUBLIC_KEY,preferenceId)
                .setAdvancedConfiguration(advancedConfiguration).build()
                .startPayment(this, 123);
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                respostaMercadoPago(payment);
            } else if (resultCode == RESULT_CANCELED) {

            } else {

            }
        }
    }

    private void respostaMercadoPago(Payment payment) {

        String status = payment.getPaymentStatus();

        String bairro = binding.editBairro.getText().toString();
        String rua_numero = binding.editRuaNumero.getText().toString();
        String cidade_estado = binding.editCidadeEstado.getText().toString();
        String celular = binding.editCelular.getText().toString();

        String endereco = "Bairro: " + bairro + " " + " Rua e Número: " + " " + rua_numero + " Cidade e Estado: " + " " + cidade_estado;
        String status_pagamento = "Status de Pagamento: " + " " + "Pagamento Aprovado";
        String status_entrega = "Status de Entrega: " + " " + "Em andamento";

        String nome_Produto = "Nome: " + " " + nome;
        String precoProduto = "Preço: " + " " + preco;
        String tamanho = "Tamanho do Calçado: " + " " + tamanho_calcado;
        String celular_usuario = "Celular: " + " " + celular;

        if(status.equalsIgnoreCase("approved")){
            Snackbar snackbar = Snackbar.make(binding.container,"Sucesso ao fazer o pagamento!",Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.BLUE);
            snackbar.setTextColor(Color.WHITE);
            snackbar.show();
            db.salvarDadosPedidosUsuario(endereco,celular_usuario,nome_Produto,precoProduto,tamanho,status_pagamento,status_entrega);
            finish();
        }else if (status.equalsIgnoreCase("rejected")){
            Snackbar snackbar = Snackbar.make(binding.container,"Erro ao fazer pagamento!",Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.RED);
            snackbar.setTextColor(Color.WHITE);
            snackbar.show();




        }
    }
}