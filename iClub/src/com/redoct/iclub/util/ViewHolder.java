package com.redoct.iclub.util;

import android.util.SparseArray;
import android.view.View;

/**
 * ViewHolder
 * 
 * @author �̲�
 * @date  2014��6��16��
 */
public class ViewHolder
{
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id)
    {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null)
        {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        
        View childView = viewHolder.get(id);
        if (childView == null)
        {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        
        return (T) childView;
    }
}