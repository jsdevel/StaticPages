/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Joseph Spencer
 */
public class HttpExternalLinkResponse {
   public final String requestedURL;
   public final String requestedMethod;
   private final Map<String, List<String>> responseHeaders;
   public final int responseCode;

   public HttpExternalLinkResponse(
      String url,
      String method,
      Map<String, List<String>> headers,
      int statusCode
   ) {
      requestedURL=url;
      requestedMethod=method;
      responseHeaders=headers;
      responseCode=statusCode;
   }
   public List<String> getHeader(String header){
      return responseHeaders.get(header);
   }
}
