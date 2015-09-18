package net.king.appinfo.adapter;

import java.util.List;

import net.king.appinfo.R;
import net.king.appinfo.model.AppInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppInfoAdapter extends ArrayAdapter<AppInfo> {
    
    private int resourceId;
    
    public AppInfoAdapter(Context context, int resource, List<AppInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    /**
     * 必须重写此方法
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获取此位置对应的内容
        AppInfo appInfo = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            // 未创建View则载入布局文件和组件
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        // 给每个组件设置相应的信息
        viewHolder.ivIcon.setImageDrawable(appInfo.getIcon());
        viewHolder.tvAppName.setText(appInfo.getAppName());
        viewHolder.tvPackageName.setText(appInfo.getPackageName());
        return view;
    }
    
    private class ViewHolder {
        ImageView ivIcon;
        TextView tvAppName;
        TextView tvPackageName;
        
        public ViewHolder(View view) {
            ivIcon = (ImageView) view.findViewById(R.id.ivApp);
            tvAppName = (TextView) view.findViewById(R.id.tvAppName);
            tvPackageName = (TextView) view.findViewById(R.id.tvPackageName);
        }
    }

}
