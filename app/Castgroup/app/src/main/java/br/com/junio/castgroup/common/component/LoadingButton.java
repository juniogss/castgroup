package br.com.junio.castgroup.common.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import br.com.junio.castgroup.R;


public class LoadingButton extends FrameLayout {

    private MaterialButton button;
    private ProgressBar progress;
    private String text;

    public LoadingButton(@NonNull Context context) {
        super(context);
        setup(context, null);
    }

    public LoadingButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setup(context, attrs);
    }

    public LoadingButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context, attrs);
    }

    private void setup(@NotNull Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.button_loading, this, true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0, 0);
        text = typedArray.getString(R.styleable.LoadingButton_text);

        typedArray.recycle();

        button = (MaterialButton) getChildAt(0);
        button.setText(text);
        button.setEnabled(false);

        progress = (ProgressBar) getChildAt(1);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        button.setOnClickListener(l);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        button.setEnabled(enabled);
    }

    public void showProgress(boolean enabled) {
        progress.setVisibility(enabled ? VISIBLE : GONE);
        button.setText(enabled ? "" : text);
        button.setClickable(!enabled);
    }

    public void setText(String text){
        button.setText(text);
    }
}
