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

package com.ibm.watsonwork.service.impl;

import com.ibm.watsonwork.WatsonWorkConstants;
import com.ibm.watsonwork.WolframAlphaProperties;
import com.ibm.watsonwork.client.WolframAlphaClient;
import com.ibm.watsonwork.model.WebhookEvent;
import com.ibm.watsonwork.service.WatsonWorkService;
import com.ibm.watsonwork.service.WolframAlphaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ibm.watsonwork.utils.MessageUtils.buildMessage;

@Service
@Slf4j
public class DefaultWolframAlphaService implements WolframAlphaService {
    @Autowired
    private WolframAlphaClient wolframAlphaClient;
    @Autowired
    private WolframAlphaProperties wolframAlphaProperties;
    @Autowired
    private WatsonWorkService watsonWorkService;

    @Override
    public void getShortAnswer(WebhookEvent webhookEvent) {
        String input = webhookEvent.getContent().toLowerCase()
            .replace(WatsonWorkConstants.APP_INVOCATION_TRIGGER, StringUtils.EMPTY).trim();
        Call<String> call = wolframAlphaClient.shortAnswer(wolframAlphaProperties.getAppId(), input);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String answer = response.body();
                log.info("Input: {}; Answer: {}", input, answer);
                if (answer == null) {
                    watsonWorkService.createMessage(webhookEvent.getSpaceId(),
                        buildMessage(WatsonWorkConstants.APP_MESSAGE_TITLE, "Sorry, I can't answer that one..."));
                } else {
                    watsonWorkService.createMessage(webhookEvent.getSpaceId(),
                        buildMessage(WatsonWorkConstants.APP_MESSAGE_TITLE, answer));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                log.error("Short answers API call failed. ", t);
                watsonWorkService.createMessage(webhookEvent.getSpaceId(),
                    buildMessage(WatsonWorkConstants.APP_MESSAGE_TITLE, "Sorry, something went wrong"));
            }
        });
    }
}
