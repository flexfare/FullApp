//package com.flexfare.android;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.LinearSmoothScroller;
//import android.support.v7.widget.RecyclerView;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.flexfare.android.activities.DriverDetail;
//import com.flexfare.android.activities.FeedLogin;
//import com.flexfare.android.activities.FeedSetting;
//import com.flexfare.android.activities.PostActivity;
//import com.flexfare.android.activities.WallActivity;
//import com.flexfare.android.newsfeed.UserActivity;
//import com.flexfare.android.newsfeed.adapter.FeedAdapter;
//import com.flexfare.android.newsfeed.model.Blog;
//import com.flexfare.flextools.ChronoIsh;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;
//
//import java.util.ArrayList;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * Created by kodenerd on 9/18/17.
// */
//
//public class FeedMain extends AppCompatActivity {
//
//    private RecyclerView rvListPost;
//    private DatabaseReference mDatabase;
//    private DatabaseReference mDatabaseUser;
//    private ArrayList<Blog> listBlog;
//
//    private FeedAdapter adapter = null;
//    private final String TAG = "Main";
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener mAuthListener;
//
//    private DatabaseReference mDatabaseBlog;
//    private boolean progressLike = false;
//
//    private DatabaseReference mDatabaseLike;
//    @Bind(R.id.imAvatar)
//    ImageView imAvatar;
//
//    @Bind(R.id.new_feed)
//    FloatingActionButton newPost;
//
//    private String name;
//    private String image;
//
//    private CircleProgressDialog dialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_feed_main);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        init();
//
//        checkCurrentUser();
//
//        getData();
//
//        setAdapter();
//
//        checkUserSetting();
//
//        newPost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent iPost = new Intent(FeedMain.this, PostActivity.class);
//                iPost.putExtra("avatar", image);
//                startActivity(iPost);
//            }
//        });
//    }
//
//
//    private void init() {
//        ButterKnife.bind(this);
//        rvListPost = (RecyclerView) findViewById(R.id.rvListPost);
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("User");
//        mDatabaseBlog = FirebaseDatabase.getInstance().getReference().child("Blog");
//        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
//
//        mDatabase.keepSynced(true);
//        mDatabaseUser.keepSynced(true);
//        mDatabaseBlog.keepSynced(true);
//        mDatabaseLike.keepSynced(true);
//
//        listBlog = new ArrayList<>();
//
//        dialog = new CircleProgressDialog(this);
//        //dialog.setMessage("Updating Feed...");
//        dialog.setText("Updating Feed....");
//        dialog.setProgressColor(Color.parseColor("#4CAF50"));
//        dialog.setTextColor(Color.parseColor("#FF9800"));
//        dialog.showDialog();
//    }
//
//
//    @OnClick(R.id.imAvatar)
//    public void wall() {
//        Intent iWall = new Intent(FeedMain.this, WallActivity.class);
//        startActivity(iWall);
//    }
//
//    private void setAdapter() {
//        adapter = new FeedAdapter(getApplicationContext(), listBlog);
//        //Log.d(TAG + "--adapter", String.valueOf(adapter));
//        rvListPost.setHasFixedSize(true);
//        rvListPost.setLayoutManager(new LinearLayoutManager(this));
//        rvListPost.setItemAnimator(new DefaultItemAnimator());
//        rvListPost.setNestedScrollingEnabled(false);
//
//        rvListPost.setAdapter(adapter);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setReverseLayout(true);
//        layoutManager.setStackFromEnd(true);
//        rvListPost.setLayoutManager(layoutManager);
//    }
//    LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
//
//        @Override
//        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
//            LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getApplicationContext()) {
//
//                private static final float SPEED = 300f;// Change this value (default=25f)
//
//                @Override
//                protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
//                    return SPEED / displayMetrics.densityDpi;
//                }
//            };
//            smoothScroller.setTargetPosition(position);
//            startSmoothScroll(smoothScroller);
//        }
//    };
//
//    private void getData() {
//        //get data
//        mDatabase.child("Blog").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Blog blog = dataSnapshot.getValue(Blog.class);
//
//                Blog added = new Blog(blog.key, blog.title, blog.description, blog.image, blog.username, blog.uid, blog.like);
//                listBlog.add(added);
//                //Log.d(TAG + "--listBlog", String.valueOf(listBlog));
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//
//    private void checkCurrentUser() {
//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    String uid = user.getUid();
//                    mDatabaseUser.child(uid).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            name = (String) dataSnapshot.child("name").getValue();
//                            image = (String) dataSnapshot.child("image").getValue();
//
//                            Glide.with(getApplicationContext())
//                                    .load(image)
//                                    .error(R.drawable.no_image)
//                                    .into(imAvatar);
//                            dialog.dismiss();
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                            dialog.dismiss();
//                        }
//                    });
//
//                } else {
//                    // User is signed out
//                    Intent iLogin = new Intent(FeedMain.this, FeedLogin.class);
//                    iLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(iLogin);
//                }
//            }
//        };
//    }
//
//    @OnClick(R.id.btStatus)
//    public void status() {
//        Intent iPost = new Intent(FeedMain.this, PostActivity.class);
//        iPost.putExtra("avatar", image);
//        startActivity(iPost);
//    }
//
//    private void loadData() {
//        FirebaseRecyclerAdapter<Blog, BlogViewHolde> fbadapter =
//                new FirebaseRecyclerAdapter<Blog, BlogViewHolde>(
//                        Blog.class,
//                        R.layout.item_post,
//                        BlogViewHolde.class,
//                        mDatabaseBlog)
//                {
//                    @Override
//                    protected void populateViewHolder(final BlogViewHolde viewHolder, Blog model, final int position) {
//                        final String post_key = getRef(position).getKey();
//                        viewHolder.tvTitle.setText(model.getTitle());
//                        viewHolder.tvDesc.setText(model.getDescription());
//                        viewHolder.tvName.setText(model.getUsername());
//
//                        Glide.with(FeedMain.this)
//                                .load(model.getImage())
//                                .into(viewHolder.imPost);
//
//                        viewHolder.itemView.setOnClickListener(v ->  {
//                        });
//
//                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
//                                    viewHolder.imLike.setImageResource(R.drawable.thumb_up_outline);
//                                } else {
//                                    viewHolder.imLike.setImageResource(R.drawable.thumb_up);
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//                        viewHolder.imLike.setOnClickListener(v ->  {
//                                progressLike = true;
//
//                                mDatabaseLike.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        if (progressLike) {
//
//                                            if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {
//
//                                                mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
//                                                progressLike = false;
//                                            } else {
//                                                mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
//                                                progressLike = false;
//                                            }
//                                        }
//                                    }
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                });
//                        });
//                    }
//                };
//
//        rvListPost = (RecyclerView) findViewById(R.id.rvListPost);
//        rvListPost.setHasFixedSize(true);
//        rvListPost.setLayoutManager(new LinearLayoutManager(this));
//        rvListPost.setItemAnimator(new DefaultItemAnimator());
//
//
//        rvListPost.setAdapter(fbadapter);
//        fbadapter.notifyDataSetChanged();
//    }
//
//    private void checkUserSetting() {
//        if (mAuth.getCurrentUser() != null) {
//            final String userId = mAuth.getCurrentUser().getUid();
//            mDatabaseUser.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    // This method is called once with the initial value and again
//                    // whenever data at this location is updated.
//
//                    Boolean uid = dataSnapshot.hasChild(userId);
//                    Log.d("--uid", uid + "");
//                    if (!dataSnapshot.hasChild(userId)) {
//                        Intent iSetting = new Intent(FeedMain.this, FeedSetting.class);
//                        iSetting.putExtra("name", "setting");
//                        iSetting.putExtra("image", "setting");
//                        startActivity(iSetting);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_feed, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    //menu setting
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        if (item.getItemId() == R.id.ic_post) {
////            Intent iPost = new Intent(FeedMain.this, PostActivity.class);
////            iPost.putExtra("avatar", image);
////            startActivity(iPost);
////        }
//        if (item.getItemId() == R.id.ic_message){
//            Intent iMessage = new Intent(FeedMain.this, UserActivity.class);
//            startActivity(iMessage);
//        }
//        if (item.getItemId() == R.id.action_logout) {
//            startActivity(new Intent(this, DriverDetail.class));
//        }
//
////        if (item.getItemId() == R.id.action_logout) {
////            logOut();
////        }
//        if (item.getItemId() == R.id.action_setting) {
//            Intent iSetting = new Intent(FeedMain.this, FeedSetting.class);
//            iSetting.putExtra("name", name);
//            iSetting.putExtra("image", image);
//            startActivity(iSetting);
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    private void logOut() {
//        mAuth.signOut();
//    }
//
//    public class BlogViewHolde extends RecyclerView.ViewHolder {
//        TextView tvTitle;
//        TextView tvTime;
//        TextView tvDesc;
//        ImageView imPost;
//        TextView tvName;
//        ImageButton imLike;
//
//        public BlogViewHolde(View itemView) {
//            super(itemView);
//            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
//            tvDesc = (TextView) itemView.findViewById(R.id.tvDes);
//            tvName = (TextView) itemView.findViewById(R.id.tvName);
//            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
//            imPost = (ImageView) itemView.findViewById(R.id.imPost);
//            imLike = (ImageButton) itemView.findViewById(R.id.imLike);
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent =new Intent(this, DriverDetail.class);
//        startActivity(intent);
//        finish();
//    }
//}
