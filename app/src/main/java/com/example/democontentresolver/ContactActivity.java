package com.example.democontentresolver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

    private final String contactAddress = "content://com.android.contacts/contacts";
    private ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        list = findViewById(R.id.list);
        Uri allContacts = Uri.parse(contactAddress);

        ContentResolver cr = getContentResolver();
        Cursor cur = null;
        ArrayList<String> mArrayList = new ArrayList<>();
        cur = getContentResolver().query(allContacts,
                null, null, null, null);
        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                int idColumnIndex = cur.getColumnIndex(ContactsContract.Contacts._ID);
                String id = cur.getString(
                        idColumnIndex);

                int displayNameColumnIndex = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                String name = cur.getString(displayNameColumnIndex);

                int hasPhoneNumberColumnIndex = cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                if (cur.getInt(hasPhoneNumberColumnIndex) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        int phoneNumberColumnIndex = pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                        mArrayList.add(cur.getString(displayNameColumnIndex) + " - " + pCur.getString(phoneNumberColumnIndex));
                    }

                    ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mArrayList);
                    list.setAdapter(itemsAdapter);
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
    }
}