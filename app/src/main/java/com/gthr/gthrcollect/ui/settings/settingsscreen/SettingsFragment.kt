package com.gthr.gthrcollect.ui.settings.settingsscreen

import androidx.fragment.app.activityViewModels
import com.gthr.gthrcollect.databinding.SettingsFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.settings.SettingsViewModel

class SettingsFragment : BaseFragment<SettingsViewModel, SettingsFragmentBinding>() {
    override fun getViewBinding() = SettingsFragmentBinding.inflate(layoutInflater)
    override val mViewModel: SettingsViewModel by activityViewModels()

    override fun onBinding() {}

}