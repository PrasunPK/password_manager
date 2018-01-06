package me.opens.password_manager.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.opens.password_manager.R;
import me.opens.password_manager.entity.Credential;

public class CredentialAdapter extends RecyclerView.Adapter<CredentialAdapter.CredentialViewHolder> {
    private List<Credential> list;

    public CredentialAdapter(List<Credential> list) {
        this.list = list;
    }

    @Override
    public CredentialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_item, parent, false);
        return new CredentialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CredentialViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static public class CredentialViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView ageTextView;

        public CredentialViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.person_name);
            ageTextView = itemView.findViewById(R.id.person_age);
        }

        public void bind(Credential credential) {
            nameTextView.setText(credential.getDomain());
            ageTextView.setText(credential.getUsername());
        }
    }
}
