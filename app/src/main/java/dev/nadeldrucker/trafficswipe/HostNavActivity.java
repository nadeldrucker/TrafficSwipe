package dev.nadeldrucker.trafficswipe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.threetenabp.AndroidThreeTen;

import dev.nadeldrucker.trafficswipe.logic.VehicleUiSupport;

public class HostNavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        new VehicleUiSupport(getApplicationContext()); //fixme Potential reference leak because context is stored static
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_host_nav);

    }
}
