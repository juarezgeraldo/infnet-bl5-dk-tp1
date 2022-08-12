package com.example.artesanato.model

import androidx.room.*

@Dao
interface ItemProdutoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun incluir(vararg itemProduto: ItemProduto)

    @Delete()
    fun excluir(vararg itemProduto: ItemProduto)

    @Update()
    fun alterar(vararg itemProduto: ItemProduto)

    @Query("SELECT * FROM ItemProduto")
    fun listar(): List<ItemProduto>

    @Query("SELECT * FROM ItemProduto WHERE id = :id")
    fun obter(id: Int) : ItemProduto

}