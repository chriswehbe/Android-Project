package com.example.teachersapp;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @POST("/loginAndroid")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/signup")
    Call<Void> executeSignup(@Body HashMap<String, String> map);

    @POST("/signupCheck")
    Call<Void> checkExistingUser(@Body HashMap<String, String> map);

    @GET("/getmaterials")
    Call<List<Material>> getMaterials();

    @POST("/creatematerial")
    Call<Void> createMaterial(@Body HashMap<String, String> map);

    @GET("/getquestionstype")
    Call<List<QuestionType>> getQuestionTypes();

    @GET("/getassesmenttype")
    Call<List<AssessmentType>> getAssessmentTypes();

    @POST("/postassesmentAndroid")
    Call<Assessment> postAssessment(@Body HashMap<String, String> map);

    @POST("/createAssesment")
    Call<Void> createAssessment(@Body HashMap<String, Object> map);

    @GET("/getassesments")
    Call<List<AssessmentItem>> getAssessments();

    @POST("/activateassesment")
    Call<Void> activateAssessment(@Body HashMap<String, String> map);

    @POST("/isanswered")
    Call<IsAnswerd> isAnswered(@Body HashMap<String, String> map);
    @POST("/getquestions")
    Call<List<Question>> getquestions(@Body HashMap<String, String> map);

    @POST("/insertanswers")
    Call<Void> insertAnswers(@Body HashMap<String, Object> map);

    @POST("/useransweredandroid")
    Call<List<UserItem>> getUserAnswers(@Body HashMap<String, String> map);

    @POST("/getquestionsandanswers")
    Call<List<QuestionAnswerItem>> getQuestionsAndAnswers(@Body HashMap<String, String> map);

}
