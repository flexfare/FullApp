//package com.flexfare.android.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.flexfare.android.FeedMain;
//import com.flexfare.android.R;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * Created by kodenerd on 9/19/17.
// */
//
//public class DetailPost extends AppCompatActivity {
//
//    private String key;
//    private DatabaseReference databaseBlog;
//    private DatabaseReference databaseLike;
//    private DatabaseReference databaseText;
//    private String description;
//    private String title;
//    private String image;
//    private String uId;
//    //  @BindView(R.id.tvTitle) TextView tvTitle;
//    @Bind(R.id.tvDescription)
//    TextView tvDescription;
//    @Bind(R.id.imPost)
//    ImageView imPost;
//    @Bind(R.id.btDelete)
//    Button btDelete;
//    private FirebaseAuth mAuth;
//
//
//    private RecyclerView commentRecyclerView;
//    private LinearLayoutManager commentLinearLayoutManager;
////    private FirebaseRecyclerAdapter<String, CommentViewHolder>
////            mFirebaseAdapterComment;
//
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail_post);
//        ButterKnife.bind(this);
////        tvTitle = (TextView) findViewById(R.id.tvTitle);
////        tvDescription = (TextView) findViewById(R.id.tvDescription);
////        imPost = (ImageView) findViewById(R.id.imPost);
////        getSupportActionBar().setTitle("Flex Social");
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
////        getSupportActionBar().setHomeButtonEnabled(true);
//
//        databaseBlog = FirebaseDatabase.getInstance().getReference().child("Blog");
//        databaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
//        mAuth = FirebaseAuth.getInstance();
//        key = getIntent().getStringExtra("key");
//
//
//        databaseBlog.child(key).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                title = (String) dataSnapshot.child("title").getValue();
//                description = (String) dataSnapshot.child("description").getValue();
//                image = (String) dataSnapshot.child("image").getValue();
//                uId = (String) dataSnapshot.child("uid").getValue();
//
//                //    tvTitle.setText(title);
//                tvDescription.setText(description);
//                Glide.with(getApplicationContext())
//                        .load(image)
//                        .error(R.drawable.no_image)
//                        .skipMemoryCache(true)
//                        .into(imPost);
//
//                if (mAuth.getCurrentUser().getUid().equals(uId)){
//                    btDelete.setVisibility(View.VISIBLE);
//                }else {
//                    btDelete.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }
//
//    @OnClick(R.id.btDelete)
//    public void deletePost(){
//
//        databaseBlog.child(key).removeValue();
//        databaseLike.child(key).removeValue();
//        startActivity(new Intent(DetailPost.this, FeedMain.class));
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home){
//            finish();
//        }
//        return true;
//    }
//}
