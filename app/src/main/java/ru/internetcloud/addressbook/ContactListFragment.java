package ru.internetcloud.addressbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContactListFragment extends Fragment {

    public interface Callbacks {
        public void onSelectContact(long contactId);
        public void onAddContact();
    }

    private Callbacks hostActivity;

    private RecyclerView contact_list_recycler_view;

    private FloatingActionButton add_fab;
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

        add_fab = view.findViewById(R.id.add_fab);
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hostActivity.onAddContact();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    public void updateUI() {

        List<Contact> contactList = ContactLab.getInstance(getActivity()).getContactList();

        if (contactListAdapter == null) {
            contactListAdapter = new ContactListAdapter(contactList);
            contact_list_recycler_view.setAdapter(contactListAdapter);
            // Присоединение ItemDecorator для вывода разделителей
            contact_list_recycler_view.addItemDecoration(new ItemDivider(getContext()));

            // Улучшает быстродействие, если размер макета RecyclerView не изменяется
            contact_list_recycler_view.setHasFixedSize(true);
        } else {
            // если не обновлять contactList, то будут показаны устаревшие данные,
            // поэтому приходится делать:
            contactListAdapter.setContactList(contactList); // ???
            contactListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        hostActivity = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        hostActivity = null;
    }

    // внутренний класс Holder:
    private class ContactListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Contact contact;

        private TextView name_text_view;
        private ImageView icon_image_view;

        public ContactListViewHolder(@NonNull View itemView) {
            super(itemView);

            name_text_view = itemView.findViewById(R.id.name_text_view);
            icon_image_view = itemView.findViewById(R.id.icon_image_view);

            itemView.setOnClickListener(this);
        }

        private void bind(Contact currentContact) {
            contact = currentContact;

            name_text_view.setText(contact.getName());

            File contactPhotoFile = ContactLab.getInstance(getActivity()).getPhotoFile(contact);
            if (contactPhotoFile == null || !contactPhotoFile.exists()) {
                Drawable ic_photo_camera = getResources().getDrawable(R.drawable.ic_person_white_24dp);
                icon_image_view.setImageDrawable(ic_photo_camera);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmapForIcon(contactPhotoFile.getPath(), getActivity(), (int) getResources().getDimension(R.dimen.list_icon_width), (int) getResources().getDimension(R.dimen.list_icon_height));
                icon_image_view.setImageBitmap(bitmap);
            }
        }

        @Override
        public void onClick(View v) {
            hostActivity.onSelectContact(contact.getId());
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
