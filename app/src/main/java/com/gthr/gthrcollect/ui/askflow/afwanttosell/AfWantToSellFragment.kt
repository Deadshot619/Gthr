package com.gthr.gthrcollect.ui.askflow.afwanttosell

import android.app.Activity
import android.content.Intent
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.switchmaterial.SwitchMaterial
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.AfWantToSellFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModelFactory
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.editaccountinfo.EditAccountInfoActivity
import com.gthr.gthrcollect.utils.customviews.CustomSecondaryButton
import com.gthr.gthrcollect.utils.enums.EditAccountInfoFlow
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.helper.isUserVerified
import kotlinx.coroutines.launch

class AfWantToSellFragment : BaseFragment<AskFlowViewModel, AfWantToSellFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by activityViewModels{
        AskFlowViewModelFactory(AskFlowRepository())
    }
    override fun getViewBinding() =  AfWantToSellFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvBack: ImageView
    private lateinit var mBtnNext: CustomSecondaryButton
    private lateinit var mSwitchWantToSell: SwitchMaterial

    override fun onBinding() {
        initViews()
        setUpClickListeners()
    }

    private fun initViews() {
        mViewBinding.run {
            mIvBack = ivBack
            mBtnNext = btnNext
            mSwitchWantToSell = scWantToSell

            //If this fragment is start destination, then hide back button
            if (findNavController().previousBackStackEntry == null)
                mIvBack.gone()
        }
    }

    private fun setUpClickListeners(){
        mViewBinding.run {
            mBtnNext.setOnClickListener {
                if (mViewModel.isSell.value == true) {
                    lifecycleScope.launch {
                        (activity as AskFlowActivity).showProgressBar()
                        activity?.isUserVerified(runEverytime = {
                            (activity as AskFlowActivity).showProgressBar(false)
                        }, verified = {
                            findNavController().navigate(AfWantToSellFragmentDirections.actionAfWantToSellFragmentToAfAddPicFragment())
                        }, notVerified = {
                            startActivityForResult(
                                EditAccountInfoActivity.getInstance(
                                    requireContext(),
                                    EditAccountInfoFlow.GOV_ID
                                ), REQUEST_CODE_ID_VERIFICATION_SELL
                            )
                        })
                    }
                } else
                    findNavController().navigate(AfWantToSellFragmentDirections.actionAfWantToSellFragmentToAfAddPicFragment())

/*                if (mViewModel.isSell.value == true && GthrCollect.prefs?.isUserGovIdVerified() != true)
                    startActivityForResult(
                        EditAccountInfoActivity.getInstance(
                            requireContext(),
                            EditAccountInfoFlow.GOV_ID
                        ), REQUEST_CODE_ID_VERIFICATION_SELL
                    )
                else
                    findNavController().navigate(AfWantToSellFragmentDirections.actionAfWantToSellFragmentToAfAddPicFragment())*/
            }

            mIvBack.setOnClickListener {
                findNavController().navigateUp()
            }

            mSwitchWantToSell.setOnClickListener {
                mViewModel.setSell(mSwitchWantToSell.isChecked)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_ID_VERIFICATION_SELL)
                showToast(getString(R.string.text_id_under_review))
        }
    }

    companion object {
        private const val REQUEST_CODE_ID_VERIFICATION_SELL = 69
    }
}