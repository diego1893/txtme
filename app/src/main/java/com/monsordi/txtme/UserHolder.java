package com.monsordi.txtme;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * Created by diego on 17/04/18.
 */

public class UserHolder extends RecyclerView.ViewHolder {

    public CircularImageView profilePicture;
    public TextView nameContactText;
    public TextView emailText;

    public interface OnItemClickListener {
        void OnItemClick(User user, int position);
    }

    public UserHolder(View itemView) {
        super(itemView);
        this.profilePicture = itemView.findViewById(R.id.row_profile_image);
        this.nameContactText = itemView.findViewById(R.id.row_name);
        this.emailText = itemView.findViewById(R.id.row_email);
    }

    public void bind(final User myUser, final OnItemClickListener listener) {
        nameContactText.setText(myUser.getName());
        emailText.setText(myUser.getEmail());
        Glide.with(itemView.getContext())
                .load(myUser.getImageString())
                .into(profilePicture);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(myUser, getAdapterPosition());
            }
        });
    }


}
