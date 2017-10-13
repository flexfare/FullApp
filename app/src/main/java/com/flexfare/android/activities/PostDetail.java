//package com.flexfare.android.activities;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.flexfare.android.FeedMain;
//import com.flexfare.android.MainActivity;
//import com.flexfare.android.R;
//import com.flexfare.android.newsfeed.model.Blog;
//import com.flexfare.android.newsfeed.model.Comment;
//import com.flexfare.android.newsfeed.model.User;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * Created by kodenerd on 9/18/17.
// */
//
//public class PostDetail extends AppCompatActivity implements View.OnClickListener{
//
//
//    private String key;
//    private DatabaseReference databaseBlog;
//    private DatabaseReference databaseLike;
//    private DatabaseReference mCommentsReference;
//
//    private String description;
//    private String title;
//    private String image;
//    private String uId;
//
//    @Bind(R.id.tvDescription)
//    TextView tvDescription;
//    @Bind(R.id.imPost)
//    ImageView imPost;
//    @Bind(R.id.btDelete)
//    Button btDelete;
//
//    ImageView commentPhoto;
//    String comImg;
//
//    @Bind(R.id.sendButton)
//    FloatingActionButton comment;
//
//    @Bind(R.id.commentEditText)
//    EditText commentEdit;
//
//    @Bind(R.id.recycler_comments)
//    RecyclerView recyclerComment;
//
//    private CommentAdapter mAdapter;
//    TextView tvAuthor;
//
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail_post);
//        ButterKnife.bind(this);
//
//
//        commentPhoto = (ImageView) findViewById(R.id.imAv);
//        tvAuthor = (TextView) findViewById(R.id.author);
//
//        databaseBlog = FirebaseDatabase.getInstance().getReference().child("Blog");
//        databaseLike = FirebaseDatabase.getInstance().getReference().child("Like");
//        mCommentsReference = FirebaseDatabase.getInstance().getReference().child("Comments");
//
//        mAuth = FirebaseAuth.getInstance();
//        key = getIntent().getStringExtra("key");
//        if (key == null) {
//            throw new IllegalArgumentException("Must pass key");
//        }
//
//        comment.setOnClickListener(this);
//        recyclerComment.setLayoutManager(new LinearLayoutManager(this));
//
//    }
//        @Override
//        public void onStart() {
//            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//            super.onStart();
//
//        databaseBlog.child(key).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                title = (String) dataSnapshot.child("title").getValue();
//                description = (String) dataSnapshot.child("description").getValue();
//                image = (String) dataSnapshot.child("image").getValue();
//                uId = (String) dataSnapshot.child("uid").getValue();
//                tvDescription.setText(description);
//                Glide.with(getApplicationContext())
//                        .load(image)
//                        .error(R.drawable.no_image)
//                        .skipMemoryCache(true)
//                        .into(imPost);
//                if (mAuth.getCurrentUser().getUid().equals(uId)){
//                    btDelete.setVisibility(View.VISIBLE);
//                }else {
//                    btDelete.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//            mAdapter = new CommentAdapter(this, mCommentsReference);
//        recyclerComment.setAdapter(mAdapter);
//    }
//    @Override
//    public void onClick(View view){
//        switch (view.getId()) {
//            case R.id.sendButton:
//                postComment();
//                break;
//        }
//    }
//    public String getUid() {
//        return FirebaseAuth.getInstance().getCurrentUser().getUid();
//    }
//    private void postComment() {
//        final String uid = getUid();
////        FirebaseDatabase.getInstance().getReference().child("User").child(uid)
////                .addListenerForSingleValueEvent(new ValueEventListener() {
//        mCommentsReference.child(key).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Blog user = dataSnapshot.getValue(Blog.class);
//                        String authName = user.username;
//
//                        String image = (String) dataSnapshot.child("image").getValue();
//                        Glide.with(getApplicationContext())
//                                .load(image)
//                                .error(R.drawable.no_image)
//                                .skipMemoryCache(true)
//                                .into(commentPhoto);
//
//
//
//                        String commentText = commentEdit.getText().toString();
//                        Comment comment = new Comment(uid, authName, commentText);
//
//                        mCommentsReference.push().setValue(comment);
//
//                        commentEdit.setText(null);
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//            }
//
//
//
//    @OnClick(R.id.btDelete)
//    public void deletePost(){
//        databaseBlog.child(key).removeValue();
//        databaseLike.child(key).removeValue();
//        startActivity(new Intent(this, MainActivity.class));
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home){
//            finish();
//        }
//        return true;
//    }
//
//    private static class CommentViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView authorView;
//        public TextView bodyView;
//        public ImageView mavi;
//
//        public CommentViewHolder(View itemView) {
//            super(itemView);
//
//            authorView = (TextView) itemView.findViewById(R.id.author);
//            bodyView = (TextView) itemView.findViewById(R.id.comment_body);
//            mavi = (ImageView) itemView.findViewById(R.id.imAv);
//        }
//    }
//    private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
//
//        private Context mContext;
//        private DatabaseReference mDatabaseReference;
//        private ChildEventListener mChildEventListener;
//
//        private List<String> mCommentIds = new ArrayList<>();
//        private List<Comment> mComments = new ArrayList<>();
//
//        public CommentAdapter(final Context context, DatabaseReference ref) {
//            mContext = context;
//            mDatabaseReference = ref;
//
//            mChildEventListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
//                    Comment comment = dataSnapshot.getValue(Comment.class);
//
//                    // [START_EXCLUDE]
//                    // Update RecyclerView
//                    mCommentIds.add(dataSnapshot.getKey());
//                    mComments.add(comment);
//                    notifyItemInserted(mComments.size() - 1);
//                    // [END_EXCLUDE]
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
//
//                    Comment newComment = dataSnapshot.getValue(Comment.class);
//                    String commentKey = dataSnapshot.getKey();
//
//                    // [START_EXCLUDE]
//                    int commentIndex = mCommentIds.indexOf(commentKey);
//                    if (commentIndex > -1) {
//                        // Replace with the new data
//                        mComments.set(commentIndex, newComment);
//
//                        // Update the RecyclerView
//                        notifyItemChanged(commentIndex);
//                    } else {
//                        //Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
//                    }
//                    // [END_EXCLUDE]
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    //Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
//
//                    // A comment has changed, use the key to determine if we are displaying this
//                    // comment and if so remove it.
//                    String commentKey = dataSnapshot.getKey();
//
//                    // [START_EXCLUDE]
//                    int commentIndex = mCommentIds.indexOf(commentKey);
//                    if (commentIndex > -1) {
//                        // Remove data from the list
//                        mCommentIds.remove(commentIndex);
//                        mComments.remove(commentIndex);
//
//                        // Update the RecyclerView
//                        notifyItemRemoved(commentIndex);
//                    } else {
//                        //Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
//                    }
//                    // [END_EXCLUDE]
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
//                    //Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
//
//                    // A comment has changed position, use the key to determine if we are
//                    // displaying this comment and if so move it.
//                    Comment movedComment = dataSnapshot.getValue(Comment.class);
//                    String commentKey = dataSnapshot.getKey();
//
//                    // ...
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    //Log.w(TAG, "postComments:onCancelled", databaseError.toException());
//                    Toast.makeText(mContext, "Failed to load comments.",
//                            Toast.LENGTH_SHORT).show();
//                }
//            };
//            ref.addChildEventListener(mChildEventListener);
//            // [END child_event_listener_recycler]
//        }
//
//        @Override
//        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(mContext);
//            View view = inflater.inflate(R.layout.item_comment, parent, false);
//            return new CommentViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(CommentViewHolder holder, int position) {
//            Comment comment = mComments.get(position);
//            holder.authorView.setText(comment.getAuthor());
//            holder.bodyView.setText(comment.getText());
//
////            Glide.with(mContext)
////                    .load(comment.getAvi())
////                    .error(R.drawable.no_image)
////                    .into(holder.mavi);
//        }
//
//        @Override
//        public int getItemCount() {
//            return mComments.size();
//        }
//
//        public void cleanupListener() {
//            if (mChildEventListener != null) {
//                mDatabaseReference.removeEventListener(mChildEventListener);
//            }
//        }
//    }
//    @Override
//    public void onResume() {
//        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        super.onResume();
//    }
//    @Override
//    public void onStop() {
//        super.onStop();
//    }
//
//}
