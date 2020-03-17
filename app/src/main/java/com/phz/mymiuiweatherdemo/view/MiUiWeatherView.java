package com.phz.mymiuiweatherdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import com.phz.mymiuiweatherdemo.R;
import com.phz.mymiuiweatherdemo.bean.WeatherBean;
import com.phz.mymiuiweatherdemo.util.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;


/**
 * @author:haizhuo 仿小米UI可滑动的天气控件
 */
public class MiUiWeatherView extends View {
    public static final String TAG = "weatherView";
    /*-------------自定义属性相关Begin------------*/
    /**
     * 折线、坐标、文字颜色，默认分别为
     * {0XFFFF00BF,Color.GRAY,Color.BLACK}
     */
    private int brokenLineColor;
    private int coordinateColor;
    private int textColor;
    /**
     * 背景颜色
     */
    private int backgroundColor;
    /**
     * 行距（2个坐标间距,默认为60dp）
     */
    private int lineInterval;
    /**
     * 字体大小(默认10sp)
     */
    private float textSize;
    /**
     * 折线点的半径(默认2.5dp的像素)
     */
    private float pointRadius;
    /*-------------自定义属性相关End------------*/

    private int viewWidth;
    private int viewHeight;
    private int screenWidth;
    private int screenHeight;

    /**
     * 控件最低点高度
     * 与行距（lineInterval）相等，默认为60
     */
    private int minPointHeight;

    /**
     * 控件的最低高度
     * 默认控件最低点高度（minPointHeight）*3
     */
    private int minViewHeight;

    /**
     * 折线单位高度差
     */
    private float unitPointGap;

    /**
     * 天气图标高宽（默认行距的1/3）
     */
    private float iconWH;

    /**
     * 折线图四周留出来的偏移量
     * 默认为控件最低点高度的1/2
     */
    private int defaultPadding;

    /**
     * 折线画笔
     */
    private Paint brokenLinePaint;

    /**
     * 坐标画笔
     */
    private Paint coordinatePaint;

    /**
     * 文字画笔
     */
    private Paint textPaint;

    /**
     * 圆点画笔
     */
    private Paint circlePaint;

    /**
     * 最高温
     */
    private int maxTemperature;

    /**
     * 最低温
     */
    private int minTemperature;

    /**
     * 天气图标集合
     */
    private Map<String, Bitmap> weatherIconMap = new HashMap<>();

    /**
     * 天气数组
     */
    private String[] stringsWeather;

    /**
     * 元数据集合列表
     */
    private List<WeatherBean> dataList = new ArrayList<>();

    /**
     * 对元数据中天气分组后的集合列表
     * first值为连续相同天气的数量，second值为对应天气
     */
    private List<Pair<Integer, String>> weatherDataList = new ArrayList<>();

    /**
     * 虚线的x坐标集合列表
     */
    private List<Float> dottedList = new ArrayList<>();

    /**
     * 折线拐点集合列表
     */
    private List<PointF> pointFList = new ArrayList<>();

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

    public MiUiWeatherView(Context context) {
        this(context, null);
    }

    public MiUiWeatherView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MiUiWeatherView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //关闭硬件加速，绘制虚线需要
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        viewConfiguration=ViewConfiguration.get(context);
        scroller=new Scroller(context);
        //设置默认值，如果没有通过xml设置则使用默认
        backgroundColor=Color.WHITE;
        brokenLineColor=0XFFFF00BF;
        coordinateColor= Color.GRAY;
        lineInterval= UIUtil.dp2px(60);
        pointRadius=UIUtil.dp2pxF(2.5f);
        textColor=Color.BLACK;
        textSize=UIUtil.dp2pxF(10);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MiUiWeatherView);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.MiUiWeatherView_background_color:
                    backgroundColor = typedArray.getColor(R.styleable.MiUiWeatherView_background_color, Color.WHITE);
                    break;
                case R.styleable.MiUiWeatherView_broken_line_color:
                    brokenLineColor = typedArray.getColor(R.styleable.MiUiWeatherView_broken_line_color, 0XFFFF00BF);
                    break;
                case R.styleable.MiUiWeatherView_coordinate_color:
                    coordinateColor = typedArray.getColor(R.styleable.MiUiWeatherView_coordinate_color, Color.GRAY);
                    break;
                case R.styleable.MiUiWeatherView_line_interval:
                    lineInterval = (int) typedArray.getDimension(R.styleable.MiUiWeatherView_line_interval, UIUtil.dp2pxF(60));
                    break;
                case R.styleable.MiUiWeatherView_point_radius:
                    pointRadius = typedArray.getDimension(R.styleable.MiUiWeatherView_point_radius, UIUtil.dp2pxF(2.5f));
                    break;
                case R.styleable.MiUiWeatherView_text_color:
                    textColor = typedArray.getColor(R.styleable.MiUiWeatherView_text_color, Color.BLACK);
                    break;
                case R.styleable.MiUiWeatherView_text_size:
                    textSize = typedArray.getDimension(R.styleable.MiUiWeatherView_text_size, UIUtil.dp2pxF(10));
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
        setBackgroundColor(backgroundColor);
        //最低点高度和x轴单位长度一致
        minPointHeight = lineInterval;

        //初始化一些高宽度数据
        initSize();
        //初始化画笔
        brokenLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        brokenLinePaint.setColor(brokenLineColor);
        brokenLinePaint.setStrokeWidth(UIUtil.dp2pxF(1f));
        brokenLinePaint.setStyle(Paint.Style.STROKE);
        coordinatePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        coordinatePaint.setColor(coordinateColor);
        coordinatePaint.setStrokeWidth(UIUtil.dp2pxF(1f));
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //初始化天气图标集合
        weatherDataList.clear();
        stringsWeather = getResources().getStringArray(R.array.weatherData);
        for (int i = 0; i < stringsWeather.length; i++) {
            Bitmap bitmap = getWeatherIcon(stringsWeather[i], iconWH, iconWH);
            weatherIconMap.put(stringsWeather[i], bitmap);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //确定高度
        if (heightMode == MeasureSpec.EXACTLY) {
            //特定大小，不管他多大
            viewHeight = Math.max(heightSize, minViewHeight);
        } else {
            viewHeight = minViewHeight;
        }

        //确定宽度
        int totalWidth = 0;
        if (dataList.size() > 1) {
            totalWidth = 2 * defaultPadding + lineInterval * (dataList.size() - 1);
        }
        viewWidth = Math.max(screenWidth, totalWidth);
        calculateBrokenLineGap();
        Log.d(TAG, "viewHeight = " + viewHeight + ";viewWidth = " + viewWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dataList.isEmpty()) {
            return;
        }
        //花时间轴
        drawAxis(canvas);
        //画折线和拐点的圆
        drawLinesAndPoints(canvas);
        //画温度描述
        drawTemperatureDescription(canvas);
        //画不同天气之间的虚线
        drawDottedLine(canvas);
        //画天气图标和他下方文字
        drawWeatherIcon(canvas);
    }


    private float x,lastX;

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
                lastX = x =event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                x=event.getX();
                int deltaX = (int) (lastX - x);
                //越界恢复
                if (getScrollX() + deltaX < 0) {
                    scrollTo(0, 0);
                    return true;
                } else if (getScrollX() + deltaX > viewWidth - screenWidth) {
                    scrollTo(viewWidth - screenWidth, 0);
                    return true;
                }
                scrollBy(deltaX, 0);
                lastX = x;
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

    /**
     * 公开方法，用于设置元数据
     *
     * @param data
     */
    public void setData(List<WeatherBean> data) {
        if (data == null || data.size() < 2) {
            return;
        }
        this.dataList = data;
        //数据清理
        weatherDataList.clear();
        dottedList.clear();
        pointFList.clear();
        //初始化分组好的天气列表
        initWeatherDataList();
        //todo other things
        invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initSize();
        calculateBrokenLineGap();
    }

    /**
     * 根据元数据中天气类型做分组
     * pair中的first值为连续相同天气的数量，second值为对应天气
     */
    private void initWeatherDataList() {
        String lastWeather = "";
        int count = 0;
        for (int i = 0; i < dataList.size(); i++) {
            WeatherBean weatherBean = dataList.get(i);
            if (i == 0) {
                lastWeather = weatherBean.getWeather();
            }
            if (!weatherBean.getWeather().equals(lastWeather)) {
                Pair<Integer, String> pair = new Pair<>(count, lastWeather);
                weatherDataList.add(pair);
                count = 1;
                lastWeather = weatherBean.getWeather();
            } else {
                count++;
            }

            //到最后一个，添加一次Pair
            if (i == dataList.size() - 1 && count != 1) {
                Pair<Integer, String> pair = new Pair<>(count, lastWeather);
                weatherDataList.add(pair);
            }
        }
    }

    /**
     * 初始化默认数据
     */
    private void initSize() {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        minViewHeight = 3 * minPointHeight;
        defaultPadding = (int) (0.5 * minPointHeight);
        iconWH = (1.0f / 3.0f) * lineInterval;
    }

    /**
     * 获取天气图标
     *
     * @param weather 天气
     * @param requestW 要求宽度
     * @param requestH 要求高度
     * @return 天气图标bitmap
     */
    private Bitmap getWeatherIcon(String weather, float requestW, float requestH) {
        int resId = getIconResId(weather);
        Bitmap bmp;
        int outWidth, outHeight;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, options);
        outWidth = options.outWidth;
        outHeight = options.outHeight;
        options.inSampleSize = 1;
        if (outWidth > requestW || outHeight > requestH) {
            int ratioW = Math.round(outWidth / requestW);
            int ratioH = Math.round(outHeight / requestH);
            options.inSampleSize = Math.max(ratioW, ratioH);
        }
        options.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeResource(getResources(), resId, options);
        return bmp;
    }


    private int getIconResId(String weather) {
        int resId;
        switch (weather) {
            case WeatherBean.SUN:
                resId = R.drawable.sun;
                break;
            case WeatherBean.CLOUDY:
                resId = R.drawable.cloudy;
                break;
            case WeatherBean.RAIN:
                resId = R.drawable.rain;
                break;
            case WeatherBean.SNOW:
                resId = R.drawable.snow;
                break;
            case WeatherBean.OVERCAST:
                resId = R.drawable.overcast;
                break;
            case WeatherBean.THUNDER:
                resId = R.drawable.thunder;
            default:
                //默认是晴天
                resId = R.drawable.sun;
                break;
        }
        return resId;
    }

    /**
     * 计算折线单位高度差
     */
    private void calculateBrokenLineGap() {
        int lastMaxTem = -Integer.MAX_VALUE;
        int lastMinTem = Integer.MAX_VALUE;
        for (WeatherBean weatherBean : dataList) {
            if (weatherBean.getTemperature() > lastMaxTem) {
                maxTemperature = weatherBean.getTemperature();
                lastMaxTem = weatherBean.getTemperature();
            }
            if (weatherBean.getTemperature() < lastMinTem) {
                minTemperature = weatherBean.getTemperature();
                lastMinTem = weatherBean.getTemperature();
            }
            float gap = (maxTemperature - minTemperature) * 1.0f;
            if (gap == 0.0f) {
                //确保不为0
                gap = 1.0f;
            }
            //计算单位高度差
            unitPointGap = (viewHeight - minPointHeight - 2 * defaultPadding) / gap;
            Log.d(TAG, "unitPointGap = " + unitPointGap);
        }
    }

    /**
     * 花轴线
     *
     * @param canvas
     */
    private void drawAxis(Canvas canvas) {
        canvas.save();

        canvas.drawLine(defaultPadding, viewHeight - defaultPadding,
                viewWidth - defaultPadding, viewHeight - defaultPadding, coordinatePaint);

        float centerX;
        float centerY = viewHeight - defaultPadding + UIUtil.dp2pxF(15f);
        for (int i = 0; i < dataList.size(); i++) {
            String text = dataList.get(i).getTime();
            centerX = defaultPadding + i * lineInterval;
            Paint.FontMetrics m = textPaint.getFontMetrics();
            canvas.drawText(text, 0, text.length(), centerX, centerY - (m.ascent + m.descent) / 2, textPaint);
        }
        canvas.restore();
    }

    /**
     * 画折线和拐点的圆
     */
    private void drawLinesAndPoints(Canvas canvas) {
        canvas.save();
        float centerX;
        float centerY;
        //最低点高度
        int baseHeight = minPointHeight + defaultPadding;
        pointFList.clear();
        //用路径画出折线
        Path brokenLinePath = new Path();
        for (int i = 0; i < dataList.size(); i++) {
            int temperature = dataList.get(i).getTemperature();
            temperature = temperature - minTemperature;
            centerY = viewHeight - (baseHeight + temperature * unitPointGap);
            centerX = defaultPadding + i * lineInterval;
            pointFList.add(new PointF(centerX, centerY));
            if (i == 0) {
                brokenLinePath.moveTo(centerX, centerY);
            } else {
                brokenLinePath.lineTo(centerX, centerY);
            }
        }
        canvas.drawPath(brokenLinePath, brokenLinePaint);
        //画圆
        float x, y;
        for (int i = 0; i < pointFList.size(); i++) {
            x = pointFList.get(i).x;
            y = pointFList.get(i).y;
            //用背景色在拐点处画实心圆
            circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            circlePaint.setColor(backgroundColor);
            canvas.drawCircle(x, y, pointRadius + UIUtil.dp2pxF(1f), circlePaint);
            //用折线颜色在拐点处画空心圆
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setColor(brokenLineColor);
            canvas.drawCircle(x, y, pointRadius, circlePaint);
        }
        canvas.restore();
    }

    /**
     * 画温度描述
     */
    private void drawTemperatureDescription(Canvas canvas) {
        canvas.save();
        float centerX;
        float centerY;
        String text;

        textPaint.setTextSize(textSize * 1.2f);
        for (int i = 0; i < pointFList.size(); i++) {
            text = dataList.get(i).getTemperatureDescription();
            centerX = pointFList.get(i).x;
            centerY = pointFList.get(i).y - UIUtil.dp2pxF(13f);
            //FontMetrics使用
            Paint.FontMetrics m = textPaint.getFontMetrics();
            canvas.drawText(text, centerX, centerY - (m.ascent + m.descent) / 2, textPaint);
        }
        textPaint.setTextSize(textSize);
        canvas.restore();
    }

    /**
     * 画不同天气之间的虚线
     */
    private void drawDottedLine(Canvas canvas) {
        canvas.save();

        float startX, startY, endX, endY;
        coordinatePaint.setStrokeWidth(UIUtil.dp2pxF(0.5f));
        coordinatePaint.setAlpha(0xcc);

        endY = viewHeight - defaultPadding;

        //设置画笔画出虚线 PathEffect使用
        //两个值分别为循环的实线长度、空白长度
        float[] f = {UIUtil.dp2pxF(5f), UIUtil.dp2pxF(2f)};
        PathEffect pathEffect = new DashPathEffect(f, 0);
        coordinatePaint.setPathEffect(pathEffect);

        //第一个坐标虚线需要手动画上，因为weatherDataList里没有0坐标的
        canvas.drawLine(defaultPadding, pointFList.get(0).y + UIUtil.dp2pxF(pointRadius + 1), defaultPadding, endY, coordinatePaint);
        dottedList.add((float) defaultPadding);

        int interval = 0;
        for (int i = 0; i < weatherDataList.size(); i++) {
            interval += weatherDataList.get(i).first;
            //考虑到数据过度屏幕显示不下
            if (interval > pointFList.size() - 1) {
                interval = pointFList.size() - 1;
            }
            startX = endX = defaultPadding + interval * lineInterval;
            startY = pointFList.get(interval).y + UIUtil.dp2pxF(pointRadius + 1);
            dottedList.add(startX);
            canvas.drawLine(startX, startY, endX, endY, coordinatePaint);
        }
        coordinatePaint.setPathEffect(null);
        coordinatePaint.setAlpha(0xff);
        canvas.restore();
    }

    /**
     * 画天气图标和他下方文字
     */
    private void drawWeatherIcon(Canvas canvas) {
        canvas.save();
        textPaint.setTextSize(0.9f * textSize);

        //范围控制在0 ~ viewWidth-screenWidth
        int scrollX = getScrollX();
        float left, right;
        float iconX, iconY;
        float textY;     //文字的x坐标跟图标是一样的，无需额外声明
        iconY = viewHeight - (defaultPadding + minPointHeight / 2.0f);
        textY = iconY + iconWH / 2.0f + UIUtil.dp2pxF(10);
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        for (int i = 0; i < weatherDataList.size(); i++) {
            left = dottedList.get(i);
            right = dottedList.get(i + 1);
            //以下校正的情况为：两条虚线都在屏幕内或只有一条在屏幕内
            if (right - left > iconWH) {
                //经过上述校正之后左右距离还大于图标宽度
                iconX = left + (right - left) / 2.0f;
            } else {
                iconX = right;
            }


            Bitmap bitmap = weatherIconMap.get(weatherDataList.get(i).second);

            //经过上述校正之后可以得到图标和文字的绘制区域
            RectF iconRect = new RectF(iconX - iconWH / 2.0f,
                    iconY - iconWH / 2.0f,
                    iconX + iconWH / 2.0f,
                    iconY + iconWH / 2.0f);

            //画图标
            canvas.drawBitmap(bitmap, null, iconRect, null);

            //画图标下方文字
            canvas.drawText(weatherDataList.get(i).second,
                    iconX,
                    textY - (metrics.ascent + metrics.descent) / 2,
                    textPaint);
        }

        textPaint.setTextSize(textSize);
        canvas.restore();
    }
}
