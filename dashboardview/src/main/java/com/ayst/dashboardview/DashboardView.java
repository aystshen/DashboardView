/*
 * Copyright(c) 2018 Habo Shen <ayst.shen@foxmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ayst.dashboardview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class DashboardView extends View {

    // First range color
    private int mFirstRangeColor = 0xffff0000;

    // Second range color
    private int mSecondRangeColor = 0xfffffc00;

    // Third range color
    private int mThirdRangeColor = 0xff29fd2f;

    // Start angle
    private int mStartAngle = 180;

    // Sweep angle
    private int mSweepAngle = 180;

    // Min value
    private int mMin = 0;

    // Max value
    private int mMax = 100;

    // Value range (mMax-mMin) equal parts
    private int mSection = 10;

    // One mSection equal parts
    private int mPortion = 10;

    // Header text
    private String mHeaderText = "Header";

    // Value
    private int mValue = mMin;

    // Whether to display value text
    private boolean isShowValueText = true;

    // Whether to display pointer range
    private boolean isShowPointerRange = true;

    // Line stroke width
    private float mLineStrokeWidth = 2.0f;

    // Color progress stroke width
    private float mColorStrokeWidth = 10.0f;

    // Color progress padding
    private float mColorArcPadding = 10.0f;

    // Length of long scale
    private float mLongScaleLength = 18.0f;

    // Length of short scale
    private float mShortScaleLength = 15.0f;

    // Scale color
    private int mScaleColor = 0xffffffff;

    // Pointer color
    private int mPointerColor = 0xffffffff;

    // Min Pointer color
    private int mMinPointerColor = 0xff9e9e9e;

    // Max Pointer color
    private int mMaxPointerColor = 0xff9e9e9e;

    // Scale text size
    private float mScaleTextSize = 30.0f;

    // Header text color
    private int mHeaderTextColor = 0xffffffff;

    // Header text size
    private float mHeaderTextSize = 30.0f;

    // This is a percentage, starting with the minimum value, showing first color within this percentage
    private int mFirstRange = 30;

    // This is a percentage, starting with the first range, showing second color within this percentage
    private int mSecondRange = 50;

    // Pointer long radius
    private int mPLRadius;

    // Pointer short radius
    private int mPSRadius;

    // Main radius
    private int mRadius;

    private int mPadding;
    private float mCenterX, mCenterY;
    private Paint mPaint;
    private RectF mRectFColorArc;
    private RectF mRectFLineArc;
    private RectF mRectFInnerArc;
    private Path mPath;
    private Rect mRectText;
    private String[] mTexts;
    private int mMinValue = mValue;
    private int mMaxValue = mValue;

    public DashboardView(Context context) {
        this(context, null);
    }

    public DashboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DashboardView, 0, 0);

        mFirstRangeColor = a.getColor(R.styleable.DashboardView_dbv_firstColor, mFirstRangeColor);
        mSecondRangeColor = a.getColor(R.styleable.DashboardView_dbv_secondColor, mSecondRangeColor);
        mThirdRangeColor = a.getColor(R.styleable.DashboardView_dbv_thirdColor, mThirdRangeColor);
        mMax = a.getInteger(R.styleable.DashboardView_dbv_max, mMax);
        mMin = a.getInteger(R.styleable.DashboardView_dbv_min, mMin);
        mLineStrokeWidth = a.getDimension(R.styleable.DashboardView_dbv_lineWidth, mLineStrokeWidth);
        mColorStrokeWidth = a.getDimension(R.styleable.DashboardView_dbv_colorWidth, mColorStrokeWidth);
        mColorArcPadding = a.getDimension(R.styleable.DashboardView_dbv_colorPadding, mColorArcPadding);
        mLongScaleLength = a.getDimension(R.styleable.DashboardView_dbv_longScaleLength, mLongScaleLength);
        mShortScaleLength = a.getDimension(R.styleable.DashboardView_dbv_shortScaleLength, mShortScaleLength);
        mStartAngle = a.getInteger(R.styleable.DashboardView_dbv_startAngle, mStartAngle);
        mSweepAngle = a.getInteger(R.styleable.DashboardView_dbv_sweepAngle, mSweepAngle);
        mSection = a.getInteger(R.styleable.DashboardView_dbv_section, mSection);
        mPortion = a.getInteger(R.styleable.DashboardView_dbv_portion, mPortion);
        mValue = a.getInteger(R.styleable.DashboardView_dbv_value, mValue);
        isShowValueText = a.getBoolean(R.styleable.DashboardView_dbv_showValueText, isShowValueText);
        isShowPointerRange = a.getBoolean(R.styleable.DashboardView_dbv_showPointerRange, isShowPointerRange);
        mFirstRange = a.getInteger(R.styleable.DashboardView_dbv_firstRange, mFirstRange);
        mSecondRange = a.getInteger(R.styleable.DashboardView_dbv_secondRange, mSecondRange);
        mScaleColor = a.getColor(R.styleable.DashboardView_dbv_scaleColor, mScaleColor);
        mPointerColor = a.getColor(R.styleable.DashboardView_dbv_pointerColor, mPointerColor);
        mMinPointerColor = a.getColor(R.styleable.DashboardView_dbv_minPointerColor, mMinPointerColor);
        mMaxPointerColor = a.getColor(R.styleable.DashboardView_dbv_maxPointerColor, mMaxPointerColor);
        mScaleTextSize = a.getDimension(R.styleable.DashboardView_dbv_scaleTextSize, mScaleTextSize);
        mHeaderTextColor = a.getColor(R.styleable.DashboardView_dbv_headerTextColor, mHeaderTextColor);
        mHeaderTextSize = a.getDimension(R.styleable.DashboardView_dbv_headerTextSize, mHeaderTextSize);
        mHeaderText = a.getString(R.styleable.DashboardView_dbv_headerText);
        a.recycle();

        init();
    }

    private void init() {
        mPSRadius = dp2px(10);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mRectFColorArc = new RectF();
        mRectFLineArc = new RectF();
        mRectFInnerArc = new RectF();
        mPath = new Path();
        mRectText = new Rect();

        mTexts = new String[mSection + 1];
        for (int i = 0; i < mTexts.length; i++) {
            int n = (mMax - mMin) / mSection;
            mTexts[i] = String.valueOf(mMin + i * n);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * Reset padding
         */
        mPadding = Math.max(
                Math.max(getPaddingLeft(), getPaddingTop()),
                Math.max(getPaddingRight(), getPaddingBottom())
        );
        setPadding(mPadding, mPadding, mPadding, mPadding);

        /**
         * Calculated width
         */
        int width = resolveSize(dp2px(200), widthMeasureSpec);

        /**
         * Calculated radius
         */
        mRadius = (int) (width / 2 - mPadding - mLineStrokeWidth - mColorStrokeWidth - mColorArcPadding);

        /**
         * Calculated height
         */
        mPaint.setTextSize(mHeaderTextSize);
        if (isShowValueText) {
            mPaint.getTextBounds("0", 0, "0".length(), mRectText);
        } else {
            mPaint.getTextBounds("0", 0, 0, mRectText);
        }
        // Height by radius + line width + color width + color padding + Pointer short radius + value text*3
        int height = (int) (mRadius + mLineStrokeWidth + mColorStrokeWidth + mColorArcPadding + mPSRadius + mRectText.height() * 3);
        // Height by the starting angle
        float[] startPoint = getCoordinatePoint(mRadius, mStartAngle);
        // Height by the end angle
        float[] endPoint = getCoordinatePoint(mRadius, mStartAngle + mSweepAngle);
        // Take the maximum
        int maxHeight = (int) Math.max(
                height,
                Math.max(startPoint[1] + mLineStrokeWidth + mColorStrokeWidth + mColorArcPadding,
                        endPoint[1] + mLineStrokeWidth + mColorStrokeWidth + mColorArcPadding)
        );
        height = maxHeight + getPaddingTop() + getPaddingBottom();

        /**
         * Reset width and height
         */
        setMeasuredDimension(width, height);

        /**
         * Calculate center of a circle
         */
        mCenterX = mCenterY = getMeasuredWidth() / 2f;

        /**
         * Calculate color arc rect
         */
        mRectFColorArc.set(
                getPaddingLeft() + mColorStrokeWidth,
                getPaddingTop() + mColorStrokeWidth,
                getMeasuredWidth() - getPaddingRight() - mColorStrokeWidth,
                getMeasuredWidth() - getPaddingBottom() - mColorStrokeWidth
        );

        /**
         * Calculate line arc rect
         */
        mRectFLineArc.set(
                mRectFColorArc.left + mColorArcPadding + mLineStrokeWidth,
                mRectFColorArc.top + mColorArcPadding + mLineStrokeWidth,
                mRectFColorArc.right - mColorArcPadding - mLineStrokeWidth,
                mRectFColorArc.bottom - mColorArcPadding - mLineStrokeWidth
        );

        /**
         * Calculate scale text arc rect
         */
        mPaint.setTextSize(mScaleTextSize);
        mPaint.getTextBounds("0", 0, "0".length(), mRectText);
        mRectFInnerArc.set(
                mRectFLineArc.left + mLongScaleLength + mRectText.height() + dp2px(3),
                mRectFLineArc.top + mLongScaleLength + mRectText.height() + dp2px(3),
                mRectFLineArc.right - mLongScaleLength - mRectText.height() - dp2px(3),
                mRectFLineArc.bottom - mLongScaleLength - mRectText.height() - dp2px(3)
        );

        /**
         * Calculate pointer long radius
         */
        mPLRadius = (int) (mRadius - (mShortScaleLength + mRectText.height() + dp2px(5)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * Drawing third color arc
         */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mColorStrokeWidth);
        mPaint.setColor(mThirdRangeColor);
        canvas.drawArc(mRectFColorArc, mStartAngle, mSweepAngle, false, mPaint);

        /**
         * Drawing second color arc
         */
        mPaint.setColor(mSecondRangeColor);
        canvas.drawArc(mRectFColorArc, mStartAngle, mSweepAngle*(mFirstRange+mSecondRange)/100, false, mPaint);

        /**
         * Drawing first color arc
         */
        mPaint.setColor(mFirstRangeColor);
        canvas.drawArc(mRectFColorArc, mStartAngle, mSweepAngle*(mFirstRange)/100, false, mPaint);

        /**
         * Drawing line arc
         */
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mLineStrokeWidth);
        mPaint.setColor(mScaleColor);
        canvas.drawArc(mRectFLineArc, mStartAngle, mSweepAngle, false, mPaint);

        /**
         * Drawing long scale
         * Draw a scale of the starting angle and rotate the canvas
         * around the origin to draw the remaining long scale.
         */
        double cos = Math.cos(Math.toRadians(mStartAngle - 180));
        double sin = Math.sin(Math.toRadians(mStartAngle - 180));
        float x0 = (float) (mPadding + mLineStrokeWidth + mColorStrokeWidth + mColorArcPadding + mRadius * (1 - cos));
        float y0 = (float) (mPadding + mLineStrokeWidth + mColorStrokeWidth + mColorArcPadding + mRadius * (1 - sin));
        float x1 = (float) (mPadding + mLineStrokeWidth + mColorStrokeWidth + mColorArcPadding + mRadius - (mRadius - mLongScaleLength) * cos);
        float y1 = (float) (mPadding + mLineStrokeWidth + mColorStrokeWidth + mColorArcPadding + mRadius - (mRadius - mLongScaleLength) * sin);

        canvas.save();
        canvas.drawLine(x0, y0, x1, y1, mPaint);
        float angle = mSweepAngle * 1f / mSection;
        for (int i = 0; i < mSection; i++) {
            canvas.rotate(angle, mCenterX, mCenterY);
            canvas.drawLine(x0, y0, x1, y1, mPaint);
        }
        canvas.restore();

        /**
         * Drawing short scale
         * Draw a scale of the starting angle and rotate the canvas
         * around the origin to draw the remaining long scale.
         */
        canvas.save();
        mPaint.setStrokeWidth(1);
        float x2 = (float) (mPadding + mLineStrokeWidth + mColorStrokeWidth + mColorArcPadding + mRadius - (mRadius - mLongScaleLength / 2f) * cos);
        float y2 = (float) (mPadding + mLineStrokeWidth + mColorStrokeWidth + mColorArcPadding + mRadius - (mRadius - mLongScaleLength / 2f) * sin);
        canvas.drawLine(x0, y0, x2, y2, mPaint);
        angle = mSweepAngle * 1f / (mSection * mPortion);
        for (int i = 1; i < mSection * mPortion; i++) {
            canvas.rotate(angle, mCenterX, mCenterY);
            if (i % mPortion == 0) {
                // Avoid overlapping with long scales
                continue;
            }
            canvas.drawLine(x0, y0, x2, y2, mPaint);
        }
        canvas.restore();

        /**
         * Drawing scale text
         * Add an arc path, the text is drawn along the path
         */
        mPaint.setTextSize(mScaleTextSize);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mTexts.length; i++) {
            mPaint.getTextBounds(mTexts[i], 0, mTexts[i].length(), mRectText);
            /**
             * Roughly consider the width of the text as the arc length corresponding to
             * the central angle 2*θ, and use the arc length formula to get θ. The following
             * is used to correct the angle.
             */
            float θ = (float) (180 * mRectText.width() / 2 /
                    (Math.PI * (mRadius - mLongScaleLength - mRectText.height())));

            mPath.reset();
            // Positive starting angle minus θ to center the text on the long scale
            mPath.addArc(
                    mRectFInnerArc,
                    mStartAngle + i * (mSweepAngle / mSection) - θ,
                    mSweepAngle
            );
            canvas.drawTextOnPath(mTexts[i], mPath, 0, 0, mPaint);
        }

        /**
         * Drawing header text
         */
        if (!TextUtils.isEmpty(mHeaderText)) {
            mPaint.setColor(mHeaderTextColor);
            mPaint.setTextSize(mHeaderTextSize);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.getTextBounds(mHeaderText, 0, mHeaderText.length(), mRectText);
            canvas.drawText(mHeaderText, mCenterX, mCenterY / 2f + mRectText.height(), mPaint);
        }

        /**
         * Drawing pointer
         */
        drawPointer(canvas, mValue, mPointerColor);

        if (isShowPointerRange) {
            /**
             * Drawing min pointer
             */
            drawPointer(canvas, mMinValue, mMinPointerColor);

            /**
             * Drawing max pointer
             */
            drawPointer(canvas, mMaxValue, mMaxPointerColor);
        }

        /**
         * Hollow center around the pointer
         */
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(mCenterX, mCenterY, dp2px(2), mPaint);

        /**
         * Draw values text
         */
        if (isShowValueText) {
            mPaint.setTextSize(sp2px(16));
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setColor(mHeaderTextColor);
            String value = String.valueOf(mValue);
            mPaint.getTextBounds(value, 0, value.length(), mRectText);
            canvas.drawText(value, mCenterX, mCenterY + mPSRadius + mRectText.height() * 2, mPaint);
        }
    }

    private void drawPointer(Canvas canvas, int value, int color) {
        float θ = mStartAngle + mSweepAngle * (value - mMin) / (mMax - mMin); // The angle between the pointer and the horizontal line
        int d = dp2px(5); // The pointer consists of two isosceles triangles, and <d> is half the length of the common base
        mPath.reset();
        float[] p1 = getCoordinatePoint(d, θ - 90);
        mPath.moveTo(p1[0], p1[1]);
        float[] p2 = getCoordinatePoint(mPLRadius, θ);
        mPath.lineTo(p2[0], p2[1]);
        float[] p3 = getCoordinatePoint(d, θ + 90);
        mPath.lineTo(p3[0], p3[1]);
        float[] p4 = getCoordinatePoint(mPSRadius, θ - 180);
        mPath.lineTo(p4[0], p4[1]);
        mPath.close();
        mPaint.setColor(color);
        canvas.drawPath(mPath, mPaint);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

    public float[] getCoordinatePoint(int radius, float angle) {
        float[] point = new float[2];

        double arcAngle = Math.toRadians(angle); // Convert angle to radians
        if (angle < 90) {
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 90) {
            point[0] = mCenterX;
            point[1] = mCenterY + radius;
        } else if (angle > 90 && angle < 180) {
            arcAngle = Math.PI * (180 - angle) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
        } else if (angle == 180) {
            point[0] = mCenterX - radius;
            point[1] = mCenterY;
        } else if (angle > 180 && angle < 270) {
            arcAngle = Math.PI * (angle - 180) / 180.0;
            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        } else if (angle == 270) {
            point[0] = mCenterX;
            point[1] = mCenterY - radius;
        } else {
            arcAngle = Math.PI * (360 - angle) / 180.0;
            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
        }

        return point;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        if (mValue == value) {
            return;
        }

        if (value < mMin) {
            mValue = mMin;
        } else if (value > mMax) {
            mValue = mMax;
        } else {
            mValue = value;
        }

        if (mValue < mMinValue) {
            mMinValue = mValue;
        }
        if (mValue > mMaxValue) {
            mMaxValue = mValue;
        }

        postInvalidate();
    }

    public void resetValue(int value) {
        if (value < mMin) {
            mValue = mMin;
        } else if (value > mMax) {
            mValue = mMax;
        } else {
            mValue = value;
        }
        mMinValue = mValue;
        mMaxValue = mValue;

        postInvalidate();
    }

    public int getFirstRange() {
        return mFirstRange;
    }

    public void setFirstRange(int firstRange) {
        this.mFirstRange = firstRange;
        postInvalidate();
    }

    public int getSecondRange() {
        return mSecondRange;
    }

    public void setSecondRange(int secondRange) {
        this.mSecondRange = secondRange;
        postInvalidate();
    }
}
