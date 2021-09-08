package com.gthr.gthrcollect.ui.homebottomnav.search

import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.SearchRepository
import com.gthr.gthrcollect.databinding.SearchFragmentBinding
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.homebottomnav.search.adapter.ProductAdapter
import com.gthr.gthrcollect.ui.homebottomnav.search.adapter.SearchCollectionAdapter
import com.gthr.gthrcollect.ui.productdetail.ProductDetailActivity
import com.gthr.gthrcollect.utils.customviews.*
import com.gthr.gthrcollect.utils.enums.*
import com.gthr.gthrcollect.utils.extensions.animateVisibility
import com.gthr.gthrcollect.utils.extensions.gone
import com.gthr.gthrcollect.utils.extensions.visible
import com.gthr.gthrcollect.utils.logger.GthrLogger

class SearchFragment : BaseFragment<SearchViewModel, SearchFragmentBinding>() {

    var mSelectedSort:String?=null
    var mSelectedCategory:ProductCategory?=null
    var mSelectedProductType:ProductType?=null
    var mSearchKey:String?=null
    var mLimit:Int?=null

    override fun getViewBinding() = SearchFragmentBinding.inflate(layoutInflater)
    override val mViewModel: SearchViewModel by viewModels{
        SearchViewModelFactory(
            SearchRepository()
        )
    }

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

    private lateinit var mAdapterCollections: SearchCollectionAdapter
    private lateinit var mProductAdapter: ProductAdapter

    private lateinit var mSearchBar : CustomSearchView

    private val args by navArgs<SearchFragmentArgs>()

    override fun onBinding() {
        initViews()
        setUpOnClickListeners()
        setUpRecyclerView()
        setUpObservers()

        if(args.type==SearchType.COLLECTIONS){
            setCollectionSelected()
        }
        else if(args.type==SearchType.PRODUCT){
            when(args.sortFilter){
                ProductSortFilter.LOWEST_ASK -> selectSortingCategory(mCfcvAskLowest)
                ProductSortFilter.HIGHEST_ASK -> selectSortingCategory(mCfcvAskHighest)
                ProductSortFilter.NONE -> {}
            }
            when(args.categoryFilter){
                ProductCategoryFilter.CARD -> selectCard()
                ProductCategoryFilter.TOY -> selectToys()
                ProductCategoryFilter.SEALED -> selectSealed()
                ProductCategoryFilter.NONE -> searchProduct()

            }
        }
    }

    private fun setUpObservers() {
        mViewModel.productList.observe(viewLifecycleOwner) {  it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Failed -> showProgressBar(false)
                    is State.Success -> {
                        mProductAdapter.submitList(it.data)
                        showProgressBar(false)
                        if (mDrawer.isDrawerVisible(GravityCompat.END)) {
                            mDrawer.closeDrawer(GravityCompat.END)
                        }
                        GthrLogger.e("observedata","data: ${it.data}")
                    }
                }
            }
        }

        mViewModel.collectionList.observe(viewLifecycleOwner) {  it ->
            it.contentIfNotHandled?.let {
                when (it) {
                    is State.Loading -> showProgressBar()
                    is State.Failed -> showProgressBar(false)
                    is State.Success -> {
                        mAdapterCollections.submitList(it.data)
                        showProgressBar(false)
                        if (mDrawer.isDrawerVisible(GravityCompat.END)) {
                            mDrawer.closeDrawer(GravityCompat.END)
                        }
                        GthrLogger.e("observedata","data: ${it.data}")
                    }
                }
            }
        }

    }

    private fun setUpRecyclerView() {
        mAdapterCollections = SearchCollectionAdapter {
            when {
                it % 5 == 0 -> startActivity(
                    ProductDetailActivity.getInstance(
                        requireContext(),
                        null
                    )
                )
                it % 5 == 1 -> startActivity(
                    ProductDetailActivity.getInstance(
                        requireContext(),
                        null
                    )
                )
                it % 5 == 2 -> startActivity(
                    ProductDetailActivity.getInstance(
                        requireContext(),
                        null
                    )
                )
                it % 5 == 3 -> startActivity(
                    ProductDetailActivity.getInstance(
                        requireContext(),
                        null
                    )
                )
                it % 5 == 4 -> startActivity(
                    ProductDetailActivity.getInstance(
                        requireContext(),
                        null
                    )
                )
            }
        }
        mProductAdapter = ProductAdapter(CustomProductCell.State.NORMAL){
            when {
                mCctForSale.mIsActive -> startActivity(
                    AskFlowActivity.getInstance(
                        requireContext(),
                        AskFlowType.BUY_DIRECTLY_FROM_SOMEONE,
                        it
                    )
                )
                else -> startActivity(ProductDetailActivity.getInstance(requireContext(), it))
            }
        }
        mRvMain.apply {
            layoutManager = GridLayoutManager(requireContext(),spanCount)
            adapter = mProductAdapter
        }

    }

    private fun setUpOnClickListeners() {
        mCctProduct.setOnClickListener {
            setSelectedCct(mCctProduct)
            mRvMain.adapter = mProductAdapter
        }
        mCctForSale.setOnClickListener {
            setSelectedCct(mCctForSale)
            mRvMain.adapter = mProductAdapter
        }
        mCctCollections.setOnClickListener {
            setCollectionSelected()

            searchCollection(mLimit)
        }

        mIvFilter.setOnClickListener {
            if (mDrawer.isDrawerVisible(GravityCompat.END)) {
                mDrawer.closeDrawer(GravityCompat.END)
            } else {
                mDrawer.openDrawer(GravityCompat.END)
            }
        }

        mCfcvAskLowest.setOnClickListener {
            mSelectedSort=  selectSortingCategory(mCfcvAskLowest).toString()
          //  showToast(mCfcvAskLowest.text.toString())
        }

        mCfcvAskHighest.setOnClickListener {
            mSelectedSort=selectSortingCategory(mCfcvAskHighest).toString()
         //   showToast(mCfcvAskHighest.text.toString())
        }

        mCfcvMostFavourite.setOnClickListener {
            mSelectedSort=selectSortingCategory(mCfcvMostFavourite).toString()
          //  showToast(mCfcvMostFavourite.text.toString())
        }

        mCfscvCardsPokemon.setOnClickListener {
            setSealedSubCategoryUnSelected()
            mCardSubCategories.selectSubCategory(mCfscvCardsPokemon)

            if (mCfscvCardsPokemon.mIsActive){
                searchProduct(ProductCategory.CARDS,ProductType.POKEMON,mLimit)

            }else{
                searchProduct(ProductCategory.CARDS,null,mLimit)
            }
        }

        mCfscvCardsYuGiOh.setOnClickListener {
            setSealedSubCategoryUnSelected()
             mCardSubCategories.selectSubCategory(mCfscvCardsYuGiOh)
          //  showToast(mSelectedProductType)
            if (mCfscvCardsYuGiOh.mIsActive){
                searchProduct(ProductCategory.CARDS,ProductType.YUGIOH,mLimit)

            }else{
                searchProduct(ProductCategory.CARDS,null,mLimit)

            }
        }

        mCfscvCardsMagic.setOnClickListener {
            setSealedSubCategoryUnSelected()
            mCardSubCategories.selectSubCategory(mCfscvCardsMagic)
          //  showToast(mSelectedProductType)

            if (mCfscvCardsMagic.mIsActive){
                searchProduct(ProductCategory.CARDS,ProductType.MAGIC_THE_GATHERING,mLimit)

            }else{
                searchProduct(ProductCategory.CARDS,null,mLimit)
            }
        }

        mCfcvToys.setOnClickListener {
            selectToys()
        }

        mCfscvSealedPokemon.setOnClickListener {
            setCardSubCategoryUnSelected()
            mSealedSubCategories.selectSubCategory(mCfscvSealedPokemon)
            if (mCfscvSealedPokemon.mIsActive){
                searchProduct(ProductCategory.SEALED,ProductType.SEALED_POKEMON,mLimit)
            }else{
                searchProduct(ProductCategory.SEALED,null,mLimit)

            }
        }

        mCfscvSealedYuGiOh.setOnClickListener {
            setCardSubCategoryUnSelected()
            mSealedSubCategories.selectSubCategory(mCfscvSealedYuGiOh)

            if (mCfscvSealedYuGiOh.mIsActive){
                searchProduct(ProductCategory.SEALED,ProductType.SEALED_YUGIOH,mLimit)
            }else{

                searchProduct(ProductCategory.SEALED,null,mLimit)
            }
        }

        mCfscvSealedMagic.setOnClickListener {
            setCardSubCategoryUnSelected()
            mSealedSubCategories.selectSubCategory(mCfscvSealedMagic)

            if (mCfscvSealedMagic.mIsActive){
                searchProduct(ProductCategory.SEALED,ProductType.SEALED_MTG,mLimit)
            }else{
                searchProduct(ProductCategory.SEALED,null,mLimit)
            }
        }

        mCfcvCards.setOnClickListener {
            selectCard()

        }

        mCfcvSealed.setOnClickListener {
            selectSealed()
        }

        mSearchBar.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!mSearchBar.text.toString().trim().isNullOrEmpty()){
                        if (mCctCollections.mIsActive)  searchCollection(mLimit)

                        if (mCctProduct.mIsActive) searchProduct(mSelectedCategory,mSelectedProductType,mLimit)

                    }
                    return true
                }
                return false
            }
        })
    }

    private fun selectToys() {
        mCfcvToys.setActive(!mCfcvToys.mIsActive)
        mLayoutCards.animateVisibility(false)
        mLayoutSealed.animateVisibility(false)
        setCardSubCategoryUnSelected()
        setSealedSubCategoryUnSelected()

        if (mCfcvToys.mIsActive){
            searchProduct(ProductCategory.TOYS,null,mLimit)
        }else{
            searchProduct(null,null,mLimit)
        }
    }

    private fun selectSealed() {
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

        if (mCfcvSealed.mIsActive){
            searchProduct(ProductCategory.SEALED,null,mLimit)
        }else{
            searchProduct(null,null,mLimit)
        }
    }

    private fun selectCard() {
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
        if (mCfcvCards.mIsActive){
            searchProduct(ProductCategory.CARDS,null,mLimit)
        }else{
            searchProduct(null,null,mLimit)
        }
    }

    private fun setCollectionSelected() {
        setSelectedCct(mCctCollections)
        mDrawer.closeDrawer(GravityCompat.END)
        mRvMain.adapter = mAdapterCollections
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

        mSearchBar=mViewBinding.csvSearch

        initProgressBar(mViewBinding.layoutProgress)
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

    private fun searchProduct(productCategory:ProductCategory?=null,productType: ProductType?=null,limit:Int?=null){
        mSelectedCategory=productCategory
        mSelectedProductType=productType
        mLimit=limit
        mSearchKey=mSearchBar.text.toString().trim()
        mViewModel.searchProducts(mSearchKey,mSelectedCategory?.title,mSelectedProductType?.title,mLimit)
    }

    private fun searchCollection(limit:Int? ){
        mSearchKey=mSearchBar.text.toString().trim()
        mViewModel.searchCollection(mSearchKey,limit)
    }

    companion object {
        private const val spanCount = 2
    }
}