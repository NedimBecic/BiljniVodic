//package com.example.spirala1
//
//import android.app.Activity
//import android.app.Instrumentation
//import android.content.Intent
//import android.graphics.drawable.BitmapDrawable
//import android.net.Uri
//import android.provider.MediaStore
//import android.view.View
//import android.widget.ImageView
//import android.widget.ListView
//import android.widget.TextView
//import androidx.core.content.ContextCompat
//import androidx.recyclerview.widget.RecyclerView
//import androidx.test.core.app.ApplicationProvider
//import androidx.test.espresso.Espresso.onData
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.Root
//import androidx.test.espresso.action.ViewActions.clearText
//import androidx.test.espresso.matcher.ViewMatchers.withId
//import androidx.test.espresso.assertion.ViewAssertions.matches
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
//import androidx.test.espresso.action.ViewActions.scrollTo
//import androidx.test.espresso.action.ViewActions.typeText
//import androidx.test.espresso.intent.Intents
//import androidx.test.espresso.intent.matcher.IntentMatchers
//import androidx.test.espresso.intent.rule.IntentsTestRule
//import androidx.test.espresso.matcher.BoundedMatcher
//import androidx.test.espresso.matcher.RootMatchers.withDecorView
//import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
//import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
//import androidx.test.espresso.matcher.ViewMatchers.withText
//import androidx.test.ext.junit.rules.ActivityScenarioRule
//import androidx.test.platform.app.InstrumentationRegistry
//import com.example.spirala1.Objects.SlikaState.testSlika
//import org.junit.Test
//import org.hamcrest.Description
//import org.hamcrest.Matcher
//import org.hamcrest.MatcherAssert
//import org.hamcrest.Matchers.instanceOf
//import org.hamcrest.Matchers.`is`
//import org.hamcrest.Matchers.not
//import org.hamcrest.TypeSafeMatcher
//import org.hamcrest.number.OrderingComparison
//import org.junit.Rule
//
//class Spirala2Testovi {
//
//    @get:Rule
//    var activityScenarioRule = ActivityScenarioRule(
//        MainActivity::class.java
//    )
//
//    @Test
////    fun dodjiDoNovaBiljkaActivity() {
////        onView(withId(R.id.novaBiljkaBtn)).perform(click())
////    }
//
//    @Test
//    fun testValidacijaDuzineKaraktera() {
//        //dodjiDoNovaBiljkaActivity()
//        onView(withId(R.id.nazivET)).perform(clearText(), typeText("A"), closeSoftKeyboard())
//        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Duzina naziva mora biti najmanje 2 karaktera, a najvise 20 karaktera duga!")))
//        onView(withId(R.id.nazivET)).perform(scrollTo(), click())
//        onView(withId(R.id.nazivET)).perform(clearText(), typeText("Ovajtextsigurnoimaviseoddvadesetkaraktera"), closeSoftKeyboard())
//        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Duzina naziva mora biti najmanje 2 karaktera, a najvise 20 karaktera duga!")))
//
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo(), click())
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(clearText(), typeText("M"), closeSoftKeyboard())
//        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Duzina medicinskog upozerenja mora biti najmanje 2 karaktera, a najvise 20 karaktera duga!")))
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo(), click())
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(clearText(), typeText("Ovajtextsigurnoimaviseoddvadesetkaraktera"), closeSoftKeyboard())
//        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Duzina medicinskog upozerenja mora biti najmanje 2 karaktera, a najvise 20 karaktera duga!")))
//
//        onView(withId(R.id.porodicaET)).perform(scrollTo(), click())
//        onView(withId(R.id.porodicaET)).perform(clearText(), typeText("P"), closeSoftKeyboard())
//        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Duzina porodice mora biti najmanje 2 karaktera, a najvise 20 karaktera duga!")))
//        onView(withId(R.id.porodicaET)).perform(scrollTo(), click())
//        onView(withId(R.id.porodicaET)).perform(scrollTo(), clearText(), typeText("Ovajtextsigurnoimaviseoddvadesetkaraktera"), closeSoftKeyboard())
//        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Duzina porodice mora biti najmanje 2 karaktera, a najvise 20 karaktera duga!")))
//
//
//        onView(withId(R.id.jeloET)).perform(scrollTo(), click())
//        onView(withId(R.id.jeloET)).perform(clearText(), typeText("J"), closeSoftKeyboard())
//        onView(withId(R.id.dodajJeloBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Duzina jela mora biti najmanje 2 karaktera, a najvise 20 karaktera duga!")))
//        onView(withId(R.id.jeloET)).perform(scrollTo(), click())
//        onView(withId(R.id.jeloET)).perform(scrollTo(), clearText(), typeText("Ovajtextsigurnoimaviseoddvadesetkaraktera"), closeSoftKeyboard())
//        onView(withId(R.id.dodajJeloBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Duzina jela mora biti najmanje 2 karaktera, a najvise 20 karaktera duga!")))
//
//    }
//
//    @Test
//    fun testDupliciranjeJela() {
//        dodjiDoNovaBiljkaActivity()
//        val jelo = "Grah"
//        onView(withId(R.id.jeloET))
//            .perform(scrollTo(), click(), clearText(), typeText(jelo), closeSoftKeyboard())
//        onView(withId(R.id.dodajJeloBtn))
//            .perform(scrollTo(), click())
//        onView(withId(R.id.jeloET))
//            .perform(scrollTo(), click(), clearText(), typeText(jelo.toLowerCase()), closeSoftKeyboard())
//        onView(withId(R.id.dodajJeloBtn))
//            .perform(scrollTo(), click())
//        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Jelo vec postoji!")))
//    }
//
//    @Test
//        fun testMedicinskaKorist() {
//            dodjiDoNovaBiljkaActivity()
//            onData(`is`(instanceOf(MedicinskaKorist::class.java)))
//                .inAdapterView(withId(R.id.medicinskaKoristLV))
//                .atPosition(MedicinskaKorist.SMIRENJE.ordinal)
//                .perform(click())
//            onView(withId(R.id.dodajBiljkuBtn))
//                .perform(scrollTo(), click())
//            onView(withId(R.id.medicinskaKoristLV)).check(matches(hasCheckedItem()))
//        }
//
//        @Test
//        fun testKlimatskiTip() {
//            dodjiDoNovaBiljkaActivity()
//            onData(`is`(instanceOf(KlimatskiTip::class.java)))
//                .inAdapterView(withId(R.id.klimatskiTipLV))
//                .atPosition(KlimatskiTip.SREDOZEMNA.ordinal)
//                .perform(click())
//            onView(withId(R.id.dodajBiljkuBtn))
//                .perform(scrollTo(), click())
//            onView(withId(R.id.klimatskiTipLV)).check(matches(hasCheckedItem()))
//        }
//
//    @Test
//    fun testZemljiste() {
//        dodjiDoNovaBiljkaActivity()
//        onView(withId(R.id.zemljisniTipLV))
//            .perform(scrollTo())
//            .check(matches(isDisplayed()))
//        onData(`is`(instanceOf(Zemljiste::class.java)))
//            .inAdapterView(withId(R.id.zemljisniTipLV))
//            .atPosition(Zemljiste.PJESKOVITO.ordinal)
//            .perform(click())
//        onView(withId(R.id.dodajBiljkuBtn))
//            .perform(scrollTo(), click())
//        onView(withId(R.id.zemljisniTipLV)).check(matches(hasCheckedItem()))
//    }
//
//    @Test
//    fun testProfilOkusa() {
//        dodjiDoNovaBiljkaActivity()
//        onView(withId(R.id.profilOkusaLV))
//            .perform(scrollTo())
//            .check(matches(isDisplayed()))
//        onData(`is`(instanceOf(ProfilOkusaBiljke::class.java)))
//            .inAdapterView(withId(R.id.profilOkusaLV))
//            .atPosition(ProfilOkusaBiljke.MENTA.ordinal)
//            .perform(click())
//        onView(withId(R.id.dodajBiljkuBtn))
//            .perform(scrollTo(), click())
//        onView(withId(R.id.profilOkusaLV)).check(matches(hasExactlyOneCheckedItem()))
//    }
//
//
//    //Test mocka otvaranje previewa za slikanje klikom na uslikajBiljkuBtn, mijenja se vrijednost testSlika koja
//    // govori slikajBtn2 da se radi o testiranju te zapravo vrsi mock slike tako sto se displaya plant.png
//    //i onda se vrsi poredenje da li je ta nova slika zapravo ista kao i placeholder slika, ako nije znaci da je
//    //bio pozvan intent za slikanje i da je slikanje izvrseno
//    @Test
//    fun testSlikanje() {
//        dodjiDoNovaBiljkaActivity()
//        onView(withId(R.id.uslikajBiljkuBtn)).perform(scrollTo(), click())
//        testSlika = true
//        onView(withId(R.id.uslikajBiljkuBtn2)).perform(click())
//        onView(withId(R.id.slikaIV)).perform(scrollTo())
//        onView(withId(R.id.slikaIV)).check(matches(photoMatching(R.drawable.plant, R.drawable.placeholder)))
//        onView(withId(R.id.dodajJeloBtn)).perform(scrollTo(), click())
//    }
//
//    @Test
//    fun citavaForma() {
//        dodjiDoNovaBiljkaActivity()
//        onView(withId(R.id.nazivET)).perform(clearText(), typeText("Naziv"), closeSoftKeyboard())
//        onView(withId(R.id.porodicaET)).perform(scrollTo(), click())
//        onView(withId(R.id.porodicaET)).perform(clearText(), typeText("Porodica"), closeSoftKeyboard())
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo(), click())
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(clearText(), typeText("Upozorenje"), closeSoftKeyboard())
//        onView(withId(R.id.jeloET)).perform(scrollTo(), click())
//        onView(withId(R.id.jeloET)).perform(clearText(), typeText("Jelo"), closeSoftKeyboard())
//        onView(withId(R.id.dodajJeloBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.medicinskaKoristLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(MedicinskaKorist::class.java))).inAdapterView(withId(R.id.medicinskaKoristLV)).atPosition(MedicinskaKorist.SMIRENJE.ordinal).perform(click())
//        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(KlimatskiTip::class.java))).inAdapterView(withId(R.id.klimatskiTipLV)).atPosition(KlimatskiTip.SREDOZEMNA.ordinal).perform(click())
//        onView(withId(R.id.zemljisniTipLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(Zemljiste::class.java))).inAdapterView(withId(R.id.zemljisniTipLV)).atPosition(Zemljiste.PJESKOVITO.ordinal).perform(click())
//        onView(withId(R.id.profilOkusaLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(ProfilOkusaBiljke::class.java))).inAdapterView(withId(R.id.profilOkusaLV)).atPosition(ProfilOkusaBiljke.MENTA.ordinal).perform(click())
//        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
//        val recyclerView = activityScenarioRule.scenario.onActivity {
//            val recyclerView = it.findViewById<RecyclerView>(R.id.biljkeRV)
//            val itemCount = recyclerView.adapter!!.itemCount
//            MatcherAssert.assertThat(itemCount, OrderingComparison.comparesEqualTo(11))
//        }
//    }
//
//    @Test
//    fun citavaFormaBezMedicinskeKoristi() {
//        dodjiDoNovaBiljkaActivity()
//        onView(withId(R.id.nazivET)).perform(clearText(), typeText("Naziv"), closeSoftKeyboard())
//        onView(withId(R.id.porodicaET)).perform(scrollTo(), click())
//        onView(withId(R.id.porodicaET)).perform(clearText(), typeText("Porodica"), closeSoftKeyboard())
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo(), click())
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(clearText(), typeText("Upozorenje"), closeSoftKeyboard())
//        onView(withId(R.id.jeloET)).perform(scrollTo(), click())
//        onView(withId(R.id.jeloET)).perform(clearText(), typeText("Jelo"), closeSoftKeyboard())
//        onView(withId(R.id.dodajJeloBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(KlimatskiTip::class.java))).inAdapterView(withId(R.id.klimatskiTipLV)).atPosition(KlimatskiTip.SREDOZEMNA.ordinal).perform(click())
//        onView(withId(R.id.zemljisniTipLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(Zemljiste::class.java))).inAdapterView(withId(R.id.zemljisniTipLV)).atPosition(Zemljiste.PJESKOVITO.ordinal).perform(click())
//        onView(withId(R.id.profilOkusaLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(ProfilOkusaBiljke::class.java))).inAdapterView(withId(R.id.profilOkusaLV)).atPosition(ProfilOkusaBiljke.MENTA.ordinal).perform(click())
//        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.dodajBiljkuBtn)).check(matches(btnError("Mora biti odabrana bar jedna medicinska korist u listi!")))
//    }
//
//    @Test
//    fun citavaFormaBezKlimatskogTipa() {
//        dodjiDoNovaBiljkaActivity()
//        onView(withId(R.id.nazivET)).perform(clearText(), typeText("Naziv"), closeSoftKeyboard())
//        onView(withId(R.id.porodicaET)).perform(scrollTo(), click())
//        onView(withId(R.id.porodicaET)).perform(clearText(), typeText("Porodica"), closeSoftKeyboard())
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo(), click())
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(clearText(), typeText("Upozorenje"), closeSoftKeyboard())
//        onView(withId(R.id.jeloET)).perform(scrollTo(), click())
//        onView(withId(R.id.jeloET)).perform(clearText(), typeText("Jelo"), closeSoftKeyboard())
//        onView(withId(R.id.dodajJeloBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.medicinskaKoristLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(MedicinskaKorist::class.java))).inAdapterView(withId(R.id.medicinskaKoristLV)).atPosition(MedicinskaKorist.SMIRENJE.ordinal).perform(click())
//        onView(withId(R.id.zemljisniTipLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(Zemljiste::class.java))).inAdapterView(withId(R.id.zemljisniTipLV)).atPosition(Zemljiste.PJESKOVITO.ordinal).perform(click())
//        onView(withId(R.id.profilOkusaLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(ProfilOkusaBiljke::class.java))).inAdapterView(withId(R.id.profilOkusaLV)).atPosition(ProfilOkusaBiljke.MENTA.ordinal).perform(click())
//        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.dodajBiljkuBtn)).check(matches(btnError("Mora biti odabran bar jedan klimatski tip u listi!")))
//    }
//
//    @Test
//    fun citavaFormaBezZemljisnogTipa() {
//        dodjiDoNovaBiljkaActivity()
//        onView(withId(R.id.nazivET)).perform(clearText(), typeText("Naziv"), closeSoftKeyboard())
//        onView(withId(R.id.porodicaET)).perform(scrollTo(), click())
//        onView(withId(R.id.porodicaET)).perform(clearText(), typeText("Porodica"), closeSoftKeyboard())
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo(), click())
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(clearText(), typeText("Upozorenje"), closeSoftKeyboard())
//        onView(withId(R.id.jeloET)).perform(scrollTo(), click())
//        onView(withId(R.id.jeloET)).perform(clearText(), typeText("Jelo"), closeSoftKeyboard())
//        onView(withId(R.id.dodajJeloBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.medicinskaKoristLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(MedicinskaKorist::class.java))).inAdapterView(withId(R.id.medicinskaKoristLV)).atPosition(MedicinskaKorist.SMIRENJE.ordinal).perform(click())
//        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(KlimatskiTip::class.java))).inAdapterView(withId(R.id.klimatskiTipLV)).atPosition(KlimatskiTip.SREDOZEMNA.ordinal).perform(click())
//        onView(withId(R.id.profilOkusaLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(ProfilOkusaBiljke::class.java))).inAdapterView(withId(R.id.profilOkusaLV)).atPosition(ProfilOkusaBiljke.MENTA.ordinal).perform(click())
//        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.dodajBiljkuBtn)).check(matches(btnError("Mora biti odabran bar jedan zemljisni tip u listi!")))
//    }
//
//    @Test
//    fun citavaFormaBezProfilaOkusa() {
//        dodjiDoNovaBiljkaActivity()
//        onView(withId(R.id.nazivET)).perform(clearText(), typeText("Naziv"), closeSoftKeyboard())
//        onView(withId(R.id.porodicaET)).perform(scrollTo(), click())
//        onView(withId(R.id.porodicaET)).perform(
//            clearText(),
//            typeText("Porodica"),
//            closeSoftKeyboard()
//        )
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(scrollTo(), click())
//        onView(withId(R.id.medicinskoUpozorenjeET)).perform(
//            clearText(),
//            typeText("Upozorenje"),
//            closeSoftKeyboard()
//        )
//        onView(withId(R.id.jeloET)).perform(scrollTo(), click())
//        onView(withId(R.id.jeloET)).perform(clearText(), typeText("Jelo"), closeSoftKeyboard())
//        onView(withId(R.id.dodajJeloBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.medicinskaKoristLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(MedicinskaKorist::class.java))).inAdapterView(withId(R.id.medicinskaKoristLV)).atPosition(MedicinskaKorist.SMIRENJE.ordinal).perform(click())
//        onView(withId(R.id.klimatskiTipLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(KlimatskiTip::class.java))).inAdapterView(withId(R.id.klimatskiTipLV)).atPosition(KlimatskiTip.SREDOZEMNA.ordinal).perform(click())
//        onView(withId(R.id.zemljisniTipLV)).perform(scrollTo()).check(matches(isDisplayed()))
//        onData(`is`(instanceOf(Zemljiste::class.java))).inAdapterView(withId(R.id.zemljisniTipLV)).atPosition(Zemljiste.PJESKOVITO.ordinal).perform(click())
//        onView(withId(R.id.dodajBiljkuBtn)).perform(scrollTo(), click())
//        onView(withId(R.id.dodajBiljkuBtn)).check(matches(btnError("Mora biti odabran bar jedan profil okusa u listi!")))
//
//    }
//
//    fun btnError(expectedError: String): Matcher<in View>? {
//        return object : BoundedMatcher<View?, TextView>(TextView::class.java) {
//            override fun describeTo(description: Description) {
//                description.appendText("greska: $expectedError")
//            }
//
//            override fun matchesSafely(tv: TextView): Boolean {
//                return expectedError == tv.error.toString()
//            }
//        }
//    }
//
//    fun photoMatching(resourceId1: Int, resourceId2: Int): TypeSafeMatcher<View> {
//        return object : TypeSafeMatcher<View>() {
//            override fun describeTo(description: Description) {
//                description.appendText("$resourceId1 i $resourceId2")
//            }
//
//            override fun matchesSafely(view: View): Boolean {
//                if (view !is ImageView) {
//                    return false
//                }
//                val context = InstrumentationRegistry.getInstrumentation().targetContext
//                val drawable1 = ContextCompat.getDrawable(context, resourceId1) as? BitmapDrawable
//                val drawable2 = ContextCompat.getDrawable(context, resourceId2) as? BitmapDrawable
//                if (drawable1 == null || drawable2 == null) {
//                    return false
//                }
//
//                val bitmap1 = drawable1.bitmap
//                val bitmap2 = drawable2.bitmap
//
//                return !bitmap1.sameAs(bitmap2)
//            }
//        }
//    }
//
//    fun hasCheckedItem(): Matcher<View> = object : TypeSafeMatcher<View>() {
//        override fun describeTo(description: Description) {
//            description.appendText("barem jedno odabrano")
//        }
//
//        override fun matchesSafely(view: View): Boolean {
//            if (view !is ListView) {
//                return false
//            }
//            for (i in 0 until view.count) {
//                if (view.isItemChecked(i)) {
//                    return true
//                }
//            }
//            return false
//        }
//    }
//
//    fun hasExactlyOneCheckedItem(): TypeSafeMatcher<View> {
//        return object : TypeSafeMatcher<View>() {
//            override fun describeTo(description: Description) {
//                description.appendText("tacno jedno odabrano")
//            }
//
//            override fun matchesSafely(view: View): Boolean {
//                if (view !is ListView) {
//                    return false
//                }
//                var checkedItemCount = 0
//                for (i in 0 until view.count) {
//                    if (view.isItemChecked(i)) {
//                        checkedItemCount += 1
//                    }
//                }
//                return checkedItemCount == 1
//            }
//        }
//    }
//
//
//}
//
//
//
//
