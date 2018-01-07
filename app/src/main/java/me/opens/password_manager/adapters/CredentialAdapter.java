package me.opens.password_manager.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.opens.password_manager.R;
import me.opens.password_manager.entity.Credential;
import me.opens.password_manager.listener.OnItemClickListener;

public class CredentialAdapter extends RecyclerView.Adapter<CredentialAdapter.CredentialViewHolder> {
    private List<Credential> list;
    private OnItemClickListener listener;

    public CredentialAdapter(List<Credential> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public CredentialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_item, parent, false);
        return new CredentialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CredentialViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static public class CredentialViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView ageTextView;
        private TextView pwdTextView;

        public CredentialViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.domain);
            ageTextView = itemView.findViewById(R.id.username);
            pwdTextView = itemView.findViewById(R.id.password);
        }

        public void bind(final Credential credential, final OnItemClickListener listener) {
            nameTextView.setText(credential.getDomain());
            ageTextView.setText(credential.getUsername());
            pwdTextView.setText(credential.getCredential());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(credential);
                }
            });
        }
    }
}
