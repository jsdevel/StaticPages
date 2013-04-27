/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spencernetdevelopment;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents several assets that are meant to be grouped together
 * into a single file as part of a transaction.  It is assumed that instances
 * will interact with a GroupedAssetTransactionManager.<br/>
 *
 * <p><b>Main points to consider</b></p>
 * <ol>
 *    <li>
 * The transaction allows the same asset to occur multiple times within a group.
 * This facilitates the case where a script or other resource may wish to be
 * duplicated multiple times to avoid repetition in code, and other
 * unforeseeable usages.  It is therefore imperative that clients add only the
 * resources intended.
 *    </li>
 *    <li>
 * The transaction <i>must</i> be closed before any processing may be performed
 * on the underlying data.
 *    </li>
 *    <li>
 * The identifier is <i>based</i> on a Base64 encoded unique representation for
 * any given set of URLs.  The identifier will <i>not</i> contain any '/' or '='
 * which are apart of the Base64 character set.  It is assumed that the
 * identifier will be used in the filename of the resulting asset once
 * processing is complete.
 *    </li>
 *    <li>
 * List is used to hold assets in the transaction, and ordering is guaranteed
 * to occur as given.
 *    </li>
 * </ol>
 *
 * @author Joseph Spencer
 */
public class GroupedAssetTransaction {
   private final String type;
   private final boolean isCompressed;
   private final List<String> urls = new ArrayList<>();
   private boolean isOpenForAdditions=true;
   private String identifier;

   /**
    * Sets isCompressed to true.<br/>
    * See: {@link GroupedAssetTransaction#GroupedAssetTransaction(java.lang.String, boolean)}
    * @param type
    */
   public GroupedAssetTransaction(String type){
      this(type, true);
   }
   /**
    * @param type one of 'js', 'css'
    * @param compress Sets whether or not resources should be compressed.
    * @throws NullPointerException If type is null.
    * @throws IllegalArgumentException If type is <i>not</i> one of: js, css
    */
   public GroupedAssetTransaction(String type, boolean compress)
      throws NullPointerException, IllegalArgumentException
   {
      if(type == null){
         throw new NullPointerException("type was null");
      }
      switch(type){
      case "css":
      case "js":
         break;
      default:
         throw new IllegalArgumentException("Unknown type: "+type);
      }
      this.type=type;
      this.isCompressed=compress;
   }

   /**
    * Adds a URL to the underlying list.
    *
    * @param url
    * @throws IllegalStateException If the group is closed.
    * @throws NullPointerException If url is null.
    * @throws IllegalArgumentException If url is empty.
    */
   public void addURL(String url)
      throws IllegalStateException,
             NullPointerException,
             IllegalArgumentException
   {
      if(!isOpenForAdditions){
         throw new IllegalStateException(
            "This grouped resource is closed for additional urls.  "+
            "Couldn't add the following url: "+url
         );
      }
      if(url == null){
         throw new NullPointerException("url was null");
      }
      if(url.trim().isEmpty()){
         throw new IllegalArgumentException("The given url was empty.");
      }
      urls.add(FileUtils.getForcedRelativePath(url, "/"));
   }

   /**
    * Returns a URL friendly identifier that is unique to any set of URLs.
    *
    * @throws IllegalStateException If the transaction hasn't been closed.
    * @return an identifier based on the following algorithm:
    * <ol>
    *    <li>Create a new string as the value of the number of URLs to identify.</li>
    *    <li>Create temporary URLs prefixed with '_'</li>
    *    <li>Join all temporary URLs with '_'</li>
    *    <li>Base64 encode the result</li>
    *    <li>Replace any '/' characters with '_'</li>
    *    <li>Strip all '=' padding</li>
    * </ol>
    */
   public String getIdentifier() throws IllegalStateException {
      protectState();
      if(identifier == null){
         identifier = ""+urls.size();
         for(String url:urls){
            identifier += "_";
            identifier += url;
         }
         //use base64 to avoid a URL with directories, but replace '/' with '_'
         //and strip '='.
         identifier = Base64.encodeToString(
               identifier.getBytes(), false
            ).replace('/', '_').replace("=", "");
      }
      return identifier;
   }

   /**
    * @return the type of this group, typically one of: 'js' or 'css'.
    */
   public String getType(){
      return type;
   }
   /**
    * @throws IllegalStateException When the close method has not been called,
    * @return A fixed length array of the URLs provided.
    */
   public String[] toArray() throws IllegalStateException {
      protectState();
      return urls.toArray(new String[]{});
   }

   /**
    * Used to determine if the transaction is closed.
    * @return
    */
   public boolean isClosed(){
      return !isOpenForAdditions;
   }

   /**
    * Used to determine if the assets are to be compressed.
    * @return
    */
   public boolean isCompressed(){
      return isCompressed;
   }

   /**
    * Ensures that URLs have been added to this group, and updates the internal
    * state to allow further processing.
    * @throws IllegalStateException If no URLs have been added to this group.
    */
   public void close() throws IllegalStateException {
      if(urls.size() < 1){
         throw new IllegalStateException(
            "Can't close this resource, no urls have been added.");
      }
      isOpenForAdditions=false;
   }

   /**
    * @throws IllegalStateException If the group hasn't been closed.
    */
   private void protectState() throws IllegalStateException {
      if(isOpenForAdditions){
         throw new IllegalStateException(
            "This grouped resource hasn't been closed."
         );
      }
   }
}