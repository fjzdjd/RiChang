package ddw.com.richang.custom.CustomUi;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import ddw.com.richang.R;
import ddw.com.richang.commons.ConstantData;
import ddw.com.richang.ui.mine.DownloadService;

/**
 * Created by zzp on 2016/6/16.
 */
public class UpdateDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.newUpdateAvailable);
        builder.setMessage(getArguments().getString(ConstantData.APK_UPDATE_CONTENT))
                .setPositiveButton(R.string.dialogPositiveButton, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        goToDownload();
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.dialogNegativeButton, new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    private void goToDownload() {
        Intent intent = new Intent(getActivity().getApplicationContext(), DownloadService.class);
        intent.putExtra(ConstantData.APK_DOWNLOAD_URL, getArguments().getString(ConstantData
                .APK_DOWNLOAD_URL));
        getActivity().startService(intent);
    }
}
