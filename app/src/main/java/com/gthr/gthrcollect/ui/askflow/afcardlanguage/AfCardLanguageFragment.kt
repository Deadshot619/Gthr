package com.gthr.gthrcollect.ui.askflow.afcardlanguage

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.AfCardLanguageFragmentBinding
import com.gthr.gthrcollect.model.domain.LanguageDomainModel
import com.gthr.gthrcollect.ui.askflow.AskFlowActivity
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModelFactory
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.enums.AskFlowType
import com.gthr.gthrcollect.utils.enums.ProductCategory

class AfCardLanguageFragment : BaseFragment<AskFlowViewModel, AfCardLanguageFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by activityViewModels{
        AskFlowViewModelFactory(AskFlowRepository())
    }
    override fun getViewBinding() = AfCardLanguageFragmentBinding.inflate(layoutInflater)

    private lateinit var mRvMain: RecyclerView
    private lateinit var mAdapter: ConfigurationLanguageAdapter

    private val args by navArgs<AfCardLanguageFragmentArgs>()
    private lateinit var mProductCategory: ProductCategory

    override fun onBinding() {
        mProductCategory = args.productCategory

        checkAskFlowData()
        initViews()
        setUpLanguage()
        setUpObservers()
    }

    private fun checkAskFlowData() {
        when ((requireActivity() as AskFlowActivity).getAskFlowType()) {
            AskFlowType.COLLECT ->
                if (isProductSealedOrToys())
                    findNavController().navigate(AfCardLanguageFragmentDirections.actionAfCardLanguageFragmentToAfWantToSellFragment())
            AskFlowType.SELL ->
                if (isProductSealedOrToys())
                    findNavController().navigate(AfCardLanguageFragmentDirections.actionAfCardLanguageFragmentToAfAddPicFragment())
            AskFlowType.BUY ->
                if (isProductSealedOrToys())
                    findNavController().navigate(AfCardLanguageFragmentDirections.actionAfCardLanguageFragmentToAfReviewYourAskFragment())
            AskFlowType.BUY_DIRECTLY_FROM_SOMEONE ->
                findNavController().navigate(AfCardLanguageFragmentDirections.actionAfCardLanguageFragmentToAfBuyDirectlyReviewFragment())
        }
    }

    private fun setUpLanguage() {
        mAdapter = ConfigurationLanguageAdapter(object :
            ConfigurationLanguageAdapter.IConfigurationLanguageListener {
            override fun onClick(languageDomainModel: LanguageDomainModel) {
                mViewModel.setSelectedLanguage(languageDomainModel)
                goToEdition()
            }
        })
        mRvMain.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapter
        }
    }

    private fun setUpObservers() {
        mViewModel.languageList.observe(viewLifecycleOwner, {
            it.peekContent().let {
                mAdapter.submitList(it)
            }
        })
    }

    private fun initViews() {
        mViewBinding.run {
            mRvMain = rvMain
        }
    }

    private fun goToEdition() {
        findNavController().navigate(AfCardLanguageFragmentDirections.actionAfCardLanguageFragmentToAfEditionFragment())
    }

    private fun isProductSealedOrToys(): Boolean =
        mProductCategory == ProductCategory.SEALED || mProductCategory == ProductCategory.TOYS
}