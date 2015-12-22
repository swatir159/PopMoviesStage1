package com.example.android.popmoviesstage1.Util;

import android.support.v4.app.DialogFragment;
import android.app.Dialog;
import android.os.Bundle;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

import com.example.android.popmoviesstage1.R;


/**
 * Created by swatir on 12/10/2015.
 */
public class NoInternetConnectionDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.no_internet_title))
                .setMessage(context.getString(R.string.no_internet_message))
                .setNeutralButton(R.string.no_internet_negative_btn,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                getActivity().finish();
                            }
                        }

                );
        return builder.create();
    }
}

/* public class NoInternetConnectionDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.no_internet_title))
                .setMessage(context.getString(R.string.no_internet_message))
                .setNeutralButton(R.string.no_internet_negative_btn,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                getActivity().finish();
                            }
                        }

                );

                .setNegativeButton(R.string.no_internet_negative_btn,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                getActivity().finish();
                            }
                        })
                .setPositiveButton(R.string.no_internet_positive_btn,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent networkSetting = new Intent(
                                        Settings.ACTION_WIRELESS_SETTINGS);
                                startActivity(networkSetting);
                            }
                        })
                ;

        return builder.create();
    }

}
*/