package com.aqp.rutabus.rutabusaqp.ui;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.aqp.rutabus.rutabusaqp.R;
import com.aqp.rutabus.rutabusaqp.ui.Utils.SharedUtils;
import com.aqp.rutabus.rutabusaqp.ui.fragments.EmpresasFragment;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback {

    GoogleMap mMap;
    Marker miLocation;
    Marker miMarker;
    double latitud = 0.0;
    double longitud = 0.0;

    SupportMapFragment mapFragment;
    android.support.v4.app.FragmentManager sFm;

    private static final int RC_SIGN_IN = 123;
    private static final String PROVEEDOR_DESCONOCIDO = "Proveedor desconocido";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener listener;

    TextView txtUser;
    TextView txtEmail;
    CircleImageView imgPerfil;

    Vibrator vibrator;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(100);
                if (SharedUtils.getPermisionGPS(MainActivity.this)) {
                    Snackbar.make(view, "Por favor active su GPS", Snackbar.LENGTH_LONG)
                            .setAction("ATENCION!!!", null).show();
                }else{
                    miUbicacion();
                }

            }
        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        nav_view.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        //Mostrando Fragment con Mapa
        sFm = getSupportFragmentManager();

        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);

        txtUser = headerView.findViewById(R.id.tv_user_name);
        txtEmail = headerView.findViewById(R.id.tv_user_email);
        imgPerfil = headerView.findViewById(R.id.iv_user_photo);

        mFirebaseAuth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSetUserInfo(user.getDisplayName(), user.getEmail(), user.getProviders() != null ?
                                    user.getProviders().get(0) : PROVEEDOR_DESCONOCIDO,
                            user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "ninguno");
                } else {

//                    AuthUI.IdpConfig facebookIdp = new AuthUI.IdpConfig.FacebookBuilder()
//                            .setPermissions(Arrays.asList("user_Friends", "user_gender"))
//                            .build();

                    AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.GoogleBuilder()
                            .build();

                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            .setLogo(R.drawable.logoinicio)
                            .setAlwaysShowSignInMethodScreen(true)
                            .setTheme(R.style.Login)
                            .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                                    googleIdp))
                            .build(), RC_SIGN_IN
                    );
                }
            }
        };
    }

    private void onSetUserInfo(String userName, String email, String provider, String photo) {
        txtEmail.setText(email);
        txtUser.setText(userName);
        if (photo.equals("ninguno"))
            Glide.with(this).load(R.drawable.ic_user_default).into(imgPerfil);
        else
            Glide.with(this).load(photo).into(imgPerfil);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, "Algo falló", Toast.LENGTH_LONG).show();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(listener);
        if (!mapFragment.isAdded())
            sFm.beginTransaction().add(R.id.map, mapFragment).commit();
        else
            sFm.beginTransaction().show(mapFragment).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFirebaseAuth != null) {
            mFirebaseAuth.removeAuthStateListener(listener);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (mapFragment.isAdded())
            sFm.beginTransaction().hide(mapFragment).commit();

        if (id == R.id.nav_home) {
            fab.show();
            if (!mapFragment.isAdded())
                sFm.beginTransaction().add(R.id.map, mapFragment).commit();
            else
                sFm.beginTransaction().show(mapFragment).commit();
        } else if (id == R.id.nav_buses) {
            sFm.beginTransaction().replace(R.id.map, new EmpresasFragment()).commit();
            fab.hide();
//        } else if (id == R.id.nav_history) {
//            Intent empAct = new Intent(getApplicationContext(), EmpresasActivity.class);
//            startActivity(empAct);
//        } else if (id == R.id.nav_config) {
//            Intent empAct = new Intent(getApplicationContext(), MapsActivity.class);
//            startActivity(empAct);
        } else if (id == R.id.nav_signout) {
            AuthUI.getInstance().signOut(MainActivity.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        miUbicacion();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MainActivity.this, R.raw.light_gray));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Drawable ic = getResources().getDrawable(R.drawable.ic_bus);
                agregarMarcador(latLng.latitude, latLng.longitude, ic, false);
            }
        });
    }

    private void agregarMiUbicacion(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (miLocation != null) miLocation.remove();
        {
            Drawable ic = getResources().getDrawable(R.drawable.ic_person_location);
            BitmapDescriptor markerIcon = getMarkerIconFromDrawable(ic);

            miLocation = mMap.addMarker(new MarkerOptions().position(coordenadas).title("Mi ubicación")
                    .icon(markerIcon));
//                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
                mMap.animateCamera(miUbicacion);
        }
    }

    private void agregarMarcador(double lat, double lng, Drawable icono, Boolean animate) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 16);
        if (miMarker != null)miMarker.remove();
        {
            BitmapDescriptor markerIcon = getMarkerIconFromDrawable(icono);
            miMarker = mMap.addMarker(new MarkerOptions().position(coordenadas).title("Llegada")
                    .icon(markerIcon));
            if (animate)
                mMap.animateCamera(miUbicacion);
        }
    }
    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void updateLocation(Location location) {
        if (location != null) {
            latitud = location.getLatitude();
            longitud = location.getLongitude();
            agregarMiUbicacion(latitud, longitud);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            //updateLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @SuppressLint("MissingPermission")
    private void miUbicacion() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (SharedUtils.getPermisionGPS(this)) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateLocation(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000, 0, locationListener);
    }

//    private void configurarNavigationDrawer() {
//
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, null, R.string.drawer_open, R.string.drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView nav_view = findViewById(R.id.nav_view);
//        nav_view.setNavigationItemSelectedListener(this);
//        View headerView = nav_view.getHeaderView(0);
//
//        nav_view.getMenu().findItem(vm.getMenuItem()).setChecked(true);
//
//        TextView nombreUser = headerView.findViewById(R.id.tv_user_name);
//        TextView emailUser = headerView.findViewById(R.id.tv_user_email);
//    }

}
