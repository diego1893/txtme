package com.monsordi.txtme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    List<Message> messages;
    String uId;

    public MessageAdapter(Context context, int resource, List<Message> messages, String uId) {
        super(context, resource, messages);
        this.messages = messages;
        this.uId = uId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Message mensaje = messages.get(position);

        LayoutInflater inflater = LayoutInflater.from(getContext());

        View root;

        if (uId.equals(mensaje.getUserId())) {
            root = inflater.inflate(R.layout.row_my_message, parent, false);
        } else {
            root = inflater.inflate(R.layout.row_your_message, parent, false);
        }

        TextView autor = root.findViewById(R.id.inputAutor);
        TextView msm = root.findViewById(R.id.inputMensaje);
        TextView fecha = root.findViewById(R.id.inputHora);

        autor.setText(mensaje.getAutor());
        msm.setText(mensaje.getMessage());
        fecha.setText(mensaje.getDate());

        return root;
    }
}
