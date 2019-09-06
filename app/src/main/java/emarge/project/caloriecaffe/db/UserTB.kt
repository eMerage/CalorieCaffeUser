package emarge.project.caloriecaffe.db

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class UserTB : RealmObject() {


    @PrimaryKey
    var rowID: Int? = null
    var userID: String? = null
    var nic: String? = null
    var email: String? = null




}