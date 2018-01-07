package com.anmol.wedza.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.anmol.wedza.AlbumActivity;
import com.anmol.wedza.Model.Timeline;
import com.anmol.wedza.R;
import com.anmol.wedza.StoryMediaPreview;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import droidninja.filepicker.models.Document;

/**
 * Created by anmol on 12/26/2017.
 */

public class TimelineAdapter extends ArrayAdapter<Timeline> {
    private Activity context;
    private int resource;
    private List<Timeline> timelines;

    public TimelineAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Timeline> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        timelines = objects;
    }
    public int getViewTypeCount() {
        return getCount();
    }

    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView!=null){
            return convertView;
        }
        else{
            final FirebaseAuth auth = FirebaseAuth.getInstance();
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userid = auth.getCurrentUser().getUid();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater inflater = context.getLayoutInflater();
            View v = inflater.inflate(resource,null);
            ImageView mediaimg = (ImageView)v.findViewById(R.id.mediaphoto);
            ImageView playicon = (ImageView)v.findViewById(R.id.playicon);
            final Button like = (Button)v.findViewById(R.id.like);
            Button comment = (Button)v.findViewById(R.id.comment);
            Button share = (Button)v.findViewById(R.id.share);
            TextView uname = (TextView)v.findViewById(R.id.uname);
            TextView event = (TextView)v.findViewById(R.id.eventname);
            TextView des = (TextView)v.findViewById(R.id.des);
            uname.setText(timelines.get(position).getUsername());
            event.setText(timelines.get(position).getEvent());
            des.setText(timelines.get(position).getDes());
            if(timelines.get(position).getMediatype().contains("image")){
                playicon.setVisibility(View.GONE);
                Glide.with(getContext()).load(timelines.get(position).getMedialink()).into(mediaimg);
            }
            else if(timelines.get(position).getMediatype().contains("video")){
                playicon.setVisibility(View.VISIBLE);
                Glide.with(getContext()).load(timelines.get(position).getMedialink()).into(mediaimg);
            }
            mediaimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, StoryMediaPreview.class);
                    intent.putExtra("medialink",timelines.get(position).getMedialink());
                    intent.putExtra("mediatype",timelines.get(position).getMediatype());
                    context.startActivity(intent);
                }
            });
            db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String weddingid = task.getResult().getString("currentwedding");
                    db.collection("weddings")
                            .document(weddingid)
                            .collection("timeline")
                            .document(timelines.get(position).getPostid())
                            .collection("likes").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for(DocumentSnapshot doc:task.getResult()){
                                        if(doc.getId().contains(auth.getCurrentUser().getUid())){
                                            like.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                }
                            });
                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    db.collection("users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Map<String,Object> map = new HashMap<>();
                            map.put("like",true);
                            String weddingid = task.getResult().getString("currentwedding");
                            db.collection("weddings")
                                    .document(weddingid)
                                    .collection("timeline")
                                    .document(timelines.get(position).getPostid())
                                    .collection("likes").document(auth.getCurrentUser().getUid()).set(map);
                        }
                    });
                }
            });
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            return v;
        }

    }
}
