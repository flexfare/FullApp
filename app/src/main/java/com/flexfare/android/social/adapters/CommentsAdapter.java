package com.flexfare.android.social.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flexfare.android.R;
import com.flexfare.android.social.managers.ProfileManager;
import com.flexfare.android.social.managers.listeners.OnObjectChangedListener;
import com.flexfare.android.social.model.Comment;
import com.flexfare.android.social.model.Profile;
import com.flexfare.android.social.utils.FormatterUtil;
import com.flexfare.android.social.views.CircularImageView;
import com.flexfare.android.social.views.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kodenerd on 10/9/17.
 */

public class CommentsAdapter {
    private static final String TAG = CommentsAdapter.class.getName();

    private ViewGroup parent;
    private List<Comment> commentList = new ArrayList<>();
    private LayoutInflater inflater;
    private ProfileManager profileManager;
    private OnAuthorClickListener onAuthorClickListener;


    public CommentsAdapter(ViewGroup parent, OnAuthorClickListener onAuthorClickListener) {
        this.parent = parent;
        this.onAuthorClickListener = onAuthorClickListener;
        inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        profileManager = ProfileManager.getInstance(parent.getContext().getApplicationContext());
    }

    private void initListView() {
        parent.removeAllViews();
        for (int i = 0; i < commentList.size(); i++) {
            inflater.inflate(R.layout.custom_divider, parent, true);
            Comment comment = commentList.get(i);
            parent.addView(getView(comment));
        }
    }

    private View getView(Comment comment) {
        final View convertView = inflater.inflate(R.layout.comment_list_item, parent, false);

        CircularImageView avatarImageView = (CircularImageView) convertView.findViewById(R.id.avatarImageView);
        ExpandableTextView commentTextView = (ExpandableTextView) convertView.findViewById(R.id.commentText);
        TextView dateTextView = (TextView) convertView.findViewById(R.id.dateTextView);

        final String authorId = comment.getAuthorId();
        if (authorId != null)
            profileManager.getProfileSingleValue(authorId, createOnProfileChangeListener(commentTextView,
                    avatarImageView, comment.getText()));

        commentTextView.setText(comment.getText());

        CharSequence date = FormatterUtil.getRelativeTimeSpanString(parent.getContext(), comment.getCreatedDate());
        dateTextView.setText(date);

        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAuthorClickListener.onAuthorClick(authorId, v);
            }
        });

        return convertView;
    }

    private OnObjectChangedListener<Profile> createOnProfileChangeListener(final ExpandableTextView expandableTextView, final ImageView avatarImageView, final String comment) {
        return new OnObjectChangedListener<Profile>() {
            @Override
            public void onObjectChanged(Profile obj) {
                String userName = obj.getUsername();
                fillComment(userName, comment, expandableTextView);

                if (obj.getPhotoUrl() != null) {
                    Glide.with(parent.getContext())
                            .load(obj.getPhotoUrl())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .crossFade()
                            .error(R.drawable.ic_stub)
                            .into(avatarImageView);
                }
            }
        };
    }

    private void fillComment(String userName, String comment, ExpandableTextView commentTextView) {
        Spannable contentString = new SpannableStringBuilder(userName + "   " + comment);
        contentString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(parent.getContext(), R.color.highlight_text)),
                0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        commentTextView.setText(contentString);
    }

    public void setList(List<Comment> commentList) {
        this.commentList = commentList;
        initListView();
    }

    public interface OnAuthorClickListener {
        public void onAuthorClick(String authorId, View view);
    }
}
