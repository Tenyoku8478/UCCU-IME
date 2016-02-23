package idv.tenyoku.uccuime.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.RelativeLayout;
import android.widget.TextView;

import idv.tenyoku.uccuime.R;

/**
 * Created by tenyoku on 2015/10/28.
 */
public class VLEmojiPultrusionView extends RelativeLayout {
    private TextView emoji;

    private String frontText = "", middleText = "", rearText = "";
    private int repeat = 0;
    private float frontTextWidth, middleTextWidth, rearTextWidth;

    public VLEmojiPultrusionView(Context context) {
        super(context);
        init();
    }

    public VLEmojiPultrusionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VLEmojiPultrusionView);
        setFrontText(typedArray.getString(R.styleable.VLEmojiPultrusionView_frontText));
        setMiddleText(typedArray.getString(R.styleable.VLEmojiPultrusionView_middleText));
        setRearText(typedArray.getString(R.styleable.VLEmojiPultrusionView_rearText));
        typedArray.recycle();
    }

    private void init() {
        inflate(getContext(), R.layout.vl_emoji_pultrusion, this);
        emoji = (TextView)findViewById(R.id.emoji);
        emoji.setText(getEmojiString());
    }

    public void setRepeat(int number) {
        if (number < 0) number = 0;
        repeat = number;
        emoji.setText(getEmojiString());
        invalidate();
    }

    public void setFrontText(String text) {
        if (text==null) text = "";
        frontText = text;
        frontTextWidth = getTextWidth(text);
        emoji.setText(getEmojiString());
        invalidate();
    }

    public void setMiddleText(String text) {
        if (text==null) text = "";
        middleText = text;
        middleTextWidth = getTextWidth(text);
        emoji.setText(getEmojiString());
        invalidate();
    }

    public void setRearText(String text) {
        if (text==null) text = "";
        rearText = text;
        rearTextWidth = getTextWidth(text);
        emoji.setText(getEmojiString());
        invalidate();
    }

    private float getTextWidth(String text) {
        Paint textPaint = emoji.getPaint();
        return textPaint.measureText(text);
    }

    public String getEmojiString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(frontText);
        for(int i=0; i<repeat; ++i) {
            stringBuilder.append(middleText);
        }
        stringBuilder.append(rearText);
        return stringBuilder.toString();
    }

    public void stopGrowth() {
        if (onLongPress!=null) {
            mHandlerTime.removeCallbacks(onLongPress);
            onLongPress = null;
            emoji.setScaleX(1);
            invalidate();
        }
    }

    private float countWidth(int repeat) {
        return emoji.getPaddingLeft()+emoji.getPaddingRight()+frontTextWidth+middleTextWidth*repeat+rearTextWidth;
    }

    private float applyScale(float scale) {
        while(countWidth(repeat)*(scale-1) >= middleTextWidth) {
            scale = scale * countWidth(repeat) / countWidth(repeat+1);
            repeat++;
        }
        while(scale <= 0.8 && repeat > 0) {
            scale = scale * countWidth(repeat) / countWidth(repeat-1);
            repeat--;
        }
        emoji.setText(getEmojiString());
        emoji.setScaleX(scale);
        invalidate();
        return scale;
    }

    /* Gestures */
    private Handler mHandlerTime = new Handler();
    private Runnable onLongPress;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            stopGrowth();
        }
        return true;
    }

    private GestureDetector gestureDetector =
        new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            private final float deltaX = 10f;
            private final int FPS = 30;
            private final int bombDelay = 500;
            private final int regrowDelay = 500;
            @Override
            public void onLongPress(MotionEvent e) {
                onLongPress = new Runnable() {
                    private float prevScale = 1;
                    private int fullCount = 0;
                    @Override
                    public void run() {
                        float scale = prevScale + deltaX/countWidth(repeat);
                        if (scale * countWidth(repeat) > getWidth()) {
                            if (fullCount++ >= bombDelay/FPS) {
                                fullCount = 0;
                                repeat = 0;
                                scale = 1;
                                applyScale(scale);
                                mHandlerTime.postDelayed(this, regrowDelay);
                                return;
                            }
                            else {
                                scale = getWidth() / countWidth(repeat);
                            }
                        }
                        prevScale = applyScale(scale);
                        mHandlerTime.postDelayed(this, 1000 / FPS);
                    }
                };
                mHandlerTime.post(onLongPress);
            }
        });
}
