package java2.devaunteledee.com.offlineapistorage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by devaunteledee on 3/2/15.
 */
public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<TheSubReddit> mObjects;

    private static final long ID_CONSTANT = 123456789;



    public MyAdapter(Context mContext, ArrayList<TheSubReddit> mObjects) {
        this.mContext = mContext;
        this.mObjects = mObjects;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public TheSubReddit getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listlayout, parent, false);
        }

        TheSubReddit item = getItem(position);
        TextView textView1 = (TextView)convertView.findViewById(R.id.subRedditTitle);
        TextView textView2 = (TextView)convertView.findViewById(R.id.subRedditAuthor);

        textView1.setText(item.title);
        textView2.setText(item.Author);

        Log.i("a","a:...." + convertView);

        return convertView;
//        return null;
    }


}
