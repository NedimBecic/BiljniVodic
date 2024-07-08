package com.example.spirala1

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.spirala1.DAO.BiljkaDAO
import com.example.spirala1.DAO.BiljkaDatabase
import com.example.spirala1.Models.Biljka
import com.example.spirala1.Models.Enums.ProfilOkusaBiljke
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var biljkaDao: BiljkaDAO
    private lateinit var db: BiljkaDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BiljkaDatabase::class.java).build()
        biljkaDao = db.biljkaDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun dodajProvjeri() = runBlocking {
        val biljka = Biljka(
            naziv = "Lavanda (Lavandula)",
            porodica = "Lamiaceae",
            medicinskoUpozorenje = "",
            medicinskeKoristi = emptyList(),
            profilOkusa = ProfilOkusaBiljke.SLATKI,
            jela = listOf("Caj"),
            klimatskiTipovi = emptyList(),
            zemljisniTipovi = emptyList(),
            onlineChecked = false
        )

        biljkaDao.saveBiljka(biljka)
        val allBiljkas = biljkaDao.getAllBiljkas()
        assertThat(allBiljkas.size, equalTo(1))
        assertThat(allBiljkas[0], equalTo(biljka.copy(id = allBiljkas[0].id)))
    }

    @Test
    @Throws(Exception::class)
    fun ocistiProvjeri() = runBlocking {
        val biljka1 = Biljka(
            naziv = "Lavanda (Lavandula)",
            porodica = "Lamiaceae",
            medicinskoUpozorenje = "",
            medicinskeKoristi = emptyList(),
            profilOkusa = ProfilOkusaBiljke.SLATKI,
            jela = listOf("Caj"),
            klimatskiTipovi = emptyList(),
            zemljisniTipovi = emptyList(),
            onlineChecked = false
        )
        val biljka2 = Biljka(
            naziv = "Kamilica (Matricaria chamomilla)",
            porodica = "Asteraceae",
            medicinskoUpozorenje = "",
            medicinskeKoristi = emptyList(),
            profilOkusa = ProfilOkusaBiljke.GORKO,
            jela = listOf("Caj"),
            klimatskiTipovi = emptyList(),
            zemljisniTipovi = emptyList(),
            onlineChecked = false
        )
        biljkaDao.saveBiljka(biljka1)
        biljkaDao.saveBiljka(biljka2)
        biljkaDao.clearData()
        val allBiljkas = biljkaDao.getAllBiljkas()
        assertThat(allBiljkas.size, equalTo(0))
    }
    @Test
    @Throws(Exception::class)
    fun testFixOfflineBiljka() {
        val biljka1 = Biljka(
            naziv = "Biljka (Eriogonum thymoides)",
            porodica = "porodica",
            medicinskoUpozorenje = "Medicinsko upozorenje",
            medicinskeKoristi = listOf(),
            profilOkusa = ProfilOkusaBiljke.SLATKI,
            jela = listOf("jelo"),
            klimatskiTipovi = listOf(),
            zemljisniTipovi = listOf()
        )
        val biljka2 = Biljka(
            naziv = "Biljka (Taraxacum palustre)",
            porodica = "porodica",
            medicinskoUpozorenje = "Medicinsko upozorenje",
            medicinskeKoristi = listOf(),
            profilOkusa = ProfilOkusaBiljke.SLATKI,
            jela = listOf("jelo"),
            klimatskiTipovi = listOf(),
            zemljisniTipovi = listOf()
        )

        GlobalScope.launch(Dispatchers.IO) {
            biljkaDao.saveBiljka(biljka1)
            biljkaDao.saveBiljka(biljka2)

            val updatedCount = biljkaDao.fixOfflineBiljka()

            val updatedBiljka1 = biljkaDao.getAllBiljkas().find { it.naziv == biljka1.naziv }
            val updatedBiljka2 = biljkaDao.getAllBiljkas().find { it.naziv == biljka2.naziv }

            assertEquals(2, updatedCount)
            assertEquals(true, updatedBiljka1?.onlineChecked)
            assertEquals(true, updatedBiljka2?.onlineChecked)
        }
    }
}







