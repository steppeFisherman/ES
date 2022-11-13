package com.example.es.utils

import android.view.View

interface Animation {

    fun animate(view1: View, view2: View)

    class Base : Animation {
        override fun animate(view1: View, view2: View) {

            view1.animate().scaleX(4f).scaleY(4f).alpha(0f)
                .setDuration(200)
                .withEndAction {
                    view1.scaleX = 1f
                    view1.scaleY = 1f
                    view1.alpha = 1f
                }

            view2.animate().scaleX(4f).scaleY(4f).alpha(0f)
                .setDuration(100)
                .withEndAction {
                    view2.scaleX = 1f
                    view2.scaleY = 1f
                    view2.alpha = 1f
                }
        }
    }
}