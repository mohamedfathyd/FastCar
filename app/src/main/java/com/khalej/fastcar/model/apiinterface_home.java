package com.khalej.fastcar.model;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface apiinterface_home {

    @FormUrlEncoded
    @POST("fastcar/api/login")
    Call<contact_userinfo> getcontacts_login(@Field("kayWord") String kayWord, @Field("password") String password);

    @FormUrlEncoded
    @POST("fastcar/api/contact_us")
    Call<ResponseBody> CallUs(@Field("name") String name, @Field("email") String address,
                              @Field("subject") String subject, @Field("message") String message);

    @FormUrlEncoded
    @POST("fastcar/api/canRest")
    Call<Reset>getcontacts_ResetPassword(@Field("kayWord") String kayWord);






    @FormUrlEncoded
    @POST("fastcar/api/canceling_order")
    Call<ResponseBody> getcontacts_CancelOrder(@Field("order_id") int order_id, @Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("fastcar/api/rate")
    Call<ResponseBody> getcontacts_AddRate(@Field("to_id") int to_id, @Field("form_id") int form_id, @Field("rate") Float rate,
                                           @Field("des") String des);


    @FormUrlEncoded
    @POST("fastcar/api/delete_notification")
    Call<ResponseBody> getcontacts_CancelNotification(@Field("notification_id") int order_id, @Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("fastcar/api/accept_order")
    Call<ResponseBody> getcontacts_AcceptOrder(@Field("order_id") int order_id, @Field("user_id") int user_id);


    @FormUrlEncoded
    @POST("fastcar/api/accept_out_order")
    Call<ResponseBody> getcontacts_AcceptOutOrder(@Field("order_id") int order_id, @Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("fastcar/api/finshing_order")
    Call<ResponseBody> getcontacts_FinishOrder(@Field("order_id") int order_id, @Field("user_id") int user_id);




    @FormUrlEncoded
    @POST("fastcar/api/add_subscribs")
    Call<ResponseBody> getcontacts_RemoveSubScribe(@Field("product_id") int order_id, @Field("user_id") int user_id);
 @FormUrlEncoded
    @POST("fastcar/api/add_order_to_representatives")
    Call<ResponseBody> getSpecialOrder(@Field("user_id") int id, @Field("des") String des, @Field("name") String name,
                                       @Field("amount") int amount);

    @FormUrlEncoded
    @POST("fastcar/api/add_order_to_representatives")
    Call<ResponseBody> getSpecialOrderForMandop(@Field("user_id") int id, @Field("representative_id") int mandop_id, @Field("name") String name,
                                                @Field("amount") int amount, @Field("des") String des);

    @GET("fastcar/api/all_representatives")
    Call<List<contact_userinfo>> get_all_mandops();

    @GET("fastcar/api/about_en")
    Call<AboutUs> AboutUS_en();
    @GET("fastcar/api/about_ar")
    Call<AboutUs> AboutUS_ar();

    @GET("fastcar/api/condtions_en")
    Call<AboutUs> Conditoins_en();
    @GET("fastcar/api/condtions_ar")
    Call<AboutUs> Conditoins_ar();
    @FormUrlEncoded
    @POST("fastcar/api/my_orders")
    Call<List<My_Order>>  content_myOrder(@Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("fastcar/api/add_order")
        Call<Order> content_addOrder(@Field("user_id") int user_id, @Field("from_latitude") double from_latitude, @Field("from_longitude") double from_longitude,
                                     @Field("to_latitude") double to_latitude, @Field("to_longitude") double to_longitude,  @Field("from_address") String from_address,
                                        @Field("to_address") String to_address, @Field("total_cost") double total_cost,@Field("driver_id")int driver_id);

    @FormUrlEncoded
    @POST("fastcar/api/register")
    Call<contact_userinfo> getcontacts_newaccount(@Field("name") String name, @Field("password") String password, @Field("email") String address,
                                                  @Field("phone") String phone, @Field("latitude") double country, @Field("longitude") double lng,
                                                  @Field("phone_code") String phone_code, @Field("is_agree") int is_agree, @Field("type") int type,
                                                  @Field("country_id") int country_id, @Field("city_id") int city_id);

    @Multipart
    @POST("fastcar/api/register")
    Call<contact_userinfo>getcontacts_newaccountFani(@Part MultipartBody.Part image, @Part("name") RequestBody name,
                                                     @Part("phone") RequestBody phone, @Part("email") RequestBody email,
                                                     @Part("password") RequestBody password, @Part("phone_code") RequestBody phone_code,
                                                     @Part("user_type") RequestBody type, @Part("gender") RequestBody gender,
                                                     @Part("latitude") RequestBody country, @Part("longitude") RequestBody lng);

    @Multipart
    @POST("fastcar/api/add_payment")
    Call<ResponseBody> getcontacts_addbankImage(@Part MultipartBody.Part image, @Part("user_id") RequestBody user_id, @Part("bank_id") RequestBody bank_id,
                                                @Part("amount") RequestBody amount
    );

    @FormUrlEncoded
    @POST("fastcar/api/update_profile")
    Call<Edit> getcontacts_updateProfileWithoutImage(@Field("name") String name, @Field("password") String password, @Field("phone") String phone
            , @Field("country_id") int country_id, @Field("user_id") int user_id);
    @Multipart
    @POST("fastcar/api/user_logo")
    Call<Edit> getcontacts_updateProfile(@Part MultipartBody.Part image, @Part("user_id") RequestBody user_id);
    @FormUrlEncoded
    @POST("fastcar/api/my_notification")
    Call<List<notificationData>>getcontacts_Notification(@Field("user_id") int user_id);

    @Multipart
    @POST("fastcar/api/add_car")
    Call<ResponseBody>getcontacts_addCAr(@Part MultipartBody.Part image,@Part MultipartBody.Part image2
           , @Part MultipartBody.Part image3,@Part MultipartBody.Part image4, @Part("model") RequestBody name,
                                                     @Part("color") RequestBody phone, @Part("brand") RequestBody email,
                                                     @Part("number") RequestBody password, @Part("user_id") RequestBody phone_code);

    @FormUrlEncoded
    @POST("fastcar/api/calcult_coust")
    Call<CarDistance>getcontacts_Distance( @Field("from_latitude") double from_latitude,
                                           @Field("from_longitude") double from_longitude,
                                           @Field("to_latitude") double to_latitude,
                                           @Field("to_longitude") double to_longitude,
                                           @Field("country_id") int country_id);
    @FormUrlEncoded
    @POST("fastcar/api/nearstDriver")
    Call<NearestCar>getcontacts_Nearest( @Field("from_latitude") double from_latitude,
                                         @Field("from_longitude") double from_longitude );

    @FormUrlEncoded
    @POST("fastcar/api/get_track")
    Call<List<Track>>getcontacts_Track(@Field("user_id") int user_id,@Field("order_id") int order_id);

    @FormUrlEncoded
    @POST("fastcar/api/add_track")
    Call<ResponseBody>getcontacts_AddTrack(@Field("user_id") int user_id,@Field("order_id") int order_id,
                                           @Field("latitude") double from_latitude,
                                           @Field("longitude") double from_longitude,@Field("time") int time);

    @FormUrlEncoded
    @POST("fastcar/api/last_order")
    Call<Order>getcontacts_lastOrder(@Field("user_id") int user_id);
    @FormUrlEncoded
    @POST("fastcar/api/update_profile")
    Call<Edit> getcontacts_updatelatlng( @Field("latitude") double from_latitude,
                                                      @Field("longitude") double from_longitude, @Field("user_id") int user_id);


    @FormUrlEncoded
    @POST("fastcar/api/my_wallet")
    Call<Wallet>getwallet(@Field("user_id") int user_id);

    @FormUrlEncoded
    @POST("fastcar/api/update_status")
    Call<Status>getcontacts_updatebusy(@Field("user_id") int user_id,@Field("is_busy") String is_busy);

    @FormUrlEncoded
    @POST("fastcar/api/update_profile")
    Call<ResponseBody> update_password(@Field("user_id")int id,@Field("password")String password);

    @FormUrlEncoded
    @POST("fastcar/api/fireBase_token")
    Call<ResponseBody>getcontacts_AddToken(@Field("user_id") int user_id,@Field("phone_token") String phone_token,
                                           @Field("software_type") int software_type);
    @FormUrlEncoded
    @POST("fastcar/api/finshing_order")
    Call<ResponseBody>getcontacts_finishOrder(@Field("user_id") int user_id,@Field("order_id") int order_id);

}

