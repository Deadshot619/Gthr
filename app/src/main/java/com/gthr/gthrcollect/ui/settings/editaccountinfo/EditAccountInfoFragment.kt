package com.gthr.gthrcollect.ui.settings.editaccountinfo

import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.EditAccountInfoFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment

class EditAccountInfoFragment :
    BaseFragment<EditAccountInfoViewModel, EditAccountInfoFragmentBinding>() {
    override fun getViewBinding() = EditAccountInfoFragmentBinding.inflate(layoutInflater)
    override val mViewModel: EditAccountInfoViewModel by viewModels()

    override fun onBinding() {}
}