package com.gthr.gthrcollect.ui.askflow.afcardedition

import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.data.repository.AskFlowRepository
import com.gthr.gthrcollect.databinding.AfCardEditionFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModelFactory
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.utils.enums.EditionType
import com.gthr.gthrcollect.utils.extensions.gone

class AfCardEditionFragment : BaseFragment<AskFlowViewModel, AfCardEditionFragmentBinding>() {
    override val mViewModel: AskFlowViewModel by activityViewModels{
        AskFlowViewModelFactory(AskFlowRepository())
    }

    override fun getViewBinding() = AfCardEditionFragmentBinding.inflate(layoutInflater)

    private lateinit var mIvBack: ImageView
    private lateinit var mRvMain: RecyclerView
    private lateinit var mAdapter: ConfigurationEditionAdapter

    override fun onBinding() {
        initViews()
        setUpClickListeners()
        setUpEdition()
        setUpObservers()
    }

    private fun setUpEdition() {
        mAdapter = ConfigurationEditionAdapter(object :
            ConfigurationEditionAdapter.IConfigurationEditionListener {
            override fun onClick(editionType: EditionType) {
                mViewModel.setSelectedEdition(editionType)
                goToCardCondition()
            }
        })
        mRvMain.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL, false
            )
            adapter = mAdapter
        }
    }

    private fun setUpObservers() {
        mViewModel.editionList.observe(viewLifecycleOwner, {
            it.peekContent().let {
                mAdapter.submitList(it)
            }
        })
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
        //If this fragment is start destination, then hide back button
        if (findNavController().previousBackStackEntry == null)
            mIvBack.gone()
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