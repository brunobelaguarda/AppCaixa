package com.example.appcaixa.FormExtrato

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.example.appcaixa.FormDeposito.Deposito
import com.example.appcaixa.databinding.ActivityFormExtratoBinding
import com.google.firebase.firestore.FirebaseFirestore

class formExtrato : AppCompatActivity() {
    private lateinit var binding: ActivityFormExtratoBinding
    private val db = FirebaseFirestore.getInstance()
    val listaExtrato = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar!!.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityFormExtratoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listaExtrato)
        binding.listaExtrato.adapter = adapter

        binding.btConsultar.setOnClickListener {

            val cpf = binding.txtCpf.text.toString()
            var tam = 0;

            db.collection("Extrato").get().addOnSuccessListener { docExtratos ->
                for (documentos in docExtratos) {
                    tam = docExtratos.size()
                    for (contador in 1..tam) {
                        if (documentos.id.equals(cpf + "SAQ" + contador) || documentos.id.equals(
                                cpf + "DEP" + contador
                            )
                        ) {


                            val cliente = documentos.toObject(Extrato::class.java)
                            listaExtrato.add("" + cliente?.movimento)
                            println(" NOME " + cliente?.movimento)


                        }
                    }

                }
            }
            adapter.notifyDataSetChanged()
            mostraSaldo()
        }
        binding.txtCpf.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Nova consulta")
            builder.setMessage("Você deseja realizar uma nova consulta?")
            builder.setPositiveButton("Sim") { dialog, which ->
                listaExtrato.clear()
                binding.txtSaldo.text=""
                binding.txtCpf.text.clear()
            }
            builder.setNegativeButton("Não") { dialog, which ->

            }
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }

    }

    fun mostraSaldo() {
        db.collection("Cliente").get().addOnSuccessListener { documentos ->
            for (documento in documentos) {
                if (documento.id.equals(binding.txtCpf.text.toString())) {
                    val cliente = documento.toObject(Deposito::class.java)
                    val saldo = Integer.parseInt(cliente.valor.toString())
                    binding.txtSaldo.text="Saldo: "+saldo
                }
            }
        }
    }
}

