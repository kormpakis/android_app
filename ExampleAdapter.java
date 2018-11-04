package com.example.user.recyclerviewjsonvolley;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    private Context mContext;
    private ArrayList<ExampleItem> mExampleList;
    private ArrayList<ExampleItem> mUrls;

    public ExampleAdapter(Context context, ArrayList<ExampleItem> exampleList, ArrayList<ExampleItem> mUrlList) {
        mContext = context;
        mExampleList = exampleList;
        mUrls = mUrlList;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.example_item, viewGroup, false);
        return new ExampleViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder exampleViewHolder, final int i) {

        ExampleItem currentItem = mExampleList.get(i);
        //final ExampleItem currentUrl = mUrls.get(i);

        String imageUrl = currentItem.getImageUrl();
        String title = currentItem.getTitle();
        String excerpt = currentItem.getExcerpt();
        final String url = currentItem.getUrl();
        int articleid = currentItem.getId();

        exampleViewHolder.mTextViewTitle.setText(title);
        exampleViewHolder.mTextViewExcerpt.setText(excerpt);
        Picasso.with(mContext).load(imageUrl).fit().centerCrop().into(exampleViewHolder.mImageView);

        exampleViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: ffffffffffffffffffffffffff");
                Intent intent = new Intent(mContext, WebView1.class);
                intent.putExtra("url", url);
                mContext.startActivity(intent);

                //EDW THA MPEI TO ROUTE GIA NA PERNAEI STO DATABASE TIS PLHROFORIES TOY ARTHROY
                String userid = SessionManager.ID;



                //Log.d(TAG, "onClick: gggggggggggggggggggg");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextViewTitle;
        public TextView mTextViewExcerpt;
        public TextView mTextViewUrl;
        CardView parentLayout;
        //
        public ImageView tv_footer;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_view);
            mTextViewTitle = itemView.findViewById(R.id.text_view_title);
            mTextViewExcerpt = itemView.findViewById(R.id.text_view_excerpt);
            parentLayout = itemView.findViewById(R.id.parentLayout);
        }
    }
}
