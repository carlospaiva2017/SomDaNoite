package br.com.fafeltech.somdanoite.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.helper.CustomProgressDialog;

public class BaseDialog extends AlertDialog {
    protected BaseDialog(Context context) {
        super(context);
    }

    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected BaseDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    public CustomProgressDialog progressDialog;

    public void toast(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new CustomProgressDialog(getContext());
        }
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public double convertStringToDoubleFrance(String s){
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        double d = 0;
        try {
            Number number = format.parse(s);
            d = number.doubleValue();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            Log.e("convertToNumber", "Erro ao converter String para número");
        }
        return d;
    }

    public double convertStringToDoubleEnglish(String s){
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        double d = 0;
        try {
            Number number = format.parse(s);
            d = number.doubleValue();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            Log.e("convertToNumber", "Erro ao converter String para número");
        }
        return d;
    }

    public String convertDoubleToStringFrance(double d){
        return String.format(Locale.FRANCE, "%.2f", d);
    }

    public String convertDoubleToStringEnglish(double d){
        return String.format(Locale.ENGLISH, "%.2f", d);
    }

    public void snack(String msg){
        Snackbar.make (findViewById(android.R.id.content), msg,
                Snackbar.LENGTH_LONG)
                .setActionTextColor(getContext().getResources().getColor(R.color.colorPrimary))
                .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }
                ).show();
    }

    public static Bitmap convertViewToBitmap(View view)
    {
        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        );
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();

        return view.getDrawingCache();
    }
}
