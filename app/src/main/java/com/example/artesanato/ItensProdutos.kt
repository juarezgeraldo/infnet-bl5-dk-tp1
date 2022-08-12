package com.example.artesanato

import android.os.Bundle
import android.text.Selection
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.example.artesanato.model.*

class ItensProdutos : Fragment() {
    private lateinit var produtoDAO: ProdutoDAO
    private lateinit var itemProdutoDAO: ItemProdutoDAO
    private lateinit var listaDeIds: ArrayList<Int>
    private lateinit var listaDeIdsProdutos: ArrayList<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_itens_produtos, container, false)
        val appDatabase = Room.databaseBuilder(
            this.requireActivity(),
            AppDatabase::class.java,
            "artesanato.db"
        ).allowMainThreadQueries().build()
        produtoDAO = appDatabase.obterProdutoDAO()
        itemProdutoDAO = appDatabase.obterItemProdutoDAO()

        val spnProdutos = this.requireActivity().findViewById<Spinner>(R.id.spnProdutos)

        val txtIdItemProduto = view.findViewById<TextView>(R.id.txtIdItemProduto)
        val txtNomeItemProduto = view.findViewById<TextView>(R.id.txtNomeItemProduto)
        val txtPrecoItemProduto = view.findViewById<TextView>(R.id.txtPrecoItemProduto)
        val chkItemProdutoProprio = view.findViewById<CheckBox>(R.id.chkItemProdutoProprio)

        val btnSalvarItemProduto = view.findViewById<Button>(R.id.btnSalvarItemProduto)

        val lstItensProdutos = view.findViewById<ListView>(R.id.lstItensProdutos)

        btnSalvarItemProduto.setOnClickListener() {
            if (txtNomeItemProduto.text.toString().isEmpty()) {
                Toast.makeText(this@ItensProdutos.requireActivity(), "Informe o nome do item do produto.", Toast.LENGTH_LONG).show()
            } else {
                if (txtPrecoItemProduto.text.toString()
                        .isEmpty() || txtPrecoItemProduto.text.toString().toDouble().equals(0.0)
                ) {
                    Toast.makeText(this@ItensProdutos.requireActivity(), "Informe o preço do item do produto.", Toast.LENGTH_LONG)
                        .show()
                } else {
                    val spnProdutos = this.requireActivity().findViewById<Spinner>(R.id.spnProdutos)

                    if (txtIdItemProduto.text.toString().isEmpty()) {
                        val itemProduto = ItemProduto(
                            null,
                            listaDeIdsProdutos[spnProdutos.selectedItemPosition],
                            txtNomeItemProduto.text.toString(),
                            txtPrecoItemProduto.text.toString().toDouble(),
                            chkItemProdutoProprio.isChecked
                        )
                        itemProdutoDAO.incluir(itemProduto)
                        Toast.makeText( this@ItensProdutos.requireActivity(),"Inclusão realizada com sucesso.", Toast.LENGTH_LONG).show()
                    } else {
                        val itemProduto = ItemProduto(
                            txtIdItemProduto.text.toString().toInt(),
                            listaDeIdsProdutos[spnProdutos.selectedItemPosition],
                            txtNomeItemProduto.text.toString(),
                            txtPrecoItemProduto.text.toString().toDouble(),
                            chkItemProdutoProprio.isChecked
                        )
                        itemProdutoDAO.alterar(itemProduto)
                        Toast.makeText( this@ItensProdutos.requireActivity(),"Alteração realizada com sucesso.", Toast.LENGTH_LONG).show()
                    }
                    txtIdItemProduto.setText(null)
                    txtNomeItemProduto.setText(null)
                    txtPrecoItemProduto.setText(null)
                    chkItemProdutoProprio.setChecked(false)
                    txtNomeItemProduto.requestFocus()

                    this.atualizaListaItensProdutos()
                }
            }
        }

        lstItensProdutos.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val id = listaDeIds.get(p2)
                val itemProduto = itemProdutoDAO.obter(id)

                txtIdItemProduto.setText(itemProduto.id.toString())
                txtNomeItemProduto.setText(itemProduto.nomeItem)
                txtPrecoItemProduto.setText(itemProduto.precoItem.toString())
                chkItemProdutoProprio.setChecked(itemProduto.indicadoItemProprio)

                selecionaItemSpiner(itemProduto.idProduto)
                txtNomeItemProduto.requestFocus()
            }
        }

        lstItensProdutos.onItemLongClickListener = object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ): Boolean {
                val id = listaDeIds.get(p2)
                val itemProduto = itemProdutoDAO.obter(id)

                itemProdutoDAO.excluir(itemProduto)

                txtIdItemProduto.setText(null)
                txtNomeItemProduto.setText(null)
                txtPrecoItemProduto.setText(null)
                chkItemProdutoProprio.setChecked(false)

                atualizaListaItensProdutos()
                Toast.makeText( this@ItensProdutos.requireActivity(),"Exclusão realizada com sucesso.", Toast.LENGTH_LONG).show()
                return true
            }
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        atualizaListaItensProdutos()
    }

    private fun selecionaItemSpiner(idProduto: Int) {
        val spnProdutos = this.requireActivity().findViewById<Spinner>(R.id.spnProdutos)
        var indice: Int = 0
        for (id: Int in listaDeIdsProdutos) {
            if (id == idProduto) {
                spnProdutos.setSelection(indice)
                break
            }
            indice = indice + 1
        }
    }

    private fun atualizaListaItensProdutos() {
        val itensProdutos = itemProdutoDAO.listar()
        val nomesItensProduto = ArrayList<String>()
        listaDeIds = ArrayList()
        for (itemProduto in itensProdutos) {
            nomesItensProduto.add(itemProduto.nomeItem)
            listaDeIds.add(itemProduto.id!!)
        }
        val lstItensProdutos = this.requireActivity().findViewById<ListView>(R.id.lstItensProdutos)
        val adapterItensProduto = ArrayAdapter<String>(
            this.requireActivity(),
            android.R.layout.simple_list_item_1,
            nomesItensProduto
        )
        lstItensProdutos.adapter = adapterItensProduto

        val produtos = produtoDAO.listar()
        val nomesProduto = ArrayList<String>()
        listaDeIdsProdutos = ArrayList()
        for (produto in produtos) {
            nomesProduto.add(produto.nomeProduto)
            listaDeIdsProdutos.add(produto.id!!)

        }
        val spnProdutos = this.requireActivity().findViewById<Spinner>(R.id.spnProdutos)
        val adapterProduto =
            ArrayAdapter(this.requireActivity(), android.R.layout.simple_spinner_item, nomesProduto)
        spnProdutos.adapter = adapterProduto
    }
}