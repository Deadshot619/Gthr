package com.gthr.gthrcollect.ui.profile.reciepts

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.ReceiptRepository
import com.gthr.gthrcollect.databinding.ReceiptsFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.ReceiptDisplayModel
import com.gthr.gthrcollect.model.domain.ReceiptDomainModel
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.profile.reciepts.adapter.ReceiptAdapter
import com.gthr.gthrcollect.ui.receiptdetail.ReceiptDetailActivity
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import com.gthr.gthrcollect.utils.customviews.CustomDeliveryButton
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.enums.ReceiptType
import com.gthr.gthrcollect.utils.extensions.getUserUID
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.extensions.toDate
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ReceiptsFragment : BaseFragment<ReceiptsViewModel, ReceiptsFragmentBinding>() {

    override val mViewModel: ReceiptsViewModel by viewModels{
        ReceiptViewModelFactory(
            ReceiptRepository()
        )
    }
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
        setUpObservers()
        mViewModel.fetchSaleReceipts(GthrCollect.prefs?.getUserUID()!!)
    }

    private fun setUpObservers() {

        mViewModel.mDisplayReceiptList.observe(viewLifecycleOwner) { it ->
            it.contentIfNotHandled?.let {
                val list = it.sortedByDescending { return@sortedByDescending it.receiptDomainModel?.date?.toDate()?.time }
                mAdapter.submitList(list.map { it.copy() })
                GthrLogger.i("jdbcjsd","final list $list")
            }
        }

        mViewModel.mSaleReceipt.observe(viewLifecycleOwner) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        mViewModel.setSaleReceiptList(it.data)
                        mViewModel.fetchBuyReceipts(GthrCollect.prefs?.getUserUID()!!)
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

        mViewModel.mBuyReceipt.observe(viewLifecycleOwner) { it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Success -> {
                        showProgressBar(false)
                        mViewModel.setBuyReceiptList(it.data)
                        val list = mutableListOf<ReceiptDisplayModel>()
                        list.addAll(mViewModel.mBuyReceiptList)
                        list.addAll(mViewModel.mSaleReceiptList)
                        mViewModel.setDisplayReceiptList(list)
                    }
                    is State.Failed -> {
                        showProgressBar(false)
                        showToast(it.message)
                    }
                }
            }
        }

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
            override fun onClick(receiptDomainModel: ReceiptDisplayModel?, pos: Int) {
                if (pos % 3 == 0)
                    startActivity(
                        ReceiptDetailActivity.getInstance(
                            this@ReceiptsFragment.requireContext(),
                            ReceiptType.PURCHASED,
                            ReceiptDomainModel(productType = ProductType.FUNKO)
                        )
                    )
                else if (pos % 3 == 1)
                    startActivity(
                        ReceiptDetailActivity.getInstance(
                            requireContext(),
                            ReceiptType.ASK_PLACED,
                            ReceiptDomainModel(
                                totalAskPrice = 500.0,
                                objectID = "pokemon000020662",
                                productType = ProductType.POKEMON,
                                lang = 0,
                                condition = "CGC 9",
                                edition = "Holo Unlimited",
                                refKey = "-MiTOfdj0XCDttwhYf-Q",
                                imageUrl = "https://firebasestorage.googleapis.com:443/v0/b/dlc-db-staging.appspot.com/o/general%2FProduct%20Images%2FHonedge.SWSH05.105.37622.thumb.png?alt=media&token=ccacfcbd-9c1f-4f98-ad61-43c1ac7be88a"
                            ),
                            CustomDeliveryButton.OrderStatus.ASK_PLACED
                        )
                    )
                else
                    startActivity(
                        ReceiptDetailActivity.getInstance(
                            this@ReceiptsFragment.requireContext(),
                            ReceiptType.SOLD,
                            ReceiptDomainModel(productType = ProductType.POKEMON)
                        )
                    )
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
            initProgressBar(layoutProgress)
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
            when {
                mCctAll.mIsActive -> {
                    val list = mutableListOf<ReceiptDisplayModel>()
                    list.addAll(mViewModel.mBuyReceiptList)
                    list.addAll(mViewModel.mSaleReceiptList)
                    mViewModel.setDisplayReceiptList(list)
                }
                mCctSold.mIsActive -> {
                    val list = mutableListOf<ReceiptDisplayModel>()
                    list.addAll(mViewModel.mSaleReceiptList)
                    mViewModel.setDisplayReceiptList(list)
                }
                mCctPurchased.mIsActive -> {
                    val list = mutableListOf<ReceiptDisplayModel>()
                    list.addAll(mViewModel.mBuyReceiptList)
                    mViewModel.setDisplayReceiptList(list)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainJob?.cancel()
    }
}