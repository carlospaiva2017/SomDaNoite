package br.com.fafeltech.somdanoite.fragment;

import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.Locale;

import br.com.fafeltech.somdanoite.R;
import br.com.fafeltech.somdanoite.helper.CustomProgressDialog;

public class BaseFragment extends livroandroid.lib.fragment.BaseFragment{

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

    public CustomProgressDialog progressDialog;

    public void toast(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new CustomProgressDialog(getActivity());
        }
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void snack( String msg){
        Snackbar.make (getActivity().findViewById(android.R.id.content), msg,
                Snackbar.LENGTH_LONG)
                .setActionTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }
                ).show();
    }

    public String convertSpecialCharacters(String s){
        return s.replaceAll("[ãâáàä]", "a")
                .replaceAll("[êéèë]", "e")
                .replaceAll("[îíìï]", "i")
                .replaceAll("[õôòóö]", "o")
                .replaceAll("[ûúùü]", "u")
                .replaceAll("[ÂÂÁÀÄ]", "A")
                .replaceAll("[ÊÉÈË]", "E")
                .replaceAll("[ÎÍÌÏ]", "I")
                .replaceAll("[ÔÕÓÒÖ]", "O")
                .replaceAll("[ÛÚÙÜ]", "U")
                .replaceAll("Ç", "C")
                .replaceAll("ç", "c")
                .replaceAll("ñ", "n")
                .replaceAll("Ñ", "N");
    }

    public String removeAcentos(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFC).replaceAll("[^\\p{ASCII}]", "");
    }

    public AlertDialog criarDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setView(view);
        return builder.create();
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
