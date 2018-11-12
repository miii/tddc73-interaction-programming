package com.example.jacob.tddb84project.PasswordStrengthMeter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jacob.tddb84project.PasswordStrengthMeter.algorithm.StrengthValidatorInterface;
import com.example.jacob.tddb84project.PasswordStrengthMeter.visualization.VisualizationInterface;
import com.example.jacob.tddb84project.R;

import java.util.ArrayList;
import java.util.List;

public class PasswordStrengthMeter extends LinearLayout {
    private Context context;
    private StrengthValidatorInterface validator = null;
    private List<VisualizationInterface> visualizers = new ArrayList<>();

    public PasswordStrengthMeter(Context context) {
        super(context);

        LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        setLayoutParams(params);
        setOrientation(VERTICAL);

        // Create textbox
        EditText editText = new EditText(context);
        editText.setLayoutParams(params);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editText.setTransformationMethod(new PasswordTransformationMethod());
        editText.setHint("••••••••");
        addView(editText);

        // Observe text changes
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                update(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setValidator(StrengthValidatorInterface v) {
        validator = v;
    }

    public void addVisualizer(VisualizationInterface v) {
        visualizers.add(v);

        // Add to layout
        View view = v.getView(getContext());
        addView(view);
    }

    public void removeVisualizer(VisualizationInterface v) {
        visualizers.remove(v);

        // Remove from layout
        View view = v.getView(getContext());
        removeView(view);
    }

    private void update(String text) {
        // Do nothing if no validator has been given
        if (validator == null)
            return;

        // Calculate validation score
        Float score = validator.validate(text);

        // Update visualizers
        for (VisualizationInterface visualizer : visualizers)
            visualizer.onUpdate(score);
    }
}
