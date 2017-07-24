package com.eazytec.bpm.app.contact.usercontact.contactchoose;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eazytec.bpm.app.contact.R;
import com.eazytec.bpm.app.contact.adapters.ContactBackNavigationAdapter;
import com.eazytec.bpm.app.contact.adapters.ContactChooseAdapter;
import com.eazytec.bpm.app.contact.adapters.ContactChooseHasChooseAdapter;
import com.eazytec.bpm.app.contact.adapters.DepartmentViewAdapter;
import com.eazytec.bpm.app.contact.data.BackInfoDataTObject;
import com.eazytec.bpm.app.contact.data.DepartmentDataTObject;
import com.eazytec.bpm.app.contact.data.UserDetailDataTObject;
import com.eazytec.bpm.app.contact.helper.ListViewHelper;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.fragment.ContractViewFragment;
import com.jakewharton.rxbinding.widget.RxAdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * @author Beckett_W
 * @version Id: TopDepartmentFragment, v 0.1 2017/7/24 13:11 Administrator Exp $$
 */
public class TopDepartmentFragment extends ContractViewFragment<TopDepartmentPresenter> implements TopDepartmentContract.View{

    //返回相关
    private GridView doBackView;
    private ContactBackNavigationAdapter contactBackNavigationAdapter;
    private ArrayList<BackInfoDataTObject> backTObjects;

    //已选人员
    private GridView hasChooseView;
    private TextView hasChooseTextView;
    private ArrayList<UserDetailDataTObject> chooseDataTObjects;
    private ContactChooseHasChooseAdapter contactChooseHasChooseAdapter;

    private ScrollView scrollView;

    private LinearLayout departmentLayout;
    private ListView deparmentRecyclerView;

    private LinearLayout userLayout;
    private ListView userRecyclerView;

    private DepartmentViewAdapter departmentViewAdapter;
    private ContactChooseAdapter contactChooseAdapter;

    private List<DepartmentDataTObject> departmentDataTObjects;

    private List<UserDetailDataTObject> allDataTObjects = new ArrayList<>();
    private boolean singlechoose;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_dep_and_user, container, false);

        //获取父控件
        hasChooseView = (GridView) getCommonActivity().findViewById(R.id.contactchoose_haschoose_gridview);
        hasChooseTextView = (TextView) getCommonActivity().findViewById(R.id.contactchoose_haschoose_textview);
        doBackView = (GridView) getCommonActivity().findViewById(R.id.contactchoose_backnavigation_gridview);

        contactChooseHasChooseAdapter = new ContactChooseHasChooseAdapter(getContext());
        hasChooseView.setAdapter(contactChooseHasChooseAdapter);
        contactBackNavigationAdapter = new ContactBackNavigationAdapter(getContext());
        doBackView.setAdapter(contactBackNavigationAdapter);

        //获取前面传过来的参数
        if(getArguments()!=null){
            Bundle bundle = getArguments();
            //返回的
            backTObjects = bundle.getParcelableArrayList("backdatas");
            if(backTObjects == null){
                backTObjects = new ArrayList<>();
            }
            contactBackNavigationAdapter.setItems(backTObjects);
            contactBackNavigationAdapter.notifyDataSetChanged();

            //已经选择的人
            chooseDataTObjects = bundle.getParcelableArrayList("choosedatas");
            if(chooseDataTObjects == null){
                //没有传入
                chooseDataTObjects = new ArrayList<>();
            }
            contactChooseHasChooseAdapter.setItems(chooseDataTObjects);
            contactChooseHasChooseAdapter.notifyDataSetChanged();
        }

        scrollView = (ScrollView) parentView.findViewById(R.id.dep_and_user_contract_scrollview);

        departmentLayout = (LinearLayout) parentView.findViewById(R.id.dep_and_user_department_layout);
        deparmentRecyclerView = (ListView) parentView.findViewById(R.id.dep_and_user_department_listview);

        userLayout = (LinearLayout) parentView.findViewById(R.id.dep_and_user_layout);
        userRecyclerView = (ListView)parentView.findViewById(R.id.dep_and_user_listview);

        departmentViewAdapter = new DepartmentViewAdapter(getContext());
        deparmentRecyclerView.setAdapter(departmentViewAdapter);
        deparmentRecyclerView.setFocusable(false);

        contactChooseAdapter = new ContactChooseAdapter(getContext());
        userRecyclerView.setAdapter(contactChooseAdapter);
        userRecyclerView.setFocusable(false);

        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.requestFocus();


        //监听已选择人员的动态删除
        hasChooseView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chooseDataTObjects.remove(position); //移除这个
                contactChooseHasChooseAdapter.notifyDataSetChanged();
                contactChooseAdapter.resetHasChooseList(chooseDataTObjects);
                hasChooseTextView.setText(getResources().getString(R.string.contactchoose_haschoose, chooseDataTObjects.size() + ""));
                contactChooseAdapter.notifyDataSetChanged();
                //已选择列表也要监听变动

            }
        });

        //部门条目点击
        RxAdapterView.itemClicks(this.deparmentRecyclerView).throttleFirst(100, TimeUnit.MILLISECONDS)
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

                        BackInfoDataTObject temp = new BackInfoDataTObject();
                        temp.setId(dataTObject.getId());
                        temp.setName(dataTObject.getName());
                        backTObjects.add(temp);
                        bundle.putParcelableArrayList("backdatas",backTObjects);
                        bundle.putParcelableArrayList("choosedatas",chooseDataTObjects);
                        BelowDepartmentFragment belowDepartmentFragment = new BelowDepartmentFragment();
                        belowDepartmentFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.dep_and_user_layout,belowDepartmentFragment).addToBackStack(null).commit();


                    }
                });


        //员工条目点击事件
        RxAdapterView.itemClicks(this.userRecyclerView).throttleFirst(100,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        //总的组织架构下没有员工，我就不写了，如果有的话，参照子部门传递参数选择即可
                    }
                });
        return parentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //加载一级部门列表
        getPresenter().loadDepSuccess();
    }

    @Override
    public void loadDepSuccess(DepartmentDataTObject departmentDataTObject) {
        if (departmentDataTObject.getChilds() != null && departmentDataTObject.getChilds().size() > 0) {

            departmentLayout.setVisibility(View.VISIBLE);

            DepartmentDataTObject[] childs = new DepartmentDataTObject[departmentDataTObject.getChilds().size()];
            departmentDataTObject.getChilds().toArray(childs);
            //   Arrays.sort(childs);
            departmentViewAdapter.resetList(departmentDataTObjects = Arrays.asList(childs));
            departmentViewAdapter.notifyDataSetChanged();
            ListViewHelper.setListViewHeightBasedOnChildren(deparmentRecyclerView);
        } else {
            departmentLayout.setVisibility(View.GONE);
        }

        if (departmentDataTObject.getUsers() != null && departmentDataTObject.getUsers().size() > 0) {
            userLayout.setVisibility(View.VISIBLE);
            allDataTObjects = departmentDataTObject.getUsers();
            contactChooseAdapter.resetList(departmentDataTObject.getUsers());
            contactChooseAdapter.resetHasChooseList(chooseDataTObjects);
            contactChooseAdapter.notifyDataSetChanged();
            ListViewHelper.setListViewHeightBasedOnChildren(userRecyclerView);
        } else {
            userLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected TopDepartmentPresenter createPresenter() {
        return new TopDepartmentPresenter();
    }
}

