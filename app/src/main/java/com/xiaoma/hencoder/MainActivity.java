package com.xiaoma.hencoder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.xiaoma.hencoder.ruler.OnScrollChangeListener;
import com.xiaoma.hencoder.ruler.RulerView;
import com.xiaoma.hencoder.ruler.render.HeightRender;
import com.xiaoma.hencoder.ruler.render.WeightRender;

public class MainActivity extends AppCompatActivity {

    private RulerView ruler;
    private TextView tv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ruler = (RulerView) findViewById(R.id.ruler);
        tv = (TextView) findViewById(R.id.tv);
        HeightRender render = new HeightRender();
        render.setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onChange(float index) {
                tv.setText(index+"kg");
            }
        });
        ruler.setRender(render);
    }
}
