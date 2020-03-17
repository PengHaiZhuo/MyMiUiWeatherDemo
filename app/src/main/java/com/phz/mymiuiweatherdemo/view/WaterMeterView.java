package com.phz.mymiuiweatherdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import com.phz.mymiuiweatherdemo.bean.WaterAndElectricMeterDetail;
import com.phz.mymiuiweatherdemo.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;


/**
 * @author haizhuo
 */
public class WaterMeterView extends View {

    /**
     * 折线画笔
     */
    private Paint brokenLinePaint;

    /**
     * 坐标画笔
     */
    private Paint coordinatePaint;

    /**
     * 圆点画笔
     */
    private Paint circlePaint;

    /**
     * 文字画笔
     */
    private Paint textPaint;

    /**
     * 背景颜色
     */
    private int backGroundColor= Color.WHITE;

    /**
     * 文字画笔颜色
     * 默认：黑色
     */
    private int colorTextPaint= Color.BLACK;

    /**
     * 折现画笔颜色
     * 默认：蓝色 0xFF1281FD
     */
    private int  colorBrokenLinePaint=0xFF1281FD;

    /**
     * 坐标画笔颜色
     * 默认 灰色 0xFFEEEEEE
     */
    private int colorCoordinatePaint=0xFFEEEEEE;

    /**
     * 文字大小
     * 默认11sp
     */
    private float textSize;

    /**
     * 单位高度差
     * 默认：itemWidth/unitYItem
     */
    private float unitVerticalGap;

    /**
     *  itemWidth对应的用水量（吨）
     *  默认为5
     */
    private int unitYItem=5;

    /**
     * Y方向有数据的Item个数
     */
    private int itemYSize;

    /**
     * 折线图左和下的间距，同横纵单位间隔
     * 默认：42dp
     */
    private int defaultPadding;
    private int itemWidth;

    /**
     * 控件期望高度
     */
    private int expectViewHeight;

    private int viewWidth;
    private int viewHeight;
    private int screenWidth;
    private int screenHeight;
    
    /**
     * 最多用量
     */
    private float maxDosage;

    /**
     * 最少用量
     */
    private float minDosage;

    /**
     * 数据列表data
     */
    private List<WaterAndElectricMeterDetail> list=new ArrayList<>();

    /**
     * 每个月的坐标点集
     */
    private List<PointF> pointFList=new ArrayList<>();


    public WaterMeterView(Context context) {
        this(context,null);
    }

    public WaterMeterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaterMeterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //关闭硬件加速，绘制虚线需要
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        //设置一些默认值
        textSize= UIUtil.dp2pxF(11);
        setBackgroundColor(backGroundColor);
        defaultPadding = itemWidth = (int)UIUtil.dp2pxF(42);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        //初始化画笔
        brokenLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        brokenLinePaint.setColor(colorBrokenLinePaint);
        brokenLinePaint.setStrokeWidth(UIUtil.dp2pxF(1f));
        brokenLinePaint.setStyle(Paint.Style.STROKE);
        coordinatePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coordinatePaint.setColor(colorCoordinatePaint);
        coordinatePaint.setStrokeWidth(UIUtil.dp2pxF(1f));
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(colorTextPaint);
        textPaint.setTextAlign(Paint.Align.CENTER);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculateItemYSize();
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            //特定大小，不管他多大
            viewHeight = Math.max(heightSize, expectViewHeight);
        } else {
            viewHeight = expectViewHeight;
        }

        //确定宽度
        int totalWidth = 0;
        if (list.size() > 1) {
            totalWidth = defaultPadding + itemWidth * (list.size() - 1);
        }
        viewWidth = Math.max(screenWidth, totalWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画坐标
        drawAxis(canvas);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        initDistance();
        calculateItemYSize();
    }

    /**
     * 公开方法，用于设置元数据
     *
     * @param data
     */
    public void setData(List<WaterAndElectricMeterDetail> data) {
        if (data == null || data.size() < 2) {
            return;
        }
        //数据清理
        list.clear();
        pointFList.clear();

        this.list = data;
        invalidate();
    }

    /**
     * 计算Y方向有数据item有几个
     */
    private void calculateItemYSize() {
        int lastMaxTem = -Integer.MAX_VALUE;
        int lastMinTem = Integer.MAX_VALUE;
        for (WaterAndElectricMeterDetail bean : list) {
            int dosage= Integer.parseInt(bean.getDosage());
            if (dosage > lastMaxTem) {
                maxDosage = dosage;
                lastMaxTem = dosage;
            }
            if (dosage < lastMinTem) {
                minDosage = dosage;
                lastMinTem = dosage;
            }
            //计算单位高度差
            unitVerticalGap = itemWidth/unitYItem;
            itemYSize= (int) (Math.floor(Double.valueOf(maxDosage+"")/Double.valueOf(unitYItem+""))+1.00);
            if (itemYSize<=5){
                expectViewHeight=itemWidth*(7);
            }else {
                expectViewHeight=itemWidth*(itemYSize+2);
            }

        }
    }

    /**
     * 初始化一些距离
     */
    private void initDistance() {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        defaultPadding = itemWidth = (int)UIUtil.dp2pxF(42);
    }


    /**
     * 花轴线
     *
     * @param canvas
     */
    private void drawAxis(Canvas canvas) {
        canvas.save();
            //画横轴
            for (int i = 0; i <itemYSize+1; i++) {
                canvas.drawLine(itemWidth, viewHeight - itemWidth-i*itemWidth,
                        viewWidth, viewHeight - itemWidth-i*itemWidth, coordinatePaint);
            }

        //写月份
        float centerX;
        float centerY = viewHeight - defaultPadding + UIUtil.dp2pxF(15f);
        for (int i = 0; i < list.size(); i++) {
            String text = i+"月";
            centerX = defaultPadding + i * itemWidth;
            Paint.FontMetrics m = textPaint.getFontMetrics();
            canvas.drawText(text, 0, text.length(), centerX, centerY - (m.ascent + m.descent) / 2, textPaint);
        }

        //画Y轴
        canvas.drawLine(itemWidth, itemWidth,itemWidth, viewHeight - itemWidth, coordinatePaint);
        //写用量
        float centerXNew=itemWidth/2;
        float centerYNew;
        for (int i = 0; i <itemYSize+1 ; i++) {
            String text = i*unitYItem+"";
            centerYNew = viewHeight-(defaultPadding + i * itemWidth);
            Paint.FontMetrics m = textPaint.getFontMetrics();
            canvas.drawText(text, 0, text.length(), centerXNew, centerYNew - (m.ascent + m.descent) / 2, textPaint);
        }

        canvas.restore();
    }

}
