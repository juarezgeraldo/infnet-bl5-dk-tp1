package com.example.artesanato.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Produto (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val nomeProduto: String,
    val precoProduto: Double,
    val indicadoProdutoProprio: Boolean
)