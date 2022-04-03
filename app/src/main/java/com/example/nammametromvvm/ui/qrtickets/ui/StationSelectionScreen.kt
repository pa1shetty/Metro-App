package com.example.nammametromvvm.ui.qrtickets.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nammametromvvm.R
import com.example.nammametromvvm.data.repositaries.BottomSheetTicketConfirmationDialogueData
import com.example.nammametromvvm.databinding.FragmentStationSelectionScreenBinding
import com.example.nammametromvvm.ui.qrtickets.adapter.StationListAdapter
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrPurchaseTicketsViewModel
import com.example.nammametromvvm.ui.qrtickets.viewmodel.QrPurchaseTicketsViewModelFactory
import com.example.nammametromvvm.utility.GenericMethods
import com.example.nammametromvvm.utility.interfaces.BottomSheetDialogueCallBackListener
import com.example.nammametromvvm.utility.ui.BottomSheet
import com.example.nammametromvvm.utility.ui.CustomButton
import com.example.nammametromvvm.utility.ui.GeneralUi.fadingAnimation
import com.google.android.material.datepicker.*
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class StationSelectionScreen : Fragment(), BottomSheetDialogueCallBackListener {

    lateinit var viewModel: QrPurchaseTicketsViewModel
    lateinit var binding: FragmentStationSelectionScreenBinding
    private lateinit var customButton: CustomButton

    @Inject
    lateinit var genericMethods: GenericMethods
    private var inputForValue = InputFor.FROM_STATION
    private var constraintsBuilderRange = CalendarConstraints.Builder()
    private val bottomSheet: BottomSheet = BottomSheet(this)


    @Inject
    lateinit var factory: QrPurchaseTicketsViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory)[QrPurchaseTicketsViewModel::class.java]

    }

    private fun setUpListeners() {
        binding.etFromStation.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                inputForValue = InputFor.FROM_STATION
                lifecycleScope.launch { viewModel.clearStationSelection() }
            }
        }
        binding.etToStation.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                inputForValue = InputFor.TO_STATION
                lifecycleScope.launch {
                    viewModel.clearToStationSelection()
                }
            }
        }

        binding.etFromStation.doOnTextChanged { text, _, _, _ ->
            searchForStation(
                text?.trim().toString()
            )
        }
        binding.etToStation.doOnTextChanged { text, _, _, _ ->
            searchForStation(
                text?.trim().toString()
            )
        }


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchStationList()
        setUpRV()
        setProceedButton()
        setUpPassengerCountUI()
        setUpDatePicker()
        setUpObserver()
        setUpListeners()
        setUpClick()

    }

    private fun setUpPassengerCountUI() {
        binding.sPassengerCount.valueFrom = viewModel.getMinPassengerCount().toFloat()
        binding.sPassengerCount.value = viewModel.getMinPassengerCount().toFloat()
        binding.sPassengerCount.valueTo = viewModel.getMaxPassengerCount().toFloat()
    }

    private fun setUpClick() {
        binding.ivBack.setOnClickListener { requireActivity().onBackPressed() }
        binding.etOn.setOnClickListener {
            showDateSelectionDialogue()
        }
        binding.sPassengerCount.addOnChangeListener { _, value, _ ->
            Log.d("test15", "setUpClick: $value")
            viewModel.setPassengerCount(value.toInt())
        }
        binding.cvProceed.setOnClickListener {
            customButton.startLoading()
            getFare()
            /* val bottomSheetTicketConfirmationDialogueData =
                 BottomSheetTicketConfirmationDialogueData(
                     headerText = "test",
                     infoText = "test",
                     positiveButtonText = "",
                     negativeButtonText = ""
                 )
             bottomSheet.bottomSheetTicketConfirmation(
                 requireActivity(), bottomSheetTicketConfirmationDialogueData,
                 BottomSheet.BottomSheetCalledFrom.HOME_QR_TICKET
             )*/

        }
    }

    private fun setUpDatePicker() {
        val cal: Calendar = Calendar.getInstance()
        cal.timeInMillis = MaterialDatePicker.todayInUtcMilliseconds()
        cal.add(Calendar.DAY_OF_MONTH, 5)
        val dateValidatorMin: DateValidator = DateValidatorPointForward.now()
        val dateValidatorMax: DateValidator =
            DateValidatorPointBackward.before(cal.timeInMillis)
        val listValidators = ArrayList<DateValidator>()
        listValidators.add(dateValidatorMin)
        listValidators.add(dateValidatorMax)
        val validators = CompositeDateValidator.allOf(listValidators)
        constraintsBuilderRange.setValidator(validators)
    }

    private fun showDateSelectionDialogue() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(constraintsBuilderRange.build())
                .setSelection(viewModel.getCurrentTicketDate().value)
                .setTitleText(getString(R.string.travel_date))
                .build()
        datePicker.show(childFragmentManager, "")
        datePicker.addOnPositiveButtonClickListener {
            Log.d(
                "test89",
                "showDateSelectionDialogue:2 " + datePicker.selection
            )
            viewModel.setCurrentTicketDate(datePicker.selection!!)
        }

    }

    private lateinit var stationListAdapter: StationListAdapter

    private fun setUpRV() {
        binding.rvRoute.layoutManager = LinearLayoutManager(requireContext())
        stationListAdapter =
            StationListAdapter(requireContext(), viewModel.getDateMethods()) {
                lifecycleScope.launch {
                    viewModel.setValue(it.englishName, inputForValue)
                }
            }
        binding.rvRoute.adapter = stationListAdapter
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            viewModel.fareFetchResponse.collect {
                Log.d("test5", "setUpObserver: $it")
                if (it == 1) {
                    customButton.stopLoading()
                    val bottomSheetTicketConfirmationDialogueData =
                        BottomSheetTicketConfirmationDialogueData(
                            headerText = getString(R.string.confirm_ticket),
                            positiveButtonText = getString(R.string.confirm),
                            negativeButtonText = getString(R.string.cancel),
                            fromStation = viewModel.getFromStation(),
                            toStation = viewModel.getToStation(),
                            passengerCount = viewModel.getPassengerCount().value.toString(),
                            travelDate = viewModel.getCurrentTicketDateFormatted(),
                            fare = viewModel.getTotalFare().toString()
                        )
                    bottomSheet.bottomSheetTicketConfirmation(
                        requireActivity(), bottomSheetTicketConfirmationDialogueData,
                        BottomSheet.BottomSheetCalledFrom.HOME_QR_TICKET
                    )
                }
            }
        }
        viewModel.getCurrentTicketDate().observe(viewLifecycleOwner) { travelDate ->
            binding.etOn.setText(viewModel.getCurrentTicketDateFormatted(travelDate))
        }
        viewModel.getPassengerCount().observe(viewLifecycleOwner) { passengerCount ->
            binding.tvPassengerCount.text = passengerCount.toString()
        }
        viewModel.getTicketDetails().observe(viewLifecycleOwner) {
            binding.etFromStation.setText(it.fromStation)
            binding.etToStation.setText(it.toStation)
            if (it.fromStation.isEmpty() && it.toStation.isEmpty()) {
                binding.tiFromStation.visibility = VISIBLE
                binding.tiToStation.visibility = GONE
                binding.etFromStation.requestFocusFromTouch()
                genericMethods.showKeyPad(requireActivity(), binding.etFromStation)
                customButton.disable()
                showToStationUI()
            } else
                if (it.fromStation.isNotEmpty() && it.toStation.isNotEmpty()) {
                    binding.tiFromStation.visibility = VISIBLE
                    binding.tiToStation.visibility = VISIBLE
                    genericMethods.hideKeypad(requireActivity())
                    binding.etToStation.clearFocus()
                    binding.etFromStation.clearFocus()
                    binding.rvRoute.visibility = INVISIBLE
                    binding.clAvailableStations.visibility = GONE
                    binding.clPurchaseTickets.visibility = VISIBLE
                    customButton.enable()
                } else
                    if (it.fromStation.isNotEmpty() && it.toStation.isEmpty()) {
                        binding.tiFromStation.visibility = VISIBLE
                        if (binding.tiToStation.visibility == GONE) {
                            fadingAnimation(binding.tiToStation, R.anim.slide_up, requireContext())
                        }
                        binding.tiToStation.visibility = VISIBLE
                        binding.tiToStation.requestFocusFromTouch()
                        genericMethods.showKeyPad(requireActivity(), binding.etToStation)
                        showToStationUI()
                        customButton.disable()
                    }
        }
    }


    private fun showToStationUI() {
        binding.rvRoute.visibility = VISIBLE
        binding.clAvailableStations.visibility = VISIBLE
        binding.clPurchaseTickets.visibility = GONE
    }

    private fun fetchStationList() {
        Log.d("test15", "fetchStationList: ")
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.fetchStationList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStationSelectionScreenBinding.inflate(inflater)
        return binding.root
    }

    private fun searchForStation(searchQuery: String = "") {
        lifecycleScope.launchWhenCreated {
            viewModel.searchStation("%$searchQuery%").collectLatest { stationList ->
                stationListAdapter.submitList(stationList)
            }
        }
    }

    private fun navigateTo(navDirections: NavDirections) {
        findNavController().navigate(
            navDirections
        )
    }

    override fun onNegativeButtonClick(requestedFrom: BottomSheet.BottomSheetCalledFrom) {
        // TODO("Not yet implemented")
    }

    override fun onPositiveButtonClick(requestedFrom: BottomSheet.BottomSheetCalledFrom) {
        /*val payU = PayU("50", "sd", "9741028810", "test", "Pavan", "e", "", "")
        payU.buildPayment()*/
        // payU.callPayment(requireActivity(),R.style.AppTheme_Green)
        customButton.startLoading()
    }

    private fun setProceedButton() {
        customButton = CustomButton(
            binding.cvProceed,
            binding.pbLogin,
            requireActivity(),
            tvProceed = binding.tvProceed,
        )
        customButton.disable()
    }

    private fun getFare() {
        viewModel.getFare()
    }
}

enum class InputFor {
    FROM_STATION,
    TO_STATION
}

