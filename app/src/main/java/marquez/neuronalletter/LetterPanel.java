package marquez.neuronalletter;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LetterPanel {

    private LinearLayout layout;
    private boolean isActivate = false;

    public LetterPanel(Context activity, int width){
        this.layout = new LinearLayout(activity);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(ContextCompat.getColor(activity, R.color.white));
        this.layout.setLayoutParams(new ActionBar.LayoutParams(Math.round(width/20), Math.round(width/20)));
        this.layout.setBackground(gd);
        layout.setId(this.hashCode());
    }

    public LinearLayout getLayout() {
        return layout;
    }

    public void changeColor(Context context){
        if(isActivate){
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(ContextCompat.getColor(context, R.color.white));
            this.layout.setBackground(gd);
        } else {
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(ContextCompat.getColor(context, R.color.black));
            this.layout.setBackground(gd);
        }
        this.isActivate = ! this.isActivate;
    }

    public boolean isActivate() {
        return isActivate;
    }
}
