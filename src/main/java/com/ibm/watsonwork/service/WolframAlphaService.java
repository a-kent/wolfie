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

package com.ibm.watsonwork.service;

import java.io.UnsupportedEncodingException;

import com.ibm.watsonwork.model.WebhookEvent;

public interface WolframAlphaService extends Service {
    void getShortAnswer(WebhookEvent webhookEvent) throws UnsupportedEncodingException;
}
