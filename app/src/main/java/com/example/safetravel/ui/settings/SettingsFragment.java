package com.example.safetravel.ui.settings;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.content.Context;
import android.telephony.SmsManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SettingsFragment extends Fragment {
    private ArrayList<Contact> selectedContacts = new ArrayList<>();
    private ListView contactListView;
    private ArrayAdapter<Contact> adapter;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int PERMISSIONS_REQUEST_SEND_SMS = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        requestPermissions();

        Button button = view.findViewById(R.id.buttonGetContact);
        Button sosButton = view.findViewById(R.id.buttonSOS);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhoneContacts();
            }
        });

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sendSOSMessages();
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


        ArrayList<Contact> loadedContacts = loadContactsFromJson();
        if (loadedContacts != null) {
            selectedContacts.addAll(loadedContacts);
            adapter.notifyDataSetChanged();
        }

        TextView userNameText = view.findViewById(R.id.userNameText);
        TextView userEmailText = view.findViewById(R.id.userEmailText);
        TextView userPhoneText = view.findViewById(R.id.userPhoneText);

        // Replace these sample values with the actual user data
        File internalDir = requireContext().getFilesDir();
        File profileJSON = new File(internalDir, "profile.json");

        if (profileJSON.exists()) {
            try {
                String json = readProfileJSON(profileJSON);
                JSONObject jsonObject = new JSONObject(json);
                String userName = jsonObject.getString("name");
                String userEmail = jsonObject.getString("email");
                String userPhone = jsonObject.getString("phone");

                userNameText.setText(userName);
                userEmailText.setText(userEmail);
                userPhoneText.setText(userPhone);
            } catch (Exception e) {
                e.printStackTrace();
                // Handle any errors that may occur when reading the JSON file
            }
        }

        return view;
    }

    // Helper method to read profile JSON
    private String readProfileJSON(File profileJSON) {
        try {
            FileInputStream inputStream = new FileInputStream(profileJSON);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            inputStream.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void requestPermissions() {
        ArrayList<String> permissionsToRequest = new ArrayList<>();

        if (!hasReadContactsPermission()) {
            permissionsToRequest.add(Manifest.permission.READ_CONTACTS);
        }

        if (!hasSendSmsPermission()) {
            permissionsToRequest.add(Manifest.permission.SEND_SMS);
        }

        if (!permissionsToRequest.isEmpty()) {
            String[] permissionsArray = permissionsToRequest.toArray(new String[0]);
            ActivityCompat.requestPermissions(requireActivity(), permissionsArray, PERMISSIONS_REQUEST_READ_CONTACTS);
        }
    }


    private boolean hasReadContactsPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }
    private boolean hasSendSmsPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestReadContactsPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
    }
    private void requestSendSmsPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.SEND_SMS}, PERMISSIONS_REQUEST_SEND_SMS);
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

                // Load the contacts from the JSON file
                ArrayList<Contact> loadedContacts = loadContactsFromJson();

                while (cursor.moveToNext()) {
                    int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY);
                    int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                    if (nameColumnIndex >= 0 && numberColumnIndex >= 0) {
                        String contactName = cursor.getString(nameColumnIndex);
                        String contactNumber = cursor.getString(numberColumnIndex);

                        contactNames.add(contactName);
                        contactNumbers.add(contactNumber);

                        // Check if the contact is already in the selectedContacts list
                        if (loadedContacts != null) {
                            for (Contact loadedContact : loadedContacts) {
                                if (loadedContact.getName().equals(contactName) && loadedContact.getPhoneNumber().equals(contactNumber)) {
                                    int index = contactNames.indexOf(contactName);
                                    if (index >= 0) {
                                        checkedContacts[index] = true;
                                    }
                                }
                            }
                        }
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
                                selectedContacts.removeIf(contact -> contact.getName().equals(contactNames.get(which)));
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

    private void sendSOSMessages() {
        if (selectedContacts.isEmpty()) {
            // No selected contacts to send SOS messages to
            Toast.makeText(requireContext(), "No contacts selected for SOS messages.", Toast.LENGTH_SHORT).show();
            return;
        }

        String sosMessage = "TEST: Emergency: I need help!";
        SmsManager smsManager = SmsManager.getDefault();
        Toast.makeText(requireContext(), "SOS message sent!", Toast.LENGTH_SHORT).show();


        for (Contact contact : selectedContacts) {
            String phoneNumber = contact.getPhoneNumber();

            try {
                smsManager.sendTextMessage(phoneNumber, null, sosMessage, null, null);
                // Handle success (e.g., displaying a message or UI update)
            } catch (Exception e) {
                // Handle any errors that may occur during SMS sending
                Toast.makeText(requireContext(), "Failed to send SOS message to " + contact.getName(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
    private void updateListAdapter() {
        adapter.notifyDataSetChanged();
    }
}
