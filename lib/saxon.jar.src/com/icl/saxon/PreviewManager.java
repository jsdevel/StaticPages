// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   PreviewManager.java

package com.icl.saxon;


public class PreviewManager
{

            private int previewModeNameCode;
            private int previewElements[];
            private int used;

            public PreviewManager()
            {
/*  18*/        previewModeNameCode = -1;
/*  19*/        previewElements = new int[10];
/*  20*/        used = 0;
            }

            public void setPreviewMode(int i)
            {
/*  39*/        previewModeNameCode = i;
            }

            public final int getPreviewMode()
            {
/*  43*/        return previewModeNameCode;
            }

            public void setPreviewElement(int i)
            {
/*  53*/        if(used >= previewElements.length)
                {
/*  54*/            int ai[] = new int[used * 2];
/*  55*/            System.arraycopy(previewElements, 0, ai, 0, used);
/*  56*/            previewElements = ai;
                }
/*  58*/        previewElements[used++] = i;
            }

            public boolean isPreviewElement(int i)
            {
/*  66*/        for(int j = 0; j < used; j++)
/*  67*/            if(i == previewElements[j])
/*  67*/                return true;

/*  69*/        return false;
            }
}
