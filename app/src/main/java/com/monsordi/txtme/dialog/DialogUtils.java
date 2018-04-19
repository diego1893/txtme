package com.monsordi.txtme.dialog;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by Diego on 31/03/18.
 */

public class DialogUtils {

    private Dialog dialog;
    private DialogTasks dialogTasks;

    public DialogUtils(Context context, DialogTasks dialogTasks) {
        this.dialog = new Dialog(context);
        this.dialogTasks = dialogTasks;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void showDialog(int layoutResource){
        dialog.setContentView(layoutResource);
        //Binds view for specific dialog before showing up.
        dialogTasks.bindView(dialog);
        dialog.show();
    }

    //***************************************************************************************************************

    public interface DialogTasks{
        void bindView(Dialog dialog);
    }
}
