package com.galerkinrobotics.test.util

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// Grid çizgileri için ItemDecoration sınıfı
class GridItemDecoration(
    private val spanCount: Int,
    private val spacing: Int,
    private val includeEdge: Boolean,
    private val lineColor: Int,
    private val lineWidth: Int
) : RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        color = lineColor
        strokeWidth = lineWidth.toFloat()
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        drawHorizontalLines(canvas, parent)
        drawVerticalLines(canvas, parent)
    }

    private fun drawHorizontalLines(canvas: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = parent.paddingLeft.toFloat()
            val right = (parent.width - parent.paddingRight).toFloat()
            val top = (child.bottom + params.bottomMargin).toFloat()

            // Alt çizgi çiz
            canvas.drawLine(left, top, right, top, paint)
        }
    }

    private fun drawVerticalLines(canvas: Canvas, parent: RecyclerView) {
        val childCount = parent.childCount
        val itemWidth = if (childCount > 0) {
            val child = parent.getChildAt(0)
            child.width + spacing
        } else 0

        for (i in 1 until spanCount) {
            val left = (parent.paddingLeft + i * itemWidth - spacing / 2).toFloat()
            val top = parent.paddingTop.toFloat()
            val bottom = (parent.height - parent.paddingBottom).toFloat()

            // Dikey çizgi çiz
            canvas.drawLine(left, top, left, bottom, paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount
            outRect.right = (column + 1) * spacing / spanCount
            if (position < spanCount) {
                outRect.top = spacing
            }
            outRect.bottom = spacing
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing
            }
        }
    }
}