package com.gthr.gthrcollect.ui.homebottomnav.search

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.SearchFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class SearchFragment : BaseFragment<SearchViewModel, SearchFragmentBinding>() {

    override fun getViewBinding() = SearchFragmentBinding.inflate(layoutInflater)
    override val mViewModel: SearchViewModel by viewModels()

    private lateinit var mCctProduct : CustomCollectionTypeView
    private lateinit var mCctForSale : CustomCollectionTypeView
    private lateinit var mCctCollections : CustomCollectionTypeView

    private lateinit var mIvFilter : AppCompatImageView
    private lateinit var mTvTitle : AppCompatTextView

    override fun onBinding() {
        initViews()
        setUpOnClickListeners()
    }

    private fun setUpOnClickListeners() {
        mCctProduct.setOnClickListener {
            setSelectedCct(mCctProduct)
        }
        mCctForSale.setOnClickListener {
            setSelectedCct(mCctForSale)
        }
        mCctCollections.setOnClickListener {
            setSelectedCct(mCctCollections)
        }
    }

    private fun setSelectedCct(mCct : CustomCollectionTypeView) {
        when(mCct){
            mCctProduct -> {
                mCctProduct.setActive(true)
                mCctForSale.setActive(false)
                mCctCollections.setActive(false)
                mIvFilter.visible()
                this.mTvTitle.text = getString(R.string.text_most_fav)
            }
            mCctForSale -> {
                mCctProduct.setActive(false)
                mCctForSale.setActive(true)
                mCctCollections.setActive(false)
                mIvFilter.visible()
                this.mTvTitle.text = getString(R.string.text_most_fav)
            }
            mCctCollections -> {
                mCctProduct.setActive(false)
                mCctForSale.setActive(false)
                mCctCollections.setActive(true)
                mIvFilter.gone()
                this.mTvTitle.text = getString(R.string.text_most_followed)
            }
        }
    }

    private fun initViews() {
        mCctProduct = mViewBinding.cctProduct
        mCctForSale = mViewBinding.cctForSale
        mCctCollections = mViewBinding.cctCollections
        mIvFilter = mViewBinding.ivFilter
        mTvTitle = mViewBinding.tvSearchTitle

        setSelectedCct(mCctProduct)
    }

}