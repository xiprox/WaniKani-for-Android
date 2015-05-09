package tr.xip.wanikani;

import android.content.Context;
import android.media.AudioManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import tr.xip.wanikani.app.activity.FocusWebView;
import tr.xip.wanikani.app.activity.WebReviewActivity;

/**
 * Implementation of the {@link Keyboard} interface that uses the default android keyboard.
 * This is more or less like using an external browser: the main difference being
 * that the keyboard shows up when entering the reviews page.
 */
public class NativeKeyboard implements Keyboard {

    /// Parent activity
    WebReviewActivity wav;

    /// Internal browser
    FocusWebView wv;

    /// The manager, used to popup the keyboard when needed
    InputMethodManager imm;

    /// The mute button
    ImageButton muteH;

    /**
     * Constructor
     * @param wav the parent activity
     * @param wv the internal browser
     */
    public NativeKeyboard (WebReviewActivity wav, FocusWebView wv)
    {
        this.wav = wav;
        this.wv = wv;

        imm = (InputMethodManager) wav.getSystemService (Context.INPUT_METHOD_SERVICE);

        muteH = (ImageButton) wav.findViewById (R.id.kb_mute_h);
    }

    /**
     * Called by the parent activity when the keyboard needs to be shown.
     * @param hasEnter if the keyboard contains the enter key. If unset, the hide button is shown instead
     */
    @Override
    public void show (boolean hasEnter)
    {
        wv.enableFocus ();

        imm.showSoftInput (wv, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Called when the keyboard should be iconized. This does never happen when using the
     * native keyboard, so this method does nothing
     * @param hasEnter if the keyboard contains the enter key. If unset, the hide button is shown instead
     */
    @Override
    public void iconize (boolean hasEnter)
    {
		/* empty */
    }

    /**
     * Hides the keyboard. Used also when the user chooses a different keyboard.
     */
    @Override
    public void hide ()
    {
        reset ();
    }

    /**
     * Resets the keyboard.
     */
    @Override
    public void reset ()
    {
        muteH.setVisibility (View.GONE);
    }

    /**
     * Ignore button pressed. Does nothing because it is not supported.
     */
    @Override
    public void ignore ()
    {
		/* Not supported */
    }

    /**
     * Tells if the ignore menu item may be shown.
     * @return always false, since we do not implement it in this class
     */
    @Override
    public boolean canIgnore ()
    {
        return false;
    }

    @Override
    public boolean canOverrideFonts ()
    {
        return false;
    }

    @Override
    public void overrideFonts ()
    {
		/* empty */
    }

    @Override
    public boolean getOverrideFonts ()
    {
        return false;
    }

    @Override
    public void setMute (boolean m)
    {
        AudioManager am;

        am = (AudioManager) wav.getSystemService(Context.AUDIO_SERVICE);

        am.setStreamMute (AudioManager.STREAM_MUSIC, m);
    }

}