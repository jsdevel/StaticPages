/*
 * Copyright 2013 Joseph Spencer.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.spencernetdevelopment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 *
 * @author Joseph Spencer
 */
public class ExternalLinkValidator<T> implements Callable<T> {
   private final URL url;
   private final StaticPagesConfiguration config;

   public ExternalLinkValidator(StaticPagesConfiguration config, URL path) {
      this.config=config;
      this.url = path;
   }

   @Override
   public T call() throws Exception {
      try {
         Logger.info("Validating: "+url);
         HttpURLConnection http = (HttpURLConnection)url.openConnection();
         http.setRequestMethod("GET");
         http.setConnectTimeout(config.getMaxTimeToWaitForExternalLinkValidation());
         http.connect();


         switch(http.getResponseCode()){
            case 200:
               return null;
            default:
               Logger.error("External link validation failed for the following URL: "+url);
               Logger.error("The status code of the http connection was: "+http.getResponseCode());
         }
      } catch (SocketTimeoutException ex){
         Logger.error("A connection to the following URL couldn't be established during the configured timeout period: "+url);
      } catch (IOException ex) {
         Logger.error("An IOException occurred while attempting to validate the following external URL: "+url);
         Logger.error("Here is the detailed message: "+ex.getMessage());
      }
      throw new IOException("Validation failed for: "+url);
   }

}
