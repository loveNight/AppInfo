package net.king.appinfo.activity;

import java.lang.reflect.Method;

import net.king.appinfo.R;
import net.king.appinfo.adapter.AppInfoAdapter;
import net.king.appinfo.model.AppInfo;
import net.king.appinfo.util.AppInfoTask;
import net.king.appinfo.util.AppInfoUtil;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * APP列表
 */
public class ListAppActivity extends BaseActivity implements OnItemClickListener {

    public static final int GET_LAUNCH_APP = AppInfoTask.GET_LAUNCH_APP; // 可启动APP
    public static final int GET_ALL_APP = AppInfoTask.GET_ALL_APP; // 所有APP
    public static final int GET_SYSTEM_APP = AppInfoTask.GET_SYSTEM_APP; // 系统预装APP
    public static final int GET_THIRD_APP = AppInfoTask.GET_THIRD_APP; // 第三方APP
    public static final int GET_SDCARD_APP = AppInfoTask.GET_SDCARD_APP; // SDCard的APP
    
    private static String TAG = "APP_SIZE";
    
    private int type = GET_LAUNCH_APP;
    
    private ListView lvApp;
    private ProgressBar pbLaunch;
    private LayoutInflater inflater = null;

    private AppInfoUtil appInfoUtil;
    private AppInfo clickedAppInfo;
    
    // 当前查询包的信息  
    private long cachesize ; //缓存大小  
    private long datasize  ;  //数据大小   
    private long codesize  ;  //应用程序大小  
    private long totalsize ; //总大小 
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_app_layout);
        init();
    }

    /** 初始始化 **/
    private void init() {
        lvApp = (ListView) findViewById(R.id.lvAppInfo);
        // 点击时才会触发其中的代码，而此时子线程中的任务已经执行完毕
        lvApp.setOnItemClickListener(this);
        
        setTitle("搜索中……");
        
        Intent intent = getIntent();
        type = intent.getIntExtra("type", GET_LAUNCH_APP); 
        
        new AppInfoTask(this, lvApp).execute(type);
    }
    
    /** 
     * 启动此Activity
     */
    public static void actionStart(Context context, int type) {
        Intent intent = new Intent(context, ListAppActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        Log.i(TAG, "触发OnItemClick");
        AppInfoAdapter adapter = (AppInfoAdapter) lvApp.getAdapter();
        AppInfo appInfo = adapter.getItem(position);
        clickedAppInfo = appInfo;
        switch (type) {
        case GET_LAUNCH_APP:
            Intent intent = new Intent();
            Log.i("Test", "--------------------------");
            Log.i("Test", "目标包名：" + appInfo.getPackageName());
            Log.i("Test", "目标Activity名：" + appInfo.getLaunchActivityName());
            intent.setComponent(new ComponentName(appInfo.getPackageName(), appInfo.getLaunchActivityName()));
            startActivity(intent);
            break;
        case GET_ALL_APP:
        case GET_SYSTEM_APP:
        case GET_THIRD_APP:
        case GET_SDCARD_APP:
            // 获取包大小等数据，反射需时较长，还没想好如何解决此延时问题
            try {
                queryPackageSize(appInfo.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        default:
            break;
        }
        
    }
    
    /** 创建AlertDialog并显示 **/
    private void showAlertDialog() {
        // 载入显示数据用的View
        inflater = LayoutInflater.from(this);
        View dialog = inflater.inflate(R.layout.dialog_app_size, null);
        TextView tvCacheSize = (TextView) dialog.findViewById(R.id.tv_cache_size);
        TextView tvDataSize = (TextView) dialog.findViewById(R.id.tv_data_size);
        TextView tvCodeSize = (TextView) dialog.findViewById(R.id.tv_code_size);
        TextView tvTotalSize = (TextView) dialog.findViewById(R.id.tv_total_size);
        
        //类型转换并赋值  
        tvCacheSize.setText(formateFileSize(cachesize));  
        tvDataSize.setText(formateFileSize(datasize)) ;  
        tvCodeSize.setText(formateFileSize(codesize)) ;  
        tvTotalSize.setText(formateFileSize(totalsize)) ; 
        
        //显示自定义对话框  
        AlertDialog.Builder builder =new AlertDialog.Builder(ListAppActivity.this) ;  
        builder.setView(dialog) ;  
        builder.setTitle(clickedAppInfo.getAppName()+"的大小信息为：") ;  
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.cancel() ;  
            }  
              
        });  
        builder.create().show() ;  
    }
    
    /** 获取指定包的大小信息 **/
    public void queryPackageSize(String packageName) throws Exception {
        Log.i(TAG, "packageName:" + packageName);
        if (packageName != null) {
            // 使用反射机制得到PackageManager类的隐藏函数getPackageSizeInfo  
            PackageManager pManager = getPackageManager();
            //通过反射机制获得该隐藏函数  
            Method getPackageSizeInfo = pManager.getClass().getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);  
            //调用该函数，并且给其分配参数 ，待调用流程完成后会回调PkgSizeObserver类的函数  
            getPackageSizeInfo.invoke(pManager, packageName,new PkgSizeObserver());  
        }
    }
    
    /** aidl文件形成的Bindler机制服务类 **/
    public class PkgSizeObserver extends IPackageStatsObserver.Stub{  
        /*** 回调函数， 
         * @param pStatus ,返回数据封装在PackageStats对象中 
         * @param succeeded  代表回调成功 
         */   
        @Override  
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)  
                throws RemoteException {  
            cachesize = pStats.cacheSize  ; //缓存大小  
            datasize = pStats.dataSize  ;  //数据大小   
            codesize = pStats.codeSize  ;  //应用程序大小  
            totalsize = cachesize + datasize + codesize ;              
            Log.i(TAG, "cachesize--->"+cachesize+" datasize---->"+datasize+ " codeSize---->"+codesize); 
            Log.i(TAG, "目前的线程名：" + Thread.currentThread().getName());
            // 获取数据则显示AlertDialog
            // 调试得调用这段代码的线程是Binder_1,2,3……等，所以要跳回主线程调用showAlertDialog()
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showAlertDialog();
                }
            });
        }  
    }  
    
    /** 系统函数，字符串转换**/
    private String formateFileSize(long size){
        String str = "";
        double newSize = 0;
        if (size == 0) {
            str = "0.00 B";
        } else if (size < (1 << 10)) {
            newSize = size;
            str = newSize + " B";
        } else if (size < (1 << 20)){
            newSize = 1.0 * size / (1 << 10);
            str = String.format("%.2f", newSize) + " KB";
        } else if (size < (1 << 30)) {
            newSize = 1.0 * size / (1 << 20);
            str = String.format("%.2f", newSize) + " MB";
        }
        return str;   
    }  

}

