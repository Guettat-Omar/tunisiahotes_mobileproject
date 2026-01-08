package com.tunisiahotes.data.database

import com.tunisiahotes.data.dao.MaisonHoteDao
import com.tunisiahotes.data.entity.MaisonHoteEntity
import kotlinx.coroutines.flow.Flow

class MaisonRepository(private val maisonDao: MaisonHoteDao) {

    // Lecture
    fun getAllMaisons(): Flow<List<MaisonHoteEntity>> = maisonDao.getAllMaisons()

    suspend fun getMaisonById(id: Int): MaisonHoteEntity? = maisonDao.getMaisonById(id)

    // Filtrage
    fun filterByRegion(region: String): Flow<List<MaisonHoteEntity>> =
        maisonDao.filterByRegion(region)

    fun filterByVille(ville: String): Flow<List<MaisonHoteEntity>> =
        maisonDao.filterByVille(ville)

    fun filterBySaison(saison: String): Flow<List<MaisonHoteEntity>> =
        maisonDao.filterBySaison(saison)

    // Tri
    fun sortByPrix(): Flow<List<MaisonHoteEntity>> =
        maisonDao.sortByPrixCroissant()

    fun sortByAvis(): Flow<List<MaisonHoteEntity>> =
        maisonDao.sortByAvisDecroissant()

    // Modification (Developer)
    suspend fun insertMaison(maison: MaisonHoteEntity) =
        maisonDao.insertMaison(maison)

    suspend fun updateMaison(maison: MaisonHoteEntity) =
        maisonDao.updateMaison(maison)

    suspend fun deleteMaison(maison: MaisonHoteEntity) =
        maisonDao.deleteMaison(maison)

    // Avis (Utilisateur)
    suspend fun ajouterAvis(maisonId: Int, avis: Float) =
        maisonDao.ajouterAvis(maisonId, avis)
}