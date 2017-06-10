/*
 * IBM Confidential
 *
 * OCO Source Materials
 *
 * Copyright IBM Corp. 2017
 *
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U.S. Copyright Office.
 */

package com.ibm.watsonwork.client;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WolframAlphaClient {
    @GET("/v1/result")
    Call<String> shortAnswer(@Query("appid") String appId, @Query("i") String input);
}
