package io.github.bewantbe.light_screen2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    RelativeLayout rl;
    private SeekBar seekBar1, seekBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl = (RelativeLayout) findViewById(R.id.relativeLayout1);

        seekBar1 = (SeekBar)findViewById(R.id.seekBar1);
        seekBar1.setOnSeekBarChangeListener(this);
        seekBar2 = (SeekBar)findViewById(R.id.seekBar2);
        seekBar2.setOnSeekBarChangeListener(this);

        TextView tv1, tv2;
        tv1 = (TextView)findViewById(R.id.textView1);
        tv2 = (TextView)findViewById(R.id.textView2);

        tv1.setTextColor(Color.rgb(0x80, 0x80, 0x80));
        tv2.setTextColor(Color.rgb(0x80, 0x80, 0x80));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onPause();
    }

    @Override
    public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser) {
        double Y = (double)seekBar1.getProgress() / seekBar1.getMax();
        double T = (double)seekBar2.getProgress() / seekBar2.getMax() * (10000 - 2000) + 2000;
        double[] RGB = new double[3];
        GetRGBAtTemp(T, Y, RGB);
        double s = Math.max(Math.max(RGB[0], RGB[1]), RGB[2]);
        RGB[0] = RGB[0]/s * Y;
        RGB[1] = RGB[1]/s * Y;
        RGB[2] = RGB[2]/s * Y;
        int[] iRGB = new int[3];
        iRGB[0] = (int) (255.999 * RGB[0]);
        iRGB[1] = (int) (255.999 * RGB[1]);
        iRGB[2] = (int) (255.999 * RGB[2]);
        rl.setBackgroundColor(Color.rgb(iRGB[0], iRGB[1], iRGB[2]));

        TextView tv1, tv2, tv3;
        tv1 = (TextView)findViewById(R.id.textView1);
        tv2 = (TextView)findViewById(R.id.textView2);
        tv3 = (TextView)findViewById(R.id.textView3);

        int tv_c_bright = (int)(((Y+0.4)%1.0) * 255.99);
        int bg_rgb = Color.rgb(tv_c_bright, tv_c_bright, tv_c_bright);
        tv1.setTextColor(bg_rgb);
        tv2.setTextColor(bg_rgb);
        tv3.setTextColor(bg_rgb);

        tv1.setText(String.format("%s: Y = %.3f%%", getString(R.string.seekbar_bright), 100.0 * Y));
        tv2.setText(String.format("%s: T = %.0fK (%.0fM)", getString(R.string.seekbar_colortemp), T, 1e6 / T));
        tv3.setText(String.format("R:%d, G:%d, B:%d", iRGB[0], iRGB[1], iRGB[2]));
    }

    @Override
    public void onStartTrackingTouch (SeekBar seekBar) {
        //
    }

    @Override
    public void onStopTrackingTouch (SeekBar seekBar) {
        //
    }

    private double CSRGB(double c) {
        // http://en.wikipedia.org/wiki/SRGB
        if (c <= 0.0031308) {
            return 12.92 * c;
        } else {
            return 1.055 * Math.pow(c, 1/2.4) - 0.055;
        }
    }

    private void GetRGBAtTemp(double T, double Y, double[] RGB) {
        double x, y, X, Z;
        // http://en.wikipedia.org/wiki/Planckian_locus#Approximation
        // Y <= 0.39 so that RGB < 1.0
        if (T < 1667 || T > 25000) {
            RGB[0] = 0;
            RGB[1] = 0;
            RGB[2] = 0;
            return;
        }
        if (T < 4000) {
            x = -0.2661239*1e9/T/T/T - 0.2343580*1e6/T/T + 0.8776956*1e3/T + 0.179910;
        } else {
            x = -3.0258469*1e9/T/T/T + 2.1070379*1e6/T/T + 0.2226347*1e3/T + 0.240390;
        }
        if (T < 2222) {
            y = -1.1063814*x*x*x - 1.34811020*x*x + 2.18555832*x - 0.20219683;
        } else if (T < 4000) {
            y = -0.9549476*x*x*x - 1.37418593*x*x + 2.09137015*x - 0.16748867;
        } else {
            y =  3.0817580*x*x*x - 5.87338670*x*x + 3.75112997*x - 0.37001483;
        }
        X = Y * x / y;
        Z = Y * (1-x-y) / y;
        RGB[0] = CSRGB( 3.2406*X - 1.5372*Y - 0.4986*Z);
        RGB[1] = CSRGB(-0.9689*X + 1.8758*Y + 0.0415*Z);
        RGB[2] = CSRGB( 0.0557*X - 0.2040*Y + 1.0570*Z);
    }

}
