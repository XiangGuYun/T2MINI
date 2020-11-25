package com.usr.objectbox.objectbox

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class ShopConfig {
    @Id
    var id: Long = 0
    var shopId: Int = 0
    var cashierDeskId: Int = 0
    var shopName: String = ""
    var username: String = ""
    var autoLogin: Int = 1
    var autoPrint: Int = 0

    override fun toString(): String {
        return "ShopConfig(id=$id, shopId=$shopId, cashierDeskId=$cashierDeskId, shopName='$shopName', username='$username', autoLogin=$autoLogin, autoPrint=$autoPrint)"
    }


}