package com.example.asus.netview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;

/**
 * Created by asus on 2017/8/9.
 * 蜘蛛网
 */

public class NetView extends View {

    private int mEdgeNum;//多边形的边数
    private int mLayerNum = 5;//蜘蛛网的层数（包括最外层）
    private float mRadius;//半径

    private List<NetViewData> mdataList = new ArrayList<>();

    private float mMaxValue = 100;//最大值
    private int mRegionAlpha;//阴影部分的透明度
    private int mRegionColor;//阴影部分的颜色
    private int mNetColor;//网格的颜色
    private int mPointColor;//点的颜色
    private int mTextColor;//字体的颜色
    private int mTextSize;//字体的大小


    public NetView(Context context) {
        super(context);
    }

    public NetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public NetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.NetView);

        mRegionAlpha = typeArray.getInt(R.styleable.NetView_regionAlpha, 100);
        mRegionColor = typeArray.getColor(R.styleable.NetView_regionColor, Color.YELLOW);
        mNetColor = typeArray.getColor(R.styleable.NetView_netColor, Color.BLUE);
        mPointColor = typeArray.getColor(R.styleable.NetView_pointColor, Color.YELLOW);
        mTextColor = typeArray.getColor(R.styleable.NetView_texttColor, Color.BLACK);
        mTextSize = typeArray.getDimensionPixelSize(R.styleable.NetView_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                20, getResources().getDisplayMetrics()));
        mLayerNum = typeArray.getInt(R.styleable.NetView_layerNum, 3);
        mMaxValue = typeArray.getFloat(R.styleable.NetView_maxValue, 100);

        typeArray.recycle();
    }


    //支持wrap_content
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(400, 400);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(400, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 400);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);

        mRadius = Math.min(getMeasuredWidth() / 2, getMeasuredHeight() / 2) - 100;
        mEdgeNum = mdataList.size();
//        canvas.rotate(90 - 180 / mEdgeNum);//水平放置蜘蛛网

        if (mdataList.isEmpty()) {
            return;
        }

        drawNet(canvas);
        drawText(canvas);
        drawRegion(canvas);

    }


    //绘制蜘蛛网
    private void drawNet(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mNetColor);

        for (int j = 1; j <= mLayerNum; j++) {
            for (int i = 0; i < mEdgeNum; i++) {
                Path path = new Path();
                path.lineTo((float) (1.0 / mLayerNum * j * mRadius), 0);
                path.lineTo((float) (1.0 / mLayerNum * j * mRadius * Math.cos(2 * PI / 360 * 360 / mEdgeNum)),
                        (float) (1.0 / mLayerNum * j * mRadius * Math.sin(2 * PI / 360 * 360 / mEdgeNum)));
                path.close();
                canvas.drawPath(path, paint);
                canvas.rotate(360 / mEdgeNum);
            }
        }
    }

    //绘制文字
    private void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mTextColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(mTextSize);
        for (int i = 0; i < mEdgeNum; i++) {
            float x = (float) (mRadius * Math.cos(2 * PI / 360 * 360 / mEdgeNum * i));
            float y = (float) (mRadius * Math.sin(2 * PI / 360 * 360 / mEdgeNum * i));
            Rect rect = new Rect();
            paint.getTextBounds(mdataList.get(i).getTitle(), 0, mdataList.get(i).getTitle().length(), rect);
            float width = rect.width();
            float height = rect.height();

            if (x >= 0 && y >= 0) {
                canvas.drawText(mdataList.get(i).getTitle(), x, y, paint);
            } else if (x >= 0 && y <= 0) {
                canvas.drawText(mdataList.get(i).getTitle(), x, y - height, paint);
            } else if (x <= 0 && y <= 0) {
                canvas.drawText(mdataList.get(i).getTitle(), x - width, y - height, paint);
            } else if (x <= 0 && y >= 0) {
                canvas.drawText(mdataList.get(i).getTitle(), x - width, y, paint);
            }
        }
    }


    //绘制阴影部分
    private void drawRegion(Canvas canvas) {
        Path path = new Path();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < mEdgeNum; i++) {
            float x = (float) (mdataList.get(i).getValue() / mMaxValue * mRadius * Math.cos(2 * PI / 360 * 360 / mEdgeNum * i));
            float y = (float) (mdataList.get(i).getValue() / mMaxValue * mRadius * Math.sin(2 * PI / 360 * 360 / mEdgeNum * i));

            paint.setColor(mPointColor);
            canvas.drawPoint(x, y, paint);

            paint.setColor(mRegionColor);
            paint.setAlpha(mRegionAlpha);

            if (i == 0) {
                path.moveTo(x, y);
            }
            path.lineTo(x, y);
        }
        path.close();
        canvas.drawPath(path, paint);
    }

    //设置边数，也可以不用设置，会根据传进的list长度进行绘制
    public void setEdgeNum(int mEdgeNum) {
        this.mEdgeNum = mEdgeNum;
    }
    //设置蜘蛛网的层数，默认为3层
    public void setLayerNum(int mLayerNum) {
        this.mLayerNum = mLayerNum;
    }
    //设置最大值
    public void setMaxValue(float mMaxValue) {
        this.mMaxValue = mMaxValue;
    }
    //设置区域填充颜色的透明度（0-255）
    public void setRegionAlpha(int mRegionAlpha) {
        this.mRegionAlpha = mRegionAlpha;
    }
    //设置区域填充颜色
    public void setRegionColor(int mRegionColor) {
        this.mRegionColor = mRegionColor;
    }
    //设置蜘蛛网线的颜色
    public void setNetColor(int mNetColor) {
        this.mNetColor = mNetColor;
    }
    //设置点的颜色
    public void setPointColor(int mPointColor) {
        this.mPointColor = mPointColor;
    }
    //设置标题的字体颜色
    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }
    //设置标题的字体大小
    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }
    //设置数据源
    public void setMdataList(List<NetViewData> mdataList) {
        this.mdataList = mdataList;
    }

}
