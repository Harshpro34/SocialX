package com.qunatum.socialx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProgrammingAdapter_news extends RecyclerView.Adapter<ProgrammingAdapter_news.ProgrammingViewHolder> {

    Context context;
    private ArrayList<String> date;
    private ArrayList<String> title;
    private ArrayList<String> content;
    private ArrayList<String> image_url;
    private ArrayList<String> source;


    public ProgrammingAdapter_news(ArrayList<String> m_date, ArrayList<String> m_title, ArrayList<String> m_content, ArrayList<String> m_image, ArrayList<String> m_source, Context c)
    {
        date = m_date;
        title = m_title;
        content = m_content;
        image_url = m_image;
        source = m_source;
        context=c;

    }

    @NonNull
    @Override
    public ProgrammingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.recycle_view_news,parent,false);
        return new ProgrammingViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProgrammingViewHolder holder, int position) {

        String display_date = date.get(position);
        String display_title = title.get(position);
        String display_content = content.get(position);
        String display_image = image_url.get(position);
        String display_source = source.get(position);

        // Formatting Date and Time
        display_date = display_date.substring(0,display_date.indexOf("T"))+","+display_date.substring(display_date.indexOf("T")+1,display_date.length()-1);


        holder.n_title.setText(display_title);
        holder.n_date.setText(display_date);
        holder.n_content.setText(display_content);
        holder.n_source.setText(display_source);

        Picasso.get()
                .load(display_image)
                .into(holder.n_image);

    }



    @Override
    public int getItemCount() {
        return title.size();
    }




    public void setFilter(ArrayList<String> m_date, ArrayList<String> m_title, ArrayList<String> m_content, ArrayList<String> m_image, ArrayList<String> m_source) {
        this.date = m_date;
        this.title = m_title;
        this.content = m_content;
        this.image_url =m_image;
        this.source =m_source;
        notifyDataSetChanged();
    }


    public class ProgrammingViewHolder extends RecyclerView.ViewHolder {

        ImageView n_image;
        TextView n_title;
        TextView n_date;
        TextView n_source;
        TextView n_content;


        public ProgrammingViewHolder(@NonNull View itemView) {
            super(itemView);

            n_image = itemView.findViewById(R.id.imageView3);
            n_title = itemView.findViewById(R.id.textView12);
            n_date = itemView.findViewById(R.id.textView10);
            n_source = itemView.findViewById(R.id.textView11);
            n_content = itemView.findViewById(R.id.textView13);

        }


    }




}
