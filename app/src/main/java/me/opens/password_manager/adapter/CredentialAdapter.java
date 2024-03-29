package me.opens.password_manager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.opens.password_manager.R;
import me.opens.password_manager.data.Credential;
import me.opens.password_manager.listener.OnItemClickListener;
import me.opens.password_manager.service.CryptService;

public class CredentialAdapter extends RecyclerView.Adapter<CredentialAdapter.CredentialViewHolder> {
    private List<Credential> list;
    private OnItemClickListener listener;
    private static CryptService cryptService;

    public CredentialAdapter(List<Credential> list, OnItemClickListener listener, CryptService cryptService) {
        this.list = list;
        this.listener = listener;
        CredentialAdapter.cryptService = cryptService;
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
        private TextView elapsedTimeTextView;
        private ImageView warningImageView;

        public CredentialViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.domain);
            ageTextView = itemView.findViewById(R.id.username);
            pwdTextView = itemView.findViewById(R.id.password);
            elapsedTimeTextView = itemView.findViewById(R.id.elapsed_time);
            warningImageView = itemView.findViewById(R.id.if_warning);
        }

        public void bind(final Credential credential, final OnItemClickListener listener) {
            nameTextView.setText(credential.getDomain());
            ageTextView.setText(credential.getUsername());
            pwdTextView.setText(credential.getPassword());
            long difference = getDifference(credential.getUpdatedAt());


            elapsedTimeTextView.setText(getFormat(difference));
            if (difference > 60) {
                warningImageView.setVisibility(View.VISIBLE);
            }

            itemView.setOnClickListener(view -> listener.onItemClick(credential));

        }

        private String getFormat(long difference) {
            if(difference <= 0 ) return "Last updated today";
            else if(difference == 1) return "Last updated yesterday";
            else return String.format("Last updated %s days ago", difference);
        }

        private long getDifference(Long time) {
            Date date = new Date();
            return TimeUnit.MILLISECONDS.toDays(date.getTime() - time);
        }
    }
}
