package com.example.appcaixa.FormTelaPrincipal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appcaixa.FormDeposito.formDeposito
import com.example.appcaixa.FormExtrato.formExtrato
import com.example.appcaixa.FormSaque.formSaque
import com.example.appcaixa.databinding.ActivityTelaPrincipalBinding

class TelaPrincipal : AppCompatActivity() {
    private lateinit var binding: ActivityTelaPrincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar!!.hide();
        super.onCreate(savedInstanceState)
        binding = ActivityTelaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btDepositar.setOnClickListener {
            val intent= Intent(this, formDeposito::class.java)
            startActivity(intent)
        }
        binding.btSacar.setOnClickListener {
            val intent= Intent(this, formSaque::class.java)
            startActivity(intent)
        }
        binding.btConsultaExtrato.setOnClickListener {
            val intent= Intent(this, formExtrato::class.java)
            startActivity(intent)
        }

    }
}