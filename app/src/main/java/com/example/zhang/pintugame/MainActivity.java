package com.example.zhang.pintugame;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//思想 1 先随机初始化资源
//    2 设置一个游戏结束的判定

public class MainActivity extends AppCompatActivity implements View.OnClickListener ,Handler.Callback{

    private static final int UP_DATA_TIME = 100;
    private GridLayout mGroup;
    private int[] pics={
            R.mipmap.img_xiaoxiong_00x00,R.mipmap.img_xiaoxiong_00x01,R.mipmap.img_xiaoxiong_00x02,
            R.mipmap.img_xiaoxiong_01x00,R.mipmap.img_xiaoxiong_01x01,R.mipmap.img_xiaoxiong_01x02,
            R.mipmap.img_xiaoxiong_02x00,R.mipmap.img_xiaoxiong_02x01,R.mipmap.img_xiaoxiong_02x02,
    };//图片资源初始化
    private int[] picIndex={0,1,2,3,4,5,6,7,8};
    private int childCount;
    private int blankIndex=8;//默认的空的图片
    private Button mBegin;
    private TextView mTime;
    private Handler mhandler=new Handler(this);
    private int  time=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        randomintview();
    }

    private void randomintview() {
        childCount = mGroup.getChildCount();
        //随机打乱
        for(int i=0;i<20;i++){
            int ran1,ran2;//随机函数范围0-7
            ran1=(int)(Math.random()*8);
            do {
                ran2 = (int) (Math.random() * 8);
                if(ran1!=ran2){
                    break;//如果二个随机数不一样就循环结束
                }
            }while (true);
            //交换索引值
            swap(ran1,ran2);
        }
        for(int i = 0; i< childCount; i++){
            ImageView imageview = (ImageView) mGroup.getChildAt(i);
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);


            imageview.setImageResource(pics[picIndex[i]]);
            if (i==childCount-1){
                imageview.setVisibility(View.INVISIBLE);
            }
        }
    }


    //交换
    private void swap(int ran1, int ran2) {
        int tepm=picIndex[ran1];
        picIndex[ran1]=picIndex[ran2];
        picIndex[ran2]=tepm;
        //不引入第三变量 实现二个数的交换?
//        picIndex[ran1]=picIndex[ran1]^picIndex[ran2];
//        picIndex[ran2]=picIndex[ran1]^picIndex[ran2];
//        picIndex[ran1]=picIndex[ran1]^picIndex[ran2];

    }

    private void isSuccess() {
        boolean flag=true;
        int lenth=picIndex.length;
        for(int i=0;i<lenth;i++){
            if (picIndex[i]!=i){
                flag=false;
                break;
            }


        }
        if(flag){
            Toast.makeText(this, "你成功了,我很看好你，快打钱给我Q903188706", Toast.LENGTH_SHORT).show();
            int childcount=mGroup.getChildCount();
            for(int i=0;i<childcount;i++){
                 View view =mGroup.getChildAt(i);
                view.setVisibility(View.VISIBLE);
                view.setClickable(false);
                if(i==childcount-1){
                    ((ImageView) view).setImageResource(pics[childcount-1]);
                }
            }
        }

    }

    private void init() {
        mGroup = (GridLayout) findViewById(R.id.my_group_pic);
        mBegin = (Button) findViewById(R.id.begin);
        mBegin.setOnClickListener(this);
        mTime = (TextView) findViewById(R.id.text_time);
        //获取子view的个数
        int childCount = mGroup.getChildCount();
        for ( int i=0;i<childCount;i++) {

            mGroup.getChildAt(i).setOnClickListener(this);
            mGroup.getChildAt(i).setClickable(false);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.begin:
                gameStart();
                break;
            default:
                int position=mGroup.indexOfChild(v);//不知道点击的具体的view的ID  但是他的父控件知道
                //  Toast.makeText(this, "you click"+position, Toast.LENGTH_SHORT).show();
                move(position);//移动图片
        }

    }

    private void gameStart() {
        int count=mGroup.getChildCount();
        for(int i=0;i<count;i++){
            mGroup.getChildAt(i).setClickable(true);
        }
        //开始计时

            mhandler.sendEmptyMessageDelayed(UP_DATA_TIME,1000);

    }

    private void move(int position) {
        int clickX=position/3;//二维数组第一位
        int clickY=position%3;//二维数组第二位
        //和空白 能交换条件 x相等 y相差1  或者y相等 x差1

        //空着的y 坐标
        int blankX = blankIndex/3;
        int blankY = blankIndex%3;
        int  x=Math.abs(clickX-blankX);
        int  y=Math.abs(clickY-blankY);
        if(x==0&&y==1||x==1&&y==0){
            //满足这个规则 然后就能交换了
           // Toast.makeText(this, "你很棒可以移动了", Toast.LENGTH_SHORT).show();
            mGroup.getChildAt(position).setVisibility(View.INVISIBLE);
            ImageView image=(ImageView)mGroup.getChildAt(blankIndex);
            image.setVisibility(View.VISIBLE);
            image.setImageResource(pics[picIndex[position]]);
            swap(position,blankIndex);
            blankIndex=position;
            isSuccess();
        }
        else{
            //Toast.makeText(this, "哦哦不能动", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case UP_DATA_TIME:
                //设置时间
                time++;
                mTime.setText(String.valueOf(time));

                //继续发送计时标记
                mhandler.sendEmptyMessageDelayed(UP_DATA_TIME,1000);
                break;
        }
        return false;
    }
}
