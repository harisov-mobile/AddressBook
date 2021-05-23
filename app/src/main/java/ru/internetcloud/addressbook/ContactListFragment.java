package ru.internetcloud.addressbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.internetcloud.addressbook.model.Contact;
import ru.internetcloud.addressbook.model.ContactLab;
import ru.internetcloud.addressbook.util.EmptySubmitSearchView;
import ru.internetcloud.addressbook.util.ItemDivider;
import ru.internetcloud.addressbook.util.PictureUtils;

public class ContactListFragment extends Fragment {

    public interface Callbacks {
        public void onSelectContact(long contactId, String query);
        public void onAddContact();
    }

    private Callbacks hostActivity;
    private RecyclerView contact_list_recycler_view;
    private FloatingActionButton add_fab;
    private ContactListAdapter contactListAdapter;
    private int selectedPosition = -1;
    private int previousSelectedPosition = -1;
    private boolean isTablet;
    private EmptySubmitSearchView searchView = null;
    private String query;

    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isTablet = getResources().getBoolean(R.bool.is_tablet);
        if (!isTablet) {
            setHasOptionsMenu(true);
        }
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchView = (EmptySubmitSearchView) searchItem.getActionView(); // если запрос пуст не срабатывает кнопка поиска: https://jensklingenberg.de/how-to-use-a-searchview-with-an-empty-query-text-submit/

        searchView.setOnQueryTextListener(new EmptySubmitSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ContactListFragment.this.query = query;

                // скрыть клавиатуру: 2 способа
                // статья https://rmirabelle.medium.com/close-hide-the-soft-keyboard-in-android-db1da22b09d2
//                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

                searchView.clearFocus(); // второй способ скрыть клавиатуру

                searchView.onActionViewCollapsed(); // скрывает SearchView

                updateUI();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchView.setQuery(query, false);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();
    }

    public void updateUI() {

        List<Contact> contactList = ContactLab.getInstance(getActivity()).getContactList(query);

        String subtitle = "";
        if (query != null && !query.equals("")) {
            subtitle = "" + getResources().getString(R.string.search) + ": " + query;
        }

        AppCompatActivity currentActivity = (AppCompatActivity) getActivity();
        currentActivity.getSupportActionBar().setSubtitle(subtitle);

        if (contactListAdapter == null) {
            contactListAdapter = new ContactListAdapter(contactList);
            contact_list_recycler_view.setAdapter(contactListAdapter);
            // Присоединение ItemDecorator для вывода разделителей
            contact_list_recycler_view.addItemDecoration(new ItemDivider(getContext()));

            // Улучшает быстродействие, если размер макета RecyclerView не изменяется
            contact_list_recycler_view.setHasFixedSize(true);

            if (isTablet && contactList.size() > 0) {
                selectedPosition = 0;
                hostActivity.onSelectContact(contactList.get(selectedPosition).getId(), query);
            }

        } else {
            // если не обновлять contactList, то будут показаны устаревшие данные,
            // поэтому приходится делать:
            contactListAdapter.setContactList(contactList); // при удалении в режиме планшета - без этой строчки - ошибка.
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
        private int holderPosition;

        public ContactListViewHolder(@NonNull View itemView) {
            super(itemView);

            name_text_view = itemView.findViewById(R.id.name_text_view);
            icon_image_view = itemView.findViewById(R.id.icon_image_view);

            itemView.setOnClickListener(this);
        }

        private void bind(Contact currentContact, int position) {
            contact = currentContact;
            holderPosition = position;

            name_text_view.setText(contact.getName());

            if (isTablet) {
                if (selectedPosition == holderPosition) {
                    this.itemView.setBackgroundColor(getResources().getColor(R.color.colorContactListItemBackground));
                } else {
                    this.itemView.setBackgroundColor(Color.TRANSPARENT);
                }
            }

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
            if (isTablet) {
                previousSelectedPosition = selectedPosition;
                selectedPosition = holderPosition;
                if (previousSelectedPosition > -1) {
                    contactListAdapter.notifyItemChanged(previousSelectedPosition);
                }
                contactListAdapter.notifyItemChanged(selectedPosition);
            }
            hostActivity.onSelectContact(contact.getId(), query);
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
            holder.bind(contactList.get(position), position);
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
