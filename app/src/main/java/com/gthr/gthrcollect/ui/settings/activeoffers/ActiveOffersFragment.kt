package com.gthr.gthrcollect.ui.settings.activeoffers

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ActiveOffersFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomSearchView

class ActiveOffersFragment : BaseFragment<ActiveOffersViewModel, ActiveOffersFragmentBinding>() {
    override fun getViewBinding() = ActiveOffersFragmentBinding.inflate(layoutInflater)
    override val mViewModel: ActiveOffersViewModel by viewModels()

    private lateinit var mAdapter: ActiveOffersAdapter
    private lateinit var mRvMain: RecyclerView
    private lateinit var mSearchView: CustomSearchView


    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpRecyclerView()
    }

    private fun initViews() {
        mViewBinding.run {
            mRvMain = rvMain
            mSearchView = csvSearch
        }
    }

    private fun setUpRecyclerView() {
        mAdapter = ActiveOffersAdapter {}
        mRvMain.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            mRvMain.adapter = mAdapter
        }
    }
    private fun setUpClickListeners() {

    }

}