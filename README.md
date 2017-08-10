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
       
### 欢迎大家提出意见！
   
    
    
    
    

