package idv.tenyoku.uccuime.view;

/**
 * Created by tenyoku on 2015/10/28.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class AutoRepeatImageButton extends ImageButton {

    private long initialRepeatDelay = 500;
    private long repeatIntervalInMilliseconds = 100;

    private Runnable repeatClickWhileButtonHeldRunnable = new Runnable() {
        @Override
        public void run() {
            //Perform the present repetition of the click action provided by the user
            // in setOnClickListener().
            performClick();

            //Schedule the next repetitions of the click action, using a faster repeat
            // interval than the initial repeat delay interval.
            postDelayed(repeatClickWhileButtonHeldRunnable, repeatIntervalInMilliseconds);
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN)
        {
            //Just to be sure that we removed all callbacks,
            // which should have occurred in the ACTION_UP
            removeCallbacks(repeatClickWhileButtonHeldRunnable);

            //Perform the default click action.
            performClick();

            //Schedule the start of repetitions after a one half second delay.
            postDelayed(repeatClickWhileButtonHeldRunnable, initialRepeatDelay);
            setPressed(true);
        }
        else if(action == MotionEvent.ACTION_UP) {
            //Cancel any repetition in progress.
            removeCallbacks(repeatClickWhileButtonHeldRunnable);
            setPressed(false);
        }

        //Returning true here prevents performClick() from getting called
        // in the usual manner, which would be redundant, given that we are
        // already calling it above.
        return true;
    }

    public AutoRepeatImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public AutoRepeatImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoRepeatImageButton(Context context) {
        super(context);
    }
}
