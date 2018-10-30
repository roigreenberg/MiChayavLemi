package com.roi.greenberg.michayavlemi.fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.roi.greenberg.michayavlemi.R;
import com.roi.greenberg.michayavlemi.utils.HtmlCompat;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends DialogFragment {


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Context context = getContext();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        View mView = LayoutInflater.from(context).inflate(R.layout.fragment_about, null);
        mBuilder.setView(mView);
        TextView mPrivacyPolicyUrl = mView.findViewById(R.id.privacy_policy_hyperlink);
        String privacyPolicyUrlText = "<a href='https://sites.google.com/view/michayavlemi/privacy_policy?authuser=0'>Privacy Policy</a>";
        mPrivacyPolicyUrl.setText(HtmlCompat.fromHtml(privacyPolicyUrlText));
        mPrivacyPolicyUrl.setMovementMethod(LinkMovementMethod.getInstance());

        mBuilder.setPositiveButton(R.string.label_ok, null);

        return mBuilder.create();
    }

}
