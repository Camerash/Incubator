package com.camerash.incubator.view;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region.Op;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.camerash.incubator.R.color;
import com.camerash.incubator.R.styleable;

public class BoxedVertical extends View {
    private static final String TAG = BoxedVertical.class.getSimpleName();
    private static final int MAX = 100;
    private static final int MIN = 0;
    private int mMin = 0;
    private int mMax = 100;
    private int mStep = 10;
    private int mCornerRadius = 10;
    private float mTextSize = 26.0F;
    private int mtextBottomPadding = 20;
    private int mPoints;
    private boolean mEnabled = true;
    private boolean mtextEnabled = true;
    private boolean mImageEnabled = false;
    private boolean mTouchDisabled = true;
    private float mProgressSweep = 0.0F;
    private Paint mProgressPaint;
    private Paint mTextPaint;
    private int scrWidth;
    private int scrHeight;
    private BoxedVertical.OnValuesChangeListener mOnValuesChangeListener;
    private int backgroundColor;
    private int mDefaultValue;
    private Bitmap mDefaultImage;
    private Bitmap mMinImage;
    private Bitmap mMaxImage;
    private Rect dRect = new Rect();
    private boolean firstRun = true;

    public BoxedVertical(Context context) {
        super(context);
        this.init(context, null);
    }

    public BoxedVertical(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        System.out.println("INIT");
        float density = this.getResources().getDisplayMetrics().density;
        int progressColor = ContextCompat.getColor(context, color.color_progress);
        this.backgroundColor = ContextCompat.getColor(context, color.color_background);
        this.backgroundColor = ContextCompat.getColor(context, color.color_background);
        int textColor = ContextCompat.getColor(context, color.color_text);
        this.mTextSize = (float)((int)(this.mTextSize * density));
        this.mDefaultValue = this.mMax / 2;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, styleable.BoxedVertical, 0, 0);
            this.mPoints = a.getInteger(styleable.BoxedVertical_points, this.mPoints);
            this.mMax = a.getInteger(styleable.BoxedVertical_max, this.mMax);
            this.mMin = a.getInteger(styleable.BoxedVertical_min, this.mMin);
            this.mStep = a.getInteger(styleable.BoxedVertical_step, this.mStep);
            this.mDefaultValue = a.getInteger(styleable.BoxedVertical_defaultValue, this.mDefaultValue);
            this.mCornerRadius = a.getInteger(styleable.BoxedVertical_libCornerRadius, this.mCornerRadius);
            this.mtextBottomPadding = a.getInteger(styleable.BoxedVertical_textBottomPadding, this.mtextBottomPadding);
            this.mImageEnabled = a.getBoolean(styleable.BoxedVertical_imageEnabled, this.mImageEnabled);
            if (this.mImageEnabled) {
                this.mDefaultImage = ((BitmapDrawable)a.getDrawable(styleable.BoxedVertical_defaultImage)).getBitmap();
                this.mMinImage = ((BitmapDrawable)a.getDrawable(styleable.BoxedVertical_minImage)).getBitmap();
                this.mMaxImage = ((BitmapDrawable)a.getDrawable(styleable.BoxedVertical_maxImage)).getBitmap();
            }

            progressColor = a.getColor(styleable.BoxedVertical_progressColor, progressColor);
            this.backgroundColor = a.getColor(styleable.BoxedVertical_backgroundColor, this.backgroundColor);
            this.mTextSize = (float)((int)a.getDimension(styleable.BoxedVertical_textSize, this.mTextSize));
            textColor = a.getColor(styleable.BoxedVertical_textColor, textColor);
            this.mEnabled = a.getBoolean(styleable.BoxedVertical_enabled, this.mEnabled);
            this.mTouchDisabled = a.getBoolean(styleable.BoxedVertical_touchDisabled, this.mTouchDisabled);
            this.mtextEnabled = a.getBoolean(styleable.BoxedVertical_textEnabled, this.mtextEnabled);
            this.mPoints = this.mDefaultValue;
            a.recycle();
        }

        this.mPoints = this.mPoints > this.mMax ? this.mMax : this.mPoints;
        this.mPoints = this.mPoints < this.mMin ? this.mMin : this.mPoints;
        this.mProgressPaint = new Paint();
        this.mProgressPaint.setColor(progressColor);
        this.mProgressPaint.setAntiAlias(true);
        this.mProgressPaint.setStyle(Style.STROKE);
        this.mTextPaint = new Paint();
        this.mTextPaint.setColor(textColor);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setStyle(Style.FILL);
        this.mTextPaint.setTextSize(this.mTextSize);
        this.scrHeight = context.getResources().getDisplayMetrics().heightPixels;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.scrWidth = getDefaultSize(this.getSuggestedMinimumWidth(), widthMeasureSpec);
        this.scrHeight = getDefaultSize(this.getSuggestedMinimumHeight(), heightMeasureSpec);
        this.mProgressPaint.setStrokeWidth((float)this.scrWidth);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAlpha(255);
        canvas.translate(0.0F, 0.0F);
        Path mPath = new Path();
        mPath.addRoundRect(new RectF(0.0F, 0.0F, (float)this.scrWidth, (float)this.scrHeight), (float)this.mCornerRadius, (float)this.mCornerRadius, Direction.CCW);
        canvas.clipPath(mPath, Op.INTERSECT);
        paint.setColor(this.backgroundColor);
        paint.setAntiAlias(true);
        canvas.drawRect(0.0F, 0.0F, (float)this.scrWidth, (float)this.scrHeight, paint);
        canvas.drawLine((float)(canvas.getWidth() / 2), (float)canvas.getHeight(), (float)(canvas.getWidth() / 2), this.mProgressSweep, this.mProgressPaint);
        if (this.mImageEnabled && this.mDefaultImage != null && this.mMinImage != null && this.mMaxImage != null) {
            if (this.mPoints == this.mMax) {
                this.drawIcon(this.mMaxImage, canvas);
            } else if (this.mPoints == this.mMin) {
                this.drawIcon(this.mMinImage, canvas);
            } else {
                this.drawIcon(this.mDefaultImage, canvas);
            }
        } else if (this.mtextEnabled) {
            String strPoint = String.valueOf(this.mPoints);
            this.drawText(canvas, this.mTextPaint, strPoint);
        }

        if (this.firstRun) {
            this.firstRun = false;
            this.setValue(this.mPoints);
        }

    }

    private void drawText(Canvas canvas, Paint paint, String text) {
        canvas.getClipBounds(this.dRect);
        int cWidth = this.dRect.width();
        paint.setTextAlign(Align.LEFT);
        paint.getTextBounds(text, 0, text.length(), this.dRect);
        float x = (float)cWidth / 2.0F - (float)this.dRect.width() / 2.0F - (float)this.dRect.left;
        canvas.drawText(text, x, (float)(canvas.getHeight() - this.mtextBottomPadding), paint);
    }

    private void drawIcon(Bitmap bitmap, Canvas canvas) {
        bitmap = this.getResizedBitmap(bitmap, canvas.getWidth() / 2, canvas.getWidth() / 2);
        canvas.drawBitmap(bitmap, (Rect)null, new RectF((float)(canvas.getWidth() / 2 - bitmap.getWidth() / 2), (float)(canvas.getHeight() - bitmap.getHeight()), (float)(canvas.getWidth() / 3 + bitmap.getWidth()), (float)canvas.getHeight()), (Paint)null);
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = (float)newWidth / (float)width;
        float scaleHeight = (float)newHeight / (float)height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mEnabled) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
            switch(event.getAction()) {
                case 0:
                    if (this.mOnValuesChangeListener != null) {
                        this.mOnValuesChangeListener.onStartTrackingTouch(this);
                    }

                    if (!this.mTouchDisabled) {
                        this.updateOnTouch(event);
                    }
                    break;
                case 1:
                    if (this.mOnValuesChangeListener != null) {
                        this.mOnValuesChangeListener.onStopTrackingTouch(this);
                    }

                    this.setPressed(false);
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
                case 2:
                    this.updateOnTouch(event);
                    break;
                case 3:
                    if (this.mOnValuesChangeListener != null) {
                        this.mOnValuesChangeListener.onStopTrackingTouch(this);
                    }

                    this.setPressed(false);
                    this.getParent().requestDisallowInterceptTouchEvent(false);
            }

            return true;
        } else {
            return false;
        }
    }

    private void updateOnTouch(MotionEvent event) {
        this.setPressed(true);
        double mTouch = this.convertTouchEventPoint(event.getY());
        int progress = (int)Math.round(mTouch);
        this.updateProgress(progress);
    }

    private double convertTouchEventPoint(float yPos) {
        float wReturn;
        if (yPos > (float)(this.scrHeight * 2)) {
            wReturn = (float)(this.scrHeight * 2);
            return (double)wReturn;
        } else {
            if (yPos < 0.0F) {
                wReturn = 0.0F;
            } else {
                wReturn = yPos;
            }

            return (double)wReturn;
        }
    }

    private void updateProgress(int progress) {
        this.mProgressSweep = (float)progress;
        progress = progress > this.scrHeight ? this.scrHeight : progress;
        progress = progress < 0 ? 0 : progress;
        this.mPoints = progress * (this.mMax - this.mMin) / this.scrHeight + this.mMin;
        this.mPoints = this.mMax + this.mMin - this.mPoints;
        if (this.mPoints != this.mMax && this.mPoints != this.mMin) {
            this.mPoints = this.mPoints - this.mPoints % this.mStep + this.mMin % this.mStep;
        }

        if (this.mOnValuesChangeListener != null) {
            this.mOnValuesChangeListener.onPointsChanged(this, this.mPoints);
        }

        this.invalidate();
    }

    private void updateProgressByValue(int value) {
        this.mPoints = value;
        this.mPoints = this.mPoints > this.mMax ? this.mMax : this.mPoints;
        this.mPoints = this.mPoints < this.mMin ? this.mMin : this.mPoints;
        this.mProgressSweep = (float)((this.mPoints - this.mMin) * this.scrHeight / (this.mMax - this.mMin));
        this.mProgressSweep = (float)this.scrHeight - this.mProgressSweep;
        if (this.mOnValuesChangeListener != null) {
            this.mOnValuesChangeListener.onPointsChanged(this, this.mPoints);
        }

        this.invalidate();
    }

    public void setValue(int points) {
        points = points > this.mMax ? this.mMax : points;
        points = points < this.mMin ? this.mMin : points;
        this.updateProgressByValue(points);
    }

    public int getValue() {
        return this.mPoints;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    public int getMax() {
        return this.mMax;
    }

    public void setMax(int mMax) {
        if (mMax <= this.mMin) {
            throw new IllegalArgumentException("Max should not be less than zero");
        } else {
            this.mMax = mMax;
        }
    }

    public void setCornerRadius(int mRadius) {
        this.mCornerRadius = mRadius;
        this.invalidate();
    }

    public int getCornerRadius() {
        return this.mCornerRadius;
    }

    public int getDefaultValue() {
        return this.mDefaultValue;
    }

    public void setDefaultValue(int mDefaultValue) {
        if (mDefaultValue > this.mMax) {
            throw new IllegalArgumentException("Default value should not be bigger than max value.");
        } else {
            this.mDefaultValue = mDefaultValue;
        }
    }

    public int getStep() {
        return this.mStep;
    }

    public void setStep(int step) {
        this.mStep = step;
    }

    public boolean isImageEnabled() {
        return this.mImageEnabled;
    }

    public void setImageEnabled(boolean mImageEnabled) {
        this.mImageEnabled = mImageEnabled;
    }

    public void setOnBoxedPointsChangeListener(BoxedVertical.OnValuesChangeListener onValuesChangeListener) {
        this.mOnValuesChangeListener = onValuesChangeListener;
    }

    public interface OnValuesChangeListener {
        void onPointsChanged(BoxedVertical var1, int var2);

        void onStartTrackingTouch(BoxedVertical var1);

        void onStopTrackingTouch(BoxedVertical var1);
    }

    public void setProgressColor(int color) {
        this.mProgressPaint.setColor(color);
        invalidate();
    }
}
