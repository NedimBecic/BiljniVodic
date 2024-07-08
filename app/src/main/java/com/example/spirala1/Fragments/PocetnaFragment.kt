package com.example.spirala1.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spirala1.Adapters.BotanickiListaAdapter
import com.example.spirala1.Adapters.CustomSpinnerAdapter
import com.example.spirala1.Adapters.KuharskiListaAdapter
import com.example.spirala1.Adapters.MedicinskiListaAdapter
import com.example.spirala1.DAO.BiljkaDAO
import com.example.spirala1.DAO.BiljkaDatabase
import com.example.spirala1.Objects.BiljkaList.filterOn
import com.example.spirala1.Data.BiljkeStaticData
import com.example.spirala1.R
import com.example.spirala1.DAO.TrefleDAO
import kotlinx.coroutines.launch

class PocetnaFragment : Fragment() {

    private lateinit var biljkeLista: RecyclerView
    private lateinit var medicinskiListaAdapter: MedicinskiListaAdapter
    private lateinit var botanickiListaAdapter: BotanickiListaAdapter
    private lateinit var kuharskiListaAdapter: KuharskiListaAdapter

    //private lateinit var bojeSpinner: Spinner
    //private lateinit var colorTitle: TextView
    //private lateinit var search: EditText
    //private lateinit var searchButton: Button

    private var isFiltered = false
    private lateinit var trefleDAO: TrefleDAO
    private lateinit var biljkaDAO: BiljkaDAO

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.pocetna_layout, container, false)

        trefleDAO = TrefleDAO()
        trefleDAO.setContext(requireContext())

        biljkaDAO = BiljkaDatabase.getInstance(requireContext()).biljkaDao()

        biljkeLista = view.findViewById(R.id.biljkeRV)
        biljkeLista.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val izbor = resources.getStringArray(R.array.Izbor)
        val spinner = view.findViewById<Spinner>(R.id.modSpinner)
        var mod = ""
        var prethodniMod = ""

        val boje = resources.getStringArray(R.array.boje)
        //bojeSpinner = view.findViewById(R.id.bojaSPIN)
        val bojeAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, boje)
        bojeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //bojeSpinner.adapter = bojeAdapter

//        colorTitle = view.findViewById(R.id.dodatniFilter)
//        search = view.findViewById(R.id.pretragaET)
//        searchButton = view.findViewById(R.id.brzaPretraga)

        lifecycleScope.launch {
            initializeDatabase()
            var biljke = biljkaDAO.getAllBiljkas()

            if (spinner != null) {
                val adapter = CustomSpinnerAdapter(requireContext(), izbor)
                spinner.adapter = adapter

                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (parent != null) {
                            when (parent.getItemAtPosition(position) as String) {
                                "Medicinski" -> {
                                    mod = "Medicinski"
                                    medicinskiListaAdapter = MedicinskiListaAdapter(arrayListOf(), { clickedBiljka -> medicinskiListaAdapter.filterOnClick(clickedBiljka) }, trefleDAO, biljkaDAO)
                                    biljkeLista.adapter = medicinskiListaAdapter

                                    if (prethodniMod == "Kuharski") {
                                        isFiltered = kuharskiListaAdapter.isFiltered
                                        if (isFiltered) biljke = kuharskiListaAdapter.filteredList
                                    } else if (prethodniMod == "Botanički") {
                                        isFiltered = botanickiListaAdapter.isFiltered
                                        if (isFiltered) biljke = botanickiListaAdapter.filteredList
                                    }

                                    medicinskiListaAdapter.updateBiljke(biljke)
                                    prethodniMod = "Medicinski"
                                    //sakrijDodatniFilter()
                                }
                                "Kuharski" -> {
                                    mod = "Kuharski"
                                    kuharskiListaAdapter = KuharskiListaAdapter(arrayListOf(), { clickedBiljka -> kuharskiListaAdapter.filterOnClick(clickedBiljka) }, trefleDAO, biljkaDAO)
                                    biljkeLista.adapter = kuharskiListaAdapter

                                    if (prethodniMod == "Medicinski") {
                                        isFiltered = medicinskiListaAdapter.isFiltered
                                        if (isFiltered) biljke = medicinskiListaAdapter.filteredList
                                    } else if (prethodniMod == "Botanički") {
                                        isFiltered = botanickiListaAdapter.isFiltered
                                        if (isFiltered) biljke = botanickiListaAdapter.filteredList
                                    }

                                    kuharskiListaAdapter.updateBiljke(biljke)
                                    prethodniMod = "Kuharski"
                                    //sakrijDodatniFilter()
                                }
                                "Botanički" -> {
                                    mod = "Botanički"
                                    botanickiListaAdapter = BotanickiListaAdapter(arrayListOf(), { clickedBiljka -> botanickiListaAdapter.filterOnClick(clickedBiljka) }, trefleDAO, biljkaDAO)
                                    biljkeLista.adapter = botanickiListaAdapter

                                    if (prethodniMod == "Medicinski") {
                                        isFiltered = medicinskiListaAdapter.isFiltered
                                        if (isFiltered) biljke = medicinskiListaAdapter.filteredList
                                    } else if (prethodniMod == "Kuharski") {
                                        isFiltered = kuharskiListaAdapter.isFiltered
                                        if (isFiltered) biljke = kuharskiListaAdapter.filteredList
                                    }

                                    botanickiListaAdapter.updateBiljke(biljke)
                                    prethodniMod = "Botanički"
                                    //prikaziDodatniFilter()
                                }
                            }
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        medicinskiListaAdapter = MedicinskiListaAdapter(
                            arrayListOf(),
                            { clickedBiljka -> medicinskiListaAdapter.filterOnClick(clickedBiljka) },
                            trefleDAO,
                            biljkaDAO
                        )
                        biljkeLista.adapter = medicinskiListaAdapter
                        medicinskiListaAdapter.updateBiljke(biljke)
                    }
                }

//                searchButton.setOnClickListener {
//                    val query = search.text.toString()
//                    val selectedColor = bojeSpinner.selectedItem.toString()
//                    if (query.isNotEmpty() && selectedColor != "Odaberite boju") {
//                        lifecycleScope.launch {
//                            val searchResults = trefleDAO.getPlantsWithFlowerColor(selectedColor, query)
//                            filterOn = false
//                            botanickiListaAdapter.updateBiljke(ArrayList(searchResults))
//                        }
//                    } else {
//                        Toast.makeText(requireContext(), "Mora biti odabrana boja i unesen text da pretragu", Toast.LENGTH_SHORT).show()
//                    }
//                }

                val buttonClick = view.findViewById<Button>(R.id.resetBtn)
                buttonClick.setOnClickListener {
                    lifecycleScope.launch {
                        val biljke = biljkaDAO.getAllBiljkas()
                        filterOn = true
                        when (mod) {
                            "Medicinski" -> {
                                medicinskiListaAdapter.updateBiljke(biljke)
                                medicinskiListaAdapter.isFiltered = false
                            }
                            "Kuharski" -> {
                                kuharskiListaAdapter.updateBiljke(biljke)
                                kuharskiListaAdapter.isFiltered = false
                            }
                            "Botanički" -> {
                                botanickiListaAdapter.updateBiljke(biljke)
                                botanickiListaAdapter.isFiltered = false
                            }
                        }
                        isFiltered = false
                    }
                }

            }
        }

        return view
    }

//    private fun prikaziDodatniFilter() {
//        colorTitle.visibility = View.VISIBLE
//        bojeSpinner.visibility = View.VISIBLE
//        search.visibility = View.VISIBLE
//        searchButton.visibility = View.VISIBLE
//    }
//
//    private fun sakrijDodatniFilter() {
//        colorTitle.visibility = View.GONE
//        bojeSpinner.visibility = View.GONE
//        search.visibility = View.GONE
//        searchButton.visibility = View.GONE
//    }

    private suspend fun initializeDatabase() {
        val biljkeInDb = biljkaDAO.getAllBiljkas()
        if (biljkeInDb.isEmpty()) {
            BiljkeStaticData.biljke.forEach { biljka ->
                biljkaDAO.saveBiljka(biljka)
            }
        }
    }
}
