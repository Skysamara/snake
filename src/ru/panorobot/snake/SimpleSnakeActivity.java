package ru.panorobot.snake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SimpleSnakeActivity extends Activity implements OnClickListener {

    Button butt;
    TextView tv;

    // режим запуска активити - 0 первый запуск
    // 1 - запуск активити после проигрыша
    public static int GAME_MODE=0;

    public static int GAME_SCORE=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();
        // Вот тут забавный момент с
        // загрузкой разных разметок
        if (GAME_MODE==0){
            setContentView(R.layout.main);
            butt = (Button) this.findViewById(R.id.button1);
            butt.setOnClickListener(this);
        }
        else
        {
            setContentView(R.layout.lose);
            butt = (Button) this.findViewById(R.id.button2);
            tv = (TextView) this.findViewById(R.id.textView2);
            tv.setText("Your score: "+GAME_SCORE);
            butt.setOnClickListener(this);
        }
    }

    public void onClick(View v) {
        // Для любой разметки если мы нажимем на кнопку, то игра запускается
        Intent i = new Intent(this, ru.panorobot.snake.GameActivity.class);
        GAME_MODE=0;
        GAME_SCORE=0;
        this.startActivity(i);
    }

}
