package com.app.demoslotmachne.utils

import com.app.demoslotmachne.data.model.Setting

class ListUtils {
    companion object{
        fun settingList():ArrayList<Setting>{
          return  arrayListOf(
                Setting(1,"Privacy Policy"),
                Setting(2,"Terms of Use"),
                Setting(3,"About Us"),
             //   Setting(4,"Contact Us"),
            )
        }

        fun betList():ArrayList<Setting>{
            return  arrayListOf(
                Setting(1,"50"),
                Setting(2,"100"),
                Setting(3,"200"),
                Setting(4,"500"),
            )
        }
    }
}