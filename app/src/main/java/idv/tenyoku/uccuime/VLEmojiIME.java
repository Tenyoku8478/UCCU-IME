package idv.tenyoku.uccuime;

import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.ImageButton;

import com.daimajia.swipe.SwipeLayout;

import idv.tenyoku.uccuime.view.AutoRepeatImageButton;
import idv.tenyoku.uccuime.view.VLEmojiPultrusionView;

/**
 * Created by tenyoku on 2015/10/28.
 */
public class VLEmojiIME extends InputMethodService {
    private SwipeLayout swipeLayout;
    private VLEmojiPultrusionView VLEmoji;
    @Override
    public View onCreateInputView() {
        super.onCreateInputView();
        swipeLayout = (SwipeLayout)getLayoutInflater().inflate(R.layout.main, null);
        VLEmoji = (VLEmojiPultrusionView)swipeLayout.findViewById(R.id.VLEmoji);
        AutoRepeatImageButton backspaceButton = (AutoRepeatImageButton)swipeLayout.findViewById(R.id.backspace);
        ImageButton enterButton = (ImageButton)swipeLayout.findViewById(R.id.enter);

        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                VLEmoji.stopGrowth();
            }

            @Override
            public void onOpen(SwipeLayout layout) {
            }

            @Override
            public void onStartClose(SwipeLayout layout) {
            }

            @Override
            public void onClose(SwipeLayout layout) {
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
            }
        });

        VLEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputConnection ic = getCurrentInputConnection();
                ic.commitText(VLEmoji.getEmojiString(), 1);
                VLEmoji.setRepeat(0);
            }
        });

        backspaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputConnection ic = getCurrentInputConnection();
                ic.deleteSurroundingText(1, 0);
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputConnection ic = getCurrentInputConnection();
                ic.performEditorAction(EditorInfo.IME_ACTION_GO);
            }
        });
        return swipeLayout;
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        swipeLayout.close();
    }
}
