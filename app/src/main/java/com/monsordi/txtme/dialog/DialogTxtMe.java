package com.monsordi.txtme.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.monsordi.txtme.R;

/**
 * Created by Diego on 31/03/18.
 */

public class DialogTxtMe extends Activity implements DialogUtils.DialogTasks, View.OnClickListener {

    private TextView message;
    private Button cancelButton;
    private Button okButton;

    private DialogUtils dialogUtils;
    private Context mContext;
    private DialogTxtMeTasks mDialogTxtMeTasks;
    private String mMessage;

    public DialogTxtMe(Context context, DialogTxtMeTasks dialogTxtMeTasks) {
        this.mContext = context;
        this.mDialogTxtMeTasks = dialogTxtMeTasks;
    }

    public void showGoTravelDialog(String message){
        this.mMessage = message;
        dialogUtils = new DialogUtils(mContext,this);
        dialogUtils.showDialog(R.layout.dialog);
    }

    //****************************************************************************************************************

    // Implementation of the DialogUtils interface

    //Binds views and sets some properties to them
    @Override
    public void bindView(Dialog dialog) {
        message = (TextView) dialog.findViewById(R.id.dialog_message);
        message.setText(mMessage);
        cancelButton = (Button) dialog.findViewById(R.id.dialog_cancel);
        cancelButton.setOnClickListener(this);
        okButton = (Button) dialog.findViewById(R.id.dialog_ok);
        okButton.setOnClickListener(this);
    }

    //****************************************************************************************************************

    // Implementation of the OnClickListener interface

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_cancel:
                mDialogTxtMeTasks.doCancelTask(dialogUtils.getDialog());
                break;
            case R.id.dialog_ok:
                mDialogTxtMeTasks.doOkTask(dialogUtils.getDialog());
                break;
        }
    }

    //****************************************************************************************************************

    //Methods to be implemented for our specific dialog

    public interface DialogTxtMeTasks {
        void doCancelTask(Dialog dialog);
        void doOkTask(Dialog dialog);
    }

}
