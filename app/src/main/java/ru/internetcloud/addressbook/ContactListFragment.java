package ru.internetcloud.addressbook;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ContactListFragment extends Fragment {

    private RecyclerView contact_list_recycler_view;

    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

}
