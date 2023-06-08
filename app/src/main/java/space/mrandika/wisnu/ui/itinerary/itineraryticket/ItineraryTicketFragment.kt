package space.mrandika.wisnu.ui.itinerary.itineraryticket

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import space.mrandika.wisnu.R
import space.mrandika.wisnu.databinding.FragmentItineraryTicketBinding
import space.mrandika.wisnu.model.poi.POI
import space.mrandika.wisnu.ui.itinerary.ItineraryViewModel
import space.mrandika.wisnu.ui.itinerary.itineraryguides.ItineraryGuidesFragment

class ItineraryTicketFragment : Fragment() {
    private var _binding : FragmentItineraryTicketBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel : ItineraryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItineraryTicketBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            activityViewModel.state.value.apply{
                PoiData?.let { setTicket(it) }
                setViewActivity()
            }
        }

    }

    private fun setTicket(data: List<POI>){
        val ticket : MutableList<POI> = mutableListOf()
        for(POI in data){
            if(POI.tickets != null ){
                if (POI.tickets.isTicketingEnabled){
                    Log.d("Enable ticket ", POI.tickets.isTicketingEnabled.toString())
                    ticket.add(POI)
                }
            }
        }
        binding.rvTicket.apply {
            adapter = ItineraryTicketAdapter(ticket,activityViewModel)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
    private fun setViewActivity() {
        val tvTitle : TextView? = activity?.findViewById(R.id.tv_title)
        val tvDescription : TextView? = activity?.findViewById(R.id.tv_description)
        val btnMain : MaterialButton? = activity?.findViewById(R.id.btn_main)
        val tvButton : TextView? = activity?.findViewById(R.id.tv_text_button)
        tvButton?.apply {
            activityViewModel.state.value.includeTicket = false
            visibility = View.VISIBLE
            text = context.getString(R.string.lewati)
            activityViewModel.setTotalTicket(0)
            setOnClickListener {
                activityViewModel.setAdult(1)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, ItineraryGuidesFragment())
                    .commit()
            }
        }
        btnMain?.apply {
            text = context.getString(R.string.lanjut)
            setIconResource(R.drawable.baseline_arrow_forward_24)
            setOnClickListener {
                activityViewModel.state.value.includeTicket = true
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, ItineraryGuidesFragment())
                    .commit()
            }
        }
        tvDescription?.apply {
            visibility = View.VISIBLE
            text = "Mau sekalian beli di Wisnu?"
        }

        tvTitle?.apply {
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = "Perlu tiket\n" + "masuk, nih."
        }
    }
}