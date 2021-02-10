package com.otpl.newcreate.network;
import com.otpl.newcreate.data.model.api.ApiResponse;
import com.otpl.newcreate.data.model.api.District;
import com.otpl.newcreate.data.model.api.HunarInterest;
import com.otpl.newcreate.data.model.api.LoggedInUser;
import com.otpl.newcreate.data.model.api.Sector;
import com.otpl.newcreate.data.model.api.Skills;
import com.otpl.newcreate.data.model.api.SpinnerItem;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiHelper {
    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.GET_MASTERS)
    Call<ApiResponse<List<District>>> getDistrict(@Query("ProcId") int procId);

    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.REGISTRATION)
    Call<ApiResponse> registration(@Body RequestBody requestBody);

    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.LOGIN)
    Call<ApiResponse<LoggedInUser>> login(@Body RequestBody requestBody);

    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.FORGOT_PASSWORD+"/{Mobile}")
    Call<ApiResponse> requestOtp(@Path("Mobile") String Mobile);

    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.VERIFY_OTP)
    Call<ApiResponse> verifyOtp(@Body RequestBody requestBody);

    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.GET_MASTERS)
    Call<ApiResponse<List<SpinnerItem>>> getSpinnerItem(@Query("ProcId") int procId);

    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.ADD_SKILL)
    Call<ApiResponse> addSkills(@Body RequestBody requestBody);

    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.VIEW_SKILL)
    Call<ApiResponse<List<Skills>>> getSkills(@Query("uid") String uid);

    @Multipart
    @POST(ApiEndPoint.UPDATE_PROFILE)
    Call<ApiResponse> updateProfile(@PartMap Map<String, RequestBody> bodyMap,
                                    @Part MultipartBody.Part profileFile);

    @Multipart
    @POST(ApiEndPoint.UPDATE_PROFILE)
    Call<ApiResponse> updateProfileWithoutPic(@PartMap Map<String, RequestBody> bodyMap);

    @Headers({"Content-Type: application/json"})
    @GET(ApiEndPoint.SKILLS_LIST)
    Call<ApiResponse<List<Sector>>> getSkillsList(@Query("sid") String sid, @Query("userId") String userId);

    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.ADD_INTEREST)
    Call<ApiResponse<HunarInterest>> addInterest(@Body RequestBody requestBody);

    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.DELETE_INTEREST)
    Call<ApiResponse> deleteInterest(@Body RequestBody requestBody);

    @Headers({"Content-Type: application/json"})
    @POST(ApiEndPoint.CHANGE_PASSWORD)
    Call<ApiResponse> changePassword(@Body RequestBody requestBody);
}
