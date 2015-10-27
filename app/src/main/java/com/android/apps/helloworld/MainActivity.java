package com.android.apps.helloworld;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import android.app.PendingIntent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class MainActivity extends ActionBarActivity {

    Switch Bold, Italic, Color, FlashLight, Notification;
    TextView tvText, tvTextShown;
    Random r1, r2, r3;
    EditText editTextShown;
    Button btnUpdateTextShown;
    TabHost tabHost;
    List<UserAction> ListUserActions;
    ListView ListViewUserActions;
    UserActionListAdapter userActionListAdapter;
    Integer ActionCount;
    Button btnClearHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bold = (Switch) findViewById(R.id.switchBold);
        Italic = (Switch) findViewById(R.id.switchItalic);
        Color = (Switch) findViewById(R.id.switchColor);
        tvText = (TextView) findViewById(R.id.tvText);
        FlashLight = (Switch) findViewById(R.id.switchFlashlight);
        Notification = (Switch) findViewById(R.id.switchNotify);
        editTextShown = (EditText) findViewById(R.id.editTextShown);
        tvTextShown = (TextView) findViewById(R.id.tvTextShown);
        editTextShown.setEnabled(false);
        btnUpdateTextShown = (Button) findViewById(R.id.btnUpdate);
        ListViewUserActions = (ListView) findViewById(R.id.listViewActions);
        btnClearHistory = (Button) findViewById(R.id.btnClear);
        ActionCount = 0;
        ListUserActions = new ArrayList<>();

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("helloworld");
        tabSpec.setContent(R.id.tabHelloWorld);
        tabSpec.setIndicator("Hello World");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("history");
        tabSpec.setContent(R.id.tabHistory);
        tabSpec.setIndicator("History");
        tabHost.addTab(tabSpec);

        r1 = new Random(10);
        r2 = new Random(20);
        r3 = new Random(30);

        Bold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvText.setTypeface(null, (Italic.isChecked() ? Typeface.BOLD_ITALIC : Typeface.BOLD));
                }
                else {
                    tvText.setTypeface(null, (Italic.isChecked() ? Typeface.ITALIC : Typeface.NORMAL));
                }
                addToUserActionList("Bold " + (isChecked ? "ON" : "OFF"));
            }
        });

        Italic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvText.setTypeface(null, (Bold.isChecked() ? Typeface.BOLD_ITALIC : Typeface.ITALIC));
                }
                else {
                    tvText.setTypeface(null, (Bold.isChecked() ? Typeface.BOLD : Typeface.NORMAL));
                }
                addToUserActionList("Italic " + (isChecked ? "ON" : "OFF"));
            }
        });

        Color.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvText.setTextColor(android.graphics.Color.rgb(r1.nextInt(200), r2.nextInt(200), r3.nextInt(200)));
                }
                else {
                    tvText.setTextColor(android.graphics.Color.BLACK);
                }
                addToUserActionList("Color " + (isChecked ? "ON" : "OFF"));
            }
        });

        FlashLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(getApplicationContext(), "Flashlight " + (isChecked ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
                addToUserActionList("Flashlight " + (isChecked ? "ON" : "OFF"));
            }
        });

        Notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sendNotification(Notification);
                    Toast.makeText(getApplicationContext(), "Notification shown", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Notification not shown", Toast.LENGTH_SHORT).show();
                }
                addToUserActionList("Notification " + (isChecked ? "ON" : "OFF"));
            }
        });

        btnUpdateTextShown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextShown.setTypeface(null, Typeface.NORMAL);
                if (!String.valueOf(editTextShown.getText()).trim().isEmpty())
                    tvTextShown.setText(editTextShown.getText());
                editTextShown.setText("");
                editTextShown.setActivated(false);
                editTextShown.setEnabled(false);
                addToUserActionList("Update Pressed");
            }
        });

        btnClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListUserActions.clear();
                userActionListAdapter.notifyDataSetChanged();
            }
        });

        registerForContextMenu(tvTextShown);

        userActionListAdapter = new UserActionListAdapter();
        ListViewUserActions.setAdapter(userActionListAdapter);
    }

    private void addToUserActionList(String ActionDesc) {
        ListUserActions.add(new UserAction(ActionCount, new Date(), ActionDesc));
        ActionCount++;
        userActionListAdapter.notifyDataSetChanged();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderIcon(R.mipmap.ic_launcher2);
        menu.setHeaderTitle("Edit Text");
        menu.add(ContextMenu.NONE, 0, ContextMenu.NONE, "Edit Text");
        menu.add(ContextMenu.NONE, 1, ContextMenu.NONE, "Clear Text");
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                addToUserActionList("Edit Text selected");
                editTextShown.setEnabled(true);
                editTextShown.setText(tvTextShown.getText());
                editTextShown.setActivated(true);
                break;
            case 1:
                addToUserActionList("Clear Text selected");
                tvTextShown.setText("No Text");
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A numeric value that identifies the notification that we'll be sending.
     * This value needs to be unique within this app, but it doesn't need to be
     * unique system-wide.
     */
    public static final int NOTIFICATION_ID = 1;

    /**
     * Send a sample notification using the NotificationCompat API.
     */
    public void sendNotification(View view) {

        /** Create an intent that will be fired when the user clicks the notification.
         * The intent needs to be packaged into a {@link android.app.PendingIntent} so that the
         * notification service can fire it on our behalf.
         */
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://developer.android.com/reference/android/app/Notification.html"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        /**
         * Use NotificationCompat.Builder to set up our notification.
         */
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        /** Set the icon that will appear in the notification bar. This icon also appears
         * in the lower right hand corner of the notification itself.
         *
         * Important note: although you can use any drawable as the small icon, Android
         * design guidelines state that the icon should be simple and monochrome. Full-color
         * bitmaps or busy images don't render well on smaller screens and can end up
         * confusing the user.
         */
        //builder.setSmallIcon(R.drawable.ic_stat_notification);
        builder.setSmallIcon(R.drawable.ic_stat_name);

        // Set the intent that will fire when the user taps the notification.
        builder.setContentIntent(pendingIntent);

        // Set the notification to auto-cancel. This means that the notification will disappear
        // after the user taps it, rather than remaining until it's explicitly dismissed.
        builder.setAutoCancel(true);

        /**
         *Build the notification's appearance.
         * Set the large icon, which appears on the left of the notification. In this
         * sample we'll set the large icon to be the same as our app icon. The app icon is a
         * reasonable default if you don't have anything more compelling to use as an icon.
         */
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        /**
         * Set the text of the notification. This sample sets the three most commonly used
         * text areas:
         * 1. The content title, which appears in large type at the top of the notification
         * 2. The content text, which appears in smaller text below the title
         * 3. The subtext, which appears under the text on newer devices. Devices running
         *    versions of Android prior to 4.2 will ignore this field, so don't use it for
         *    anything vital!
         */
        builder.setContentTitle("BasicNotifications Sample");
        builder.setContentText("Time to learn about notifications!");
        builder.setSubText("Tap to view documentation about notifications.");


        /**
         * Send the notification. This will immediately display the notification icon in the
         * notification bar.
         */
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private class UserActionListAdapter extends ArrayAdapter<UserAction> {

        public UserActionListAdapter() {
            super(MainActivity.this, R.layout.listview_item, ListUserActions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            UserAction userAction = ListUserActions.get(position);

            TextView action = (TextView) convertView.findViewById(R.id.tvAction);
            action.setText(userAction.getActionDesc());
            TextView actionTime = (TextView) convertView.findViewById(R.id.tvActionTime);
            actionTime.setText(userAction.getActionDate().toString());

            return convertView;
        }
    }
}
