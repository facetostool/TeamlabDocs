package com.example.teamlabdocsapp.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabFolderResponse;
import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabResponseFileItem;
import com.example.teamlabdocsapp.app.api.TeamlabFolderRespose.TeamlabResponseFolderItem;

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

    // кол-во элементов
    @Override
    public int getCount() {
        return object.files.size() + object.folders.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        Object item = null;
        int fSize = object.folders.size();
        if (fSize > position) {
            item = object.folders.get(position);
        } else {
            item = object.files.get(position - (fSize) );
        }
        return item;
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }
    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item, parent, false);
        }

        Object p = getItem(position);

        imageView = (ImageView) view.findViewById(R.id.ivImage);
        tvName = (TextView) view.findViewById(R.id.tvName);
        textDesc = (TextView) view.findViewById(R.id.textDesc);

        if (p instanceof TeamlabResponseFolderItem) {
            TeamlabResponseFolderItem folder = ((TeamlabResponseFolderItem) p);

            imageView.setImageResource(R.drawable.folder);
            tvName.setText(folder.title);
            textDesc.setText(folder.getInfo());

            view.setTag(folder.id);
        } else {
            TeamlabResponseFileItem file = ((TeamlabResponseFileItem) p);
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
        return view;
    }

//    // содержимое корзины
//    ArrayList<Product> getBox() {
//        ArrayList<Product> box = new ArrayList<Product>();
//        for (Product p : objects) {
//            // если в корзине
//            if (p.box)
//                box.add(p);
//        }
//        return box;
//    }

//    // обработчик для чекбоксов
//    OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
//        public void onCheckedChanged(CompoundButton buttonView,
//                                     boolean isChecked) {
//            // меняем данные товара (в корзине или нет)
//            getProduct((Integer) buttonView.getTag()).box = isChecked;
//        }
//    };
}