package com.example.artesanato

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.artesanato.model.AppDatabase
import com.example.artesanato.model.Produto
import com.example.artesanato.model.ProdutoDAO

class Produtos : Fragment() {
    private lateinit var produtoDAO: ProdutoDAO
    private lateinit var listaDeIds: ArrayList<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_produtos, container, false)
        val appDatabase = Room.databaseBuilder(
            this.requireActivity(),
            AppDatabase::class.java,
            "artesanato.db"
        ).allowMainThreadQueries().build()
        produtoDAO = appDatabase.obterProdutoDAO()

        val txtIdProduto = view.findViewById<TextView>(R.id.txtIdProduto)
        val txtNomeProduto = view.findViewById<EditText>(R.id.txtNomeProduto)
        val txtPrecoProduto = view.findViewById<EditText>(R.id.txtPrecoProduto)
        val chkProdutoProprio = view.findViewById<CheckBox>(R.id.chkProdutoProprio)

        val btnSalvarProduto = view.findViewById<Button>(R.id.btnSalvarProduto)

        val lstProdutos = view.findViewById<ListView>(R.id.lstProdutos)

        btnSalvarProduto.setOnClickListener() {
            if (txtNomeProduto.text.toString().isEmpty()) {
                Toast.makeText(null, "Informe o nome do produto.", Toast.LENGTH_LONG).show()
            } else {
                if (txtPrecoProduto.text.toString().isEmpty() || txtPrecoProduto.text.toString()
                        .toDouble().equals(0.0)
                ) {
                    Toast.makeText(null, "Informe o preço do produto.", Toast.LENGTH_LONG).show()
                } else {
                    if (txtIdProduto.text.toString().isEmpty()) {
                        val produto = Produto(
                            null,
                            txtNomeProduto.text.toString(),
                            txtPrecoProduto.text.toString().toDouble(),
                            chkProdutoProprio.isChecked
                        )
                        atualizarProduto(produto, "incluir")
                    } else {
                        val produto = Produto(
                            txtIdProduto.text.toString().toInt(),
                            txtNomeProduto.text.toString(),
                            txtPrecoProduto.text.toString().toDouble(),
                            chkProdutoProprio.isChecked
                        )
                        atualizarProduto(produto, "alterar")
                    }
                    txtIdProduto.setText(null)
                    txtNomeProduto.setText(null)
                    txtPrecoProduto.setText(null)
                    chkProdutoProprio.setChecked(false)
                    txtNomeProduto.requestFocus()

                    this.atualizaListaProdutos()
                }
            }
        }

        lstProdutos.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val id = listaDeIds.get(p2)
                val produto = produtoDAO.obter(id)

                txtIdProduto.setText(produto.id.toString())
                txtNomeProduto.setText(produto.nomeProduto)
                txtPrecoProduto.setText(produto.precoProduto.toString())
                chkProdutoProprio.setChecked(produto.indicadoProdutoProprio)
                txtNomeProduto.requestFocus()

                atualizaListaProdutos()
            }
        }

        lstProdutos.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ): Boolean {
                val id = listaDeIds.get(p2)
                val produto = produtoDAO.obter(id)

                atualizarProduto(produto, "excluir")

                txtIdProduto.setText(null)
                txtNomeProduto.setText(null)
                txtPrecoProduto.setText(null)
                chkProdutoProprio.setChecked(false)
                txtNomeProduto.requestFocus()

                atualizaListaProdutos()
                return true
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        atualizaListaProdutos()
    }

    private fun atualizarProduto(produto: Produto, operacao: String) {
        Thread {
            when (operacao) {
                "incluir" -> {
                    produtoDAO.incluir(produto)
                    this@Produtos.requireActivity().runOnUiThread {
                        Toast.makeText(
                            this@Produtos.requireActivity(),
                            "Inclusão realizada com sucesso.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                "alterar" -> {
                    produtoDAO.alterar(produto)
                    this@Produtos.requireActivity().runOnUiThread {
                        Toast.makeText(
                            this@Produtos.requireActivity(),
                            "Alteração realizada com sucesso.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                "excluir" -> {
                    if (produtoDAO.excluir(produto.id.toString().toInt()) > 0) {
                        this@Produtos.requireActivity().runOnUiThread {
                            Toast.makeText(
                                this@Produtos.requireActivity(),
                                "Exclusão realizada com sucesso.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        this@Produtos.requireActivity().runOnUiThread {
                            Toast.makeText(
                                this@Produtos.requireActivity(),
                                "Exclusão não pode ser realizada, pois há Itens de produtos.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }.start()
    }

    private fun atualizaListaProdutos() {
        val produtos = produtoDAO.listar()
        val nomesProduto = ArrayList<String>()
        listaDeIds = ArrayList()
        for (produto in produtos) {
            nomesProduto.add(produto.nomeProduto)
            listaDeIds.add(produto.id!!)
        }
        val lstProdutos = this.requireActivity().findViewById<ListView>(R.id.lstProdutos)
        val adapter = ArrayAdapter<String>(
            this.requireActivity(),
            android.R.layout.simple_list_item_1,
            nomesProduto
        )
        lstProdutos.adapter = adapter
    }
}

