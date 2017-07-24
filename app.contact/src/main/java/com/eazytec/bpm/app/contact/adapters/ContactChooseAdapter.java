package com.eazytec.bpm.app.contact.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.appstub.view.checkbox.SmoothCheckBox;
import com.eazytec.bpm.appstub.view.imageview.AvatarImageView;
import com.eazytec.bpm.lib.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Beckett_W
 * @version Id: ContactChooseAdapter, v 0.1 2017/7/24 13:06 Administrator Exp $$
 */
public class ContactChooseAdapter extends BaseAdapter {

    private List<UserDetailDataTObject> items;
    private List<UserDetailDataTObject> chooseitems;
    private List<String> haschooseitems;

    private Context context;

    public ContactChooseAdapter(Context context) {
        super();
        this.items = new ArrayList<>();
        this.haschooseitems = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        if (position < items.size()) {
            return items.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contactchoose, parent, false);
        }
        int length = items.get(position).getFullName().length();
        if (position < items.size() && items.get(position) != null) {
            if(length<3){
                ((AvatarImageView) ViewHolder.get(convertView, R.id.item_contactchoose_imageview)).setText(items.get(position).getFullName());
            }else{
                ((AvatarImageView) ViewHolder.get(convertView, R.id.item_contactchoose_imageview)).setText(items.get(position).getFullName().substring(length-2));
            }
            ((TextView) ViewHolder.get(convertView, R.id.item_contactchoose_title)).setText(items.get(position).getFullName());

            SmoothCheckBox checkBox = ViewHolder.get(convertView, R.id.item_contactchoose_checkbox);

            if (haschooseitems.contains(items.get(position).getId())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

            checkBox.setOnTouchListener(new View.OnTouchListener() {
                @Override public boolean onTouch(View v, MotionEvent event) {
                    return true; // 禁止checkBox的点击事件交给item去处理
                }
            });
        }
        return convertView;
    }

    public void resetList(List<UserDetailDataTObject> list) {
        items.clear();
        items.addAll(list);
    }

    public void resetHasChooseList(List<UserDetailDataTObject> list) {
        haschooseitems.clear();

        if (list != null && list.size() > 0) {
            for (UserDetailDataTObject ob : list) {
                haschooseitems.add(ob.getId());
            }
        }
    }
}


