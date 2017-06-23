package com.example.zhang.pintugame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

//思想 1 先随机初始化资源
//    2 设置一个游戏结束的判定

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GridLayout mGroup;
    private int[] pics={
            R.mipmap.img_xiaoxiong_00x00,R.mipmap.img_xiaoxiong_00x01,R.mipmap.img_xiaoxiong_00x02,
            R.mipmap.img_xiaoxiong_01x00,R.mipmap.img_xiaoxiong_01x01,R.mipmap.img_xiaoxiong_01x02,
            R.mipmap.img_xiaoxiong_02x00,R.mipmap.img_xiaoxiong_02x01,R.mipmap.img_xiaoxiong_02x02,
    };//图片资源初始化
    private int[] picIndex={0,1,2,3,4,5,6,7,8};
    private int childCount;

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

    private void init() {
        mGroup = (GridLayout) findViewById(R.id.my_group_pic);
        //获取子view的个数
        int childCount = mGroup.getChildCount();
        for ( int i=0;i<childCount;i++) {
            mGroup.getChildAt(i).setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {
        int position=mGroup.indexOfChild(v);//不知道点击的具体的view的ID  但是他的父控件知道
        Toast.makeText(this, "you click"+position, Toast.LENGTH_SHORT).show();

    }
}
