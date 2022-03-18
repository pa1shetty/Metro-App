package com.example.nammametromvvm.utility.ui

import android.app.Activity
import android.view.View
import com.example.nammametromvvm.R
import com.example.nammametromvvm.data.repositaries.BottomSheetDialogueData
import com.example.nammametromvvm.data.repositaries.BottomSheetTicketConfirmationDialogueData
import com.example.nammametromvvm.databinding.BottomSheetDialogLayoutBinding
import com.example.nammametromvvm.databinding.BottomSheetTicketConfirmationBinding
import com.example.nammametromvvm.utility.interfaces.BottomSheetDialogueCallBackListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class BottomSheet(private val bottomSheetDialogueCallBackListener: BottomSheetDialogueCallBackListener) {

    private fun bottomSheet(
        activity: Activity,
        bottomSheetDialogueData: BottomSheetDialogueData,
        calledFrom: BottomSheetCalledFrom
    ) {
        val bottomSheetDialog = BottomSheetDialog(activity)
        val bottomSheetDialogueBinding =
            BottomSheetDialogLayoutBinding.inflate(activity.layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetDialogueBinding.root)
        bottomSheetDialog.setCancelable(bottomSheetDialogueData.setCancelable)
        bottomSheetDialog.setCanceledOnTouchOutside(bottomSheetDialogueData.setCanceledOnTouchOutside)
        bottomSheetDialog.behavior.state = bottomSheetDialogueData.state
        bottomSheetDialog.show()
        if (bottomSheetDialogueData.showHeader) {
            bottomSheetDialogueBinding.tvHeader.visibility = View.VISIBLE
            bottomSheetDialogueBinding.tvHeader.text = bottomSheetDialogueData.headerText
        } else {
            bottomSheetDialogueBinding.tvHeader.visibility = View.GONE
        }
        if (bottomSheetDialogueData.showInfo) {
            bottomSheetDialogueBinding.tvInfo.visibility = View.VISIBLE
            bottomSheetDialogueBinding.tvInfo.text = bottomSheetDialogueData.infoText
        } else {
            bottomSheetDialogueBinding.tvInfo.visibility = View.GONE
        }
        if (bottomSheetDialogueData.showNegativeButton) {
            bottomSheetDialogueBinding.negativeButton.visibility = View.VISIBLE
            bottomSheetDialogueBinding.negativeButton.text =
                bottomSheetDialogueData.negativeButtonText
        } else {
            bottomSheetDialogueBinding.negativeButton.visibility = View.GONE
        }
        if (bottomSheetDialogueData.showPositiveButton) {
            bottomSheetDialogueBinding.positiveButton.visibility = View.VISIBLE
            bottomSheetDialogueBinding.positiveButton.text =
                bottomSheetDialogueData.positiveButtonText
        } else {
            bottomSheetDialogueBinding.positiveButton.visibility = View.GONE
        }
        bottomSheetDialogueBinding.negativeButton.setOnClickListener {
            bottomSheetDialog.cancel()
            bottomSheetDialogueCallBackListener.onNegativeButtonClick(calledFrom)
        }
        bottomSheetDialogueBinding.positiveButton.setOnClickListener {
            bottomSheetDialog.cancel()
            bottomSheetDialogueCallBackListener.onPositiveButtonClick(calledFrom)
        }
    }

    fun bottomSheetLoginRequired(activity: Activity, calledFrom: BottomSheetCalledFrom) {
        bottomSheet(
            activity,
            BottomSheetDialogueData(
                true,
                activity.getString(R.string.login_required),
                true,
                activity.getString(R.string.login_required_details),
                true,
                activity.getString(R.string.log_in),
                true,
                activity.getString(R.string.not_now),
                setCancelable = false,
                setCanceledOnTouchOutside = false,
                state = BottomSheetBehavior.STATE_EXPANDED
            ),
            calledFrom
        )
    }

    enum class BottomSheetCalledFrom {
        HOME_PROFILE,
        HOME_TOP_UP,
        HOME_QR_TICKET
    }

    fun bottomSheetTicketConfirmation(
        activity: Activity,
        bottomSheetTicketConfirmationDialogueData: BottomSheetTicketConfirmationDialogueData,
        calledFrom: BottomSheetCalledFrom
    ) {
        val bottomSheetDialog = BottomSheetDialog(activity)
        val bottomSheetTicketConfirmationBinding =
            BottomSheetTicketConfirmationBinding.inflate(activity.layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetTicketConfirmationBinding.root)
        bottomSheetDialog.setCancelable(bottomSheetTicketConfirmationDialogueData.setCancelable)
        bottomSheetDialog.setCanceledOnTouchOutside(bottomSheetTicketConfirmationDialogueData.setCanceledOnTouchOutside)
        bottomSheetDialog.behavior.state = bottomSheetTicketConfirmationDialogueData.state
        bottomSheetDialog.show()
        if (bottomSheetTicketConfirmationDialogueData.showHeader) {
            bottomSheetTicketConfirmationBinding.tvHeader.visibility = View.VISIBLE
            bottomSheetTicketConfirmationBinding.tvHeader.text =
                bottomSheetTicketConfirmationDialogueData.headerText
        } else {
            bottomSheetTicketConfirmationBinding.tvHeader.visibility = View.GONE
        }

        if (bottomSheetTicketConfirmationDialogueData.showNegativeButton) {
            bottomSheetTicketConfirmationBinding.negativeButton.visibility = View.VISIBLE
            bottomSheetTicketConfirmationBinding.negativeButton.text =
                bottomSheetTicketConfirmationDialogueData.negativeButtonText
        } else {
            bottomSheetTicketConfirmationBinding.negativeButton.visibility = View.GONE
        }
        if (bottomSheetTicketConfirmationDialogueData.showPositiveButton) {
            bottomSheetTicketConfirmationBinding.positiveButton.visibility = View.VISIBLE
            bottomSheetTicketConfirmationBinding.positiveButton.text =
                bottomSheetTicketConfirmationDialogueData.positiveButtonText
        } else {
            bottomSheetTicketConfirmationBinding.positiveButton.visibility = View.GONE
        }
        bottomSheetTicketConfirmationBinding.negativeButton.setOnClickListener {
            bottomSheetDialog.cancel()
            bottomSheetDialogueCallBackListener.onNegativeButtonClick(calledFrom)
        }
        bottomSheetTicketConfirmationBinding.positiveButton.setOnClickListener {
            bottomSheetDialog.cancel()
            bottomSheetDialogueCallBackListener.onPositiveButtonClick(calledFrom)
        }

        bottomSheetTicketConfirmationBinding.tvHeader.text =
            bottomSheetTicketConfirmationDialogueData.headerText
        bottomSheetTicketConfirmationBinding.tvFrom.text =
            bottomSheetTicketConfirmationDialogueData.fromStation
        bottomSheetTicketConfirmationBinding.tvTo.text =
            bottomSheetTicketConfirmationDialogueData.toStation
        bottomSheetTicketConfirmationBinding.tvOn.text =
            bottomSheetTicketConfirmationDialogueData.travelDate
        bottomSheetTicketConfirmationBinding.tvPassengerCount.text =
            bottomSheetTicketConfirmationDialogueData.passengerCount
        bottomSheetTicketConfirmationBinding.tvFare.text =
            bottomSheetTicketConfirmationDialogueData.fare

    }
}