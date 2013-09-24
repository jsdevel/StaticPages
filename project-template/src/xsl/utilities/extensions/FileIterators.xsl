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
   xmlns:e="extension"
   xmlns:E="com.spencernetdevelopment.Extensions"
   xmlns:FI="com.spencernetdevelopment.FileIterator"
   xmlns:VM="com.spencernetdevelopment.VariableManager"
   exclude-result-prefixes="e E FI VM"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:param name="E"/>
   <xsl:param name="VM"/>

   <xsl:template match="e:forAssets[@in]">
      <xsl:param name="itr" select="E:makeAssetsIterator(
         $E,
         string(@in),
         string(@extension),
         @recursive
      )"/>

      <xsl:if test="FI:hasNext($itr)">
         <xsl:value-of select="VM:addContext($VM)"/>
         <xsl:value-of select="FI:takeNextIntoVariableContext($itr, $VM)"/>

         <xsl:apply-templates/>

         <xsl:value-of select="VM:removeContext($VM)"/>

         <xsl:apply-templates select=".">
            <xsl:with-param name="itr" select="$itr"/>
         </xsl:apply-templates>
      </xsl:if>
   </xsl:template>

</xsl:stylesheet>
