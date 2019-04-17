package marquez.neuronalletter;
import marquez.neuronalletter.LetterPanel;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.app.ActionBar;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import marquez.neuronalletter.data.GoodPixels;

import java.lang.reflect.Array;
import java.util.ArrayList;
import marquez.neuronalletter.gui.DrawLineCanvas;

import marquez.neuronalletter.neural.Train;

public class MainActivity extends AppCompatActivity {

    private Train networkTrainer;
    private DrawLineCanvas dlc;
    private static final int numOfRow = 20;
    private static final int numOfCol = 20;
    private ArrayList<LetterPanel> layouts = new ArrayList<>();
    private TextView textsol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkTrainer = new Train(this.getResources(), getPackageName());
        dlc = findViewById(R.id.dlc);
        textsol = findViewById(R.id.textsolution);

        //GridLayout myGridLayout = (GridLayout) findViewById(R.id.grid);

        Point size = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(size);
        final int width = size.x;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, width);
        dlc.setLayoutParams(params);

        Button buttonClear = (Button) findViewById(R.id.button_clear);
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlc.getC().drawColor(getResources().getColor(R.color.white));
            }
        });

        Button buttonTrain = (Button) findViewById(R.id.button_train);
        buttonTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkTrainer.train(5000);
                Toast.makeText(getApplicationContext(), "The network has been train", Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonConvert = (Button) findViewById(R.id.button_convert);
        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkTrainer.setInputs(this.getPixels());

                ArrayList<Double> outputs = networkTrainer.getOutputs();

                int index = 0;
                for (int i = 0; i < outputs.size(); i++) {
                    if (outputs.get(i) > outputs.get(index))
                        index = i;
                }

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < outputs.size(); i++) {
                    int letterValue = i + 65;
                    sb.append((char) letterValue);
                    double value = outputs.get(i);
                    if (value < 0.01)
                        value = 0;
                    if (value > 0.99)
                        value = 1;

                    value *= 1000;
                    int x = (int) (value);
                    value = x / 1000.0;

                    sb.append("\t " + value);
                    sb.append("\n");
                }

                textsol.setText("La letra es: " + Character.toString((char) (index+ 65)));
                //drawGoodLetter(GoodPixels.getInstance().getGoodPixels(index));

            }

            public ArrayList<Integer> getPixels() {
                ArrayList<Integer> pixels = new ArrayList<>();
                Bitmap b= dlc.getB();
                for(int j=0; j<b.getWidth()/(width/20); j++){
                    for(int i=0; i<b.getHeight()/(width/20); i++){

                        if(b.getPixel(j*(width/20), i*(width/20)) != 0 && b.getPixel(j*(width/20), i*(width/20)) != -1){
                            pixels.add(1);
                        }
                        else
                            pixels.add(0);
                    }
                }
                Log.d("pixels", pixels.toString());
                return pixels;
            }

            @Deprecated
            public void drawGoodLetter(ArrayList<Integer> pixels){
                for (int xPos = 0; xPos < numOfRow ; xPos++) {
                    for (int yPos = 0; yPos < numOfCol; yPos++) {
                        int pixel = pixels.get(xPos*numOfCol + yPos);

                        if (pixel == 0 && layouts.get(yPos*numOfCol + xPos).isActivate())
                            layouts.get(yPos*numOfCol + xPos).changeColor(getApplicationContext());

                        else if (pixel == 1 && !layouts.get(yPos*numOfCol + xPos).isActivate())
                            layouts.get(yPos*numOfCol + xPos).changeColor(getApplicationContext());
                    }
                }
            }
        });
    }
}

