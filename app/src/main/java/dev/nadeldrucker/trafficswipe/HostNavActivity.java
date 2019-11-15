package dev.nadeldrucker.trafficswipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.ImageView;

import dev.nadeldrucker.trafficswipe.dao.transport.model.data.vehicle.StaticUiElement;

public class HostNavActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        new StaticUiElement(getApplicationContext()); //fixme Potential reference leak because context is stored static
        setContentView(R.layout.activity_host_nav);



    }
}
