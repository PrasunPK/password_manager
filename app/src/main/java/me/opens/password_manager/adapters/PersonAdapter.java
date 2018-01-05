package me.opens.password_manager.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.opens.password_manager.R;
import me.opens.password_manager.entity.Person;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {
    private List<Person> list;

    public PersonAdapter(List<Person> list) {
        this.list = list;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_list_item, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static public class PersonViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView ageTextView;

        public PersonViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.person_name);
            ageTextView = itemView.findViewById(R.id.person_age);
        }

        public void bind(Person person) {
            nameTextView.setText(person.getName());
            ageTextView.setText(person.getAge());
        }
    }
}
