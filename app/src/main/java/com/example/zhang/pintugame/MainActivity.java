package com.example.zhang.pintugame;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
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
    private  boolean isgaming;

    private Bitmap[] bitmaps=new Bitmap[9];//加入图片 切成9张

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initBitmaps();
        init();
        randomintview();
    }

    private void initBitmaps() {
        //加载完成的图片
        Bitmap bitmapimag = BitmapFactory.decodeResource(getResources(), R.mipmap.wzry);
        int width = bitmapimag.getWidth();
        int height = bitmapimag.getHeight();//获得原图的宽高
        int singlewidth=width/3;
        int singleheight=height/3;
        for (int i = 0; i <9 ; i++) {
            /*0x0 0x1 0x2
              1x0 1x1 1x2
              2x0 2x1 2x2

             */
            Bitmap imag_slide = Bitmap.createBitmap(bitmapimag, (i % 3) * singlewidth, (i / 3) * singleheight, singlewidth, singleheight);
            bitmaps[i]=imag_slide;


        }
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


            imageview.setVisibility(View.VISIBLE);//全都可见 重置不设置有bug
          //静态
           // imageview.setImageResource(pics[picIndex[i]]);
            //动态 将所有的静态换成动态
             imageview.setImageBitmap(bitmaps[picIndex[i]]);
            if (i==blankIndex){
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
            Toast.makeText(this, "你成功了,时间"+time+"，快打钱给我Q903188706", Toast.LENGTH_SHORT).show();
            int childcount=mGroup.getChildCount();
            for(int i=0;i<childcount;i++){
                 View view =mGroup.getChildAt(i);
                view.setVisibility(View.VISIBLE);
                view.setClickable(false);
                if(i==childcount-1){
             //静态     --
                   // ((ImageView) view).setImageResource(pics[childcount-1]);
              //动态
                    ((ImageView)view).setImageBitmap(bitmaps[childcount-1]);
                }
            }

            isgaming=false;
            //停止计时
            mhandler.removeMessages(UP_DATA_TIME);
           AlertDialog.Builder build= new AlertDialog.Builder(this).setMessage("你成功了,时间"+time+"，快打钱给我Q903188706").setTitle("过关")
                   .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           Toast.makeText(MainActivity.this, "确认恭喜", Toast.LENGTH_SHORT).show();
                       }
                   })
                   .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           Toast.makeText(MainActivity.this, "未来继续努力", Toast.LENGTH_SHORT).show();
                       }
                   })
                   ;

            build.create().show();
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
                if(!isgaming) {
                    isgaming=true;
                    mBegin.setText("重新开始");
                    gameStart();
                }
                else{//重置游戏
                    time=0;

                    mhandler.removeMessages(UP_DATA_TIME);
                    randomintview();
                    gameStart();


                }
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
           //静态 --
           // image.setImageResource(pics[picIndex[position]]);
         //动态
            image.setImageBitmap(bitmaps[picIndex[position]]);
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
