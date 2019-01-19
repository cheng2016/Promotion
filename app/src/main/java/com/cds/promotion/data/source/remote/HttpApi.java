package com.cds.promotion.data.source.remote;

import com.cds.promotion.data.BaseResp;
import com.cds.promotion.data.entity.AchievemenBean;
import com.cds.promotion.data.entity.ClockOnInfo;
import com.cds.promotion.data.entity.ClockOnList;
import com.cds.promotion.data.entity.Info;
import com.cds.promotion.data.entity.SalesInfo;
import com.cds.promotion.data.entity.StoreList;
import com.cds.promotion.data.entity.VisitingList;

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

    @GET("sales/clockOn")
    Observable<BaseResp<ClockOnInfo>> getClockOn(@Query("content") String json);

    @GET("sales/clockOnList")
    Observable<BaseResp<ClockOnList>> getClockOnList(@Query("content") String json);

    @GET("visiting/list")
    Observable<BaseResp<VisitingList>> getVisitingList(@Query("content") String json);

    @GET("visiting/info")
    Observable<BaseResp> getVisitingInfo(@Query("content") String json);

    @GET("sales/getAchievement")
    Observable<BaseResp<AchievemenBean>> getAchievement(@Query("content") String json);

    @GET("dealer/list")
    Observable<BaseResp<StoreList>> getDealerList(@Query("content") String json);
}
