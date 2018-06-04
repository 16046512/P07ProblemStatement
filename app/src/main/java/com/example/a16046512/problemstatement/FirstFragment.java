package com.example.a16046512.problemstatement;


import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {

    TextView etNumber, tvRetrieveNumber;
    Button btnNumber;

    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_first, container, false);

        etNumber = (TextView) view.findViewById(R.id.etNumber);
        tvRetrieveNumber = (TextView) view.findViewById(R.id.tvRetrieveNumber);
        btnNumber = (Button) view.findViewById(R.id.btnNumber);


        btnNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int permissionCheck = PermissionChecker.checkSelfPermission
                        (getActivity(), Manifest.permission.READ_SMS);

                if (permissionCheck != PermissionChecker.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_SMS}, 0);
                    // stops the action from proceeding further as permission not
                    //  granted yet
                    return;
                }



                Uri uri = Uri.parse("content://sms");
                String[] reqCols = new String[]{"date", "address", "body", "type"};

                // Get Content Resolver object from which to
                //  query the content provider
//                ContentResolver cr = getContentResolver();
                ContentResolver cr = getActivity().getContentResolver();






                // The filter String
                String filter="address LIKE ?";
                // The matches for the ?
                String[] filterArgs = {"%"+etNumber.getText().toString()+"%"};
                // Fetch SMS Message from Built-in Content Provider





                // Fetch SMS Message from Built-in Content Provider
                Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);
                String smsBody = "";
                if (cursor.moveToFirst()) {
                    do {
                        long dateInMillis = cursor.getLong(0);
                        String date = (String) DateFormat
                                .format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                        String address = cursor.getString(1);
                        String body = cursor.getString(2);
                        String type = cursor.getString(3);
                        if (type.equalsIgnoreCase("1")) {
                            type = "Inbox:";
                            smsBody += type + " " + address + "\n at " + date
                                    + "\n\"" + body + "\"\n\n";
                        } else {
                            type = "Sent:";
                        }
//                        smsBody += type + " " + address + "\n at " + date
//                                + "\n\"" + body + "\"\n\n";
                    } while (cursor.moveToNext());
                }
                tvRetrieveNumber.setText(smsBody);
            }
        });


        return view;


    }

}
