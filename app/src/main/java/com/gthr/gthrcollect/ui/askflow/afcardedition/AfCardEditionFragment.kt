package com.gthr.gthrcollect.ui.askflow.afcardedition

import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.AfCardEditionFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.ConfigurationAdapter
import com.gthr.gthrcollect.ui.base.BaseFragment

class AfCardEditionFragment : BaseFragment<AskFlowViewModel, AfCardEditionFragmentBinding>() {
    override val mViewModel: AskFlowViewModel by viewModels()

    override fun getViewBinding() = AfCardEditionFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvBack: ImageView
    private lateinit var mRvMain : RecyclerView
    private lateinit var mAdapter: ConfigurationAdapter

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpEdition()
    }

    private fun setUpEdition() {
        mAdapter = ConfigurationAdapter{
            goToCardCondition()
        }
        mRvMain.apply {
            layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false)
            adapter = mAdapter
        }
        mAdapter.submitList(getEdition())
    }

    private fun getEdition(): List<String> {
        val list = arrayListOf<String>()
        list.add(getString(R.string.foil))
        list.add(getString(R.string.first_edition))
        list.add(getString(R.string.rev_holo))
        list.add(getString(R.string.asdasd))
        list.add(getString(R.string.foil))
        list.add(getString(R.string.first_edition))
        list.add(getString(R.string.rev_holo))
        list.add(getString(R.string.asdasd))
        list.add(getString(R.string.foil))
        list.add(getString(R.string.first_edition))
        list.add(getString(R.string.rev_holo))
        list.add(getString(R.string.asdasd))
        return list
    }

    private fun initViews() {
        mViewBinding.run {
            mIvBack = ivBack
            mRvMain = rvMain
        }
    }


    private fun setUpClickListeners() {
        mViewBinding.run {
            mIvBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun goToCardCondition() {
        findNavController().navigate(AfCardEditionFragmentDirections.actionAfEditionFragmentToAfConfigureCardFragment())
    }
}