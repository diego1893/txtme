package com.monsordi.txtme;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

/**
 * Created by diego on 17/04/18.
 */

public class UserDisplayHolder extends RecyclerView.ViewHolder {

    public CircularImageView profilePicture;
    public TextView nameContactText;
    public TextView messageText;
    public TextView hourText;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void OnItemClick(UserDisplay userDisplay, int position);
    }

    public UserDisplayHolder(View itemView) {
        super(itemView);
        this.profilePicture = itemView.findViewById(R.id.row_profile_image);
        this.nameContactText = itemView.findViewById(R.id.row_name);
        this.messageText = itemView.findViewById(R.id.row_message);
        this.hourText = itemView.findViewById(R.id.row_hour);
    }

    public void bind(final UserDisplay myUserDisplay, final OnItemClickListener listener) {
        nameContactText.setText(myUserDisplay.getUser());
        messageText.setText(myUserDisplay.getMessage());
        hourText.setText(myUserDisplay.getHour());
        Glide.with(itemView.getContext())
                .load(myUserDisplay.getImageUrl())
                .into(profilePicture);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(myUserDisplay, getAdapterPosition());
            }
        });
    }


}
