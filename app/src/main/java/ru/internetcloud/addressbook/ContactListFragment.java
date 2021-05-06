package ru.internetcloud.addressbook;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContactListFragment extends Fragment {

    private RecyclerView contact_list_recycler_view;
    private ContactListAdapter contactListAdapter;

    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        contact_list_recycler_view = view.findViewById(R.id.contact_list_recycler_view);
        contact_list_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    private void updateUI() {

        List<Contact> contactList = ContactLab.getInstance(getActivity()).getContactList();

        if (contactListAdapter == null) {
            contactListAdapter = new ContactListAdapter(contactList);
            contact_list_recycler_view.setAdapter(contactListAdapter);
        } else {
            // если не обновлять contactList, то будут показаны устаревшие данные,
            // поэтому приходится делать:
            contactListAdapter.setContactList(contactList); // ???

            contactListAdapter.notifyDataSetChanged();
        }


    }

    // внутренний класс Holder:
    private class ContactListViewHolder extends RecyclerView.ViewHolder {

        private Contact contact;

        private TextView name_text_view;

        public ContactListViewHolder(@NonNull View itemView) {
            super(itemView);

            name_text_view = itemView.findViewById(R.id.name_text_view);
        }

        private void bind(Contact currentContact) {
            contact = currentContact;

            name_text_view.setText(contact.getName());
        }
    }

    // внутренний класс Adapter:
    private class ContactListAdapter extends RecyclerView.Adapter<ContactListViewHolder> {

        private List<Contact> contactList;

        public ContactListAdapter(List<Contact> contactList) {
            this.contactList = contactList;
        }

        @NonNull
        @Override
        public ContactListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View itemView = layoutInflater.inflate(R.layout.contact_list_item, parent, false);

            return new ContactListViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactListViewHolder holder, int position) {
            holder.bind(contactList.get(position));
        }

        @Override
        public int getItemCount() {
            return contactList.size();
        }

        public void setContactList(List<Contact> contactList) {
            this.contactList = contactList;
        }
    }
}
