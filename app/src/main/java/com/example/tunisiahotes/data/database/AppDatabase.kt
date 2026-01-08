package com.tunisiahotes.data.database

import MaisonHoteEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tunisiahotes.data.dao.MaisonHoteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [MaisonHoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun maisonHoteDao(): MaisonHoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tunisia_hotes_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.maisonHoteDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(dao: MaisonHoteDao) {
            // Données initiales basées sur votre fichier Excel
            val maisons = listOf(
                MaisonHoteEntity(
                    nom = "Maison Hannibal",
                    description = "Maison d'hôtes de charme située dans le quartier résidentiel calme de Tunis. Elle est alignée par des sculptures élégant et se convenancent publiquement à une chambre d'affaires ou à des couples.",
                    region = "Tunis",
                    ville = "Tunis",
                    saison = "printemps / automne",
                    prixNuite = 220.0,
                    vueMer = false,
                    procheMer = "Non",
                    avis = 9.5f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1PJNc2qEbxe3oL-A0qCCkj8dxPFxKJBhg/view"
                ),
                MaisonHoteEntity(
                    nom = "L'Orchidée",
                    description = "Établissement moderne situé dans la zone du Lac de Tunis, caractérisé par une architecture contemporaine et une ambiance haut de gamme.",
                    region = "Tunis",
                    ville = "Tunis",
                    saison = "printemps / automne",
                    prixNuite = 220.0,
                    vueMer = false,
                    procheMer = "Non",
                    avis = 9.0f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/198ovVBXLyZVZKRtlUopABO065kDnihE1/view"
                ),
                MaisonHoteEntity(
                    nom = "Dar Yasmine",
                    description = "Maison d'hôtes traditionnelle située au cœur de la médina de Tunis, offrant une immersion culturelle authentique avec ses chambres décorées dans le style traditionnel tunisien.",
                    region = "Tunis",
                    ville = "Tunis",
                    saison = "printemps / automne",
                    prixNuite = 140.0,
                    vueMer = false,
                    procheMer = "Non",
                    avis = 8.5f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1l5pXQfqFJW-LdmPJqNz86YYnZ3DdnABz/view"
                ),
                MaisonHoteEntity(
                    nom = "Villa Bleue de Gammarth",
                    description = "Maison d'hôtes familiale réputée pour son accueil chaleureux et son environnement conviviale. Située dans ce belle verdoyant.",
                    region = "Tunis",
                    ville = "Gammarth",
                    saison = "été",
                    prixNuite = 350.0,
                    vueMer = true,
                    procheMer = "Oui",
                    avis = 9.7f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1O6LzT5gKvYEWKuVvTi8-N3o1MqNBOF2x/view"
                ),
                MaisonHoteEntity(
                    nom = "Villa Bleue du Golfe Hammamet",
                    description = "Maison d'hôtes familiale réputée pour son accueil chaleureux et son environnement conviviale.",
                    region = "Cap Bon",
                    ville = "Hammamet",
                    saison = "été",
                    prixNuite = 350.0,
                    vueMer = true,
                    procheMer = "Oui",
                    avis = 9.7f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1FjBqDPUwKEevQOqWxg0ksKG-wMvqFwWO/view"
                ),
                MaisonHoteEntity(
                    nom = "Résidence Hammamet",
                    description = "Résidence d'hébergement située dans un cadre non-compacte et comprend une ancienne tour de guet rénovée en suites de charme.",
                    region = "Cap Bon",
                    ville = "Hammamet",
                    saison = "été / printemps",
                    prixNuite = 160.0,
                    vueMer = false,
                    procheMer = "Variable",
                    avis = 8.6f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1EE9qQhShEn7hpgZEy8EWgY72eP-9OhF6/view"
                ),
                MaisonHoteEntity(
                    nom = "Le petit Palais",
                    description = "Villa luxueuse située directement en bord de mer à Rafraf. Elle offre un environnement naturel exceptionnel très prisé en été.",
                    region = "Cap Bon",
                    ville = "Hammamet",
                    saison = "été / printemps",
                    prixNuite = 380.0,
                    vueMer = true,
                    procheMer = "Variable",
                    avis = 9.0f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1EQ3jrXeGE6sSyoVkYEQN4o_oRzEg2dkw/view"
                ),
                MaisonHoteEntity(
                    nom = "Dar Fi'Daf",
                    description = "Maison d'hôtes contemporaine située dans une zone résidentielle calme du quartier des souches qui s'y trouvent.",
                    region = "Cap Bon",
                    ville = "Hammamet",
                    saison = "été / printemps",
                    prixNuite = 250.0,
                    vueMer = false,
                    procheMer = "Variable",
                    avis = 8.6f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1FG0E7OEZEYLzjxI3WUjf0bZEgTzr2tEz/view"
                ),
                MaisonHoteEntity(
                    nom = "Villa Salama",
                    description = "Maison d'hôtes chaleureuse offrant un cadre familial et un environnement proche des souches et de la mer.",
                    region = "Cap Bon",
                    ville = "Rafraf",
                    saison = "été",
                    prixNuite = 180.0,
                    vueMer = false,
                    procheMer = "Oui",
                    avis = 8.3f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1FLHp-NcN_UFTB7V-68h7D7pSLGlcEsKH/view"
                ),
                MaisonHoteEntity(
                    nom = "Le Clapmart Bleu",
                    description = "Hébergement touristique localisé dans la région de Rafraf, apprécié pour son environnement naturel et sa proximité immédiate à la mer.",
                    region = "Nord Est",
                    ville = "Rafraf",
                    saison = "été",
                    prixNuite = 60.0,
                    vueMer = false,
                    procheMer = "Oui",
                    avis = 8.2f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1FMRJjI1dX0z_BZMN8k-NQK1MBtFG6cKG/view"
                ),
                MaisonHoteEntity(
                    nom = "Rafraf maison",
                    description = "Maison d'hôtes qui offre une atmosphère paisible et authentique monda une terrasse permettant de profiter de la brise marine.",
                    region = "Nord Est",
                    ville = "Rafraf",
                    saison = "été / printemps",
                    prixNuite = 90.0,
                    vueMer = false,
                    procheMer = "Oui",
                    avis = 8.0f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1FS9c0wR_1jVgNszsKZz-pYYVaHZBN7pX/view"
                ),
                MaisonHoteEntity(
                    nom = "Dar El Yasmine Sousse",
                    description = "Maison d'hôtes traditionnelle rénovée, située dans un environnement calme à 3 Mahdia.",
                    region = "Sahel",
                    ville = "Sousse",
                    saison = "printemps / automne",
                    prixNuite = 280.0,
                    vueMer = false,
                    procheMer = "Non/variable",
                    avis = 9.1f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1FZ8paMgbCdx0DGaQpZxvqXraPg0z9tVB/view"
                ),
                MaisonHoteEntity(
                    nom = "Dar Jerba",
                    description = "Ensemble de maisons d'hôtes recouvert d'une peinture blanche authentique, dans l'esprit architecture traditionnel jerbien.",
                    region = "Sahel",
                    ville = "Mahdia",
                    saison = "été / printemps",
                    prixNuite = 280.0,
                    vueMer = true,
                    procheMer = "Variable",
                    avis = 9.2f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1FbPH3ZMgI9n9xL6KhxvqOqF7G3cF9VTz/view"
                ),
                MaisonHoteEntity(
                    nom = "Dar Dhiba Djerba",
                    description = "Large offre de maisons d'hôtes réparties sur l'île, combinant architecture traditionnelle, confort moderne et proximité de la mer.",
                    region = "Djerba",
                    ville = "Houmt Souk",
                    saison = "printemps / automne",
                    prixNuite = 420.0,
                    vueMer = true,
                    procheMer = "Variable",
                    avis = 9.8f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1Fg0ccLB9MgLzPjv1xTfH3hxWb4gMFVTz/view"
                ),
                MaisonHoteEntity(
                    nom = "Dar Youssef",
                    description = "Maison d'hôtes traditionnelle jerbienne apportée un cadre paisible et authentique.",
                    region = "Djerba",
                    ville = "Houmt Souk",
                    saison = "printemps / automne",
                    prixNuite = 370.0,
                    vueMer = false,
                    procheMer = "Non",
                    avis = 9.0f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1FjFZqoBxGE6wJpUy_WKm0y4wfiFG1FVz/view"
                ),
                MaisonHoteEntity(
                    nom = "DAR EL-HACHR",
                    description = "Hébergement touristique moderne intégré dans l'environnement oasien. Il combine confort contemporain et immersion dans le paysage désertique.",
                    region = "Sud Ouest",
                    ville = "Tozeur",
                    saison = "automne / hiver / printemps",
                    prixNuite = 280.0,
                    vueMer = false,
                    procheMer = "Non",
                    avis = 8.7f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1FoPELX_9wL8xn-1EQHuB7F-wK7SBr5VB/view"
                ),
                MaisonHoteEntity(
                    nom = "Étoile Sing",
                    description = "Maison d'hébergement touristique située dans la région de Tozeur, offrant un point de départ idéal pour explorer les oasis et le tourisme d'aventure.",
                    region = "Sud Ouest",
                    ville = "Tozeur",
                    saison = "automne / hiver / printemps",
                    prixNuite = 300.0,
                    vueMer = false,
                    procheMer = "Non",
                    avis = 8.5f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1FqoFH8K5jV1DGJdm9PJqCLjwB-wK7SVz/view"
                ),
                MaisonHoteEntity(
                    nom = "Dar Agez Tamezert",
                    description = "Maison d'hôtes berbère située dans le village de Touâgen, combinant architecture traditionnelle et cadre spectaculaire.",
                    region = "Sud Est",
                    ville = "Tamezret",
                    saison = "automne / hiver / printemps",
                    prixNuite = 420.0,
                    vueMer = false,
                    procheMer = "Non",
                    avis = 9.5f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1FuFs5Z6uM3ozMEbJ7gV_3-1eKQdQ1FVB/view"
                ),
                MaisonHoteEntity(
                    nom = "Dar Fatma Touzeur",
                    description = "Maison d'hôtes située dans un cadre oasien où l'hébergement ample offre un charme authentique et un confort moderne.",
                    region = "Sud Est",
                    ville = "Tamezret",
                    saison = "automne / hiver / printemps",
                    prixNuite = 350.0,
                    vueMer = false,
                    procheMer = "Oui (but near oasis)",
                    avis = 8.8f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1Fv9Ck9xE7_uP-NJoQlp3qwF7G6cF9VTz/view"
                ),
                MaisonHoteEntity(
                    nom = "Tadouinerarch",
                    description = "Hébergement touristique rural à Taraouine, orienté vers l'écotourisme et le séjours en milieu désert que. Il attire les amateurs de nature.",
                    region = "Sud Est",
                    ville = "Tataouine",
                    saison = "automne / hiver",
                    prixNuite = 170.0,
                    vueMer = false,
                    procheMer = "Non",
                    avis = 9.2f,
                    nbAvis = 1,
                    photoLink = "https://drive.google.com/file/d/1G0L7E8qZ9uM3pFJ7gV_3-1eKQdQ1FVB/view"
                )
            )

            maisons.forEach { maison ->
                dao.insertMaison(maison)
            }
        }
    }
}