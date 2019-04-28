package com.MONT3.partypoolapp;

import android.support.v4.app.DialogFragment;

public interface DialogListener {
    void onDialogPositiveClickModeSelect(DialogFragment dialog);
    void onDialogNegativeClickModeSelect(DialogFragment dialog);
    void onDialogPositiveClickCreateParty(DialogFragment dialog);
    void onDialogNegativeClickCreateParty(DialogFragment dialog);
}
