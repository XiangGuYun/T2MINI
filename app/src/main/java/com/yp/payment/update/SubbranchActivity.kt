package com.yp.payment.update

import android.os.Bundle
import com.yp.baselib.annotation.LayoutId
import com.yp.baselib.base.BaseActivity
import com.yp.payment.Constant
import com.yp.payment.R
import com.yp.payment.ui.MoneyActivity
import kotlinx.android.synthetic.main.activity_subbranch.*

/**
 * 分店选择
 */
@LayoutId(R.layout.activity_subbranch)
class SubbranchActivity : BaseActivity() {
    
    override fun init(bundle: Bundle?) {
        hideStatusBar()
        Req.getBranchList{ 
            branches ->
            rvSubBranch.wrap.generate(branches, {
                h,p, item->
                h.tv(R.id.tvBranchId).text = branches[p].branchId.toString()
                h.tv(R.id.tvBranchName).text = branches[p].branchName
                h.itemClick {
                    Constant.branchId = branches[p].branchId
                    Constant.shopName = branches[p].branchName
                    //记录上次选择的分店id
                    SPHelper.putShopName(Constant.shopName)
                    SPHelper.putBranchId(Constant.branchId)
                    goTo<MoneyActivity>()
                    finish()
                }
            }, null, R.layout.subbranch_item)
        }

        autoSelectBranch()
    }

    private fun autoSelectBranch() {
        val branchId = SPHelper.getBranchId()
        if (branchId != 0) {
            branchId.toString().toInt().also { Constant.branchId = it }
            Constant.shopName = SPHelper.getShopName()
            goTo<MoneyActivity>()
            finish()
        }
    }

}