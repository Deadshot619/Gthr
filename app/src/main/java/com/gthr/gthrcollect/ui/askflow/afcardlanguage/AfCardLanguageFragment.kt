package com.gthr.gthrcollect.ui.askflow.afcardlanguage

import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.AfCardLanguageFragmentBinding
import com.gthr.gthrcollect.ui.askflow.AskFlowViewModel
import com.gthr.gthrcollect.ui.askflow.ConfigurationAdapter
import com.gthr.gthrcollect.ui.base.BaseFragment

class AfCardLanguageFragment : BaseFragment<AskFlowViewModel, AfCardLanguageFragmentBinding>() {

    override val mViewModel: AskFlowViewModel by viewModels()
    override fun getViewBinding() = AfCardLanguageFragmentBinding.inflate(layoutInflater)

    private lateinit var mRvMain : RecyclerView
    private lateinit var mAdapter: ConfigurationAdapter

    override fun onBinding() {
        initViews()
        setUpLanguage()
    }

    private fun setUpLanguage() {
        mAdapter = ConfigurationAdapter{
            goToEdition()
        }
        mRvMain.apply {
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            adapter = mAdapter
        }
        mAdapter.submitList(getLanguage())
    }

    private fun getLanguage() : List<String> {
        val list = arrayListOf<String>()
        list.add(getString(R.string.english))
        list.add(getString(R.string.cheneses))
        list.add(getString(R.string.chinese))
        list.add(getString(R.string.french))
        list.add(getString(R.string.english))
        list.add(getString(R.string.cheneses))
        list.add(getString(R.string.chinese))
        list.add(getString(R.string.french))
        list.add(getString(R.string.english))
        list.add(getString(R.string.cheneses))
        list.add(getString(R.string.chinese))
        list.add(getString(R.string.french))
        return list
    }

    private fun initViews() {
        mViewBinding.run {
            mRvMain = rvMain
        }
    }

    private fun goToEdition() {
        findNavController().navigate(AfCardLanguageFragmentDirections.actionAfCardLanguageFragmentToAfEditionFragment())
    }
}