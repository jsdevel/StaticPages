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
   exclude-result-prefixes="e"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <!-- All extension elements should exist within the "extensions" namespace.
   -->
   <xsl:template match="e:*">
      <xsl:message terminate="yes">
   Unknown extension element 'e:<xsl:value-of select="local-name(.)"/>'.
   The following attributes were found on this element:
         <xsl:for-each select="@*">
            <xsl:value-of select="concat(local-name(.), '=', .)"/>
         </xsl:for-each>
      </xsl:message>
   </xsl:template>

</xsl:stylesheet>
