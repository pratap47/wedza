package com.anmol.wedza;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.anmol.wedza.Fragments.gallery;
import com.anmol.wedza.Fragments.guestlist;
import com.anmol.wedza.Fragments.home;
import com.anmol.wedza.Fragments.media;
import com.anmol.wedza.Fragments.story;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Button timeline,guestlist,gallery,story;
    ImageButton camera;
    private static long back_pressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(auth.getCurrentUser()==null){
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
            finish();
        }
        else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);



            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            setFragment(new home());

            camera = (ImageButton)findViewById(R.id.camera);
            timeline = (Button)findViewById(R.id.vtimeline);
            guestlist = (Button)findViewById(R.id.vguestlist);
            gallery = (Button)findViewById(R.id.vgallery);
            story = (Button)findViewById(R.id.vstory);
            timeline.setBackgroundResource(R.drawable.homer);
            guestlist.setBackgroundResource(R.drawable.familyblue);
            camera.setBackgroundResource(R.drawable.round_button);
            gallery.setBackgroundResource(R.drawable.galleryblue);
            story.setBackgroundResource(R.drawable.storyblue);
            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeline.setBackgroundResource(R.drawable.homeb);
                    guestlist.setBackgroundResource(R.drawable.familyblue);
                    camera.setBackgroundResource(R.drawable.round_button1);
                    gallery.setBackgroundResource(R.drawable.galleryblue);
                    story.setBackgroundResource(R.drawable.storyblue);
                    //startActivity(new Intent(HomeActivity.this,CameraActivity.class));
                    setFragment(new media());
                }
            });
            guestlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeline.setBackgroundResource(R.drawable.homeb);
                    guestlist.setBackgroundResource(R.drawable.familyred);
                    camera.setBackgroundResource(R.drawable.round_button);
                    gallery.setBackgroundResource(R.drawable.galleryblue);
                    story.setBackgroundResource(R.drawable.storyblue);
                    setFragment(new guestlist());
                }
            });
            timeline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeline.setBackgroundResource(R.drawable.homer);
                    guestlist.setBackgroundResource(R.drawable.familyblue);
                    camera.setBackgroundResource(R.drawable.round_button);
                    gallery.setBackgroundResource(R.drawable.galleryblue);
                    story.setBackgroundResource(R.drawable.storyblue);
                    setFragment(new home());
                }
            });
            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeline.setBackgroundResource(R.drawable.homeb);
                    guestlist.setBackgroundResource(R.drawable.familyblue);
                    camera.setBackgroundResource(R.drawable.round_button);
                    gallery.setBackgroundResource(R.drawable.galleryr);
                    story.setBackgroundResource(R.drawable.storyblue);
                    setFragment(new gallery());
                }
            });
            story.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timeline.setBackgroundResource(R.drawable.homeb);
                    guestlist.setBackgroundResource(R.drawable.familyblue);
                    camera.setBackgroundResource(R.drawable.round_button);
                    gallery.setBackgroundResource(R.drawable.galleryblue);
                    story.setBackgroundResource(R.drawable.storyred);
                    setFragment(new story());
                }
            });
        }


    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.still,R.anim.slide_out_down);
        }else {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content,new home()).commit();
            Toast.makeText(getBaseContext(), "Double tap to exit!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

        if (id == R.id.nav_edtpersonalinfo) {
            startActivity(new Intent(HomeActivity.this,Editpersonalinfo.class));
        }
        else if(id == R.id.nav_wedset){
            startActivity(new Intent(HomeActivity.this,Weddingsettings.class));
        }
        else if (id == R.id.nav_createwed) {
            startActivity(new Intent(HomeActivity.this,CreateweddingActivity.class));
        } else if (id == R.id.nav_contactwed) {

        } else if (id == R.id.nav_like) {
            Uri uri = Uri.parse("market://details?id=" + "com.anmol.hibiscus");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + "com.anmol.hibiscus")));
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content,fragment).commit();
        fragmentManager.executePendingTransactions();
    }
}
