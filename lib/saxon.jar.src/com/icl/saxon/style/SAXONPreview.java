// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   SAXONPreview.java

package com.icl.saxon.style;

import com.icl.saxon.Context;
import com.icl.saxon.PreviewManager;
import com.icl.saxon.om.NamespaceException;
import com.icl.saxon.tree.*;
import java.util.StringTokenizer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

// Referenced classes of package com.icl.saxon.style:
//            StyleElement, StandardNames, XSLStyleSheet

public class SAXONPreview extends StyleElement
{

            int previewModeNameCode;
            String elements;

            public SAXONPreview()
            {
/*  21*/        previewModeNameCode = -1;
/*  22*/        elements = null;
            }

            public void prepareAttributes()
                throws TransformerConfigurationException
            {
/*  27*/        StandardNames standardnames = getStandardNames();
/*  28*/        AttributeCollection attributecollection = getAttributeList();
/*  30*/        for(int i = 0; i < attributecollection.getLength(); i++)
                {
/*  31*/            int j = attributecollection.getNameCode(i);
/*  32*/            int k = j & 0xfffff;
/*  33*/            if(k == standardnames.MODE)
                    {
/*  34*/                String s = attributecollection.getValue(i);
/*  36*/                try
                        {
/*  36*/                    previewModeNameCode = makeNameCode(s, false);
                        }
/*  38*/                catch(NamespaceException namespaceexception)
                        {
/*  38*/                    compileError(namespaceexception.getMessage());
                        }
                    } else
/*  40*/            if(k == standardnames.ELEMENTS)
/*  41*/                elements = attributecollection.getValue(i);
/*  43*/            else
/*  43*/                checkUnknownAttribute(j);
                }

/*  47*/        if(previewModeNameCode == -1)
/*  48*/            reportAbsence("mode");
/*  50*/        if(elements == null)
/*  51*/            reportAbsence("elements");
            }

            public void validate()
                throws TransformerConfigurationException
            {
/*  56*/        checkTopLevel();
            }

            public void preprocess()
                throws TransformerConfigurationException
            {
/*  61*/        XSLStyleSheet xslstylesheet = getPrincipalStyleSheet();
/*  62*/        PreviewManager previewmanager = xslstylesheet.getPreviewManager();
/*  63*/        if(previewmanager == null)
                {
/*  64*/            previewmanager = new PreviewManager();
/*  65*/            xslstylesheet.setPreviewManager(previewmanager);
                }
/*  67*/        previewmanager.setPreviewMode(previewModeNameCode);
/*  69*/        for(StringTokenizer stringtokenizer = new StringTokenizer(elements); stringtokenizer.hasMoreTokens();)
                {
/*  71*/            String s = stringtokenizer.nextToken();
/*  73*/            try
                    {
/*  73*/                previewmanager.setPreviewElement(makeNameCode(s, true) & 0xfffff);
                    }
/*  75*/            catch(NamespaceException namespaceexception)
                    {
/*  75*/                compileError(namespaceexception.getMessage());
                    }
                }

            }

            public void process(Context context)
                throws TransformerException
            {
            }
}
