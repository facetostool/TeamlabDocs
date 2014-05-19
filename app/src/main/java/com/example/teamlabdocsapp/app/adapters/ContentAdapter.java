package com.example.teamlabdocsapp.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamlabdocsapp.app.InfoActivity;
import com.example.teamlabdocsapp.app.R;
import com.example.teamlabdocsapp.app.api.TeamlabRespose.TeamlabFolderResponse;
import com.example.teamlabdocsapp.app.api.TeamlabRespose.TeamlabResponseFileItem;
import com.example.teamlabdocsapp.app.api.TeamlabRespose.TeamlabResponseFolderItem;
import com.example.teamlabdocsapp.app.api.TeamlabRespose.TeamlabResponseItem;

public class ContentAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    TeamlabFolderResponse object;

    ImageView imageView;
    TextView tvName;
    TextView textDesc;

    public ContentAdapter(Context context, TeamlabFolderResponse folderResponse) {
        ctx = context;
        object = folderResponse;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return object.getSize();
    }

    @Override
    public TeamlabResponseItem getItem(int position) {
        return object.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        TeamlabResponseItem i = getItem(position);

        imageView = (ImageView) view.findViewById(R.id.ivImage);
        tvName = (TextView) view.findViewById(R.id.tvName);
        textDesc = (TextView) view.findViewById(R.id.textDesc);

        final int type;
        if (i instanceof TeamlabResponseFolderItem) {
            TeamlabResponseFolderItem folder = ((TeamlabResponseFolderItem) i);

            imageView.setImageResource(R.drawable.folder);
            tvName.setText(folder.title);
            textDesc.setText(folder.getInfo());

            type = InfoActivity.FOLDER_INT;
        } else {
            TeamlabResponseFileItem file = ((TeamlabResponseFileItem) i);
            if (file.type.equals(TeamlabResponseFileItem.SPREADSHEET)) {
                imageView.setImageResource(R.drawable.file_xls);
            } else if (file.type.equals(TeamlabResponseFileItem.DOCUMENT)) {
                imageView.setImageResource(R.drawable.file_doc);
            } else if (file.type.equals(TeamlabResponseFileItem.PRESENTATION)) {
                imageView.setImageResource(R.drawable.file_ppt);
            }

            tvName.setText(file.title);
            textDesc.setText(file.getInfo());

            type = InfoActivity.FILE_INT;
        }
        ImageView iv = (ImageView) view.findViewById(R.id.ivInfo);
        iv.setTag(i.getId());
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemId = (String) view.getTag();
                Intent i=new Intent(ctx, InfoActivity.class);
                i.putExtra(InfoActivity.ID, itemId);
                i.putExtra(InfoActivity.ITEM_TYPE, type);
                ((Activity) ctx).startActivityForResult(i, 1);
//                ((Activity) ctx).overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_left);
                Toast.makeText(ctx, "Info click", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}