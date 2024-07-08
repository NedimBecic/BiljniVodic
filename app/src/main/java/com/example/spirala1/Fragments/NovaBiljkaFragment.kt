package com.example.spirala1.Fragments

import com.example.spirala1.Adapters.ZemljisniTipChoiceAdapter
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.widget.ListView
import android.widget.Toast
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.spirala1.Adapters.KlimatskiTipChoiceAdapter
import com.example.spirala1.Adapters.ListaJelaAdapter
import com.example.spirala1.Adapters.MedicinskaKoristChoiceAdapter
import com.example.spirala1.Adapters.ProfilOkusaChoiceAdapter
import com.example.spirala1.Models.Biljka
import com.example.spirala1.DAO.BiljkaDAO
import com.example.spirala1.DAO.BiljkaDatabase
import com.example.spirala1.Activities.CameraActivity
import com.example.spirala1.Models.Enums.KlimatskiTip
import com.example.spirala1.Activities.MainActivity
import com.example.spirala1.Models.Enums.MedicinskaKorist
import com.example.spirala1.Models.Enums.ProfilOkusaBiljke
import com.example.spirala1.R
import com.example.spirala1.DAO.TrefleDAO
import com.example.spirala1.Models.Enums.Zemljiste
import com.google.android.material.textfield.TextInputLayout


class NovaBiljkaFragment : Fragment() {

    private lateinit var nazivET: TextInputLayout
    private lateinit var porodicaET: TextInputLayout
    private lateinit var medicinskoUpozorenjeET: TextInputLayout
    private lateinit var jeloET: TextInputLayout
    private lateinit var submitButton: Button
    private lateinit var addJelo: Button
    private lateinit var listaJelaAdapter: ListaJelaAdapter
    private lateinit var listaJela: ListView
    private lateinit var slikajBtn: Button
    private lateinit var slika: ImageView
    private lateinit var trefleDAO: TrefleDAO
    private lateinit var biljkaDao: BiljkaDAO
    private var capturedBitmap: Bitmap? = null
    var validation = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.dodaj_biljku_forma, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trefleDAO = TrefleDAO()
        trefleDAO.setContext(requireContext())

        biljkaDao = BiljkaDatabase.getInstance(requireContext()).biljkaDao()

        val medicinskaKoristLV = view.findViewById<ListView>(R.id.medicinskaKoristLV)
        val klimatskiTipLV = view.findViewById<ListView>(R.id.klimatskiTipLV)
        val zemljisniTipLV = view.findViewById<ListView>(R.id.zemljisniTipLV)
        val profilOkusaBiljkeLV = view.findViewById<ListView>(R.id.profilOkusaLV)

        val medicinskaKoristAdapter = MedicinskaKoristChoiceAdapter(
            requireContext(),
            MedicinskaKorist.entries.toTypedArray()
        )
        medicinskaKoristLV.adapter = medicinskaKoristAdapter
        medicinskaKoristLV.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        val klimatskiTipAdapter = KlimatskiTipChoiceAdapter(
            requireContext(),
            KlimatskiTip.entries.toTypedArray()
        )
        klimatskiTipLV.adapter = klimatskiTipAdapter
        klimatskiTipLV.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        val zemljisteAdapter = ZemljisniTipChoiceAdapter(requireContext(), Zemljiste.entries.toTypedArray())
        zemljisniTipLV.adapter = zemljisteAdapter
        zemljisniTipLV.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        val profilOkusaAdapter = ProfilOkusaChoiceAdapter(
            requireContext(),
            ProfilOkusaBiljke.entries.toTypedArray()
        )
        profilOkusaBiljkeLV.adapter = profilOkusaAdapter
        profilOkusaBiljkeLV.choiceMode = ListView.CHOICE_MODE_SINGLE

        submitButton = view.findViewById(R.id.dodajBiljkuBtn)
        nazivET = view.findViewById(R.id.nazivET)
        porodicaET = view.findViewById(R.id.porodicaET)
        medicinskoUpozorenjeET = view.findViewById(R.id.medicinskoUpozorenjeET)
        jeloET = view.findViewById(R.id.jeloET)

        listaJela = view.findViewById(R.id.jelaLV)
        val jela = mutableListOf<String>()
        listaJelaAdapter = ListaJelaAdapter(requireContext(), jela)
        listaJela.adapter = listaJelaAdapter

        addJelo = view.findViewById(R.id.dodajJeloBtn)
        addJelo.setOnClickListener {
            val novoJelo = jeloET.editText?.text.toString()
            if (novoJelo.length < 2 || novoJelo.length > 20) {
                jeloET.error = "Duzina jela mora biti najmanje 2 karaktera, a najvise 20 karaktera duga!"
            } else if (novoJelo.isNotBlank()) {
                if (!listaJelaAdapter.addItem(novoJelo)) {
                    jeloET.error = "Jelo vec postoji!"
                } else {
                    jeloET.editText?.setText("")
                }
            }
        }

        listaJela.setOnItemClickListener { _, view, position, _ ->
            val odabir = listaJelaAdapter.getItem(position)
            jeloET.editText?.setText(odabir)
            addJelo.text = "Izmijeni jelo"

            addJelo.setOnClickListener {
                val updatedJelo = jeloET.editText?.text.toString()
                if (checkInput(updatedJelo)) {
                    updateItem(position, updatedJelo)
                }
            }
        }

        slikajBtn = view.findViewById(R.id.uslikajBiljkuBtn)
        slika = view.findViewById(R.id.slikaIV)

        slikajBtn.setOnClickListener {
            checkPermission(Manifest.permission.CAMERA, 100)
        }

        submitButton.setOnClickListener {
            checkValidation(
                medicinskaKoristLV,
                klimatskiTipLV,
                zemljisniTipLV,
                profilOkusaBiljkeLV
            )

            if (validation) {
                val selectedMedicinskeKoristi = mutableListOf<MedicinskaKorist>()
                var checkedPositions = medicinskaKoristLV.checkedItemPositions

                for (i in 0 until medicinskaKoristLV.adapter.count) {
                    if (checkedPositions[i]) {
                        selectedMedicinskeKoristi.add(MedicinskaKorist.entries[i])
                    }
                }

                val selectedKlimatskiTip = mutableListOf<KlimatskiTip>()
                checkedPositions = klimatskiTipLV.checkedItemPositions

                for (i in 0 until klimatskiTipLV.adapter.count) {
                    if (checkedPositions[i]) {
                        selectedKlimatskiTip.add(KlimatskiTip.entries[i])
                    }
                }

                val selectedZemljisniTip = mutableListOf<Zemljiste>()
                checkedPositions = zemljisniTipLV.checkedItemPositions

                for (i in 0 until zemljisniTipLV.adapter.count) {
                    if (checkedPositions[i]) {
                        selectedZemljisniTip.add(Zemljiste.entries[i])
                    }
                }
                val selectedPosition = profilOkusaBiljkeLV.checkedItemPosition
                var checkedProfilOkusa = ProfilOkusaBiljke.entries[selectedPosition]

                val listJela = listaJelaAdapter.getListJelo()

                val formBiljka = Biljka(
                    naziv = nazivET.editText?.text.toString(),
                    porodica = porodicaET.editText?.text.toString(),
                    medicinskoUpozorenje = medicinskoUpozorenjeET.editText?.text.toString(),
                    medicinskeKoristi = selectedMedicinskeKoristi,
                    profilOkusa = checkedProfilOkusa,
                    jela = listJela,
                    klimatskiTipovi = selectedKlimatskiTip,
                    zemljisniTipovi = selectedZemljisniTip,
                    onlineChecked = false,
                    favourite = false
                )
                lifecycleScope.launch {
                    val biljkaSaved = biljkaDao.saveBiljka(formBiljka)
                    if (biljkaSaved) {
                        val savedBiljka =
                            biljkaDao.getAllBiljkas().find { it.naziv == formBiljka.naziv }
                        if (savedBiljka != null) {
                            val biljkaId = savedBiljka.id
                            val fixedBiljka = trefleDAO.fixData(formBiljka)
                            biljkaDao.updateBiljka(fixedBiljka.copy(id = biljkaId))

                            capturedBitmap?.let { bitmap ->
                                val cropWidth = 500
                                val cropHeight = 500
                                val croppedBitmap = cropBitmap(bitmap, cropWidth, cropHeight)
                                biljkaDao.addImage(biljkaId, croppedBitmap)
                            }
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Greska pri dodavanju biljke!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            data?.let {
                val byteArray = it.getByteArrayExtra("captured_image")
                if (byteArray != null) {
                    capturedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    slika.setImageBitmap(capturedBitmap)
                }
            }
        }
    }

    fun updateItem(position: Int, item: String) {
        listaJelaAdapter.items[position] = item
        listaJelaAdapter.notifyDataSetChanged()
        jeloET.editText?.setText("")
        addJelo.text = "Dodaj jelo"

        addJelo.setOnClickListener {
            val novoJelo = jeloET.editText.toString()
            if (checkInput(novoJelo)) {
                if (listaJelaAdapter.addItem(novoJelo)) {
                    jeloET.editText?.setText("")
                }
            }
        }
    }

    private fun checkInput(input: String): Boolean {
        return if (input.isBlank() || input.length < 2 || input.length > 20) {
            jeloET.error = "Duzina jela mora biti najmanje 2 karaktera, a najvise 20 karaktera duga!"
            false
        } else true
    }

    private fun checkValidation(
        medicinskaKoristLV: ListView,
        klimatskiTipLV: ListView,
        zemljisniTipLV: ListView,
        profilOkusaBiljkeLV: ListView
    ) {
        var isValid = true

        if (nazivET.editText?.text.toString().isEmpty() || nazivET.editText?.text.toString().length < 3) {
            nazivET.error = "Naziv mora imati vise od 3 karaktera"
            isValid = false
        }

        if (porodicaET.editText?.text.toString().isEmpty() || porodicaET.editText?.text.toString().length < 3) {
            porodicaET.error = "Naziv porodice mora imati vise od 3 karaktera"
            isValid = false
        }

        if (medicinskaKoristLV.checkedItemCount < 1) {
            Toast.makeText(requireContext(), "Mora biti cekirana barem jedna medicinska korist!", Toast.LENGTH_LONG).show()
            isValid = false
        }

        if (klimatskiTipLV.checkedItemCount < 1) {
            Toast.makeText(requireContext(), "Mora biti cekiran barem jedan klimatski tip!", Toast.LENGTH_LONG).show()
            isValid = false
        }

        if (zemljisniTipLV.checkedItemCount < 1) {
            Toast.makeText(requireContext(), "Mora biti cekiran barem jedan zemljisni tip!", Toast.LENGTH_LONG).show()
            isValid = false
        }

        if (profilOkusaBiljkeLV.checkedItemCount < 1) {
            Toast.makeText(requireContext(), "Mora biti cekiran barem jedan profil okusa biljke!", Toast.LENGTH_LONG).show()
            isValid = false
        }

        validation = isValid
    }

    private fun cropBitmap(bitmap: Bitmap, cropWidth: Int, cropHeight: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val startX = (width - cropWidth) / 2
        val startY = (height - cropHeight) / 2
        return Bitmap.createBitmap(bitmap, startX, startY, cropWidth, cropHeight)
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if(ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(requireContext() as Activity, arrayOf(permission), requestCode)
        } else {
            val intent = Intent(requireContext(), CameraActivity::class.java)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 100) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(requireContext(), CameraActivity::class.java)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            } else {
                Toast.makeText(requireContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
