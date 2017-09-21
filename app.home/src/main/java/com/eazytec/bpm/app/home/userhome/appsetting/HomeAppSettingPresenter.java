package com.eazytec.bpm.app.home.userhome.appsetting;

import com.eazytec.bpm.app.home.data.app.tobject.AppsDataTObject;
import com.eazytec.bpm.app.home.webservice.WebApi;
import com.eazytec.bpm.appstub.delegate.ToastDelegate;
import com.eazytec.bpm.lib.common.RxPresenter;
import com.eazytec.bpm.lib.common.webservice.BPMRetrofit;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * @author Beckett_W
 * @version Id: HomeAppSettingPresenter, v 0.1 2017/9/21 10:01 Beckett_W Exp $$
 */
public class HomeAppSettingPresenter extends RxPresenter<HomeAppSettingContract.View> implements HomeAppSettingContract.Presenter<HomeAppSettingContract.View> {

    @Override public void loadApps() {

        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).menuList(true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override public void call() {
                        mView.showProgress();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override public void call() {
                        mView.dismissProgress();
                    }
                })
                .subscribe(new Observer<AppsDataTObject>() {
                    @Override public void onNext(AppsDataTObject data) {
                        if (data.isSuccess()) {
                            mView.loadAppsSuccess(data);
                        } else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR, "远程应用列表加载失败:" + data.getErrorMsg());
                        }
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR, "远程应用列表加载失败:网络错误,请稍后再试");
                    }
                });
        addSubscrebe(rxSubscription);
    }

    @Override
    public void loadAllApps() {
        Subscription rxSubscription = BPMRetrofit.retrofit().create(WebApi.class).menuList(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override public void call() {
                        mView.showProgress();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override public void call() {
                        mView.dismissProgress();
                    }
                })
                .subscribe(new Observer<AppsDataTObject>() {
                    @Override public void onNext(AppsDataTObject data) {
                        if (data.isSuccess()) {
                            mView.loadAllAppsSuccess(data);
                        } else {
                            mView.toast(ToastDelegate.TOAST_TYPE_ERROR, "远程应用列表加载失败:" + data.getErrorMsg());
                        }
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        mView.toast(ToastDelegate.TOAST_TYPE_ERROR, "远程应用列表加载失败:网络错误,请稍后再试");
                    }
                });
        addSubscrebe(rxSubscription);
    }

}
