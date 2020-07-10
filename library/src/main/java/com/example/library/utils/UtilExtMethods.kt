package com.example.library.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.library.base.BaseFragment

fun FragmentManager.activeFragment() : BaseFragment? {
    if (backStackEntryCount == 0) {
        return null
    }
    val tag: String? = getBackStackEntryAt(backStackEntryCount- 1).name
    return (findFragmentByTag(tag) as? BaseFragment)
}

fun FragmentManager.addFragment(fragment: Fragment, container:Int, fragmentTag:String, addToBackStack:Boolean = true) {
    if(!addToBackStack) {
        beginTransaction().add(container,fragment,fragmentTag).commit()
    }
    else {
        beginTransaction().add(container,fragment,fragmentTag).addToBackStack(fragmentTag).commit()
    }
}