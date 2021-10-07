package com.gthr.gthrcollect.data.repository

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.JsonElement
import com.gthr.gthrcollect.data.remote.fetchData
import com.gthr.gthrcollect.model.State
import com.gthr.gthrcollect.model.domain.FeedDomainModel
import com.gthr.gthrcollect.model.mapper.toFeedDomainModel
import com.gthr.gthrcollect.model.network.firebaserealtimedb.FeedModel
import com.gthr.gthrcollect.utils.constants.CloudFunctions
import com.gthr.gthrcollect.utils.constants.FirebaseRealtimeDatabase
import com.gthr.gthrcollect.utils.constants.FirebaseStorage
import com.gthr.gthrcollect.utils.constants.FirebaseStorage.HOME_PAGE_BANNER
import com.gthr.gthrcollect.utils.enums.ProductCategory
import com.gthr.gthrcollect.utils.extensions.fromJsonElement
import com.gthr.gthrcollect.utils.extensions.gson
import com.gthr.gthrcollect.utils.extensions.toJsonElement
import com.gthr.gthrcollect.utils.logger.GthrLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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

    fun fetchFeed(limit: Int?, page: Int?, productCategory: ProductCategory?,creatorUID: String?) = flow<State<List<FeedDomainModel>>> {
        emit(State.loading())

        val data = hashMapOf(
            CloudFunctions.LIMIT to (limit?:20),
            CloudFunctions.PAGE to (page?:0),
            CloudFunctions.PRODUCT_CATEGORY to (productCategory?.title?:""),
            CloudFunctions.CREATOR_UID to (creatorUID?:""),
        )

        GthrLogger.i("sdnvksdnf", "fetchFeed: $data")
        val feeds = fetchData<List<HashMap<String, *>>>(CloudFunctions.FEED_ACTIVITY, data).await()
        GthrLogger.i("hdsbcsdb", "fetchFeed: ${feeds.size}")

        val list = mutableListOf<FeedDomainModel>()
        feeds.forEach {
            GthrLogger.i("dhcjsdf","list $it")
            try{
                when{
                    it.containsKey(FirebaseRealtimeDatabase.BID) -> {
                        val jsonElement: JsonElement = gson.toJsonElement(it[FirebaseRealtimeDatabase.BID])
                        val feed: FeedModel? = gson.fromJsonElement(jsonElement)
                        feed?.let {
                            feed.feedType = "bid"
                            list.add(feed.toFeedDomainModel())
                        }
                    }
                    it.containsKey(FirebaseRealtimeDatabase.ASK) -> {
                        val jsonElement: JsonElement = gson.toJsonElement(it[FirebaseRealtimeDatabase.ASK])
                        val feed: FeedModel? = gson.fromJsonElement(jsonElement)
                        feed?.let {
                            feed.feedType = "ask"
                            list.add(feed.toFeedDomainModel())
                        }
                    }
                    it.containsKey(FirebaseRealtimeDatabase.COLLECTION) -> {
                        val jsonElement: JsonElement = gson.toJsonElement(it[FirebaseRealtimeDatabase.COLLECTION])
                        val feed: FeedModel? = gson.fromJsonElement(jsonElement)
                        feed?.let {
                            feed.feedType = "collection"
                            list.add(feed.toFeedDomainModel())
                        }
                    }
                }
            }
            catch (e : Exception){
                GthrLogger.e("dfjcndf", "failed. "+e.message.toString())
            }
        }

        list.forEach {
            delay(100)
            GthrLogger.i("hdsbcsdb","final list ${it.language}")
            GthrLogger.i("hdsbcsdb","final list ${it.condition}")
        }

        GthrLogger.i("hdsbcsdb","final list ${list.size}")

        emit(State.Success(list))
    }.catch {
        // If exception is thrown, emit failed state along with message.
        emit(State.failed(it.message.toString()))
        GthrLogger.e("dfjcndf", "failed. "+it.message.toString())
    }.flowOn(Dispatchers.IO)
}