package com.ctmy.expensemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    ArrayList<Project> projectList;
    private FirebaseDatabase mFiredatabaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildListener;

    public ProjectAdapter(){
        FirebaseUtil.openFbReference("projects", null);
        mFiredatabaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        projectList = FirebaseUtil.mProjects;

        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Project project = dataSnapshot.getValue(Project.class);
                //Log.d("Deal: ", project.getProjectName());
                project.setProjectId(dataSnapshot.getKey());
                projectList.add(project);
                notifyItemInserted(projectList.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildListener);
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_project_list, parent, false);
        return new ProjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project proj = projectList.get(position);
        holder.bind(proj);
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle;
        TextView tvDueDate;
        TextView tvTotalExpenses;
        TextView tvTotalIncomes;
        TextView tvBalance;
        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_proj_name);
            tvDueDate = (TextView) itemView.findViewById(R.id.tv_due_date);
            tvTotalExpenses = (TextView) itemView.findViewById(R.id.tv_expenses);
            tvTotalIncomes = (TextView) itemView.findViewById(R.id.tv_incomes);
            tvBalance = (TextView) itemView.findViewById(R.id.tv_balance);
            itemView.setOnClickListener(this);
        }

        public void bind(Project project){
            Double totalExpenses = project.getTotalExpenses();
            Double totalIncomes = project.getTotalIncomes();
            Double balance = totalIncomes - totalExpenses;

            if(balance < 0){
                tvBalance.setTextColor(Color.RED);
            }

            String shortPattern = DateUtil.getDatePattern(itemView.getContext());
            tvTitle.setText(project.getProjectName());
            tvDueDate.setText(DateUtil.epochToDateString(project.getDueDate(), shortPattern));
            tvTotalExpenses.setText(String.valueOf(totalExpenses));
            tvTotalIncomes.setText(String.valueOf(totalIncomes));
            tvBalance.setText(String.valueOf(totalIncomes-totalExpenses));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            //Log.d("Click", String.valueOf(position));
            Project selectedProj = projectList.get(position);
            Intent intent = new Intent(v.getContext(), ProjectTransaction.class);
            intent.putExtra("Project", selectedProj);
            v.getContext().startActivity(intent);
        }
    }
}
