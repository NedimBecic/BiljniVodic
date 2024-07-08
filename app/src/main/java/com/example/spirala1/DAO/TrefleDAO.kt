package com.example.spirala1.DAO

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.spirala1.Adapters.RetrofitInstance
import com.example.spirala1.Objects.BiljkaList.defaultImage
import com.example.spirala1.Models.Biljka
import com.example.spirala1.Models.Enums.KlimatskiTip
import com.example.spirala1.Models.Enums.Zemljiste
import com.example.spirala1.Models.SinglePlantResponseModel
import com.example.spirala1.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class TrefleDAO {

    private val token = "BcZv1-TvoH0KS0HzsqeIhRlJ6rNXyKdYHDEGqWk-Zd8"
    private lateinit var context: Context

    fun setContext(newContext: Context) {
        context = newContext
    }

    companion object {
        private var defaultBitmap: Bitmap? = null

        fun getDefaultBitmap(context: Context): Bitmap {
            if (defaultBitmap == null) {
                defaultBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.plant)
                defaultImage = true
            }
            return defaultBitmap!!
        }
    }

    suspend fun getImage(biljka: Biljka): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val latinskoIme = extractLatinskoIme(biljka.naziv)
                val response = RetrofitInstance.api.searchBiljke(latinskoIme, token)
                Log.d("TrefleDAO", "API Response: $response.id")
                if (response.isSuccessful) {
                    val plants = response.body()?.data
                    if (!plants.isNullOrEmpty()) {
                        val imageUrl = plants[0].imageUrl
                        val url = URL(imageUrl)
                        BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    } else {
                        getDefaultBitmap(context)
                    }
                } else {
                    getDefaultBitmap(context)
                }
            } catch (e: Exception) {
                getDefaultBitmap(context)
            }
        }
    }

    suspend fun fixData(biljka: Biljka): Biljka {
        return withContext(Dispatchers.IO) {
            val naziv = extractLatinskoIme(biljka.naziv)
            val searchResponse = RetrofitInstance.api.searchBiljke(naziv, token)
            if (searchResponse.isSuccessful) {
                val searchResults = searchResponse.body()?.data
                if (!searchResults.isNullOrEmpty()) {
                    val slug = searchResults[0].slug
                    val detailsResponse = RetrofitInstance.api.getBiljkeDetails(slug, token)
                    if (detailsResponse.isSuccessful) {
                        val details = detailsResponse.body()?.data
                        if (details != null) {
                            val updatedBiljka = mapResponseToBiljka(details, biljka)
                            return@withContext biljka.copy(
                                porodica = updatedBiljka.porodica,
                                medicinskoUpozorenje = updatedBiljka.medicinskoUpozorenje,
                                klimatskiTipovi = updatedBiljka.klimatskiTipovi,
                                zemljisniTipovi = updatedBiljka.zemljisniTipovi,
                                jela = updatedBiljka.jela
                            )
                        } else {
                            throw Exception("Nisu pronadjeni detalji biljke")
                        }
                    } else {
                        throw Exception(
                            "Greska pri nalazenju detalja biljke: ${
                                detailsResponse.errorBody()?.string()
                            }"
                        )
                    }
                } else {
                    throw Exception("Nema rezultata pri trazenju biljke")
                }
            } else {
                throw Exception(
                    "Greska pri trazenju biljke: ${
                        searchResponse.errorBody()?.string()
                    }"
                )
            }
        }
    }

    suspend fun getPlantsWithFlowerColor(flowerColor: String, substr: String): List<Biljka> {
        return withContext(Dispatchers.IO) {
            val biljkaList = mutableListOf<Biljka>()
            var page = 1
            var totalPages = 1
            do {
                val response = RetrofitInstance.api.getBiljkeWithFlowerColor(flowerColor.lowercase(), substr, token, page)
                //Log.d("TrefleDAO", "API Response: $response.id")
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        val plants = body.data
                        if (plants != null) {
                            for (plant in plants) {
                                val detailsResponse = RetrofitInstance.api.getBiljkeDetails(plant.slug, token)
                                if (detailsResponse.isSuccessful) {
                                    val details = detailsResponse.body()?.data
                                    if (details != null) {
                                        biljkaList.add(mapFlowerSearchResponseToBiljka(details))
                                    }
                                }
                            }
                        }
                        totalPages = (body.meta.total / 20) + 1
                        page++
                    } else {
                        break
                    }
                } else {
                    break
                }
            } while (page <= totalPages)
            return@withContext biljkaList
        }
    }


    private fun mapResponseToBiljka(details: SinglePlantResponseModel, biljka: Biljka): Biljka {
        val zemljisniTipovi = details.mainSpecies?.growth?.soilTexture?.mapNotNull { soil ->
            when (soil.toIntOrNull()) {
                1, 2 -> Zemljiste.GLINENO
                3, 4 -> Zemljiste.PJESKOVITO
                5, 6 -> Zemljiste.ILOVACA
                7, 8 -> Zemljiste.CRNICA
                9 -> Zemljiste.SLJUNKOVITO
                10 -> Zemljiste.KRECNJACKO
                else -> null
            }
        } ?: emptyList()

        val klimatskiTipovi = mutableListOf<KlimatskiTip>()
        val light = details.mainSpecies?.growth?.light
        val humidity = details.mainSpecies?.growth?.atmosphericHumidity

        if (light != null && humidity != null) {
            if (light in 6..9 && humidity in 1..5) klimatskiTipovi.add(KlimatskiTip.SREDOZEMNA)
            if (light in 8..10 && humidity in 7..10) klimatskiTipovi.add(KlimatskiTip.TROPSKA)
            if (light in 6..9 && humidity in 5..8) klimatskiTipovi.add(KlimatskiTip.SUBTROPSKA)
            if (light in 4..7 && humidity in 3..7) klimatskiTipovi.add(KlimatskiTip.UMJERENA)
            if (light in 7..9 && humidity in 1..2) klimatskiTipovi.add(KlimatskiTip.SUHA)
            if (light in 0..5 && humidity in 3..7) klimatskiTipovi.add(KlimatskiTip.PLANINSKA)
        }

        var medicinskoUpozorenje =
            details.mainSpecies?.specfications?.toxicity?.takeIf { it != "none" }?.let {
                val upozorenje = "TOKSIČNO"
                if (biljka.medicinskoUpozorenje.contains(upozorenje)) {
                    biljka.medicinskoUpozorenje
                } else {
                    "${biljka.medicinskoUpozorenje} $upozorenje".trim()
                }
            } ?: biljka.medicinskoUpozorenje

        var jela: List<String> = biljka.jela
        if (details.mainSpecies?.edible == false) {
            medicinskoUpozorenje += if (medicinskoUpozorenje.isEmpty()) "NIJE JESTIVO" else " NIJE JESTIVO"
            jela = listOf()
        }

        return Biljka(
            naziv = biljka.naziv,
            porodica = details.family.name,
            medicinskoUpozorenje = medicinskoUpozorenje,
            medicinskeKoristi = listOf(),
            profilOkusa = null,
            jela = jela,
            klimatskiTipovi = klimatskiTipovi,
            zemljisniTipovi = zemljisniTipovi,
            onlineChecked = true
        )
    }

    private fun mapFlowerSearchResponseToBiljka(details: SinglePlantResponseModel?): Biljka {
        val zemljisniTipovi = details?.mainSpecies?.growth?.soilTexture?.mapNotNull { soil ->
            when (soil.toIntOrNull()) {
                1, 2 -> Zemljiste.GLINENO
                3, 4 -> Zemljiste.PJESKOVITO
                5, 6 -> Zemljiste.ILOVACA
                7, 8 -> Zemljiste.CRNICA
                9 -> Zemljiste.SLJUNKOVITO
                10 -> Zemljiste.KRECNJACKO
                else -> null
            }
        } ?: emptyList()

        val klimatskiTipovi = mutableListOf<KlimatskiTip>()
        val light = details?.mainSpecies?.growth?.light
        val humidity = details?.mainSpecies?.growth?.atmosphericHumidity

        if (light != null && humidity != null) {
            if (light in 6..9 && humidity in 1..5) klimatskiTipovi.add(KlimatskiTip.SREDOZEMNA)
            if (light in 8..10 && humidity in 7..10) klimatskiTipovi.add(KlimatskiTip.TROPSKA)
            if (light in 6..9 && humidity in 5..8) klimatskiTipovi.add(KlimatskiTip.SUBTROPSKA)
            if (light in 4..7 && humidity in 3..7) klimatskiTipovi.add(KlimatskiTip.UMJERENA)
            if (light in 7..9 && humidity in 1..2) klimatskiTipovi.add(KlimatskiTip.SUHA)
            if (light in 0..5 && humidity in 3..7) klimatskiTipovi.add(KlimatskiTip.PLANINSKA)
        }

        var empty: String = details?.scientificName?: "Ime nije pronađeno"
        var porodicaEmpty: String = details?.family?.name ?: ""

        return Biljka(
            naziv = empty,
            porodica = porodicaEmpty,
            medicinskoUpozorenje = "",
            medicinskeKoristi = listOf(),
            profilOkusa = null,
            jela = listOf(),
            klimatskiTipovi = klimatskiTipovi,
            zemljisniTipovi = zemljisniTipovi,
            onlineChecked = false
        )
    }
    fun extractLatinskoIme(ime: String): String {
        return if (ime.contains("(") && ime.contains(")")) {
            ime.substringAfter("(").substringBefore(")")
        } else {
            ime
        }
    }
}