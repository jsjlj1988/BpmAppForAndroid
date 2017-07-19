package com.eazytec.bpm.app.webkit.event;

import android.support.annotation.StringDef;

/**
 * jswebview与activity通信事件，主要作用还是解耦
 *
 * @author ConDey
 * @version Id: BPMJsMsgEvent, v 0.1 2017/7/3 下午3:19 ConDey Exp $$
 */
public class BPMJsMsgEvent {

    /**
     * 设置BPMWebViewActivity.title(),需要由主线程执行
     */
    public static final String JS_SET_TITLEBAR_VISIBLE = "JS_SET_TITLEBAR_VISIBLE";
    public static final String JS_SET_TITLEBAR_RIGHT_IV_BGIMAGE = "JS_SET_TITLEBAR_RIGHT_IV_BGIMAGE";
    public static final String JS_SET_TITLEBAR_BGCOLOR = "JS_SET_TITLEBAR_BGCOLOR";
    public static final String JS_SET_TITLEBAR_BGIMAGE = "JS_SET_TITLEBAR_BGIMAGE";
    public static final String JS_SET_TITLE = "JS_SET_TITLE";
    public static final String JS_DOWNLOAD_FILE = "JS_DOWNLOAD_FILE";
    public static final String JS_UPLOAD_FILE = "JS_UPLOAD_FILE";

    // 自定义一个注解MyState
    @StringDef({JS_SET_TITLE,
                JS_SET_TITLEBAR_VISIBLE,
                JS_SET_TITLEBAR_RIGHT_IV_BGIMAGE,
                JS_SET_TITLEBAR_BGCOLOR,
                JS_SET_TITLEBAR_BGIMAGE,
                JS_DOWNLOAD_FILE,
                JS_UPLOAD_FILE})
    public @interface BPM_JS_ID {
    }

    private String id;

    private String message;

    public BPMJsMsgEvent() {

    }

    /**
     * 默认的构造函数
     *
     * @param id
     * @param message
     */
    public BPMJsMsgEvent(@BPM_JS_ID String id, String message) {
        this.id = id;
        this.message = message;
    }

    @BPM_JS_ID
    public String getId() {
        return id;
    }

    public void setId(@BPM_JS_ID String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
