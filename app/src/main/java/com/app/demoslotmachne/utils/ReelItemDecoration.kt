package com.app.demoslotmachne.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ReelItemDecoration(
    private val visibleItemCount: Int = 3
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val parentHeight = parent.height
        val itemHeight = parentHeight / visibleItemCount
        val position = parent.getChildAdapterPosition(view)

        val topBottomPadding = itemHeight * (visibleItemCount - 1) / 2

        if (position == 0) {
            outRect.top = topBottomPadding
        }

        if (position == state.itemCount - 1) {
            outRect.bottom = topBottomPadding
        }
    }
}