package com.monsordi.txtme.firebasestorage;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by diego on 17/04/18.
 */

public class StorageHandler {

    private StorageReference storageReference;

    public StorageHandler() {
        this.storageReference = FirebaseStorage.getInstance().getReference();
    }
}
