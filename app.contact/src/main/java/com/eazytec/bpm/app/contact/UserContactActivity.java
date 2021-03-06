package com.eazytec.bpm.app.contact;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.adapters.DepartmentViewAdapter;
import com.eazytec.bpm.app.contact.adapters.UserViewAdapter;
import com.eazytec.bpm.app.contact.data.DepartmentDataTObject;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.app.contact.helper.ListViewHelper;
import com.eazytec.bpm.app.contact.usercontact.contactchoose.UserChooseActivity;
import com.eazytec.bpm.app.contact.usercontact.contactchoose.UserChoosedemoActivity;
import com.eazytec.bpm.app.contact.usercontact.department.DepartmentActivity;
import com.eazytec.bpm.app.contact.usercontact.localcontact.LocalContactActivity;
import com.eazytec.bpm.app.contact.usercontact.search.UserSearchActivity;
import com.eazytec.bpm.app.contact.usercontact.userdetail.UserDetailActivity;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.activity.ContractViewActivity;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * 通讯录activity
 * Created by beckett_W on 2017/7/3.
 */

public class UserContactActivity extends ContractViewActivity<UserContactPresenter> implements UserContactContract.View{

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    private ScrollView scrollView;
    private RelativeLayout contractSearchRelativeLayout;
    private RelativeLayout localContactRelativeLayout;
    private RelativeLayout selectionRelativeLayout;

    private LinearLayout departmentLayout;
    private ListView deparmentRecyclerView;

    private LinearLayout userLayout;
    private ListView userRecyclerView;

    private DepartmentViewAdapter departmentViewAdapter;
    private UserViewAdapter userViewAdapter;

    private List<DepartmentDataTObject> departmentDataTObjects;
    private List<UserDetailDataTObject> userDetailDataTObjects;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercontact);

        toolbar = (Toolbar) findViewById(R.id.bpm_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_common_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.bpm_toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbarTitleTextView.setText("通讯录");

        scrollView = (ScrollView) findViewById(R.id.contract_scrollview);
        contractSearchRelativeLayout = (RelativeLayout) findViewById(R.id.contract_search_relativelayout);
        localContactRelativeLayout = (RelativeLayout) findViewById(R.id.contract_local_relativelayout);
        selectionRelativeLayout = (RelativeLayout) findViewById(R.id.contract_selection_relativelayout);

        departmentLayout = (LinearLayout) findViewById(R.id.department_layout);
        deparmentRecyclerView = (ListView) findViewById(R.id.department_listview);

        userLayout = (LinearLayout) findViewById(R.id.user_layout);
        userRecyclerView = (ListView) findViewById(R.id.user_listview);

        departmentViewAdapter = new DepartmentViewAdapter(getContext());
        deparmentRecyclerView.setAdapter(departmentViewAdapter);
        deparmentRecyclerView.setFocusable(false);

        userViewAdapter = new UserViewAdapter(getContext());
        userRecyclerView.setAdapter(userViewAdapter);
        userRecyclerView.setFocusable(false);

        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.requestFocus();


        //加载一级部门列表
        getPresenter().loadDepSuccess();

        //人员搜索
        RxView.clicks(this.contractSearchRelativeLayout).throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startActivity(UserContactActivity.this,UserSearchActivity.class);
                    }
                });

        //手机通讯录
        RxView.clicks(this.localContactRelativeLayout).throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        RxPermissions rxPermissions = new RxPermissions(UserContactActivity.this);
                        rxPermissions.request(Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS)
                                     .subscribe(new Action1<Boolean>() {
                                         @Override
                                         public void call(Boolean aBoolean) {
                                                 if(aBoolean){
                                                     startActivity(UserContactActivity.this, LocalContactActivity.class);
                                                 }else{
                                                     ToastDelegate.info(getContext(),"您没有权限查看手机通讯录");
                                                 }
                                         }
                                     });


                    }
                });

        //人员选择
        RxView.clicks(this.selectionRelativeLayout).throttleFirst(100, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                      startActivity(UserContactActivity.this,UserChoosedemoActivity.class);
                    }
                });
        //部门条目点击
        RxAdapterView.itemClicks(this.deparmentRecyclerView).throttleFirst(100,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        DepartmentDataTObject dataTObject = departmentDataTObjects.get(integer);

                        if (dataTObject.getChildCount() == 0 && dataTObject.getUserCount() == 0) {
                            ToastDelegate.info(getContext(),"此部门下面没有相关信息");
                            return;
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString("id", dataTObject.getId());
                        bundle.putString("name", dataTObject.getName());

                        startActivity(UserContactActivity.this, DepartmentActivity.class, bundle);
                    }
                });
        //员工条目点击事件
        RxAdapterView.itemClicks(this.userRecyclerView).throttleFirst(100,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        UserDetailDataTObject dataTObject = userDetailDataTObjects.get(integer);

                        Bundle bundle = new Bundle();
                        bundle.putString("id", dataTObject.getId());
                        bundle.putString("name", dataTObject.getFullName());

                        startActivity(UserContactActivity.this, UserDetailActivity.class, bundle);

                    }
                });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               UserContactActivity.this.finish();
            }
        });

    }




    @Override
    protected UserContactPresenter createPresenter() {
        return new UserContactPresenter();
    }

    /**
     * 加载一级部门列表成功后的页面处理
     * @param departmentDataTObject
     */

    @Override
    public void loadDepSuccess(DepartmentDataTObject departmentDataTObject) {
        if (departmentDataTObject.getChilds() != null && departmentDataTObject.getChilds().size() > 0) {

            departmentLayout.setVisibility(View.VISIBLE);

            DepartmentDataTObject[] childs = new DepartmentDataTObject[departmentDataTObject.getChilds().size()];
            departmentDataTObject.getChilds().toArray(childs);
          //  Arrays.sort(childs, Collections.reverseOrder());
            departmentViewAdapter.resetList(departmentDataTObjects = Arrays.asList(childs));
            departmentViewAdapter.notifyDataSetChanged();
            ListViewHelper.setListViewHeightBasedOnChildren(deparmentRecyclerView);
        } else {
            departmentLayout.setVisibility(View.GONE);
        }

        if (departmentDataTObject.getUsers() != null && departmentDataTObject.getUsers().size() > 0) {
            userLayout.setVisibility(View.VISIBLE);
            userDetailDataTObjects = departmentDataTObject.getUsers();

            userViewAdapter.resetList(departmentDataTObject.getUsers());
            userViewAdapter.notifyDataSetChanged();
            ListViewHelper.setListViewHeightBasedOnChildren(userRecyclerView);
        } else {
            userLayout.setVisibility(View.GONE);
        }
    }
}
