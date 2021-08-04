package com.gthr.gthrcollect.ui.profile.reciepts

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.databinding.ReceiptsFragmentBinding
import com.gthr.gthrcollect.model.domain.ReceiptDomainModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.profile.reciepts.adapter.ReceiptAdapter
import com.gthr.gthrcollect.ui.receiptdetail.ReceiptDetailActivity
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ReceiptsFragment : BaseFragment<ReceiptsViewModel, ReceiptsFragmentBinding>() {

    override val mViewModel: ReceiptsViewModel by viewModels()
    override fun getViewBinding() = ReceiptsFragmentBinding.inflate(layoutInflater)

    private var mainJob: Job? = null

    private lateinit var mRvMain : RecyclerView
    private lateinit var mCctAll: CustomCollectionTypeView
    private lateinit var mCctPurchased: CustomCollectionTypeView
    private lateinit var mCctSold: CustomCollectionTypeView

    //List of Collection filter views
    private lateinit var mCctvList: List<CustomCollectionTypeView>

    private lateinit var mAdapter : ReceiptAdapter

    override fun onBinding() {
        initViews()
        setUpOnClickListener()
        setUpRecyclerView()
    }

    private fun setUpOnClickListener() {
        mCctvList.forEach { view ->
            view.setOnClickListener {
                view.selectView()
            }
        }
    }

    private fun setUpRecyclerView() {
        mAdapter = ReceiptAdapter (object : ReceiptAdapter.ReceiptListener{
            override fun onClick(receiptDomainModel: ReceiptDomainModel?) {
                startActivity(ReceiptDetailActivity.getInstance(this@ReceiptsFragment.requireContext()))
            }
        })

        mRvMain.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }
    }

    private fun initViews() {
        mViewBinding.run {
            mRvMain = rvMain
            mCctAll = cctAll
            mCctPurchased = cctPurchased
            mCctSold = cctSold
            mCctvList = listOf(mCctAll, mCctPurchased, mCctSold)
        }
    }

    //Method to select Single Collection Filter
    private fun CustomCollectionTypeView.selectView() {
        if (this.mIsActive) return

        mainJob?.cancel()
        mainJob = MainScope().launch {
            mCctvList.forEach {
                it.setActive(it == this@selectView)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainJob?.cancel()
    }
}