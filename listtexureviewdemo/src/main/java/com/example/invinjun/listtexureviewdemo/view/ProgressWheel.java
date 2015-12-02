package com.example.invinjun.listtexureviewdemo.view;





import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.example.invinjun.listtexureviewdemo.utils.FileUtil;


/**
 * An indicator of progress, similar to Android's ProgressBar.
 * Can be used in 'spin mode' or 'increment mode'
 *
 * @author Todd Davies
 *         <p/>
 *         Licensed under the Creative Commons Attribution 3.0 license see:
 *         http://creativecommons.org/licenses/by/3.0/
 */
public class ProgressWheel extends View {
private Context context;
    //Sizes (with defaults)
    private int layout_height = 0;
    private int layout_width = 0;
    private int fullRadius = 100;
    private int circleRadius = 80;
    private int barLength = 60;
    private int barWidth = 28;
    private int rimWidth = 28;
    private int textSize = 38;
    private float contourSize = 0;

    //Padding (with defaults)
    private int paddingTop = 5;
    private int paddingBottom = 5;
    private int paddingLeft = 5;
    private int paddingRight = 5;

    //Colors (with defaults)
    private int barColor = 0xFF4DA527;
    private int contourColor = 0xFFFFFFFF;
    private int circleColor = 0x00000000;
    private int rimColor = 0xFFFFFFFF;
    private int textColor = 0xFFFFFFFF;

    //Paints
    private Paint barPaint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint rimPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint contourPaint = new Paint();

    //Rectangles
    @SuppressWarnings("unused")
    private RectF rectBounds = new RectF();
    private RectF circleBounds = new RectF();
    private RectF circleOuterContour = new RectF();
    private RectF circleInnerContour = new RectF();

    //Animation
    //The amount of pixels to move the bar by on each draw
    private int spinSpeed = 2;
    //The number of milliseconds to wait inbetween each draw
    private int delayMillis = 0;
    private Handler spinHandler = new Handler() {
        /**
         * This is the code that will increment the progress variable
         * and so spin the wheel
         */
        @Override
        public void handleMessage(Message msg) {
            invalidate();
            if (isSpinning) {
                progress += spinSpeed;
                if (progress > 360) {
                    progress = 0;
                }
                spinHandler.sendEmptyMessageDelayed(0, delayMillis);
            }
            //super.handleMessage(msg);
        }
    };
    int progress = 0;
    boolean isSpinning = false;

    //Other
    private String text = "";
    private String[] splitText = {};

    /**
     * The constructor for the ProgressWheel
     *
     * @param context
     * @param attrs
     */
    public ProgressWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        textSize=setFontSize(context);
        barWidth= setBarSize(context);
        rimWidth= setBarSize(context);
        parseAttributes(context.obtainStyledAttributes(attrs,
        		ProgressWheel));
    }
    public static final int[] ProgressWheel = {
        0x7f010000, 0x7f010001, 0x7f010002, 0x7f010003,
        0x7f010004, 0x7f010005, 0x7f010006, 0x7f010007,
        0x7f010008, 0x7f010009, 0x7f01000a, 0x7f01000b,
        0x7f01000c, 0x7f01000d
    };
    //----------------------------------
    //Setting up stuff
    //----------------------------------

    /**
     * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of the view,
     * because this method is called after measuring the dimensions of MATCH_PARENT & WRAP_CONTENT.
     * Use this dimensions to setup the bounds and paints.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Share the dimensions
        layout_width = w;
        layout_height = h;

        setupBounds();
        setupPaints();
        invalidate();
    }

    /**
     * Set the properties of the paints we're using to
     * draw the progress wheel
     */
    private void setupPaints() {
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Style.STROKE);
        barPaint.setStrokeWidth(barWidth);

        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Style.STROKE);
        rimPaint.setStrokeWidth(rimWidth);

        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.FILL);

        textPaint.setColor(textColor);
        textPaint.setFakeBoldText(true); 
        textPaint.setStyle(Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);

        contourPaint.setColor(contourColor);
        contourPaint.setAntiAlias(true);
        contourPaint.setStyle(Style.STROKE);
        contourPaint.setStrokeWidth(contourSize);
    }

    /**
     * Set the bounds of the component
     */
    private void setupBounds() {
        // Width should equal to Height, find the min value to steup the circle
        int minValue = Math.min(layout_width, layout_height);

        // Calc the Offset if needed
        int xOffset = layout_width - minValue;
        int yOffset = layout_height - minValue;

        // Add the offset
        paddingTop = this.getPaddingTop() + (yOffset / 2);
        paddingBottom = this.getPaddingBottom() + (yOffset / 2);
        paddingLeft = this.getPaddingLeft() + (xOffset / 2);
        paddingRight = this.getPaddingRight() + (xOffset / 2);

        rectBounds = new RectF(paddingLeft,
                paddingTop,
                this.getLayoutParams().width - paddingRight,
                this.getLayoutParams().height - paddingBottom);

        circleBounds = new RectF(paddingLeft + barWidth,
                paddingTop + barWidth,
                this.getLayoutParams().width - paddingRight - barWidth,
                this.getLayoutParams().height - paddingBottom - barWidth);
        circleInnerContour = new RectF(circleBounds.left + (rimWidth / 2.0f) + (contourSize / 2.0f), circleBounds.top + (rimWidth / 2.0f) + (contourSize / 2.0f), circleBounds.right - (rimWidth / 2.0f) - (contourSize / 2.0f), circleBounds.bottom - (rimWidth / 2.0f) - (contourSize / 2.0f));
        circleOuterContour = new RectF(circleBounds.left - (rimWidth / 2.0f) - (contourSize / 2.0f), circleBounds.top - (rimWidth / 2.0f) - (contourSize / 2.0f), circleBounds.right + (rimWidth / 2.0f) + (contourSize / 2.0f), circleBounds.bottom + (rimWidth / 2.0f) + (contourSize / 2.0f));

        fullRadius = (this.getLayoutParams().width - paddingRight - barWidth) / 2;
        circleRadius = (fullRadius - barWidth) + 1;
    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param a the attributes to parse
     */
    private void parseAttributes(TypedArray a) {
        barWidth = (int) a.getDimension(FileUtil.getStyleableResIDByName(context, "ProgressWheel_barWidth"),
                barWidth);

        rimWidth = (int) a.getDimension(FileUtil.getStyleableResIDByName(context, "ProgressWheel_rimWidth"),
                rimWidth);

        spinSpeed = (int) a.getDimension(FileUtil.getStyleableResIDByName(context, "ProgressWheel_spinSpeed"),
                spinSpeed);

        delayMillis = a.getInteger(FileUtil.getStyleableResIDByName(context, "ProgressWheel_delayMillis"),
                delayMillis);
        if (delayMillis < 0) {
            delayMillis = 0;
        }

        barColor = a.getColor(FileUtil.getStyleableResIDByName(context, "ProgressWheel_barColor"), barColor);

        barLength = (int) a.getDimension(FileUtil.getStyleableResIDByName(context, "ProgressWheel_barLength"),
                barLength);

        textSize = (int) a.getDimension(FileUtil.getStyleableResIDByName(context, "ProgressWheel_textSize"),
                textSize);

        textColor = a.getColor(FileUtil.getStyleableResIDByName(context, "ProgressWheel_textColor"),
                textColor);

        //if the text is empty , so ignore it
        if (a.hasValue(FileUtil.getStyleableResIDByName(context, "ProgressWheel_text"))) {
            setText(a.getString(FileUtil.getStyleableResIDByName(context, "ProgressWheel_text")));
        }

        rimColor = a.getColor(FileUtil.getStyleableResIDByName(context, "ProgressWheel_rimColor"),
                rimColor);

        circleColor = a.getColor(FileUtil.getStyleableResIDByName(context, "ProgressWheel_circleColor"),
                circleColor);

        contourColor = a.getColor(FileUtil.getStyleableResIDByName(context, "ProgressWheel_contourColor"), contourColor);
        contourSize = a.getDimension(FileUtil.getStyleableResIDByName(context, "ProgressWheel_contourSize"), contourSize);


        // Recycle
        a.recycle();
    }

    //----------------------------------
    //Animation stuff
    //----------------------------------

    @Override
	protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Draw the rim
        canvas.drawArc(circleBounds, 360, 360, false, rimPaint);
        canvas.drawArc(circleOuterContour, 360, 360, false, contourPaint);
        canvas.drawArc(circleInnerContour, 360, 360, false, contourPaint);
        //Draw the bar
        if (isSpinning) {
            canvas.drawArc(circleBounds, progress - 90, barLength, false,
                    barPaint);
        } else {
            canvas.drawArc(circleBounds, -90, progress, false, barPaint);
        }
        //Draw the inner circle
        canvas.drawCircle((circleBounds.width() / 2) + rimWidth + paddingLeft,
                (circleBounds.height() / 2) + rimWidth + paddingTop,
                circleRadius,
                circlePaint);
        //Draw the text (attempts to center it horizontally and vertically)
        float textHeight = textPaint.descent() - textPaint.ascent();
        float verticalTextOffset = (textHeight / 2) - textPaint.descent();

        for (String s : splitText) {
            float horizontalTextOffset = textPaint.measureText(s) / 2;
            canvas.drawText(s, this.getWidth() / 2 - horizontalTextOffset,
                    this.getHeight() / 2 + verticalTextOffset, textPaint);
        }
    }

    /**
    *   Check if the wheel is currently spinning
    */
    
    public boolean isSpinning() {
        if(isSpinning){
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Reset the count (in increment mode)
     */
    public void resetCount(int currDuInTotal) {
        progress = 0;
        setText(currDuInTotal+"%");
        invalidate();
    }

    /**
     * Turn off spin mode
     */
    public void stopSpinning() {
        isSpinning = false;
        progress = 0;
        spinHandler.removeMessages(0);
    }


    /**
     * Puts the view on spin mode
     */
    public void spin() {
        isSpinning = true;
        spinHandler.sendEmptyMessage(0);
    }

    /**
     * Increment the progress by 1 (of 360)
     */
    public void incrementProgress(int per) {
        isSpinning = false;
        progress=per;
        if (progress > 360)
        	progress = 0;
        setText(Math.round(((float) progress / 360) * 100) + "%");
        spinHandler.sendEmptyMessage(0);
    }


    /**
     * Set the progress to a specific value
     */
    public void setProgress(int i) {
        isSpinning = false;
        progress = i;
        spinHandler.sendEmptyMessage(0);
    }

    //----------------------------------
    //Getters + setters
    //----------------------------------

    /**
     * Set the text in the progress bar
     * Doesn't invalidate the view
     *
     * @param text the text to show ('\n' constitutes a new line)
     */
    public void setText(String text) {
        this.text = text;
        splitText = this.text.split("\n");
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public int getBarLength() {
        return barLength;
    }

    public void setBarLength(int barLength) {
        this.barLength = barLength;
    }

    public int getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    @Override
	public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    @Override
	public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    @Override
	public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    @Override
	public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public int getRimColor() {
        return rimColor;
    }

    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;
    }


    public Shader getRimShader() {
        return rimPaint.getShader();
    }

    public void setRimShader(Shader shader) {
        this.rimPaint.setShader(shader);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getSpinSpeed() {
        return spinSpeed;
    }

    public void setSpinSpeed(int spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    public int getRimWidth() {
        return rimWidth;
    }

    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;
    }

    public int getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }
    private int setFontSize(Context mContext){  
        int screenWidth = 0;
        screenWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        if (screenWidth <= 240) {        // 240X320 屏幕  
              
            return 10;  
      
        }else if (screenWidth <= 320){   // 320X480 屏幕  
      
            return 15;  
      
        }else if (screenWidth <= 480){   // 480X800 或 480X854 屏幕  
      
            return 20;  
      
        }else if (screenWidth <= 540){   // 540X960 屏幕   
      
            return 25;  
              
        }else if(screenWidth <= 720){
        	
        	return 28;
        	
        }else if(screenWidth <= 800){    // 800X1280 屏幕   
      
            return 30;  
              
        }else if(screenWidth <= 1080){                          // 大于1080X1920  
      
            return 35;  
              
        }else if(screenWidth <= 1440){                          // 大于 1440X2560  
    	
    	return 38;  
    	
    }
        return 40;  
    } 
    private int setBarSize(Context mContext){  
        int screenWidth = 0;
        screenWidth = ((Activity) mContext).getWindowManager().getDefaultDisplay().getWidth();
        if (screenWidth <= 240) {        // 240X320 屏幕  
              
            return 5;  
      
        }else if (screenWidth <= 320){   // 320X480 屏幕  
      
            return 8;  
      
        }else if (screenWidth <= 480){   // 480X800 或 480X854 屏幕  
      
            return 13;  
      
        }else if (screenWidth <= 540){   // 540X960 屏幕   
      
            return 18;  
              
        }else if(screenWidth <= 720){
        	
        	return 21;
        	
        }else if(screenWidth <= 800){    // 800X1280 屏幕   
      
            return 23;  
              
        }else if(screenWidth <= 1080){                          // 大于1080X1920  
      
            return 28;  
              
        }else if(screenWidth <= 1440){                          // 大于 1440X2560  
    	
    	return 32;  
    	
    }
        return 37;  
    }
}
