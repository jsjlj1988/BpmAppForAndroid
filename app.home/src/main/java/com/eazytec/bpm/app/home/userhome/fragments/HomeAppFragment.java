package com.eazytec.bpm.app.home.userhome.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.eazytec.bpm.app.home.R;
import com.eazytec.bpm.app.home.data.app.BPMApp;
import com.eazytec.bpm.app.home.data.app.tobject.AppDataTObjectHelper;
import com.eazytec.bpm.app.home.data.app.tobject.AppsDataTObject;
import com.eazytec.bpm.app.home.data.commonconfig.ImgDataTObject;
import com.eazytec.bpm.app.home.userhome.UserHomeAppContract;
import com.eazytec.bpm.app.home.userhome.UserHomeAppPresenter;
import com.eazytec.bpm.app.home.userhome.adapters.CommonAppAdapter;
import com.eazytec.bpm.app.home.userhome.appsetting.HomeAppSettingActivity;
import com.eazytec.bpm.appstub.Config;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.appstub.view.gridview.ScrollGridView;
import com.eazytec.bpm.lib.common.fragment.ContractViewFragment;
import com.eazytec.bpm.lib.utils.StringUtils;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * APP列表页的Fragment
 *
 * @author ConDey
 * @version Id: HomeAppFragment, v 0.1 2017/6/30 上午9:06 ConDey Exp $$
 */
public class HomeAppFragment extends ContractViewFragment<UserHomeAppPresenter> implements UserHomeAppContract.View {

    private ScrollGridView appGridView;
    private CommonAppAdapter homeAppAdapter;

    public static boolean needRefresh = true;

    private ScrollGridView allAppGridView;
    private CommonAppAdapter homeAllAppAdapter;

    private List<BPMApp> bpmApps;

    private List<BPMApp> bpmAllApps;

    private ImageView bannerIv;

    private boolean longPress;
    private boolean alongPress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View parentView = inflater.inflate(R.layout.fragment_homeapp, container, false);

        appGridView = (ScrollGridView) parentView.findViewById(R.id.gv_homeapp);
        allAppGridView = (ScrollGridView) parentView.findViewById(R.id.gv_all_homeapp);

        bannerIv = (ImageView) parentView.findViewById(R.id.fragment_homeapp_banner);

        // 设置AppGridView的适配器
        homeAppAdapter = new CommonAppAdapter(getContext());
        appGridView.setAdapter(homeAppAdapter);

        homeAllAppAdapter = new CommonAppAdapter(getContext());
        allAppGridView.setAdapter(homeAllAppAdapter);

        RxAdapterView.itemClicks(appGridView).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (longPress) {
                    longPress = false;
                    return;
                } else {
                    longPress = false;
                    BPMApp bpmApp = bpmApps.get(integer);
                    // 在Debug模式下应用肯定都是没有安装的
                    if (bpmApp.installed()) {
                        bpmApp.getIntoApp(getContext());
                    } else {
                        ToastDelegate.warning(getContext(), getString(R.string.userhome_app_is_not_installed));
                    }
                }
            }
        });

        RxAdapterView.itemClicks(allAppGridView).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (alongPress) {
                    alongPress = false;
                    return;
                } else {
                    alongPress = false;
                    BPMApp bpmApp = bpmAllApps.get(integer);
                    // 在Debug模式下应用肯定都是没有安装的
                    if (bpmApp.installed()) {
                        bpmApp.getIntoApp(getContext());
                    } else {
                        ToastDelegate.warning(getContext(), getString(R.string.userhome_app_is_not_installed));
                    }
                }
            }
        });

        allAppGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                alongPress = true;
                new MaterialDialog.Builder(getContext())
                        .content("想调整菜单应用，去“菜单设置”进行编辑")
                        .cancelable(true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //跳转
                                getCommonActivity().startActivity(getCommonActivity(), HomeAppSettingActivity.class);

                            }
                        })
                        .positiveText("去编辑")
                        .positiveColor(getResources().getColor(R.color.color_primary))
                        .negativeText("取消")
                        .negativeColor(getResources().getColor(R.color.color_grey_primary))
                        .show();
                return false;
            }
        });

        appGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longPress = true;
                new MaterialDialog.Builder(getContext())
                        .content("想调整菜单应用，去“菜单设置”进行编辑")
                        .cancelable(true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //跳转
                                getCommonActivity().startActivity(getCommonActivity(), HomeAppSettingActivity.class);
                            }
                        })
                        .positiveText("去编辑")
                        .negativeText("取消")
                        .positiveColor(getResources().getColor(R.color.color_primary))
                        .negativeColor(getResources().getColor(R.color.color_grey_primary))
                        .show();
                return false;
            }
        });
        return parentView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getPresenter().commonconfig(); //改变banner
        // 这里的代码是用的加载app.json的默认模式
        //this.bpmApps = getBpmApps();
        //homeAppAdapter.setItems(this.bpmApps);
        //homeAppAdapter.notifyDataSetChanged();
    }

    @Override protected UserHomeAppPresenter createPresenter() {
        return new UserHomeAppPresenter();
    }

    @Override public void loadAppsSuccess(AppsDataTObject appsDataTObject) {
        this.bpmApps = AppDataTObjectHelper.createBpmAppsByTObjects(appsDataTObject.getApps());
        homeAppAdapter.setItems(this.bpmApps);
        homeAppAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadAllAppsSuccess(AppsDataTObject appsDataTObject) {
        this.bpmAllApps = AppDataTObjectHelper.createBpmAppsByTObjects(appsDataTObject.getApps());
        homeAllAppAdapter.setItems(this.bpmAllApps);
        homeAllAppAdapter.notifyDataSetChanged();
    }

    @Override
    public void getImgUrl(ImgDataTObject imgDataTObject) {
        if (!StringUtils.isEmpty(imgDataTObject.getAppBackgroundImg())) {
            String url = Config.WEB_URL + imgDataTObject.getAppBackgroundImg();
            Picasso.with(getContext()).load(url).placeholder(R.mipmap.ic_homeapp_banner).error(R.mipmap.ic_homeapp_banner).into(bannerIv);
        }
    }

    /**
     * 老的本地模式
     *
     * @return
     */
    @Deprecated
    private List<BPMApp> getBpmApps() {
        bpmApps = new ArrayList<>();

        try {
            InputStreamReader isr = new InputStreamReader(getCommonActivity().getAssets().open("app.json"), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            JSONObject testjson = new JSONObject(builder.toString());//builder读取了JSON中的数据。
            JSONArray array = testjson.getJSONArray("apps");         //从JSONObject中取出数组对象
            for (int i = 0; i < array.length(); i++) {
                JSONObject appobject = array.getJSONObject(i);    //取出数组中的对象

                BPMApp bpmApp = new BPMApp();
                bpmApp.setId(appobject.getString("id"));
                bpmApp.setPackageName(appobject.getString("packagename"));
                bpmApp.setName(appobject.getString("name"));
                bpmApp.setDisplayName(appobject.getString("diplayname"));

                String imgUrlType = appobject.getString("imageurltype");

                if (imgUrlType.equals("IMAGE_URL_TYPE_INNER")) {
                    bpmApp.setImageUrlType(BPMApp.IMAGE_URL_TYPE_INNER);
                } else if (imgUrlType.equals("IMAGE_URL_TYPE_REMOTE")) {
                    bpmApp.setImageUrlType(BPMApp.IMAGE_URL_TYPE_REMOTE);
                }

                bpmApp.setImageUrl(appobject.getString("imageurl"));

                String type = appobject.getString("type");

                if (type.equals("APP_TYPE_INNER")) {
                    bpmApp.setType(BPMApp.APP_TYPE_INNER);
                } else if (type.equals("APP_TYPE_REMOTE")) {
                    bpmApp.setType(BPMApp.APP_TYPE_REMOTE);
                } else {
                    bpmApp.setType(BPMApp.APP_TYPE_WEB);
                }
                bpmApp.setBundleName(appobject.getString("bundlename"));
                bpmApps.add(bpmApp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bpmApps;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && needRefresh) {
            getPresenter().loadAllApps();
            getPresenter().loadApps();
            needRefresh = false;
        }
    }
}
