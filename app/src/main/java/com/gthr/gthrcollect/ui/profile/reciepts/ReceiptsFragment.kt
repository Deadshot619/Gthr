package com.gthr.gthrcollect.ui.profile.reciepts

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ReceiptsFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.profile.reciepts.adapter.ReceiptAdapter
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView

class ReceiptsFragment : BaseFragment<ReceiptsViewModel, ReceiptsFragmentBinding>() {

    override val mViewModel: ReceiptsViewModel by viewModels()
    override fun getViewBinding() = ReceiptsFragmentBinding.inflate(layoutInflater)

    private lateinit var mRvMain : RecyclerView
    private lateinit var mCctAll: CustomCollectionTypeView
    private lateinit var mCctPurchased: CustomCollectionTypeView
    private lateinit var mCctSold: CustomCollectionTypeView

    private lateinit var mAdapter : ReceiptAdapter

    override fun onBinding() {
        initViews()
        setUpOnClickListener()
        setUpRecyclerView()
    }

    private fun setUpOnClickListener() {
        mCctAll.setOnClickListener {
            mCctAll.setActive(true)
            mCctPurchased.setActive(false)
            mCctSold.setActive(false)
        }
        mCctPurchased.setOnClickListener {
            mCctAll.setActive(false)
            mCctPurchased.setActive(true)
            mCctSold.setActive(false)
        }
        mCctSold.setOnClickListener {
            mCctAll.setActive(false)
            mCctPurchased.setActive(false)
            mCctSold.setActive(true)
        }
    }

    private fun setUpRecyclerView() {
        mAdapter = ReceiptAdapter {}
        mRvMain.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }

    private fun initViews() {
        mRvMain = mViewBinding.rvMain
        mCctAll = mViewBinding.cctAll
        mCctPurchased = mViewBinding.cctPurchased
        mCctSold = mViewBinding.cctSold
    }

}