package com.anmol.wedza.Fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anmol.wedza.Adapters.TimelineAdapter;
import com.anmol.wedza.AlertsActivity;
import com.anmol.wedza.CreateeventActivity;
import com.anmol.wedza.EditcoverpicActivity;
import com.anmol.wedza.EventsActivity;
import com.anmol.wedza.KeypeopleActivity;
import com.anmol.wedza.Model.Timeline;
import com.anmol.wedza.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by anmol on 12/25/2017.
 */

public class home extends Fragment implements AbsListView.OnScrollListener{
    private ListView lv;
    private DatabaseReference db;
    private ImageView coverpic;
    private int lastTopValue = 0;
    private List<Timeline> timelines;
    private TimelineAdapter timelineAdapter;
    private Button keypeople,alerts,events;
    private FirebaseAuth auth;
    private TextView weddingdate;
    private RelativeLayout kpl,evl,alrl;
    private FloatingActionButton editcoverpic;

    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home,container,false);
        getActivity().setTitle("Timeline");
        lv = (ListView)view.findViewById(R.id.newsfeed);
        keypeople = (Button)view.findViewById(R.id.keypeople);
        alerts = (Button)view.findViewById(R.id.alerts);
        events = (Button)view.findViewById(R.id.events);
        kpl = (RelativeLayout)view.findViewById(R.id.kpl);
        evl = (RelativeLayout)view.findViewById(R.id.evl);
        alrl = (RelativeLayout)view.findViewById(R.id.alrl);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();

        timelines = new ArrayList<>();
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        ViewGroup header = (ViewGroup)layoutInflater.inflate(R.layout.listheader,lv,false);
        lv.addHeaderView(header,null,false);
        coverpic = (ImageView)header.findViewById(R.id.listHeaderImage);
        weddingdate = (TextView)header.findViewById(R.id.date);
        editcoverpic = (FloatingActionButton)header.findViewById(R.id.editcover);
        editcoverpic.setVisibility(View.GONE);
        // receiving current wedding id
        //edit
        db.child("users").child(auth.getCurrentUser().getUid()).child("currentwedding").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String weddingid = dataSnapshot.getValue(String.class);
                    loadcoverpic(weddingid);
                    loadtimeline(weddingid);
                    checkadmin(weddingid);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*db.child("users").child(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot snapshot = task.getResult();
                if(snapshot.exists()){
                    String weddingid = snapshot.getString("currentwedding");
                    loadcoverpic(weddingid);
                    loadtimeline(weddingid);
                    checkadmin(weddingid);

                }
            }
        });*/

        lv.setOnScrollListener(this);

        kpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), KeypeopleActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.still);
            }
        });
        evl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EventsActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.still);
            }
        });
        alrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AlertsActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.still);
            }
        });
        keypeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), KeypeopleActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.still);
            }
        });
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EventsActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.still);
            }
        });
        alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AlertsActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.still);
            }
        });
        return view;
    }

    private void checkadmin(String weddingid) {
        //check if admin or not
        //edit
        db.child("weddings").child(weddingid).child("users").child(auth.getCurrentUser().getUid()).child("admin")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists() && dataSnapshot.getValue(Boolean.class)) {
                        editcoverpic.setVisibility(View.VISIBLE);
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /*db.child("weddings").child(weddingid).child("users").child(auth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot snapshot = task.getResult();
                        if(snapshot.exists()){
                            if(snapshot.getBoolean("admin")){
                                editcoverpic.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                });*/
    }

    private void loadtimeline(String weddingid) {
        timelines.clear();
        // load timeline
        // edit
        // **important

        db.child("weddings").child(weddingid).child("timeline").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    if(dataSnapshot.exists()){
                        Timeline timeline = new Timeline(ds.child("medialink").getValue(String.class),
                                ds.child("event").getValue(String.class),
                                ds.child("mediatype").getValue(String.class),
                                ds.child("des").getValue(String.class),
                                ds.child("username").getValue(String.class),
                                ds.child(auth.getCurrentUser().getUid()).getValue(String.class),
                                ds.getKey(),
                                ds.child("profilepicturepath").getValue(String.class));
                        timelines.add(timeline);
                    }

                }

                if(getActivity()!=null) {
                    if(!timelines.isEmpty()) {
                        Collections.reverse(timelines);
                        timelineAdapter = new TimelineAdapter(getActivity(),R.layout.timeline,timelines);
                        lv.setAdapter(timelineAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(getActivity()!=null){
                    Toast.makeText(getActivity(),""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*db.collection("weddings").document(weddingid).collection("timeline").orderBy("time", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc:task.getResult()){
                    if(doc.exists()){
                        Timeline timeline = new Timeline(doc.getString("medialink")
                                ,doc.getString("event")
                                ,doc.getString("mediatype")
                                ,doc.getString("des")
                                ,doc.getString("username")
                                ,auth.getCurrentUser().getUid()
                                ,doc.getId()
                                ,doc.getString("profilepicturepath"));
                        timelines.add(timeline);
                    }

                }
                if(getActivity()!=null){
                    if(!timelines.isEmpty()){
                        timelineAdapter = new TimelineAdapter(getActivity(),R.layout.timeline,timelines);
                        lv.setAdapter(timelineAdapter);
                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(getActivity()!=null){
                    Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });*/
    }

    private void loadcoverpic(String weddingid) {
        //loads the coverpic
        //edit

        db.child("weddings").child(weddingid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if(getActivity()!=null && dataSnapshot.exists()) {
                    Glide.with(getActivity()).load(dataSnapshot.child("coverpic").getValue(String.class)).into(coverpic);
                    weddingdate.setText(dataSnapshot.child("weddingdate").getValue(String.class));
                    editcoverpic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), EditcoverpicActivity.class);
                            intent.putExtra("coverpic",dataSnapshot.child("coverpic").getValue(String.class));
                            intent.putExtra("weddate",dataSnapshot.child("weddingdate").getValue(String.class));
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*db.collection("weddings").document(weddingid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(final DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if(getActivity()!=null){
                    if(documentSnapshot.exists()){
                        Glide.with(getActivity()).load(documentSnapshot.getString("coverpic")).into(coverpic);
                        weddingdate.setText(documentSnapshot.getString("weddingdate"));
                        editcoverpic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), EditcoverpicActivity.class);
                                intent.putExtra("coverpic",documentSnapshot.getString("coverpic"));
                                intent.putExtra("weddate",documentSnapshot.getString("weddingdate"));
                                startActivity(intent);
                            }
                        });
                    }

                }
            }
        });*/
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        Rect rect = new Rect();
        coverpic.getLocalVisibleRect(rect);
        if (lastTopValue != rect.top) {
            lastTopValue = rect.top;
            coverpic.setY((float) (rect.top / 2.0));
        }
    }
}
