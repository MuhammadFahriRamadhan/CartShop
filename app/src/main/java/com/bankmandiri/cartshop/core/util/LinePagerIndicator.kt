package com.bankmandiri.cartshop.core.util

import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LinePagerIndicator : RecyclerView.ItemDecoration() {
    private val colorActive =  0xFF3E8CB9
    private val colorInactive =  0xFF8F90A6

    /**
     * Height of the space the indicator takes up at the bottom of the view.
     */
    private val mIndicatorHeight = (DP * 16).toInt()

    /**
     * Indicator stroke width.
     */
    private val mIndicatorStrokeWidth = DP * 2

    /**
     * Indicator width.
     */
    private val mIndicatorItemLength = DP * 9

    /**
     * Padding between indicators.
     */
    private val mIndicatorItemPadding = DP * 4

    /**
     * Some more natural animation interpolation
     */
    private val mInterpolator: Interpolator = AccelerateDecelerateInterpolator()
    private val mPaint: Paint = Paint()
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val itemCount: Int = parent.getAdapter()!!.getItemCount()

        // center horizontally, calculate width and subtract half from center
        val totalLength = mIndicatorItemLength * itemCount
        val paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding
        val indicatorTotalWidth = totalLength + paddingBetweenItems
        val indicatorStartX: Float = (parent.getWidth() - indicatorTotalWidth) / 2f

        // center vertically in the allotted space
        val indicatorPosY: Float = parent.getHeight() - mIndicatorHeight / 2f
        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)


        // find active page (which should be highlighted)
        val layoutManager: LinearLayoutManager = parent.getLayoutManager() as LinearLayoutManager
        val activePosition: Int = layoutManager.findFirstVisibleItemPosition()
        if (activePosition == RecyclerView.NO_POSITION) {
            return
        }

        // find offset of active page (if the user is scrolling)
        val activeChild: View? = layoutManager.findViewByPosition(activePosition)
        val left: Int = activeChild?.getLeft() ?: 0
        val width: Int = activeChild?.getWidth() ?: 0

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
        val progress: Float = mInterpolator.getInterpolation(left * -1 / width.toFloat())
        drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount)
    }

    private fun drawInactiveIndicators(
        c: Canvas,
        indicatorStartX: Float,
        indicatorPosY: Float,
        itemCount: Int
    ) {
        mPaint.color =   colorInactive.toInt()

        // width of item indicator including padding
        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
        var start = indicatorStartX
        for (i in 0 until itemCount) {
            // draw the line for every item
            c.drawLine(start, indicatorPosY, start + mIndicatorItemLength, indicatorPosY, mPaint)
            start += itemWidth
        }
    }

    private fun drawHighlights(
        c: Canvas, indicatorStartX: Float, indicatorPosY: Float,
        highlightPosition: Int, progress: Float, itemCount: Int
    ) {
        mPaint.color = colorActive.toInt()

        // width of item indicator including padding
        val itemWidth = mIndicatorItemLength + mIndicatorItemPadding
        if (progress == 0f) {
            // no swipe, draw a normal indicator
            val highlightStart = indicatorStartX + itemWidth * highlightPosition
            c.drawLine(
                highlightStart, indicatorPosY,
                highlightStart + mIndicatorItemLength, indicatorPosY, mPaint
            )
        } else {
            var highlightStart = indicatorStartX + itemWidth * highlightPosition
            // calculate partial highlight
            val partialLength = mIndicatorItemLength * progress

            // draw the cut off highlight
            c.drawLine(
                highlightStart + partialLength, indicatorPosY,
                highlightStart + mIndicatorItemLength, indicatorPosY, mPaint
            )

            // draw the highlight overlapping to the next item as well
            if (highlightPosition < itemCount - 1) {
                highlightStart += itemWidth
                c.drawLine(
                    highlightStart, indicatorPosY,
                    highlightStart + partialLength, indicatorPosY, mPaint
                )
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = mIndicatorHeight
    }

    companion object {
        private val DP: Float = Resources.getSystem().getDisplayMetrics().density
    }

    init {
        mPaint.setStrokeCap(Paint.Cap.ROUND)
        mPaint.setStrokeWidth(mIndicatorStrokeWidth)
        mPaint.setStyle(Paint.Style.STROKE)
        mPaint.setAntiAlias(true)
    }
}