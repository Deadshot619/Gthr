package com.gthr.gthrcollect.ui.profile

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gthr.gthrcollect.databinding.AllUsersListsBinding
import com.gthr.gthrcollect.model.domain.CollectionInfoDomainModel
import com.gthr.gthrcollect.model.mapper.toCollectionInfoDomainModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.CollectionInfoModel
import com.gthr.gthrcollect.ui.base.BaseActivity
import com.gthr.gthrcollect.ui.base.BaseViewModel
import com.gthr.gthrcollect.ui.profile.follow.FollowUserAdapter
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.enums.ProfileNavigationType
import com.gthr.gthrcollect.utils.extensions.showToast
import com.gthr.gthrcollect.utils.logger.GthrLogger

class AllUsersList() :BaseActivity<BaseViewModel,AllUsersListsBinding>() {

    private lateinit var mAdapter: FollowUserAdapter
    private lateinit var mRvMain: RecyclerView

    override val mViewModel: BaseViewModel by viewModels()

    override fun getViewBinding(): AllUsersListsBinding =
        AllUsersListsBinding.inflate(layoutInflater)

    override fun onBinding() {

        initViwes()
        fetchAllUserList()
    }

    private fun initViwes() {

        mViewBinding.run {
            mRvMain = rvMain
        }
        setUpRecyclerView()
    }

    fun fetchAllUserList(){

         val mFirebaseRD = Firebase.database.reference
        val arrayList = mutableListOf<CollectionInfoDomainModel>()

          mFirebaseRD.child(FirebaseRealtimeDatabase.COLLECTION_INFO_MODEL).get().addOnSuccessListener {

              it.children.forEach{

                  try {
                      GthrLogger.e("UserKey",it.key.toString())
                      val user =it.getValue(CollectionInfoModel::class.java)
                      arrayList.add(user!!.toCollectionInfoDomainModel(it.key.toString()))
                      mAdapter.submitList(arrayList)
                      GthrLogger.e("User",arrayList.toString())
                  }catch (e:Exception){
                      e.printStackTrace()
                  }
              }

        }

    }

    private fun setUpRecyclerView() {
        mAdapter = FollowUserAdapter(object : FollowUserAdapter.FollowUserListener {

            override fun onClick(collectionInfoDomainModel: CollectionInfoDomainModel) {
                startActivity(ProfileActivity.getInstance(this@AllUsersList, ProfileNavigationType.PROFILE, collectionInfoDomainModel.collectionId))
            }
        })
        mRvMain.apply {
            layoutManager = LinearLayoutManager(this@AllUsersList)
            mRvMain.adapter = mAdapter
        }
    }


}