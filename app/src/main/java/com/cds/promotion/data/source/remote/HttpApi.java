package com.cds.promotion.data.source.remote;

import com.cds.promotion.data.BaseResp;
import com.cds.promotion.data.entity.ClockOnInfo;
import com.cds.promotion.data.entity.ClockOnList;
import com.cds.promotion.data.entity.DoctorInfo;
import com.cds.promotion.data.entity.Info;
import com.cds.promotion.data.entity.Medicine;
import com.cds.promotion.data.entity.Order;
import com.cds.promotion.data.entity.OrderInfo;
import com.cds.promotion.data.entity.SalesInfo;
import com.cds.promotion.data.entity.VisitingList;

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
    Observable<BaseResp<SalesInfo>> getSalesInfo(@Query("content") String json);

    @POST("sales/feedback")
    Observable<BaseResp> feedback(@Body RequestBody Body);


    @POST("sales/clockOn")
    Observable<BaseResp> clockOn(@Query("content") String json);

//    @GET("sales/clockOn")
//    Observable<BaseResp> getClockOn(@Query("content") String json);

    @GET("sales/clockOnList")
    Observable<BaseResp<ClockOnList>> getClockOnList(@Query("content") String json);


    @GET("visiting/list")
    Observable<BaseResp<VisitingList>> getVisitingList(@Query("content") String json);
}
