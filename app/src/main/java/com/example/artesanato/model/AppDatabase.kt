package com.example.artesanato.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Produto :: class, ItemProduto :: class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun obterProdutoDAO() : ProdutoDAO
    abstract fun obterItemProdutoDAO() : ItemProdutoDAO
}