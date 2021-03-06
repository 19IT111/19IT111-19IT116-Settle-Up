package com.example.settleup;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.settleup.model.Model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {

    //Firebase database...


    private String type;
    private String note;
    private int amount;
    private String post_key;

    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;

    //Recyclerview...

    private RecyclerView recyclerView;
    myadapter adapter;
    private TextView expenseSumResult;


    //Edt data item;

    private EditText edtAmmount;
    private EditText edtType;
    private EditText edtNote;

    private Button btnUpdate;
    private Button btnDelete;


    // Data variable...


    private int ammount;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //recyclerView=(RecyclerView) recyclerView.findViewById(R.id.recycler_id_expense);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//
//        FirebaseRecyclerOptions<Model> options =
//                new FirebaseRecyclerOptions.Builder<Model>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("settle-up-de9e6-default-rtdb").child("ExpenseDatabase").child("y43d1z22I4fY2oNhb1VkiHViDCX2"), Model.class)
//                        .build();

       // adapter=new myadapter(options);
//        recyclerView.setAdapter(adapter);
        //adapter.startListening();
    }

   // private TextView expenseTotalSum;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myview= inflater.inflate(R.layout.fragment_expense, container, false);

        mAuth=FirebaseAuth.getInstance();

        FirebaseUser mUser=mAuth.getCurrentUser();
        String uid=mUser.getUid();

        mExpenseDatabase= FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        expenseSumResult=myview.findViewById(R.id.expense_txt_result);

        recyclerView=myview.findViewById(R.id.recycler_id_expense);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());

        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        FirebaseRecyclerOptions<Model> options =
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("settle-up-de9e6-default-rtdb").child("ExpenseDatabase").child("y43d1z22I4fY2oNhb1VkiHViDCX2"), Model.class)
                        .build();

        adapter=new myadapter(options);
        recyclerView.setAdapter(adapter);
        adapter.startListening();

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int expesnSum=0;
                for (DataSnapshot mysnapshot:dataSnapshot.getChildren()){
                    Model data=mysnapshot.getValue(Model.class);
                    expesnSum+=data.getAmount();
                    String strExpesnsum=String.valueOf(expesnSum);

                    expenseSumResult.setText(strExpesnsum+".00");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return myview;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Model> options=
                new FirebaseRecyclerOptions.Builder<Model>()
                        .setQuery(mExpenseDatabase,Model.class)
                        .setLifecycleOwner(this)
                        .build();
        FirebaseRecyclerAdapter<Model, ExpenseFragment.MyViewHolder> firebaseRecyclelerAdapter = new FirebaseRecyclerAdapter<Model, ExpenseFragment.MyViewHolder>(options) {
            @NonNull
            @Override
            public ExpenseFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ExpenseFragment.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_data,parent,false));
            }
            @Override
            protected void onBindViewHolder(@NonNull ExpenseFragment.MyViewHolder viewholder, int position, @NonNull Model model) {
                viewholder.setType(model.getType());
                viewholder.setNote(model.getNote());
                viewholder.setDate(model.getDate());
                viewholder.setAmmount(model.getAmount());

                viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        post_key=getRef(position).getKey();

                        type=model.getType();
                        note=model.getNote();
                        amount=model.getAmount();

                        updateDataItem();

                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclelerAdapter);


//        FirebaseRecyclerAdapter<Model,MyViewHolder> adapter=new FirebaseRecyclerAdapter<Model,MyViewHolder>
//                (
//                        Model.class ,
//                        R.layout.expense_recycler_data,
//                        MyViewHolder.class,
//                        mExpenseDatabase
//                ) {
//
//            @NonNull
//            @Override
//            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View mview= LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_data,parent,false);
//                return new MyViewHolder(mview);
//            }
//
//            @NonNull
//
//            @Override
//            protected void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position, @NonNull Model model) {
//
//                viewHolder.setDate(model.getDate());
//                viewHolder.setType(model.getType());
//                viewHolder.setNote(model.getNote());
//                viewHolder.setAmmount(model.getAmount());
//
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        post_key=getRef(position).getKey();
//
//                        type=model.getType();
//                        note=model.getNote();
//                        amount=model.getAmount();
//
//                    }
//                });
//
//            }
        //};

       // recyclerView.setAdapter(adapter);

    }

    private static class MyViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        private void setDate(String date){
            TextView mDate=mView.findViewById(R.id.date_txt_expense);
            mDate.setText(date);
        }

        private void setType(String type){
            TextView mType=mView.findViewById(R.id.type_txt_expense);
            mType.setText(type);
        }

        private void setNote(String note){
            TextView mNote=mView.findViewById(R.id.note_txt_expense);
            mNote.setText(note);
        }

        private void setAmmount(int ammount){
            TextView mAmmount=mView.findViewById(R.id.ammount_txt_expense);

            String strammount=String.valueOf(ammount);

            mAmmount.setText(strammount);

        }

    }

    private void updateDataItem(){

        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.update_data_item,null);

        mydialog.setView(myview);

        edtAmmount=myview.findViewById(R.id.ammount_edt);
        edtNote=myview.findViewById(R.id.note_edt);
        edtType=myview.findViewById(R.id.type_edt);

        edtType.setText(type);
        edtType.setSelection(type.length());

        edtNote.setText(note);
        edtNote.setSelection(note.length());

        edtAmmount.setText(String.valueOf(amount));
        edtAmmount.setSelection(String.valueOf(amount).length());

        btnUpdate=myview.findViewById(R.id.btn_upd_Update);
        btnDelete=myview.findViewById(R.id.btn_uPD_Delete);
        final AlertDialog dialog=mydialog.create();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type=edtType.getText().toString().trim();
                note=edtNote.getText().toString().trim();

                String stammount=String.valueOf(ammount);

                stammount=edtAmmount.getText().toString().trim();

                int intamount= Integer.parseInt(stammount);

                String mDate= DateFormat.getDateInstance().format(new Date());

                Model data=new Model(intamount,type,note,post_key,mDate);

                mExpenseDatabase.child(post_key).setValue(data);


            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mExpenseDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });

        dialog.show();

        }

    }

