package com.ibm.watsonwork;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watsonwork.client.WatsonWorkClient;
import com.ibm.watsonwork.client.AuthClient;
import com.ibm.watsonwork.client.WolframAlphaClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Configuration
public class WatsonWorkConfiguration {

    private static final String WATSONWORK = "watsonwork";
    private static final String WOLFRAM_ALPHA = "wolfram-alpha";
    @Autowired
    private WatsonWorkProperties watsonWorkProperties;

    @Autowired
    private WolframAlphaProperties wolframAlphaProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }

    @Primary
    @Bean(name = WATSONWORK)
    public Retrofit retrofit(OkHttpClient client) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .baseUrl(watsonWorkProperties.getApiUri())
                .client(client)
                .build();
    }

    @Bean(name = WOLFRAM_ALPHA)
    public Retrofit retrofitWolframAlpha(OkHttpClient client) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .baseUrl(wolframAlphaProperties.getApiUri())
            .client(client)
            .build();
    }

    @Bean
    public WatsonWorkClient watsonWorkClient(@Qualifier(value = WATSONWORK) Retrofit retrofit) {
        return retrofit.create(WatsonWorkClient.class);
    }

    @Bean
    public WolframAlphaClient wolframAlphaClient(@Qualifier(value = WOLFRAM_ALPHA) Retrofit retrofit) {
        return retrofit.create(WolframAlphaClient.class);
    }

    @Bean
    public AuthClient authClient(Retrofit retrofit) {
        return retrofit.create(AuthClient.class);
    }
}
