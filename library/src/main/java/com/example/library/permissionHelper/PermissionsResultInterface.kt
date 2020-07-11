package com.example.library.permissionHelper

interface PermissionsResultInterface {
    fun onPermissionResult(isAllGranted:Boolean, permissionResults:ArrayList<PermissionModel>?, requestCode:Int)
}