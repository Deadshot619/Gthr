package com.gthr.gthrcollect.ui.profile.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gthr.gthrcollect.databinding.BottomSheetProfilePictureBinding



class ProfileImageBottomSheet(private val clickAction : ClickAction) : BottomSheetDialogFragment() {

    private lateinit var dialogBinding : BottomSheetProfilePictureBinding
    private lateinit var mTvTackPicture : AppCompatTextView
    private lateinit var mTvChooseFromLibrary : AppCompatTextView

    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        dialogBinding = BottomSheetProfilePictureBinding.inflate(LayoutInflater.from(requireContext()))
        initViews()
        setUpOnClickListeners()
        return dialogBinding.root
    }

    private fun setUpOnClickListeners() {
        mTvTackPicture.setOnClickListener {
            clickAction.takePhoto()
            dismiss()
        }
        mTvChooseFromLibrary.setOnClickListener {
            clickAction.chooseFromLibrary()
            dismiss()
        }
    }

    private fun initViews() {
        mTvTackPicture = dialogBinding.tvTakePhoto
        mTvChooseFromLibrary = dialogBinding.tvChooseFromLibrary
    }

    interface ClickAction{
        fun takePhoto()
        fun chooseFromLibrary()
    }
}