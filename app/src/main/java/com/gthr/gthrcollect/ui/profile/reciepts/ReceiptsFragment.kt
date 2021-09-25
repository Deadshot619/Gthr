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
import com.gthr.gthrcollect.utils.customviews.CustomDeliveryButton
import com.gthr.gthrcollect.utils.enums.ProductType
import com.gthr.gthrcollect.utils.enums.ReceiptType
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
            override fun onClick(receiptDomainModel: ReceiptDomainModel?, pos: Int) {
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