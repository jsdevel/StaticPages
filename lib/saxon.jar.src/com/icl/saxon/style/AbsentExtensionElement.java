// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   AbsentExtensionElement.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement

public class AbsentExtensionElement extends StyleElement
{

            public AbsentExtensionElement()
            {
            }

            public boolean mayContainTemplateBody()
            {
/*  19*/        return true;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
            }

            public void processAllAttributes()
                throws TransformerConfigurationException
            {
/*  31*/        if(!isTopLevel() || !forwardsCompatibleModeIsEnabled())
/*  34*/            super.processAllAttributes();
            }

            public void validate()
                throws TransformerConfigurationException
            {
            }

            public void validateSubtree()
                throws TransformerConfigurationException
            {
/*  47*/        if(!isTopLevel() || !forwardsCompatibleModeIsEnabled())
/*  50*/            super.validateSubtree();
            }

            public void process(Context context)
                throws TransformerException
            {
/*  55*/        if(!isTopLevel() || !forwardsCompatibleModeIsEnabled())
/*  56*/            throw super.validationError;
/*  58*/        else
/*  58*/            return;
            }
}
