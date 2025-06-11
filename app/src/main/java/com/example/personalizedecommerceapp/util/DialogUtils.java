package com.example.personalizedecommerceapp.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.activity.MainActivity;


public class DialogUtils {

    public static Dialog showLoadingDialog(Context context, String message, Dialog dialog) {
        try {
            DialogUtils.dismissDialog(dialog);
            dialog = new Dialog(context, androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
            dialog.setContentView(R.layout.loading);
            TextView tvLoadingText = dialog.findViewById(R.id.tvLoadingText);
            if (message.trim().isEmpty()) {
                tvLoadingText.setVisibility(View.GONE);
            } else {
                tvLoadingText.setText(message);
                tvLoadingText.setVisibility(View.VISIBLE);
            }
            dialog.setCancelable(false);
            dialog.show();
            return dialog;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static void dismissDialog(Dialog dialog) {
        try {
            if (dialog != null) {
                try {
                    dialog.cancel();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static AlertDialog showOptionAlertDialog(Context context, String title
            , String[] optionsList, DialogInterface.OnClickListener onCLickListItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(optionsList, onCLickListItem);
        return builder.create();
    }

    public static void forceLogout(Context context) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Unauthorized");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Unauthorized user found, Please login again");
        alertDialog.setNeutralButton("Login again", (dialog, which) -> {
            dialog.cancel();
            UserPref.deleteAll(context);
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            ((Activity) context).finish();
        });
        alertDialog.show();
    }

    public static AlertDialog openAlertDialog(final Context context, String title
            , String message, String positiveBtnText, String negativeBtnText
            , DialogInterface.OnClickListener onClickPositiveButton
            , DialogInterface.OnClickListener onClickNegativeButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(positiveBtnText, onClickPositiveButton);
        builder.setNegativeButton(negativeBtnText, onClickNegativeButton);
        return builder.create();
    }


    public static AlertDialog openAlertDialog(final Context context, String message, String positiveBtnText,
                                              boolean showNegativeBtn, boolean isFinish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);

        builder.setPositiveButton(positiveBtnText, (dialog, id) -> {
            dialog.cancel();
            if (isFinish)
                ((Activity) context).finish();
        });

        if (showNegativeBtn)
            builder.setNegativeButton("No", (dialog, id) -> {
                dialog.cancel();
            });
        return builder.create();
    }

    public static AlertDialog openAlertDialog(final Context context, String title, String message, String positiveBtnText,
                                              boolean showNegativeBtn, boolean isFinish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(false);

        builder.setPositiveButton(positiveBtnText, (dialog, id) -> {
            dialog.cancel();
            if (isFinish)
                ((Activity) context).finish();
        });

        if (showNegativeBtn)
            builder.setNegativeButton("No", (dialog, id) -> {
                dialog.cancel();
            });
        return builder.create();
    }

    public static AlertDialog logoutDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to logout?");

        builder.setPositiveButton("Yes", (dialog, id) -> {
            dialog.cancel();
            int flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK;
            UserPref.deleteAll(context);
            Helper.goToWithFlags(context, MainActivity.class, flags);
        });

        builder.setNegativeButton("No", (dialog, id) -> {
            dialog.cancel();
        });
        return builder.create();
    }

    public static AlertDialog deleteDialog(final Context context
            , DialogInterface.OnClickListener onDeleteClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to delete?\nWARNING: This action cannot be undone");
        builder.setPositiveButton("Yes", onDeleteClickListener);
        builder.setNegativeButton("No", (dialog, id) -> {
            dialog.cancel();
        });
        return builder.create();
    }

}
