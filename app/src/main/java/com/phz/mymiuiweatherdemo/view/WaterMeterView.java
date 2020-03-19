package com.phz.mymiuiweatherdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
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
import java.util.Calendar;
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
     * 文字画笔（x、y轴）
     */
    private Paint textPaint;

    /**
     * 文字画笔（水表详情）
     */
    private Paint textPaintDetail;

    /**
     * 渐变背景画笔
     */
    private Paint backGroundPaint;

    /**
     * 详情背景画笔
     */
    private Paint backGroundDetailPaint;

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
     * 水表详情文字大小
     * 默认10sp
     */
    private float textSizeDetail;

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
     * 默认5个
     */
    private int itemYSize=5;

    /**
     * 折线图左和下的间距，同横纵单位间隔
     * 默认：42dp
     */
    private int defaultPadding;
    private int itemWidth;

    /**
     * 控件期望高度
     * 默认为7个itemWidth
     */
    private int expectViewHeight;

    /**
     * 折线点的半径(默认2.5dp的像素)
     */
    private float pointRadius;

    private int viewWidth;
    private int viewHeight;
    private int screenWidth;
    private int screenHeight;

    /**
     * 曲线路径
     */
    private Path curvePath;

    /**
     * 水表详情背景路径
     */
    private Path WaterDetailBgPath;

    /**
     * 水表详情背景图范围
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
        textSizeDetail=UIUtil.dp2pxF(10);
        pointRadius=UIUtil.dp2pxF(2.5f);
        setBackgroundColor(backGroundColor);
        defaultPadding = itemWidth = (int)UIUtil.dp2pxF(42);
        //期望高度默认值为7个itemWidth，（itemYSize默认值+2）
        expectViewHeight=itemWidth*(7);
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;
        //计算单位高度差
        unitVerticalGap = (float) (itemWidth/unitYItem);
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
        textPaintDetail= new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintDetail.setTextSize(textSizeDetail);
        textPaintDetail.setColor(backGroundColor);
        textPaintDetail.setTextAlign(Paint.Align.CENTER);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coordinatePaint.setStrokeWidth(UIUtil.dp2pxF(0.5f));
        backGroundPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        backGroundDetailPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        backGroundDetailPaint.setColor(colorTextPaint);
        curvePath=new Path();
        WaterDetailBgPath=new Path();
        rectF=new RectF();

        viewConfiguration=ViewConfiguration.get(context);
        scroller=new Scroller(context);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
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
                downY =event.getY();
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
                    //判断点击pointFList中是否有满足范围的，有就invalidate
                    //偏移量
                    float tX= getScrollX();
                    for (int i = 0; i <pointFList.size() ; i++) {
                        if (Math.abs(downX-(pointFList.get(i).x-tX))<UIUtil.dp2pxF(21)&&Math.abs(downY-pointFList.get(i).y)<UIUtil.dp2pxF(21)){
                            pointFSelected=pointFList.get(i);
                            invalidate();
                            break;
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
        //计算
        calculateItemYSize();
        if (heightMode == MeasureSpec.EXACTLY) {
            //特定大小，不管他多大
            viewHeight = Math.max(heightSize, expectViewHeight);
        } else {
            viewHeight = expectViewHeight;
        }

        //确定宽度
        int totalWidth;
        totalWidth=itemWidth*13;
        viewWidth = Math.max(screenWidth, totalWidth);

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
     */
    public void setData(List<WaterAndElectricMeterDetail> data) {
        if (data == null) {
            return;
        }
        //数据清理
        list.clear();
        pointFList.clear();
        pointFSelected=null;

        this.list = data;
        initPointFData();
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
        //画小圆点和虚线
        drawPointsAndLine(canvas);
        //画水表详情框
        drawWaterDetailsText(canvas);
    }

    /**
     * 画水表详情框
     */
    private void drawWaterDetailsText(Canvas canvas) {
        canvas.save();
        WaterDetailBgPath.reset();
        if (pointFSelected==null){
            return;
        }
        rectF.left = screenWidth/2-UIUtil.dp2pxF(115)+getScrollX();
        rectF.right =screenWidth/2+UIUtil.dp2pxF(115)+getScrollX();
        rectF.top = UIUtil.dp2pxF(6);
        rectF.bottom=UIUtil.dp2pxF(36);
        WaterDetailBgPath.moveTo(rectF.left,rectF.top);
        WaterDetailBgPath.addRoundRect(rectF,UIUtil.dp2pxF(5),UIUtil.dp2pxF(5), Path.Direction.CW);

        //画背景
        canvas.drawPath(WaterDetailBgPath,backGroundDetailPaint);

        //写文字
        WaterAndElectricMeterDetail weData=list.get(pointFList.indexOf(pointFSelected));
        String text = "用量："+weData.getDosage()+"    "+"读数："+weData.getTotalReading()+"    "+"修正读数："+weData.getCorrection();
        Paint.FontMetrics m = textPaint.getFontMetrics();
        canvas.drawText(text, 0, text.length(), rectF.left+UIUtil.dp2pxF(115), UIUtil.dp2pxF(21) - (m.ascent + m.descent) / 2, textPaintDetail);

        canvas.restore();
    }

    /**
     * 画小圆点和虚线
     */
    private void drawPointsAndLine(Canvas canvas) {
        canvas.save();
        circlePaint.reset();
        if (pointFSelected==null){
            return;
        }
        //画圆
        float x_point = pointFSelected.x;
        float y_point = pointFSelected.y;
        //用背景色在拐点处画实心圆
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        circlePaint.setColor(backGroundColor);
        canvas.drawCircle(x_point, y_point, pointRadius + UIUtil.dp2pxF(1f), circlePaint);
        //用折线颜色在拐点处画空心圆
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(colorBrokenLinePaint);
        canvas.drawCircle(x_point, y_point, pointRadius, circlePaint);

        //画虚线
        circlePaint.setColor(0xFF999999);
        //f数组两个值分别为循环的实线长度、空白长度
        float[] f = {UIUtil.dp2pxF(5f), UIUtil.dp2pxF(2f)};
        PathEffect pathEffect = new DashPathEffect(f, 0);
        circlePaint.setPathEffect(pathEffect);

        canvas.drawLine(x_point,y_point+UIUtil.dp2pxF(pointRadius+0.5f),x_point,viewHeight-itemWidth,circlePaint);

        canvas.restore();
    }

    /**
     * 画渐变蓝色背景
     */
    private void drawBackBlue(Canvas canvas) {
        if (list.isEmpty()||pointFList.isEmpty()){
            return;
        }
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
     */
    private void drawCurve(Canvas canvas) {
        if (list.isEmpty()||pointFList.isEmpty()){
            return;
        }
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
        for (int i = 0; i < Calendar.UNDECIMBER; i++) {
            String text = (i+1)+"月";
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
     * @return 闭合曲线图路径
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

