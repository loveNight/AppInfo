package net.king.appinfo.util;

import java.util.List;

import net.king.appinfo.R;
import net.king.appinfo.adapter.AppInfoAdapter;
import net.king.appinfo.model.AppInfo;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ListView;

/**
 * 查询AppInfo任务线程
 * 三个泛型参数分别为传入的参数、进度单位、返回值类型
 * Void表示不需要传入参数
 */
public class AppInfoTask extends AsyncTask<Integer, Integer, AppInfoAdapter> {
    public static final int GET_LAUNCH_APP = -1; // 根据Intent获取可启动APP
    public static final int GET_ALL_APP = AppInfoUtil.GET_ALL_APP; // 所有APP
    public static final int GET_SYSTEM_APP = AppInfoUtil.GET_SYSTEM_APP; // 系统预装APP
    public static final int GET_THIRD_APP = AppInfoUtil.GET_THIRD_APP; // 第三方APP
    public static final int GET_SDCARD_APP = AppInfoUtil.GET_SDCARD_APP; // SDCard的APP
    
    private Context context;
    private ListView lvApp;
    private ProgressDialog progressDialog;

    public AppInfoTask(Context context, ListView listView) {
        super();
        this.context = context;
        this.lvApp = listView;
    }

    /** 任务开始执行之前调用，用于初始化操作 **/
    @Override
    protected void onPreExecute() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /** 所有代码都在子线程中运行，用于处理耗时任务，不能操作UI **/
    @Override
    protected AppInfoAdapter doInBackground(Integer... params) {
        int type = params[0];
        AppInfoUtil appInfoUtil = AppInfoUtil.getInstance(context);
        List<AppInfo> appInfoList = null;
        switch (type) {
        case GET_LAUNCH_APP:
            Intent launchIntent = new Intent(Intent.ACTION_MAIN, null);
            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            appInfoList = appInfoUtil.getAppInfoByIntent(launchIntent);
            break;
        case GET_ALL_APP:
        case GET_SYSTEM_APP:
        case GET_THIRD_APP:
        case GET_SDCARD_APP:
            appInfoList = appInfoUtil.getInstalledApps(type);
        default:
            break;
        }
        AppInfoAdapter adapter = new AppInfoAdapter(context, R.layout.listview_app_info, appInfoList);
        return adapter;
    }
    
    /** 后台任务通过publishProgress()方法后，此方法很快被调用，可操作UI **/
    @Override
    protected void onProgressUpdate(Integer... values) {
        
    }
    
    /** 后台任务执行完毕并使用return返回时，此方法很快被调用，可操作UI **/
    @Override
    protected void onPostExecute(AppInfoAdapter result) {
        lvApp.setAdapter(result);
        // 改Activity的标题
        ((Activity) context).setTitle("找到APP " + result.getCount() + " 个");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }   
}

