package tech.nilu.wallet.widget;

import android.content.Context;
import android.util.TypedValue;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by root on 2/12/18.
 */

public class GridAutofitLayoutManager extends GridLayoutManager {
    private int columnWidth;
    private boolean columnWidthChanged = true;

    public GridAutofitLayoutManager(Context context, int columnWidth) {
        super(context, 1);
        setColumnWidth(checkedColumnWidth(context, columnWidth));
    }

    public GridAutofitLayoutManager(Context context, int columnWidth, int orientation, boolean reverseLayout) {
        super(context, 1, orientation, reverseLayout);
        setColumnWidth(checkedColumnWidth(context, columnWidth));
    }

    private int checkedColumnWidth(Context context, int columnWidth) {
        if (columnWidth <= 0)
            /* Set default columnWidth value (48dp here). It is better to move this constant
            to static constant on top, but we need context to convert it to dp, so can't really
            do so. */
            columnWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    48,
                    context.getResources().getDisplayMetrics());
        return columnWidth;
    }

    private void setColumnWidth(int newColumnWidth) {
        if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
            columnWidth = newColumnWidth;
            columnWidthChanged = true;
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int width = getWidth();
        int height = getHeight();
        if (columnWidthChanged && columnWidth > 0 && width > 0 && height > 0) {
            int totalSpace;
            if (getOrientation() == VERTICAL)
                totalSpace = width - getPaddingRight() - getPaddingLeft();
            else
                totalSpace = height - getPaddingTop() - getPaddingBottom();
            int spanCount = Math.max(1, totalSpace / columnWidth);
            setSpanCount(spanCount);
            columnWidthChanged = false;
        }
        super.onLayoutChildren(recycler, state);
    }
}
