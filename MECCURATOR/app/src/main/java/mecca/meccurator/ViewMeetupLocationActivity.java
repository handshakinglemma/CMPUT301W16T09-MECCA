package mecca.meccurator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

// Reference: http://www.tutorialspoint.com/android/android_google_maps.htm
public class ViewMeetupLocationActivity extends AppCompatActivity {

    private LatLng meetingLocation;
    private GoogleMap googleMap;
    private int pos;
    static final LatLng TutorialsPoint = new LatLng(21 , 57);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meetup_location);

        // Get username from HomeActivity
        Intent intentRcvEdit = getIntent();
        pos = intentRcvEdit.getIntExtra("pos", 0);
        meetingLocation = ArtList.allArt.get(pos).getLatLng();
        Log.i("MEETUP meta pos", String.valueOf(pos));
        Log.i("MEETUP art is", ArtList.allArt.get(pos).getLatLng().toString());


        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            Marker TP = googleMap.addMarker(new MarkerOptions().
                    position(meetingLocation).title("Meetup Location"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
