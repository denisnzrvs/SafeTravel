package com.example.safetravel.ui.settings;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.content.Context;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.safetravel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.widget.Button;

import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ContactFragment extends Fragment {
    private ArrayList<Contact> selectedContacts = new ArrayList<>();
    private ListView contactListView;
    private ArrayAdapter<Contact> adapter;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button button = view.findViewById(R.id.buttonGetContact);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnGetContactPressed(v);
            }
        });
        contactListView = view.findViewById(R.id.contactListView);
        adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.contact_list_item,
                R.id.contactName,
                selectedContacts
        );
        contactListView.setAdapter(adapter);

        if (hasReadContactsPermission()) {
            getPhoneContacts();
        } else {
            requestReadContactsPermission();
        }

        ArrayList<Contact> loadedContacts = loadContactsFromJson();
        if (loadedContacts != null) {
            selectedContacts.addAll(loadedContacts);
            adapter.notifyDataSetChanged();
        }

        return view;
    }

    private boolean hasReadContactsPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadContactsPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
    }

    private void getPhoneContacts() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 0);
        } else {
            ContentResolver contentResolver = requireContext().getContentResolver();
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = contentResolver.query(uri, null, null, null, null);

            if (cursor.getCount() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Select Contacts");

                final ArrayList<String> contactNames = new ArrayList<>();
                final ArrayList<String> contactNumbers = new ArrayList<>();
                final boolean[] checkedContacts = new boolean[cursor.getCount()];

                while (cursor.moveToNext()) {
                    int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY);
                    int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                    if (nameColumnIndex >= 0 && numberColumnIndex >= 0) {
                        String contactName = cursor.getString(nameColumnIndex);
                        String contactNumber = cursor.getString(numberColumnIndex);

                        contactNames.add(contactName);
                        contactNumbers.add(contactNumber);
                    }
                }

                builder.setMultiChoiceItems(
                        contactNames.toArray(new String[0]),
                        checkedContacts,
                        (dialog, which, isChecked) -> {
                            if (isChecked) {
                                String name = contactNames.get(which);
                                String phoneNumber = contactNumbers.get(which);
                                Contact contact = new Contact(name, phoneNumber);
                                selectedContacts.add(contact);
                            } else {
                                selectedContacts.remove(selectedContacts.get(which));
                            }
                            updateListAdapter();
                        }
                );

                builder.setPositiveButton("OK", (dialog, which) -> {
                    saveContactsToJson(selectedContacts);
                    adapter.notifyDataSetChanged();
                });

                builder.setNegativeButton("Cancel", null);

                builder.show();
            } else {
                Toast.makeText(requireContext(), "No contacts found on the phone.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveContactsToJson(ArrayList<Contact> contacts) {
        Gson gson = new Gson();
        String json = gson.toJson(contacts);

        try {
            FileOutputStream outputStream = requireContext().openFileOutput("selected_contacts.json", Context.MODE_PRIVATE);

            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Contact> loadContactsFromJson() {
        ArrayList<Contact> loadedContacts = new ArrayList<>();
        try {
            FileInputStream inputStream = requireContext().openFileInput("selected_contacts.json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();

            String json = stringBuilder.toString();

            Gson gson = new Gson();
            Type contactListType = new TypeToken<ArrayList<Contact>>() {
            }.getType();
            loadedContacts = gson.fromJson(json, contactListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadedContacts;
    }

    private void updateListAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            getPhoneContacts();
        } else {
            Toast.makeText(requireContext(), "Permission denied. Cannot access contacts.", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnGetContactPressed(View view) {
        if (hasReadContactsPermission()) {
            getPhoneContacts();
        } else {
            requestReadContactsPermission();
        }
    }
}
