package com.eazytec.bpm.app.webkit.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 16735
 * @version Id: FileCallbackBean, v 0.1 2017-7-20 19:43 16735 Exp $$
 */
public class FileCallbackBean extends BaseCallbackBean {

    private final String LIST = "list";

    private ArrayList<String> list;

    public FileCallbackBean(boolean isSuccess, String errorMsg, ArrayList<String> list) {
        setSuccess(isSuccess);
        setErrorMsg(errorMsg);
        this.list = list;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public HashMap<String, Object> toJson() {
        StringBuffer lists = new StringBuffer("[");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(ERRORMSG, getErrorMsg());
        boolean isSuccess = getSuccess() ? true : false;
        hashMap.put(SUCCESS, isSuccess);

        hashMap.put(LIST, list);

        return hashMap;
    }
}
