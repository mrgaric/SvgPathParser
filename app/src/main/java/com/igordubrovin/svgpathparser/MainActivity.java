package com.igordubrovin.svgpathparser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SvgView svgView;
    Button btnReady;
    EditText etSvgStr;
    String strSvg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReady = (Button)findViewById(R.id.btn_ready);
        etSvgStr = (EditText)findViewById(R.id.et_svg_str);
        svgView = (SvgView)findViewById(R.id.svg_view);

        svgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.invalidate();
            }
        });

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strSvg = etSvgStr.getText().toString();
                try{
                    svgView.parseStrSvg(strSvg);
                    svgView.invalidate();
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "ошибка ввода", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("strSvg",strSvg);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            strSvg = savedInstanceState.getString("strSvg");
            try {
                svgView.parseStrSvg(strSvg);
                svgView.invalidate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
