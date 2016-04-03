package mecca.meccurator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;


// how to manually import dependancies https://stackoverflow.com/questions/32807587/com-android-build-transform-api-transformexception
// code from tutorial: http://www.truiton.com/2015/04/using-new-google-places-api-android/
// code for tutorial: https://github.com/Truiton/PlacePicker
// solution to issue: https://stackoverflow.com/questions/30434238/place-picker-automatically-close-after-launch
public class PlacePickerActivity extends AppCompatActivity {
    boolean connected;
    protected final Place meetingSpot = null;
    private static final int PLACE_PICKER_REQUEST = 1;
    protected int pos;
    private TextView mName;
    public TextView mAddress;
    private TextView mAttributions;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(53.506975,-113.6306705 ), new LatLng(53.539425, -113.4219295));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);
        mName = (TextView) findViewById(R.id.textView);
        mAddress = (TextView) findViewById(R.id.textView2);
        mAttributions = (TextView) findViewById(R.id.textView3);

        Intent bidintent = getIntent();
        pos = bidintent.getIntExtra("position", 0);

        try {
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
            Log.i("TODO", "Should launch map");
            Intent intent = intentBuilder.build(PlacePickerActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            Log.i("TODO", "map Not working");
        }
    }

    protected void onStart() {
        super.onStart();

        isConnected();
        if (!connected) {
            /* toast message */
            Context context = getApplicationContext();
            CharSequence saved = "Offline";
            int duration = Toast.LENGTH_SHORT;
            Toast.makeText(context, saved, duration).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(this, data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            final LatLng latLng = place.getLatLng();
            //latLng.latitude
            Log.i("TODO", "Set LatLng ");
            ArtList.allArt.get(pos).setLatLng(latLng);
            String attributions = (String) place.getAttributions();
            if (attributions == null) {
                attributions = "";
            }

            mName.setText(name);
            mAddress.setText(address);
            mAttributions.setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
        finish();

    }

    public void isConnected() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            connected = false;
        }
    }

}
