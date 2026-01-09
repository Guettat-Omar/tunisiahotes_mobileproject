package com.example.tunisiahotes.data.dao

import androidx.room.*
import com.example.tunisiahotes.data.entity.MaisonHoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaisonHoteDao {

    // ========== LECTURE (Utilisateur + Développeur) ==========

    @Query("SELECT * FROM maison_hote ORDER BY nom ASC")
    fun getAllMaisons(): Flow<List<MaisonHoteEntity>>

    @Query("SELECT * FROM maison_hote WHERE id = :id")
    suspend fun getMaisonById(id: Int): MaisonHoteEntity?

    // ========== FILTRAGE ==========

    @Query("SELECT * FROM maison_hote WHERE region = :region ORDER BY nom ASC")
    fun filterByRegion(region: String): Flow<List<MaisonHoteEntity>>

    @Query("SELECT * FROM maison_hote WHERE ville = :ville ORDER BY nom ASC")
    fun filterByVille(ville: String): Flow<List<MaisonHoteEntity>>

    @Query("SELECT * FROM maison_hote WHERE saison = :saison ORDER BY nom ASC")
    fun filterBySaison(saison: String): Flow<List<MaisonHoteEntity>>

    // ========== TRI ==========

    @Query("SELECT * FROM maison_hote ORDER BY prixNuite ASC")
    fun sortByPrixCroissant(): Flow<List<MaisonHoteEntity>>

    @Query("SELECT * FROM maison_hote ORDER BY avis DESC")
    fun sortByAvisDecroissant(): Flow<List<MaisonHoteEntity>>

    // ========== MODIFICATION (Développeur uniquement) ==========

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaison(maison: MaisonHoteEntity): Long

    @Update
    suspend fun updateMaison(maison: MaisonHoteEntity)

    @Delete
    suspend fun deleteMaison(maison: MaisonHoteEntity)

    // ========== MISE À JOUR AVIS (Utilisateur) ==========

    @Query("""
        UPDATE maison_hote 
        SET avis = ((avis * nbAvis) + :nouvelAvis) / (nbAvis + 1),
            nbAvis = nbAvis + 1
        WHERE id = :maisonId
    """)
    suspend fun ajouterAvis(maisonId: Int, nouvelAvis: Float)
}