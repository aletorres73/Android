package com.nan_app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nan_app.R
import com.nan_app.databinding.ItemCalendarBinding
import com.nan_app.entities.Calendar
import com.nan_app.entities.Clients

class CalendarAdapter(
    // creo que solamente necesito como parámetro la lista de clientes, los otros valores son fijos
    private var listClient: List<Clients>,
    private var dayOfWeekStr: String,
    private var onItemSelected: (Int) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    private var listTime = Calendar().timeList
    private var listSpaces = Calendar().spacesList

    class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var binding = ItemCalendarBinding.bind(view)
        private val textSpaces = listOf(
            binding.textBed1,
            binding.textBed2,
            binding.textBed3,
            binding.textBed4,
            binding.textBed5
        )

        fun render(
            calendarList: String,
            listClient: List<Clients>,
            dayOfWeekStr: String,
            spaces: Map<String, String>,
            onItemSelected: (Int) -> Unit
        ) {
            binding.itemTime.text = calendarList

            initSpaces(spaces)
            getDayFromClient(listClient, dayOfWeekStr, calendarList)

            itemView.setOnClickListener { onItemSelected(layoutPosition) }
        }

        private fun initSpaces(spaces: Map<String, String>) {
            textSpaces[0].text = spaces["Cama1"]
            textSpaces[2].text = spaces["Cama2"]
            textSpaces[3].text = spaces["Cama3"]
            textSpaces[4].text = spaces["Cama4"]
            textSpaces[0].text = spaces["Cama5"]
        }

        @SuppressLint("SetTextI18n")
        private fun getDayFromClient(
            listClient: List<Clients>,
            dayOfWeekStr: String,
            itemTime: String
        ) {
            var currentSpaceIndex = 0

            for (client in listClient) {
                val fullNameClient = "${client.Name} ${client.LastName}"
                val clientDay = client.dates[dayOfWeekStr]

                if (clientDay != null && clientDay == itemTime) {
                    while (currentSpaceIndex < textSpaces.size &&
                        textSpaces[currentSpaceIndex].text.isNotEmpty() &&
                        textSpaces[currentSpaceIndex].text == fullNameClient) {
                        currentSpaceIndex++
                    }
                    if (currentSpaceIndex < textSpaces.size) {
                        textSpaces[currentSpaceIndex].text = fullNameClient
                        currentSpaceIndex++
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.render(listTime[position], listClient, dayOfWeekStr, listSpaces, onItemSelected)
    }

    override fun getItemCount() = listTime.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList() {
        notifyDataSetChanged()
    }
}