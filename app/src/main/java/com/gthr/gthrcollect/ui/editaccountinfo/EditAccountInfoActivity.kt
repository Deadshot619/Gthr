package com.gthr.gthrcollect.ui.editaccountinfo

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.gthr.gthrcollect.databinding.ActivityEditAccountInfoBinding
import com.gthr.gthrcollect.ui.base.BaseActivity

class EditAccountInfoActivity :
    BaseActivity<EditAccountInfoViewModel, ActivityEditAccountInfoBinding>() {
    override val mViewModel: EditAccountInfoViewModel by viewModels()
    override fun getViewBinding(): ActivityEditAccountInfoBinding =
        ActivityEditAccountInfoBinding.inflate(layoutInflater)

    override fun onBinding() {}

    companion object {
        fun getInstance(context: Context) = Intent(context, EditAccountInfoActivity::class.java)
    }
}