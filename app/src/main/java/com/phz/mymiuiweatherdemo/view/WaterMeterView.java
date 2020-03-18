package com.phz.mymiuiweatherdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.phz.mymiuiweatherdemo.MyApplication;
import com.phz.mymiuiweatherdemo.R;
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
     * 渐变背景画笔
     */
    private Paint backGroundPaint;

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
     * 曲线路径
     */
    private Path curvePath;

    /**
     * 计算区域，点击范围
     */
    private RectF rectF;

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

    /**
     * 选中的那个点
     */
    private PointF pointFSelected=null;

    /**
     * 速度追踪器
     */
    private VelocityTracker velocityTracker;

    /**
     * 关于UI的标准常量
     */
    private ViewConfiguration viewConfiguration;

    /**
     * Scroller
     */
    private Scroller scroller;

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
        //计算单位高度差
        unitVerticalGap = itemWidth/unitYItem;
        //初始化画笔
        brokenLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        brokenLinePaint.setColor(colorBrokenLinePaint);
        brokenLinePaint.setStrokeWidth(UIUtil.dp2pxF(1f));
        brokenLinePaint.setStyle(Paint.Style.STROKE);
        coordinatePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coordinatePaint.setColor(colorCoordinatePaint);
        coordinatePaint.setStrokeWidth(UIUtil.dp2pxF(0.5f));
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(colorTextPaint);
        textPaint.setTextAlign(Paint.Align.CENTER);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backGroundPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        curvePath=new Path();
        rectF=new RectF();

        viewConfiguration=ViewConfiguration.get(context);
        scroller=new Scroller(context);
    }

    private float x,downX;
    private float downY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker==null){
            velocityTracker=VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (!scroller.isFinished()){
                    //没结束，手动让他结束
                    scroller.abortAnimation();
                }
                downX = x =event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                x=event.getX();
                int deltaX = (int) (downX - x);
                //越界恢复
                if (getScrollX() + deltaX < 0) {
                    scrollTo(0, 0);
                    return true;
                } else if (getScrollX() + deltaX > viewWidth - screenWidth) {
                    scrollTo(viewWidth - screenWidth, 0);
                    return true;
                }
                scrollBy(deltaX, 0);
                break;
            case MotionEvent.ACTION_UP:
                x = event.getX();
                //计算1秒内滑动过多少像素
                velocityTracker.computeCurrentVelocity(1000);
                int xVelocity = (int) velocityTracker.getXVelocity();
                if (Math.abs(xVelocity) > viewConfiguration.getScaledMinimumFlingVelocity()) {
                    //滑动速度可被判定为抛动
                    //根据挥动手势开始滚动。 行驶的距离将取决于猛击的初始速度。
                    scroller.fling(getScrollX(), 0, -xVelocity, 0, 0, viewWidth - screenWidth, 0, 0);
                    invalidate();
                }else {
                    //todo 是点击就判断pointFList中是否有满足范围的，有就invalidate
                    //偏移量
                    float tX= getTranslationX();
                    for (int i = 0; i <pointFList.size() ; i++) {
                        if (Math.abs(downX-(pointFList.get(i).x-tX))<UIUtil.dp2pxF(21)&&Math.abs(downY-pointFList.get(i).y)<UIUtil.dp2pxF(21)){
                            pointFSelected=pointFList.get(i);
                            invalidate();
                        }else {
                            pointFSelected=null;
                            invalidate();
                        }
                    }
                }
                break;
        }

        return super.onTouchEvent(event);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            //动画尚未完成
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
            totalWidth = defaultPadding + itemWidth * (list.size() - 1)+defaultPadding;
        }
        viewWidth = Math.max(screenWidth, totalWidth);

        //计算
        calculateItemYSize();
        //获取数据点集
        initPointFData();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        initDistance();
        calculateItemYSize();
        initPointFData();
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
     * 获取数据点集
     */
    private void initPointFData() {
        float centerX;
        float centerY;
        pointFList.clear();
        for (int i = 0; i <list.size() ; i++) {
            float dosage=Float.valueOf(list.get(i).getDosage());
            centerY=viewHeight-itemWidth-unitVerticalGap*dosage;
            centerX=itemWidth+itemWidth*i;
            pointFList.add(new PointF(centerX,centerY));
        }

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


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画渐变蓝色背景
        drawBackBlue(canvas);
        //画坐标
        drawAxis(canvas);
        //画曲线
        drawCurve(canvas);
        //画虚线

        //画小圆点

        //画水表详情框
    }

    /**
     * 画渐变蓝色背景
     */
    private void drawBackBlue(Canvas canvas) {
        canvas.save();
        //遍历一遍点集合，找到用量最大的点的坐标
        PointF pointFMaxCurve;
        pointFMaxCurve=pointFList.get(0);
        for (PointF p:pointFList) {
                if (p.y<pointFMaxCurve.y){
                    pointFMaxCurve=p;
                }
        }

        //设置抗锯齿
        backGroundPaint.setAntiAlias(true);
        //为Paint设置渐变
        LinearGradient linearGradient=new LinearGradient(pointFMaxCurve.x,pointFMaxCurve.y,pointFList.get(0).x,viewHeight-itemWidth,new int[]{
                0xFFE1F1FF,0xFFEFF7FF,0xFFFAFCFF},
                null, Shader.TileMode.CLAMP);
        backGroundPaint.setShader(linearGradient);
        canvas.drawPath(getCurveAndAliasPath(),backGroundPaint);
        canvas.restore();
    }

    /**
     * 画曲线
     * @param canvas
     */
    private void drawCurve(Canvas canvas) {
        canvas.save();
        curvePath.reset();

        for (int i = 0; i <pointFList.size() ; i++) {
            if (i==0){
                curvePath.moveTo(pointFList.get(i).x,pointFList.get(i).y);
            }
            if (i!=pointFList.size()-1){
                curvePath.cubicTo((pointFList.get(i).x+pointFList.get(i+1).x)/2,pointFList.get(i).y,
                        (pointFList.get(i).x+pointFList.get(i+1).x)/2,pointFList.get(i+1).y,
                        pointFList.get(i+1).x,pointFList.get(i+1).y);
            }
        }
        canvas.drawPath(curvePath,brokenLinePaint);
        canvas.restore();
    }

    /**
     * 画轴线
     *
     * @param canvas
     */
    private void drawAxis(Canvas canvas) {
        canvas.save();
            //画横轴
            for (int i = 0; i <itemYSize+1; i++) {
                canvas.drawLine(itemWidth, viewHeight - itemWidth-i*itemWidth,
                        viewWidth-UIUtil.dp2px(10), viewHeight - itemWidth-i*itemWidth, coordinatePaint);
            }

        //写月份
        float centerX;
        float centerY = viewHeight - defaultPadding + UIUtil.dp2pxF(15f);
        for (int i = 0; i < list.size(); i++) {
            String text = list.get(i).getMonth()+"月";
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

        String string= MyApplication.getInstance().getResources().getString(R.string.dosage);
        Paint.FontMetrics m = textPaint.getFontMetrics();
        canvas.drawText(string, 0, string.length(), centerXNew, itemWidth/2 - (m.ascent + m.descent) / 2, textPaint);

        canvas.restore();
    }

    /**
     * 获取曲线图路径（曲线+x轴+2竖线）
     * @return
     */
    private Path getCurveAndAliasPath(){
        curvePath.reset();
        for (int i = 0; i <pointFList.size() ; i++) {
            if (i==0){
                curvePath.moveTo(pointFList.get(i).x,pointFList.get(i).y);
            }
            if (i!=pointFList.size()-1){
                curvePath.cubicTo((pointFList.get(i).x+pointFList.get(i+1).x)/2,pointFList.get(i).y,
                        (pointFList.get(i).x+pointFList.get(i+1).x)/2,pointFList.get(i+1).y,
                        pointFList.get(i+1).x,pointFList.get(i+1).y);
            }else {
                curvePath.lineTo(pointFList.get(i).x,viewHeight-itemWidth);
                curvePath.lineTo(pointFList.get(0).x,viewHeight-itemWidth);
                curvePath.close();
            }
        }
        return curvePath;
    }

}
