package com.example.jetpack_api_call_demo.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.jetpack_api_call_demo.views.PersonSummary
import javax.inject.Inject

class AddPeopleViewModel : ViewModel() {
    var peopleList by mutableStateOf(
        listOf(
            PersonSummary("ic_user_place_holder", "James Gunn", "Owes you \$500.00"),
            PersonSummary("ic_user_place_holder", "Daniel Hunt", "Owes you -\$500.00"),
            PersonSummary("ic_user_place_holder", "Mark Rumario", "Owes you \$500.00")
        )
    )
        private set

    fun addPerson(name: String, amount: String) {
        val displayAmount = if (amount.startsWith("-")) "Owes you $amount" else "Owes you \$$amount"
        peopleList = peopleList + PersonSummary("ic_user_place_holder", name, displayAmount)
    }

}