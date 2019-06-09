package com.example.khadi.entertainmentsearch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {
    @NonNull
    private List<ArtistItem> items;
    private Context context;

    public ArtistAdapter(Context context, List<ArtistItem> items){
        this.items = items;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.artist_items, viewGroup, false);
        ArtistAdapter.ViewHolder vh = new ArtistAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ArtistItem artistItem = items.get(i);
        viewHolder.setEvent(artistItem);
        //viewHolder.setListener(artistItem);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView heading,artistName,popularity,followers,checkAt;
        ImageView image1, image2, image3, image4, image5, image6, image7, image8;
        LinearLayout nameLL, popularLL, followersLL,checkAtLL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            heading = itemView.findViewById(R.id.heading);
            image1 = itemView.findViewById(R.id.image1);
            image2 = itemView.findViewById(R.id.image2);
            image3 = itemView.findViewById(R.id.image3);
            image4 = itemView.findViewById(R.id.image4);
            image5 = itemView.findViewById(R.id.image5);
            image6 = itemView.findViewById(R.id.image6);
            image7 = itemView.findViewById(R.id.image7);
            image8 = itemView.findViewById(R.id.image8);
            artistName = itemView.findViewById(R.id.nameValue);
            popularity = itemView.findViewById(R.id.popular);
            followers = itemView.findViewById(R.id.followersTv);
            checkAt = itemView.findViewById(R.id.checkAt);
            nameLL = itemView.findViewById(R.id.nameLL);
            popularLL = itemView.findViewById(R.id.popularityLL);
            followersLL = itemView.findViewById(R.id.followersLL);
            checkAtLL = itemView.findViewById(R.id.checkAtLL);
        }

        public void setEvent(ArtistItem artistItem) {
            if(Constants.category_const.equals("Music")){
                nameLL.setVisibility(View.VISIBLE);
                popularLL.setVisibility(View.VISIBLE);
                followersLL.setVisibility(View.VISIBLE);
                checkAtLL.setVisibility(View.VISIBLE);
            }
            String popular = artistItem.getPopularity();
            popularity.setText(popular);
            heading.setText(artistItem.getHeading());
            artistName.setText(artistItem.getName());
            followers.setText(artistItem.getFollowers());
            String url = "<a href="+artistItem.getCheckAt()+">Spotify</a>";
            checkAt.setText(Html.fromHtml(url, Html.FROM_HTML_MODE_LEGACY));
            checkAt.setMovementMethod(LinkMovementMethod.getInstance());
            //checkAt.setText(artistItem.getCheckAt());
            String url1 = artistItem.getImage1();
            Picasso.with(context).load(url1).into(image1);
            String url2 = artistItem.getImage2();
            Picasso.with(context).load(url2).into(image2);
            String url3 = artistItem.getImage3();
            Picasso.with(context).load(url3).into(image3);
            String url4 = artistItem.getImage4();
            Picasso.with(context).load(url4).into(image4);
            String url5 = artistItem.getImage5();
            Picasso.with(context).load(url5).into(image5);
            String url6= artistItem.getImage6();
            Picasso.with(context).load(url6).into(image6);
            String url7 = artistItem.getImage7();
            Picasso.with(context).load(url7).into(image7);
            String url8 = artistItem.getImage8();
            Picasso.with(context).load(url8).into(image8);
        }
    }
}
