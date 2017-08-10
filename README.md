# NetView
### 自定义蜘蛛网图控件

刚好最近在学习自定义View方面的知识，然后就看了别人的自定义蜘蛛网控件源码后，把学习到的东西实践了一下，做得不好的地方，还望大家指出。

先看看效果图：



### 支持的属性:
- LayerNum 蜘蛛网的层数（包括最外层）
+ MaxValue 最大值
* RegionAlpha  阴影部分的透明度
+ RegionColor  阴影部分的颜色
* NetColor  网格的颜色
- PointColor 点的颜色
+ TextColor  字体的颜色
— TextSize  字体的大小

### 用法（Usage）：
    //设置边数，也可以不用设置，会根据传进的list长度进行绘制
    public void setEdgeNum(int mEdgeNum) {
        this.mEdgeNum = mEdgeNum;
    }
    //设置蜘蛛网的层数，默认为3层
    public void setLayerNum(int mLayerNum) {
        this.mLayerNum = mLayerNum;
    }
    //设置最大值
    public void setMaxValue(float mMaxValue) {
        this.mMaxValue = mMaxValue;
    }
    //设置区域填充颜色的透明度（0-255）
    public void setRegionAlpha(int mRegionAlpha) {
        this.mRegionAlpha = mRegionAlpha;
    }
     //设置区域填充颜色
    public void setRegionColor(int mRegionColor) {
        this.mRegionColor = mRegionColor;
    }
    //设置蜘蛛网线的颜色
    public void setNetColor(int mNetColor) {
        this.mNetColor = mNetColor;
    }
    //设置点的颜色
    public void setPointColor(int mPointColor) {
        this.mPointColor = mPointColor;
    }
    //设置标题的字体颜色
    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }
    //设置标题的字体大小
    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }
    //设置数据源
    public void setMdataList(List<NetViewData> mdataList) {
        this.mdataList = mdataList;
    }
    
   
    
### 例子（Example）
    
    XML布局文件（若部分属性没有设置，则使用默认属性）
    
    <android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.example.asus.netview.MainActivity">
    <com.example.asus.netview.NetView
        android:id="@+id/netview"
        android:layout_width="400dp"
        android:layout_height="400dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layerNum="5"
        app:maxValue="100"
        app:netColor="#FF0000FF"
        android:alpha="100"
        app:regionColor="#FFFFFF00"
        app:textSize="20sp"
        app:texttColor="#FF000000" />
    </android.support.constraint.ConstraintLayout>
    
    在activity中使用
    
    public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        List<NetViewData> list = new ArrayList<>();
        list.add(new NetViewData("语文", 80));
        list.add(new NetViewData("数学", 90));
        list.add(new NetViewData("英语", 70));
        list.add(new NetViewData("物理", 100));
        list.add(new NetViewData("化学", 80));
        
        NetView netView = (NetView) findViewById(R.id.netview);
        netView.setMdataList(list);
        }
       }
       
### 绘制思路
1. 绘制网格线
<br>利用数学知识进行绘制：正多边形的顶点，都在它的外交圆上面。
Math.sin(x)表示x 的正玄值，返回值在 -1.0 到 1.0 之间；
Math.cos(x)表示x 的余弦值。返回的是 -1.0 到 1.0 之间的数；
这两个函数中的X 都是指的“弧度”而非“角度”，弧度的计算公式为： 2*PI/360*角度；
30° 角度 的弧度 = 2*PI/360*30
解决思路：根据三角形的正玄、余弦来得值；
假设一个圆的圆心坐标是(a,b)，半径为r，
则圆上每个点   X坐标=a + Math.sin(2*Math.PI / 360) * r ；Y坐标=b + Math.cos(2*Math.PI / 360) * r ；
        
        private void drawNet(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mNetColor);
        
        for (int j = 1; j <= mLayerNum; j++) {
            for (int i = 0; i < mEdgeNum; i++) {
                Path path = new Path();
                path.lineTo((float) (1.0 / mLayerNum * j * mRadius), 0);
                path.lineTo((float) (1.0 / mLayerNum * j * mRadius * Math.cos(2 * PI / 360 * 360 / mEdgeNum)),
                        (float) (1.0 / mLayerNum * j * mRadius * Math.sin(2 * PI / 360 * 360 / mEdgeNum)));
                path.close();
                canvas.drawPath(path, paint);
                canvas.rotate(360 / mEdgeNum);
            }
        }
        }

2. 绘制填充区域
<br>根据所占比例，计算出改点距离圆心的长度，然后再去步骤一的公式计算出该点的x坐标和y坐标
<br>利用path将每一个点进行连接起来就可以了（如果是第一个点，则利用moveTo方法移动下一次操作的起点位置
），最后利用close方法形成封闭路径
 
        //绘制阴影部分
        private void drawRegion(Canvas canvas) {
        Path path = new Path();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < mEdgeNum; i++) {
            float x = (float) (mdataList.get(i).getValue() / mMaxValue * mRadius * Math.cos(2 * PI / 360 * 360 / mEdgeNum * i));
            float y = (float) (mdataList.get(i).getValue() / mMaxValue * mRadius * Math.sin(2 * PI / 360 * 360 / mEdgeNum * i));
            paint.setColor(mPointColor);
            canvas.drawPoint(x, y, paint);

            paint.setColor(mRegionColor);
            paint.setAlpha(mRegionAlpha);
    
            if (i == 0) {
                path.moveTo(x, y);
            }
            path.lineTo(x, y);
        }
        path.close();
        canvas.drawPath(path, paint);
        }
        
    
3. 绘制标题
<br>先要利用Rect计算出字体所占的长度和高度，再计算出每个顶点的坐标。因为点位于不同的象限，所以字体的位置计算方法会不一样。
     
        
        //绘制文字
        private void drawText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(mTextColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(mTextSize);
        for (int i = 0; i < mEdgeNum; i++) {
            float x = (float) (mRadius * Math.cos(2 * PI / 360 * 360 / mEdgeNum * i));
            float y = (float) (mRadius * Math.sin(2 * PI / 360 * 360 / mEdgeNum * i));
            Rect rect = new Rect();
            paint.getTextBounds(mdataList.get(i).getTitle(), 0, mdataList.get(i).getTitle().length(), rect);
            float width = rect.width();
            float height = rect.height();
    
            if (x >= 0 && y >= 0) {
                canvas.drawText(mdataList.get(i).getTitle(), x, y, paint);
            } else if (x >= 0 && y <= 0) {
                canvas.drawText(mdataList.get(i).getTitle(), x, y - height, paint);
            } else if (x <= 0 && y <= 0) {
                canvas.drawText(mdataList.get(i).getTitle(), x - width, y - height, paint);
            } else if (x <= 0 && y >= 0) {
                canvas.drawText(mdataList.get(i).getTitle(), x - width, y, paint);
            }
        }
        }       
       
### 欢迎大家提出意见！
   
    
    
    
    

