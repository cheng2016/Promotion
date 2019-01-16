package com.cds.promotion.data.source.remote;

import com.cds.promotion.data.BaseResp;
import com.cds.promotion.data.entity.DoctorInfo;
import com.cds.promotion.data.entity.Info;
import com.cds.promotion.data.entity.Medicine;
import com.cds.promotion.data.entity.Order;
import com.cds.promotion.data.entity.OrderInfo;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by chengzj on 2017/6/18.
 */

public interface HttpApi {
    //http://gank.io/api/day/2016/10/12
    public static final String base_url = "https://sit.wecarelove.com/api/eshopdaily/";

    @POST("sales/login")
    Observable<BaseResp<Info>> login(@Query("content") String json);

    @GET("sales/info")
    Observable<BaseResp<DoctorInfo>> getDoctorInfo(@Query("content") String json);

    @GET("appoint/list")
    Observable<BaseResp<List<Order>>> getOrderList(@Query("content") String json);

    @GET("appoint/info")
    Observable<BaseResp<OrderInfo>> getOrderInfo(@Query("content") String json);

    @POST("appoint/option")
    Observable<BaseResp> executeOrder(@Query("content") String json);

    @GET("sales/services")
    Observable<BaseResp<List<Medicine>>> getMedicine(@Query("content") String json);

    @POST("sales/diagnosticsresult")
    Observable<BaseResp> openOrder(@Query("content") String json);

    @POST("sales/info")
    Observable<BaseResp> updateWorkState(@Query("content") String json);


    @POST("user/feedback")
    Observable<BaseResp> feedback(@Body RequestBody Body);
}
