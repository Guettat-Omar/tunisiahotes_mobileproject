package com.tunisiahotes.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "maison_hote")
data class MaisonHoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nom: String,
    val description: String,
    val region: String,
    val ville: String,
    val saison: String,

    // Prix pour 2 personnes, 1 nuitée
    val prixNuite: Double,

    val vueMer: Boolean,
    val procheMer: String, // "Proche de la mer", "Sur la plage", etc.

    // Système d'avis
    val avis: Float, // Note actuelle (0-10)
    val nbAvis: Int = 1, // Nombre d'avis donnés

    // Lien vers les photos Google Drive
    val photoLink: String
)