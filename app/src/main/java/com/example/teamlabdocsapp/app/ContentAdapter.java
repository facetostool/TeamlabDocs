package com.example.teamlabdocsapp.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabFolderResponse;
import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabResponseFileItem;
import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabResponseFolderItem;
import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabResponseItem;

public class ContentAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    TeamlabFolderResponse object;

    ImageView imageView;
    TextView tvName;
    TextView textDesc;

    ContentAdapter(Context context, TeamlabFolderResponse folderResponse) {
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

        if (i instanceof TeamlabResponseFolderItem) {
            TeamlabResponseFolderItem folder = ((TeamlabResponseFolderItem) i);

            imageView.setImageResource(R.drawable.folder);
            tvName.setText(folder.title);
            textDesc.setText(folder.getInfo());
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

            view.setTag(file.id);
        }
        ImageView iv = (ImageView) view.findViewById(R.id.ivInfo);
        iv.setOnClickListener(infoClickListener);
        return view;
    }

    View.OnClickListener infoClickListener = (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(ctx, "Info click", Toast.LENGTH_SHORT).show();
        }
    });

}