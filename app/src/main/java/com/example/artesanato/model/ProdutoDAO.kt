package com.example.artesanato.model

import androidx.room.*

@Dao
interface ProdutoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun incluir(vararg produtos: Produto)

    @Query("DELETE FROM Produto WHERE id = :id AND (SELECT COUNT(*) FROM ItemProduto WHERE idProduto = :id) = 0")
    fun excluir(id: Int) : Int

    @Update()
    fun alterar(vararg produto: Produto)

    @Query("SELECT * FROM Produto")
    fun listar(): List<Produto>

    @Query("SELECT * FROM Produto WHERE id = :id")
    fun obter(id: Int) : Produto

}