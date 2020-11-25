package com.yp.baselib.utils.http.builder;


import com.yp.baselib.utils.http.OkHttpUtils;
import com.yp.baselib.utils.http.request.OtherRequest;
import com.yp.baselib.utils.http.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
