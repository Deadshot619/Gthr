package com.gthr.gthrcollect.ui.homebottomnav.search

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.SearchFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.homebottomnav.search.adapter.SearchCollectionAdapter
import com.gthr.gthrcollect.utils.GridSpacingItemDecoration
import com.gthr.gthrcollect.utils.customviews.CustomCollectionTypeView
import com.gthr.gthrcollect.utils.customviews.CustomFilterCategoryView
import com.gthr.gthrcollect.utils.customviews.CustomFilterSubCategoryView
import com.gthr.gthrcollect.utils.extensions.animateVisibility
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible

class SearchFragment : BaseFragment<SearchViewModel, SearchFragmentBinding>() {

    override fun getViewBinding() = SearchFragmentBinding.inflate(layoutInflater)
    override val mViewModel: SearchViewModel by viewModels()

    private lateinit var mDrawer: DrawerLayout
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var mClMain: ConstraintLayout

    private lateinit var mCctProduct: CustomCollectionTypeView
    private lateinit var mCctForSale: CustomCollectionTypeView
    private lateinit var mCctCollections: CustomCollectionTypeView

    private lateinit var mCfcvAskLowest: CustomFilterCategoryView
    private lateinit var mCfcvAskHighest: CustomFilterCategoryView
    private lateinit var mCfcvMostFavourite: CustomFilterCategoryView

    private lateinit var mCfcvCards: CustomFilterCategoryView
    private lateinit var mCfcvToys: CustomFilterCategoryView
    private lateinit var mCfcvSealed: CustomFilterCategoryView

    private lateinit var mCfscvCardsPokemon: CustomFilterSubCategoryView
    private lateinit var mCfscvCardsYuGiOh: CustomFilterSubCategoryView
    private lateinit var mCfscvCardsMagic: CustomFilterSubCategoryView

    private lateinit var mCfscvSealedPokemon: CustomFilterSubCategoryView
    private lateinit var mCfscvSealedYuGiOh: CustomFilterSubCategoryView
    private lateinit var mCfscvSealedMagic: CustomFilterSubCategoryView

    private lateinit var mLayoutCards: LinearLayoutCompat
    private lateinit var mLayoutSealed: LinearLayoutCompat

    private lateinit var mIvFilter: AppCompatImageView
    private lateinit var mTvTitle: AppCompatTextView
    private lateinit var mRvMain: RecyclerView

    private var mToggleCards: Boolean = true
    private var mToggleSealed: Boolean = true

    private lateinit var mSortCategories: List<CustomFilterCategoryView>
    private lateinit var mCategories: List<CustomFilterCategoryView>
    private lateinit var mCardSubCategories: List<CustomFilterSubCategoryView>
    private lateinit var mSealedSubCategories: List<CustomFilterSubCategoryView>

    private lateinit var mAdapterSC : SearchCollectionAdapter

    override fun onBinding() {
        initViews()
        setUpOnClickListeners()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {

        mAdapterSC = SearchCollectionAdapter{}
        mRvMain.apply {
            layoutManager = GridLayoutManager(requireContext(),spanCount)
        }
    }

    private fun setUpOnClickListeners() {
        mCctProduct.setOnClickListener {
            setSelectedCct(mCctProduct)
            mRvMain.adapter = null
        }
        mCctForSale.setOnClickListener {
            setSelectedCct(mCctForSale)
            mRvMain.adapter = null
        }
        mCctCollections.setOnClickListener {
            setSelectedCct(mCctCollections)
            mDrawer.closeDrawer(GravityCompat.END)
            mRvMain.adapter = mAdapterSC
        }

        mIvFilter.setOnClickListener {
            if (mDrawer.isDrawerVisible(GravityCompat.END)) {
                mDrawer.closeDrawer(GravityCompat.END)
            } else {
                mDrawer.openDrawer(GravityCompat.END)
            }
        }

        mCfcvAskLowest.setOnClickListener {
            selectSortingCategory(mCfcvAskLowest)
        }

        mCfcvAskHighest.setOnClickListener {
            selectSortingCategory(mCfcvAskHighest)
        }

        mCfcvMostFavourite.setOnClickListener {
            selectSortingCategory(mCfcvMostFavourite)
        }

        mCfscvCardsPokemon.setOnClickListener {
            setSealedSubCategoryUnSelected()
            mCardSubCategories.selectSubCategory(mCfscvCardsPokemon)
        }

        mCfscvCardsYuGiOh.setOnClickListener {
            setSealedSubCategoryUnSelected()
            mCardSubCategories.selectSubCategory(mCfscvCardsYuGiOh)
        }

        mCfscvCardsMagic.setOnClickListener {
            setSealedSubCategoryUnSelected()
            mCardSubCategories.selectSubCategory(mCfscvCardsMagic)
        }

        mCfcvToys.setOnClickListener {
            mCfcvToys.setActive(!mCfcvToys.mIsActive)
            mLayoutCards.animateVisibility(false)
            mLayoutSealed.animateVisibility(false)
            setCardSubCategoryUnSelected()
            setSealedSubCategoryUnSelected()
        }

        mCfscvSealedPokemon.setOnClickListener {
            setCardSubCategoryUnSelected()
            mSealedSubCategories.selectSubCategory(mCfscvSealedPokemon)
        }

        mCfscvSealedYuGiOh.setOnClickListener {
            setCardSubCategoryUnSelected()
            mSealedSubCategories.selectSubCategory(mCfscvSealedYuGiOh)
        }

        mCfscvSealedMagic.setOnClickListener {
            setCardSubCategoryUnSelected()
            mSealedSubCategories.selectSubCategory(mCfscvSealedMagic)
        }

        mCfcvCards.setOnClickListener {
            mToggleCards = if (mToggleCards) {
                mLayoutCards.animateVisibility(mToggleCards)
                mLayoutSealed.animateVisibility(false)
                selectCategory(mCfcvCards)
                setSealedSubCategoryUnSelected()
                !mToggleCards
            } else {
                mCfcvCards.setActive(false)
                setCardSubCategoryUnSelected()
                mLayoutCards.animateVisibility(mToggleCards)
                !mToggleCards
            }

        }

        mCfcvSealed.setOnClickListener {
            mToggleSealed = if (mToggleSealed) {
                mLayoutSealed.animateVisibility(mToggleSealed)
                mLayoutCards.animateVisibility(false)
                selectCategory(mCfcvSealed)
                setCardSubCategoryUnSelected()
                !mToggleSealed
            } else {
                mCfcvSealed.setActive(false)
                setSealedSubCategoryUnSelected()
                mLayoutSealed.animateVisibility(mToggleSealed)
                !mToggleSealed
            }

        }

    }



    private fun setSelectedCct(mCct: CustomCollectionTypeView) {
        when (mCct) {
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

    private fun setCardSubCategoryUnSelected() {
        mCfcvCards.setActive(false)
        mCfscvCardsPokemon.setActive(false)
        mCfscvCardsMagic.setActive(false)
        mCfscvCardsYuGiOh.setActive(false)
    }

    private fun setSealedSubCategoryUnSelected() {
        mCfcvSealed.setActive(false)
        mCfscvSealedYuGiOh.setActive(false)
        mCfscvSealedMagic.setActive(false)
        mCfscvSealedPokemon.setActive(false)
    }



    private fun initViews() {
        mCctProduct = mViewBinding.cctProduct
        mCctForSale = mViewBinding.cctForSale
        mCctCollections = mViewBinding.cctCollections
        mIvFilter = mViewBinding.ivFilter
        mTvTitle = mViewBinding.tvSearchTitle
        mRvMain = mViewBinding.rvMain

        mCfcvAskLowest = mViewBinding.cfcvAskLowest
        mCfcvAskHighest = mViewBinding.cfcvAskHighest
        mCfcvMostFavourite = mViewBinding.cfcvMostFavourite

        mCfcvCards = mViewBinding.cfcvCards
        mCfcvToys = mViewBinding.cfcvToys
        mCfcvSealed = mViewBinding.cfcvSealed

        mCfscvCardsPokemon = mViewBinding.cfscvCardsPokemon
        mCfscvCardsYuGiOh = mViewBinding.cfscvCardsYuGiOh
        mCfscvCardsMagic = mViewBinding.cfscvCardsMagic

        mCfscvSealedPokemon = mViewBinding.cfscvSealedPokemon
        mCfscvSealedYuGiOh = mViewBinding.cfscvSealedYuGiOh
        mCfscvSealedMagic = mViewBinding.cfscvSealedMagic

        mLayoutCards = mViewBinding.layoutCards
        mLayoutSealed = mViewBinding.layoutSealed

        mClMain = mViewBinding.clMain
        mDrawer = mViewBinding.drawer
        mToggle = ActionBarDrawerToggle(requireActivity(), mDrawer, R.string.open, R.string.close)
        mDrawer.addDrawerListener(mToggle)
        mToggle.syncState()

        setSelectedCct(mCctProduct)

        mSortCategories = listOf(mCfcvAskLowest, mCfcvAskHighest, mCfcvMostFavourite)
        mCategories = listOf(mCfcvCards, mCfcvToys, mCfcvSealed)
        mCardSubCategories = listOf(mCfscvCardsPokemon, mCfscvCardsYuGiOh, mCfscvCardsMagic)
        mSealedSubCategories = listOf(mCfscvSealedPokemon, mCfscvSealedYuGiOh, mCfscvSealedMagic)

    }

    //This fun used to select Sorting category as selected
    private fun selectSortingCategory(category: CustomFilterCategoryView?) {
        for (sortCategory in mSortCategories) {
            sortCategory.setActive(sortCategory == category && !sortCategory.mIsActive)
        }
    }

    //This fun used to select category
    private fun selectCategory(category: CustomFilterCategoryView?) {
        for (cat in mCategories) {
            cat.setActive(cat == category && !cat.mIsActive)
        }
    }

    //This fun used to select sub category and parent category as selected from the given list
    private fun List<CustomFilterSubCategoryView>.selectSubCategory(subCategory: CustomFilterSubCategoryView?) {
        for (category in this) {
            category.setActive(category == subCategory && !category.mIsActive)
        }
    }

    companion object {
        private const val spanCount = 2
    }
}