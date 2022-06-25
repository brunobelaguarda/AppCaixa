package com.example.appcaixa.FormSaque

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.appcaixa.FormDeposito.Deposito
import com.example.appcaixa.databinding.ActivityFormSaqueBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class formSaque : AppCompatActivity() {
    private lateinit var binding: ActivityFormSaqueBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar!!.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityFormSaqueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val date = Calendar.getInstance().time

        binding.btSacar.setOnClickListener { view->
            var dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            var data = dateTimeFormat.format(date).toString()
            var tam = 0
            var cpf = binding.txtCpf.text.toString()


            if(cpf.length==11 && binding.txtValorSacar.text.toString()!=""){
                val valorInformado = Integer.parseInt(binding.txtValorSacar.text.toString())
                ////saque
                db.collection("Cliente").get().addOnSuccessListener { documentos ->

                    println("Sacado")
                    for (documento in documentos) {
                        if(documento.id.equals(cpf)) {
                            println("Subtraiu ao saldo")
                            val key = documento.id
                            val cliente = documento.toObject(Deposito::class.java)
                            val valorantigo = Integer.parseInt(cliente.valor.toString())
                            if(valorInformado<valorantigo ) {
                                val saldoAtual = valorantigo - valorInformado

                                val clienteMap = hashMapOf(
                                    "cpf" to binding.txtCpf.text.toString(),
                                    "valor" to saldoAtual,
                                    "data" to data,
                                )

                                Log.d("wwer", "Nome Pasta ${key} -- CPF: ")

                                db.collection("Cliente").document("" + cpf)
                                    .set(clienteMap).addOnCompleteListener {
                                    }
                                val snackbar = Snackbar.make(view,"Valor sacado com sucesso! ",Snackbar.LENGTH_SHORT)
                                snackbar.setBackgroundTint(Color.GREEN)
                                snackbar.show()

                                mostrarNotas()

                                ////extrato saque
                                var movimentacao = "Sacado na data: "+ data + "-- R$"+valorInformado
                                val extratoMap = hashMapOf(
                                    "movimento" to movimentacao,
                                )
                                db.collection("Cliente").get().addOnSuccessListener { documentos ->
                                    for (documento in documentos) {
                                        if(documento.id.equals(cpf)){
                                            db.collection("Extrato").get().addOnSuccessListener { documentos ->

                                                for (documento in documentos) {
                                                    tam = documentos.size() + 1
                                                    db.collection("Extrato").document(cpf + "SAQ" + tam)
                                                        .set(extratoMap).addOnCompleteListener {
                                                        }
                                                }

                                            }
                                            break
                                        }
                                    }
                                }
                                ////extrato saque

                                break

                            }else{
                                val snackbar = Snackbar.make(view,"Você não tem saldo suficiente! ", Snackbar.LENGTH_SHORT)
                                snackbar.setBackgroundTint(Color.RED)
                                snackbar.show()

                            }
                            break

                        }else{
                            val snackbar = Snackbar.make(view,"Você não tem saldo! ", Snackbar.LENGTH_SHORT)
                            snackbar.setBackgroundTint(Color.RED)
                            snackbar.show()
                        }
                    }

                }
                ////saque


            }else{
                val snackbar = Snackbar.make(view,"Preencha o CPF corretamente e informe um valor. ", Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()
            }

        }

        binding.txtCpf.setOnClickListener {
            binding.txtQtd100.text=""
            binding.txtQtd50.text=""
            binding.txtQtd20.text=""
            binding.txtQtd10.text=""
            binding.txtQtd5.text=""
            binding.txtQtd2.text=""
        }
    }
    fun mostrarNotas(){
        db.collection("Cliente").get().addOnSuccessListener { documentos ->

            println("Sacado")
            for (documento in documentos) {
                if (documento.id.equals(binding.txtCpf.text.toString())) {
                    var valorInformado = Integer.parseInt(binding.txtValorSacar.text.toString())

                    var nota2 = 0
                    var nota5 = 0
                    var nota10 = 0
                    var nota20 = 0
                    var nota50 = 0
                    var nota100 = 0

                    while (valorInformado >= 100) {
                        nota100 = nota100 + 1
                        valorInformado = valorInformado - 100

                    }
                    while (valorInformado >= 50) {
                        nota50 = nota50 + 1
                        valorInformado = valorInformado - 50
                    }
                    while (valorInformado >= 20) {
                        nota20 = nota20 + 1
                        valorInformado = valorInformado - 20
                    }

                    while (valorInformado >= 10) {
                        nota10 = nota10 + 1
                        valorInformado = valorInformado - 10
                    }
                    while (valorInformado >= 5 && valorInformado % 2 != 0) {
                        nota5 = nota5 + 1
                        valorInformado = valorInformado - 5
                    }
                    while (valorInformado >= 2) {
                        nota2 = nota2 + 1
                        valorInformado = valorInformado - 2
                    }

                    binding.txtQtd100.text=""+nota100
                    binding.txtQtd50.text=""+nota50
                    binding.txtQtd20.text=""+nota20
                    binding.txtQtd10.text=""+nota10
                    binding.txtQtd5.text=""+nota5
                    binding.txtQtd2.text=""+nota2

                    break
                }else{
                    println("cpf não xiste")
                }
            }
        }
    }
}