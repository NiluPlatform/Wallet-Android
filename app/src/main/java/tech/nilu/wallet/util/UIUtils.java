package tech.nilu.wallet.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.ColorInt;

/**
 * Created by root on 11/12/17.
 */

public class UIUtils {
    public static void hideKeyboard(Context context, View view) {
        if (view != null) {
            Object inputMethodService = context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodService != null)
                ((InputMethodManager) inputMethodService).hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static ProgressDialog getProgressDialog(Context context, boolean cancelable, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(cancelable);
        dialog.setMessage(message);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }

    public static void setListViewHeightBasedOnChildren(ListView lv) {
        ListAdapter adapter = lv.getAdapter();
        if (adapter == null) return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(lv.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View v = null;
        for (int i = 0; i < adapter.getCount(); i++) {
            v = adapter.getView(i, v, lv);
            if (i == 0)
                v.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            v.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += v.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = lv.getLayoutParams();
        params.height = totalHeight + (lv.getDividerHeight() * (adapter.getCount() - 1));
        lv.setLayoutParams(params);
        lv.requestLayout();
    }

    @ColorInt
    public static int getColor(Context context, int resId) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(resId, typedValue, true);
        return typedValue.data;
    }
}
