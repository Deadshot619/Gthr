package com.gthr.gthrcollect.data.repository

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.gthr.gthrcollect.GthrCollect
import com.gthr.gthrcollect.data.remote.fetchData
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.SearchCollection
import com.gthr.gthrcollect.utils.constants.CloudFunctions
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.constants.FirebaseStorage
import com.gthr.gthrcollect.utils.constants.FirebaseStorage.HOME_PAGE_BANNER
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.*

class FeedRepository {

    private val mStorageRef = Firebase.storage.reference

    fun fetchBannerImage() =
        flow<State<String>> {
            emit(State.loading())
            val ref = mStorageRef.child(FirebaseStorage.BANNER_IMAGE).child(HOME_PAGE_BANNER)
            val url = ref.downloadUrl.await()
            emit(State.Success(url.toString()))
            GthrLogger.e("dfjcndf", url.toString())

        }.catch {
            // If exception is thrown, emit failed state along with message.
            emit(State.failed(it.message.toString()))
            GthrLogger.e("dfjcndf", "failed. "+it.message.toString())
        }.flowOn(Dispatchers.IO)
}