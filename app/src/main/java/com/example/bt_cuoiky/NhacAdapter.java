package com.example.bt_cuoiky;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;


import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class NhacAdapter extends FirebaseRecyclerAdapter<NhacModel, NhacAdapter.myViewHolder> {
    private Context mcontext;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NhacAdapter(@NonNull FirebaseRecyclerOptions<NhacModel> options, Context mcontext) {
        super(options);
        this.mcontext = mcontext;
    }


    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder,final @SuppressLint("RecyclerView") int position, @NonNull NhacModel model) {
        holder.ten.setText(model.getTen());
        holder.casi.setText(model.getCasi());

        Glide.with(holder.anh.getContext())
                .load(model.getAnh())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.anh);

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.anh.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1200)
                        .create();


                View view = dialogPlus.getHolderView();
                EditText name = view.findViewById(R.id.txtName);
                EditText casi = view.findViewById(R.id.txttinhtrang);
                EditText anh = view.findViewById(R.id.ivanh);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);

                name.setText(model.getTen());
                casi.setText(model.getCasi());
                anh.setText(model.getAnh());

                dialogPlus.show();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("ten", name.getText().toString());
                        map.put("casi", casi.getText().toString());
                        map.put("anh", anh.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("nhac")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.ten.getContext(), "Data Update Successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.ten.getContext(), "Error While Updating", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });

                    }
                });
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.ten.getContext());
                builder.setTitle("Are you Sure?");
                builder.setMessage("Deleted data can't be Undo");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("nhac")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.ten.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        holder.cvitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, ChiTietActivity.class);
                intent.putExtra("ten", model.getTen());
                intent.putExtra("loi", model.getLoi());
                intent.putExtra("casi", model.getCasi());
                intent.putExtra("anh", model.getAnh());
                mcontext.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nhac_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        CircleImageView anh;
        CardView cvitem;
        Button btnEdit, btnDelete;
        TextView ten, casi;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            anh = (CircleImageView) itemView.findViewById(R.id.img1);
            ten = (TextView) itemView.findViewById(R.id.nametext);
            casi = (TextView) itemView.findViewById(R.id.tinhtrang);
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            cvitem = (CardView) itemView.findViewById(R.id.cvitem);
        }
    }


}
