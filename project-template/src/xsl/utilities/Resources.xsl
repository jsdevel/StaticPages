<?xml version="1.0" encoding="UTF-8"?>
<!--
   Copyright 2012 Joseph Spencer.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->
<xsl:stylesheet version="1.0"
                xmlns:p="phrases"
                xmlns:d="default"
                xmlns:assets="com.spencernetdevelopment.xsl.Assets"
                exclude-result-prefixes="p d assets"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:param name="xmlResourcesPath"/>

   <xsl:template match="p:*">
      <xsl:choose>
         <xsl:when test="assets:assertXmlResourceExists('phrases.xml')">
            <xsl:variable name="id" select="local-name()"/>
            <xsl:variable name="phrase" select="document(
               concat($xmlResourcesPath, '/phrases.xml')
            )/d:phrases/d:phrase[@id=$id]"/>
            <xsl:choose>
               <xsl:when test="string($phrase) = ''">
                  <xsl:message>
phrases.xml:  Couldn't find phrase with id: <xsl:value-of select="$id"/>
                  </xsl:message>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:apply-templates select="$phrase"/>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:when>
         <xsl:otherwise>
            <xsl:message>
Couldn't find <xsl:value-of select="$xmlResourcesPath"/>/phrases.xml.
            </xsl:message>

         </xsl:otherwise>
      </xsl:choose>
   </xsl:template>

</xsl:stylesheet>
