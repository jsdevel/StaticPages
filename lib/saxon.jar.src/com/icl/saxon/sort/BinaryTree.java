// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) fieldsfirst lnc 
// Source File Name:   BinaryTree.java

package com.icl.saxon.sort;

import java.io.PrintStream;
import java.util.Vector;

// Referenced classes of package com.icl.saxon.sort:
//            StringComparer, UppercaseFirstComparer, DoubleComparer, Comparer

public class BinaryTree
{
    private class BinaryTreeNode
    {

                BinaryTreeNode left;
                BinaryTreeNode right;
                Object key;
                Object value;

                public boolean isDeleted()
                {
/* 284*/            return value == null;
                }

                public void delete()
                {
/* 287*/            value = null;
                }

                public BinaryTreeNode(Object obj, Object obj1)
                {
/* 277*/            left = null;
/* 278*/            right = null;
/* 279*/            key = obj;
/* 280*/            value = obj1;
                }
    }


            private BinaryTreeNode root;
            private Comparer comparer;
            private BinaryTreeNode prev;
            private boolean ascending;
            private boolean allowDuplicates;

            public BinaryTree()
            {
/*  49*/        root = null;
/*  50*/        ascending = true;
/*  51*/        allowDuplicates = false;
            }

            public void setAscending(boolean flag)
            {
/*  61*/        ascending = flag;
            }

            public void setDuplicatesAllowed(boolean flag)
            {
/*  71*/        allowDuplicates = flag;
            }

            public void setComparer(Comparer comparer1)
            {
/*  80*/        if(isEmpty())
/*  80*/            comparer = comparer1;
            }

            public Vector getValues()
            {
/*  89*/        BinaryTreeNode binarytreenode = root;
/*  90*/        Vector vector = new Vector();
/*  91*/        getValues(root, vector);
/*  92*/        return vector;
            }

            private void getValues(BinaryTreeNode binarytreenode, Vector vector)
            {
/* 101*/        if(binarytreenode != null)
                {
/* 102*/            getValues(binarytreenode.left, vector);
/* 103*/            if(!binarytreenode.isDeleted())
/* 104*/                vector.addElement(binarytreenode.value);
/* 106*/            getValues(binarytreenode.right, vector);
                }
            }

            public Vector getKeys()
            {
/* 116*/        BinaryTreeNode binarytreenode = root;
/* 117*/        Vector vector = new Vector();
/* 118*/        getKeys(root, vector);
/* 119*/        return vector;
            }

            private void getKeys(BinaryTreeNode binarytreenode, Vector vector)
            {
/* 128*/        if(binarytreenode != null)
                {
/* 129*/            getKeys(binarytreenode.left, vector);
/* 130*/            if(!binarytreenode.isDeleted())
/* 131*/                vector.addElement(binarytreenode.key);
/* 133*/            getKeys(binarytreenode.right, vector);
                }
            }

            public Object get(Object obj)
            {
/* 146*/        BinaryTreeNode binarytreenode = find(obj);
/* 147*/        return binarytreenode != null ? binarytreenode.value : null;
            }

            public boolean isEmpty()
            {
/* 157*/        return size() == 0;
            }

            public Object put(Object obj, Object obj1)
            {
/* 170*/        if(obj == null || obj1 == null)
/* 170*/            throw new NullPointerException();
/* 172*/        BinaryTreeNode binarytreenode = root;
/* 174*/        if(comparer == null)
/* 175*/            comparer = new StringComparer();
/* 178*/        if(root == null)
                {
/* 179*/            root = new BinaryTreeNode(obj, obj1);
/* 180*/            return null;
                }
/* 183*/        do
                {
                    int i;
/* 183*/            do
                    {
/* 183*/                i = comparer.compare(binarytreenode.key, obj);
/* 184*/                if(!ascending)
/* 184*/                    i = 0 - i;
/* 185*/                if(i == 0 && allowDuplicates)
/* 185*/                    i = -1;
/* 186*/                if(i > 0)
                        {
/* 187*/                    if(binarytreenode.left == null)
                            {
/* 188*/                        binarytreenode.left = new BinaryTreeNode(obj, obj1);
/* 189*/                        return null;
                            }
/* 191*/                    binarytreenode = binarytreenode.left;
                        }
/* 193*/                if(i == 0)
                        {
/* 194*/                    Object obj2 = binarytreenode.value;
/* 195*/                    binarytreenode.value = obj1;
/* 196*/                    return obj2;
                        }
                    } while(i >= 0);
/* 199*/            if(binarytreenode.right == null)
                    {
/* 200*/                binarytreenode.right = new BinaryTreeNode(obj, obj1);
/* 201*/                return null;
                    }
/* 203*/            binarytreenode = binarytreenode.right;
                } while(true);
            }

            public Object remove(Object obj)
            {
/* 221*/        BinaryTreeNode binarytreenode = find(obj);
/* 223*/        if(binarytreenode == null)
                {
/* 223*/            return null;
                } else
                {
/* 224*/            Object obj1 = binarytreenode.value;
/* 225*/            binarytreenode.delete();
/* 226*/            return obj1;
                }
            }

            public int size()
            {
/* 236*/        return count(root);
            }

            private int count(BinaryTreeNode binarytreenode)
            {
/* 245*/        if(binarytreenode == null)
/* 245*/            return 0;
/* 246*/        else
/* 246*/            return count(binarytreenode.left) + (binarytreenode.isDeleted() ? 0 : 1) + count(binarytreenode.right);
            }

            private BinaryTreeNode find(Object obj)
            {
/* 255*/        BinaryTreeNode binarytreenode = root;
/* 257*/        if(binarytreenode == null)
/* 257*/            return null;
/* 259*/        while(binarytreenode != null) 
                {
/* 259*/            int i = comparer.compare(obj, binarytreenode.key);
/* 260*/            if(i < 0)
/* 260*/                binarytreenode = binarytreenode.left;
/* 261*/            if(i == 0)
/* 261*/                return binarytreenode.isDeleted() ? null : binarytreenode;
/* 262*/            if(i > 0)
/* 262*/                binarytreenode = binarytreenode.right;
                }
/* 264*/        return null;
            }

            public static void main(String args[])
                throws Exception
            {
/* 298*/        BinaryTree binarytree = new BinaryTree();
/* 299*/        binarytree.setComparer(new UppercaseFirstComparer());
/* 300*/        binarytree.setAscending(true);
/* 301*/        binarytree.setDuplicatesAllowed(true);
/* 302*/        binarytree.put("a", "1");
/* 303*/        binarytree.put("b", "2");
/* 304*/        binarytree.put("c", "3");
/* 305*/        binarytree.put("aa", "4");
/* 306*/        binarytree.put("ab", "5");
/* 307*/        binarytree.put("A", "6");
/* 308*/        binarytree.put("A", "6a");
/* 309*/        binarytree.put("B", "7");
/* 310*/        binarytree.put("AA", "8");
/* 311*/        binarytree.put("XYZ", "9");
/* 312*/        binarytree.put("", "10");
/* 313*/        System.out.println(binarytree.getKeys());
/* 314*/        System.out.println(binarytree.getValues());
/* 316*/        binarytree = new BinaryTree();
/* 317*/        binarytree.setComparer(new DoubleComparer());
/* 318*/        binarytree.setAscending(false);
/* 319*/        binarytree.put(new Double(1.4299999999999999D), "1");
/* 320*/        binarytree.put(new Double(84.200000000000003D), "2");
/* 321*/        binarytree.put(new Double(-100D), "3");
/* 322*/        binarytree.put(new Double(0.0D), "4");
/* 323*/        System.out.println(binarytree.getKeys());
/* 324*/        System.out.println(binarytree.getValues());
            }
}
