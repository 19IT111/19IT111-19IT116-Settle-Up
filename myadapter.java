package com.example.settleup;

import android.app.AlertDialog;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.settleup.model.Model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.util.Date;

public class myadapter extends FirebaseRecyclerAdapter<Model,myadapter.myviewholder>
{

    public myadapter(@NonNull FirebaseRecyclerOptions<Model> options) {
        super(options);
    }

    private EditText edtAmmount;
    private EditText edtType;
    private EditText edtNote;

    private Button btnUpdate;
    private Button btnDelete;

    private DatabaseReference mExpenseDatabase;
    private DatabaseReference mIncomeDatabase;



    //Data item value

    private String type;
    private String note;
    private int amount;
    private String post_key;

    private RecyclerView recyclerView;
    myadapter adapter;



    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Model model) {


        holder.type.setText(model.getType());
        holder.note.setText(model.getNote());
        holder.date.setText(model.getDate());
        holder.amount.setText(model.getAmount());
//                holder.setType(model.getType());
//                holder.setNote(model.getNote());
//                holder.setDate(model.getDate());
//                holder.setAmmount(model.getAmount());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //updateDataItem();
                post_key=getRef(position).getKey();

                type=model.getType();
                note=model.getNote();
                amount=model.getAmount();

                updateDataItem();


            }

            final public FragmentActivity getActivity() {
                Fragment mHost = null;
                return mHost == null ? null : (FragmentActivity) mHost.getActivity();
            }
            private void updateDataItem() {
                AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
                LayoutInflater inflater=LayoutInflater.from(getActivity());
                View myview=inflater.inflate(R.layout.update_data_item,null);

                mydialog.setView(myview);

                edtAmmount=myview.findViewById(R.id.ammount_edt);
                edtNote=myview.findViewById(R.id.note_edt);
                edtType=myview.findViewById(R.id.type_edt);

                btnUpdate=myview.findViewById(R.id.btn_upd_Update);
                btnDelete=myview.findViewById(R.id.btn_uPD_Delete);

                AlertDialog dialog=mydialog.create();

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        type=edtType.getText().toString().trim();
                        note=edtNote.getText().toString().trim();

                        String stammount=String.valueOf(amount);
                        stammount=edtAmmount.getText().toString().trim();

                        int intamount= Integer.parseInt(stammount);

                        String mDate= DateFormat.getDateInstance().format(new Date());

                        Model data=new Model(intamount,type,note,post_key,mDate);

                        mExpenseDatabase.child(post_key).setValue(data);

                        mIncomeDatabase.child(post_key).setValue(data);
                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //dialog.dismiss();
                    }
                });

                dialog.show();

            }


        });

        recyclerView.setAdapter(adapter);


    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data,parent,false);
        View mview= LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_data,parent,false);
        return new myviewholder(view,mview);
    }

        public static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setDate(String date){
            TextView mDate=mView.findViewById(R.id.date_txt_expense);
            mDate.setText(date);
        }

        public void setType(String type){
            TextView mType=mView.findViewById(R.id.type_txt_expense);
            mType.setText(type);
        }

        public void setNote(String note){
            TextView mNote=mView.findViewById(R.id.note_txt_expense);
            mNote.setText(note);
        }

        public void setAmmount(int ammount){
            TextView mAmmount=mView.findViewById(R.id.ammount_txt_expense);

            String strammount=String.valueOf(ammount);

            mAmmount.setText(strammount);

        }

    }


    static class myviewholder extends RecyclerView.ViewHolder
    {
        TextView type,note,date,amount;
        public myviewholder(View view, @NonNull View itemView) {
            super(itemView);
            TextView Type=itemView.findViewById(R.id.type_txt_income);
            TextView note=itemView.findViewById(R.id.note_txt_income);
            TextView Date=itemView.findViewById(R.id.date_txt_income);
            TextView Ammount=itemView.findViewById(R.id.ammount_txt_income);


            TextView mType=itemView.findViewById(R.id.type_txt_expense);
            TextView mNote=itemView.findViewById(R.id.note_txt_expense);
            TextView mDate=itemView.findViewById(R.id.date_txt_expense);
            TextView mAmmount=itemView.findViewById(R.id.ammount_txt_expense);

        }

        public void setDate(String date){
            TextView mDate=itemView.findViewById(R.id.date_txt_expense);
            mDate.setText(date);
        }

        public void setType(String type){
            TextView mType=itemView.findViewById(R.id.type_txt_expense);
            mType.setText(type);
        }

        public void setNote(String note){
            TextView mNote=itemView.findViewById(R.id.note_txt_expense);
            mNote.setText(note);
        }

        public void setAmmount(int ammount){
            TextView mAmmount=itemView.findViewById(R.id.ammount_txt_expense);

            String strammount=String.valueOf(ammount);

            mAmmount.setText(strammount);

        }


    }

}
