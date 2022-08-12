package com.example.artesanato.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ItemProduto (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val idProduto: Int,
    val nomeItem: String,
    val precoItem: Double,
    val indicadoItemProprio: Boolean

)