package com.gthr.gthrcollect.ui.profile.favsold

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.gthr.gthrcollect.R
import com.gthr.gthrcollect.databinding.FollowFragmentBinding
import com.gthr.gthrcollect.ui.base.BaseFragment
import com.gthr.gthrcollect.ui.profile.ProfileActivity
import com.gthr.gthrcollect.ui.profile.follow.FollowFragmentArgs
import com.gthr.gthrcollect.ui.profile.follow.FollowViewModel
import com.gthr.gthrcollect.utils.customviews.CustomSearchView
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType

class FavSoldFragment : BaseFragment<FollowViewModel, FollowFragmentBinding>() {
    override val mViewModel: FollowViewModel by viewModels()
    override fun getViewBinding() = FollowFragmentBinding.inflate(layoutInflater)

    private val args by navArgs<FollowFragmentArgs>()
    private lateinit var mType: ProfileNavigationType

    private lateinit var mRvMain: RecyclerView
    private lateinit var mSearchView: CustomSearchView

    override fun onBinding() {
        mType = args.type

        initViews()
        setUpViews(mType)
        setUpClickListeners()
        setUpRecyclerView()
    }

    private fun initViews() {
        mViewBinding.run {
            mRvMain = rvMain
            mSearchView = csvSearch
        }
    }

    private fun setUpViews(type: ProfileNavigationType){
        when(type) {
            ProfileNavigationType.FAVOURITES -> {
                (activity as ProfileActivity).setToolbarTitle(getString(R.string.text_favorites))
                mSearchView.hint = getString(R.string.hint_favourites_search)
            }
            ProfileNavigationType.SOLD -> {
                (activity as ProfileActivity).setToolbarTitle(getString(R.string.title_sold))
                mSearchView.hint = getString(R.string.hint_sold_search)
            }
        }
    }

    private fun setUpClickListeners() {
    }

    private fun setUpRecyclerView() {
    }
}