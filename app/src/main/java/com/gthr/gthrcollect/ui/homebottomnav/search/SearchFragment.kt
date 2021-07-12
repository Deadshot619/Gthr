package com.gthr.gthrcollect.ui.homebottomnav.search

import androidx.fragment.app.viewModels
import com.gthr.gthrcollect.databinding.SearchFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment

class SearchFragment : BaseFragment<SearchViewModel, SearchFragmentBinding>() {

    override fun getViewBinding() = SearchFragmentBinding.inflate(layoutInflater)
    override val mViewModel: SearchViewModel by viewModels()
    override fun onBinding() {}

}