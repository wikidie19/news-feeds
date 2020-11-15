package com.newsfeeds.network

import com.newsfeeds.base.BaseResponse
import com.newsfeeds.model.articlesearch.ResponseArticle
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @see EndPoint to view path of url API request
 * @see ParamKey to view name of param key for API request
 */
interface ApiInterface {

    @GET(EndPoint.articleSearch)
    fun getFeedsArticle(
        @Query(ParamKey.Query) query: String?,
        @Query(ParamKey.ApiKey) apiKey: String?,
        @Query(ParamKey.Page) page: Int?
    ): Observable<BaseResponse<ResponseArticle>>

    /*@FormUrlEncoded
    @POST(EndPoint.login)
    fun login(
        @Field(ParamKey.NBParamUserName) username: String,
        @Field(ParamKey.NBParamPassword) password: String
    ): Observable<LoginResponse>

    @GET("workflow/{workflowId}/sync")
    fun syncWorkflow(
        @Path(ParamKey.NBWorkflowId) workflowId: String?
    ): Observable<JSONObject>*/

}

