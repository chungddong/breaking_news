package com.sophra.test_parsing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{

    private final List<card_item> mDataList;
    boolean card = false;
    private String test;

    public RecyclerAdapter(List<card_item> dataList) {
        mDataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview, parent, false);
        Context context = parent.getContext();
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final card_item item = mDataList.get(position);
        String str = "속보";

        holder.title.setText(item.getTitle());
        holder.link.setText("link");
        holder.contents.setText(item.getContents());
        holder.web_url = item.getWeb_url();
        if(item.getImg_url().length() < 3)
        {
            holder.image_view.setVisibility(View.GONE);
        }
        else
        {
            holder.image_view.setVisibility(View.VISIBLE);
            Glide.with(holder.image_view.getContext()).load(item.getImg_url()).into(holder.image_view);
            holder.image_view.setClipToOutline(true);
        }

        String links = item.getLink();
        links = links.replaceAll("https://",  "");
        links = links.replaceAll("www.", "");
        links = links.replaceAll("http://", "");
        int xd = links.indexOf("/");
        links = links.substring(0,xd + 1);
        links = links.replaceAll("/", "");

        holder.link.setText(" " + links);
        if(links.length() <= 0)
        {
            holder.link.setText("뉴스사 정보 불러오기 오류");
        }

        String content = item.getContents();
        holder.contents.setText(item.getContents());
        if(content.length() <= 0)
        {
            holder.contents.setText("기사 내용이 없는 뉴스입니다");
        }

        //holder.title.setText(item.getImg_url());
        final String finalLinks = links;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(),"" + links, Toast.LENGTH_LONG).show();
                if(holder.link.getText() != "  뉴스사 정보 불러오기 오류")
                {
                    Intent intent = new Intent(view.getContext(), WebViewActivity.class);
                    WebViewActivity.url = item.getLink();
                    WebViewActivity.title_url = finalLinks;
                    view.getContext().startActivity(intent);
                    /*Context context = view.getContext();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(holder.web_url));
                    context.startActivity(intent);*/
                }
                else
                {
                    Toast.makeText(view.getContext(),"링크 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(!item.getTitle().substring(1,3).equals(str))
        {
            holder.title.setText("ㄹㄹㅇㄹ");

            //remove(holder.getAdapterPosition());

            //mDataList.remove(holder.getAdapterPosition());
            //notifyItemRemoved(holder.getAdapterPosition());
            //(holder.getAdapterPosition(),mDataList.size());

        }

    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void remove(int position)
    {
        try {
            mDataList.remove(position);
            notifyItemRemoved(position);

        }catch (IndexOutOfBoundsException ex)
        {
            ex.printStackTrace();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView link;
        TextView contents;
        String web_url;
        String Img_url;
        ImageView image_view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_text);
            link = itemView.findViewById(R.id.title_link);
            contents = itemView.findViewById(R.id.contents_text);
            image_view = itemView.findViewById(R.id.image_view);

        }
    }

}
