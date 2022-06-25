
package com.example.appcaixa.FormDeposito

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appcaixa.databinding.ActivityFormDepositoBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class formDeposito : AppCompatActivity() {
    private lateinit var binding: ActivityFormDepositoBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar!!.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityFormDepositoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val date = Calendar.getInstance().time

        binding.btDepositar.setOnClickListener{ view ->

            var dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            var data = dateTimeFormat.format(date).toString()


            var cpf = binding.txtCpf.text.toString()
            var tam = 0
            if(cpf.length==11 && binding.txtValorDeposito.text.toString()!=""){
                val valorInformado = Integer.parseInt(binding.txtValorDeposito.text.toString())
                db.collection("Cliente").get().addOnSuccessListener { documentos ->

                    println("Depositado")
                    for (documento in documentos) {
                        if(documento.id.equals(cpf)) {
                            println("Somou ao saldo")
                            val key = documento.id
                            val cliente = documento.toObject(Deposito::class.java)
                            val valorantigo = Integer.parseInt(cliente.valor.toString())
                            val saldoAtual = valorantigo + valorInformado

                            val clienteMap = hashMapOf(
                                "cpf" to binding.txtCpf.text.toString(),
                                "valor" to saldoAtual,
                                "data" to data,
                            )


                            db.collection("Cliente").document("" + cpf)
                                .set(clienteMap).addOnCompleteListener {
                                }
                            val snackbar = Snackbar.make(view,"Valor depositado com sucesso! ",Snackbar.LENGTH_SHORT)
                            snackbar.setBackgroundTint(Color.GREEN)
                            snackbar.show()
                            break

                        }else{
                            val clienteMap = hashMapOf(
                                "cpf" to binding.txtCpf.text.toString(),
                                "valor" to Integer.parseInt(binding.txtValorDeposito.text.toString()),
                                "data" to data,
                            )
                            println("Novo cliente")
                            db.collection("Cliente").document("" + cpf)
                                .set(clienteMap).addOnCompleteListener {

                                }
                        }
                    }
                }
//extrato
                var movimentacao = "Depositado na data: "+ data + "-- R$"+valorInformado
                val extratoMap = hashMapOf(
                    "movimento" to movimentacao,
                )
                db.collection("Extrato").get().addOnSuccessListener { documentos ->

                    for (documento in documentos) {
                        tam = documentos.size()+1
                        db.collection("Extrato").document(cpf + "DEP"+tam)
                            .set(extratoMap).addOnCompleteListener {
                            }

                    }
                }

            }else{
                val snackbar = Snackbar.make(view,"Preencha o CPF corretamente e informe um valor.",Snackbar.LENGTH_SHORT)
                snackbar.setBackgroundTint(Color.RED)
                snackbar.show()

            }

        }

    }

}