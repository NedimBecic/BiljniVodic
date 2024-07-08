package com.example.spirala1.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.spirala1.Models.Enums.KlimatskiTip
import com.example.spirala1.DAO.KlimatskiTipConverter
import com.example.spirala1.Models.Enums.MedicinskaKorist
import com.example.spirala1.DAO.MedicinskaKoristConverter
import com.example.spirala1.Models.Enums.ProfilOkusaBiljke
import com.example.spirala1.DAO.ProfilOkusaBiljkeConverter
import com.example.spirala1.DAO.StringListConverter
import com.example.spirala1.Models.Enums.Zemljiste
import com.example.spirala1.DAO.ZemljisteConverter

@Entity(tableName = "biljka")
data class Biljka(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name ="naziv") val naziv: String,
    @ColumnInfo(name ="family") val porodica: String,
    @ColumnInfo(name ="medicinskoUpozorenje") val medicinskoUpozorenje: String,
    @TypeConverters(MedicinskaKoristConverter::class)
    @ColumnInfo(name ="medicinskeKoristi") val medicinskeKoristi: List<MedicinskaKorist>,
    @TypeConverters(ProfilOkusaBiljkeConverter::class)
    @ColumnInfo(name ="profilOkusa") val profilOkusa: ProfilOkusaBiljke?,
    @TypeConverters(StringListConverter::class)
    @ColumnInfo(name ="jela") val jela: List<String>,
    @TypeConverters(KlimatskiTipConverter::class)
    @ColumnInfo(name ="klimatskiTipovi") val klimatskiTipovi: List<KlimatskiTip>,
    @TypeConverters(ZemljisteConverter::class)
    @ColumnInfo(name ="zemljisniTipovi") val zemljisniTipovi: List<Zemljiste>,
    @ColumnInfo(name ="onlineChecked") val onlineChecked: Boolean = false,
    @ColumnInfo(name ="favourite") var favourite: Boolean = false
)

