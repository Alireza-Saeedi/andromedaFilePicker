package com.andromeda.filePicker.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andromeda.filePicker.R;
import com.andromeda.filePicker.utils.AndromedaFile;
import com.andromeda.filePicker.utils.FileThumbnail;
import com.andromeda.filePicker.interfaces.OnItemClickListener;

import java.util.List;

public class AndromedaDirectoryAdapter extends RecyclerView.Adapter<AndromedaDirectoryAdapter.ViewHolder> {

    private Context context;
    private List<AndromedaFile> files;
    private OnItemClickListener mOnItemClickListener;
    private boolean enableMultiple = false;
    private int maxItemSelected = 0;
    private FileThumbnail fileThumbnail;

    public AndromedaDirectoryAdapter(Context context, List<AndromedaFile> files) {
        this.context = context;
        this.files = files;
        this.fileThumbnail = new FileThumbnail();
    }

    public AndromedaDirectoryAdapter setFiles(List<AndromedaFile> files) {
        this.files = files;
        return this;
    }

    public AndromedaDirectoryAdapter setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        return this;
    }

    public AndromedaDirectoryAdapter setEnableMultiple(boolean enableMultiple, int maxItemSelected) {
        this.enableMultiple = enableMultiple;
        this.maxItemSelected = maxItemSelected;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.andromeda_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AndromedaFile currentFile = files.get(holder.getAdapterPosition());
        holder.iv_andromeda_item.setImageResource(currentFile.getFileType().getIcon());
        holder.tv_andromeda_item_subtitle.setText(currentFile.getFileType().getDescription());
        holder.tv_andromeda_item_name.setText(currentFile.getFile().getName());
        holder.iv_andromeda_itemSelected.setVisibility((enableMultiple && currentFile.isSelected()) ? View.VISIBLE : View.GONE);
        try {
            switch (currentFile.getFileType()) {
                case IMAGE:
                    holder.iv_andromeda_item.setImageBitmap(fileThumbnail.image(currentFile.getFile().getAbsolutePath()));
                    break;
                case MUSIC:
                    Bitmap cover = fileThumbnail.musicCoverPicture(currentFile.getFile().getAbsolutePath());
                    if (cover != null)
                        holder.iv_andromeda_item.setImageBitmap(cover);
                    break;
              /*  case VIDEO:
                    holder.iv_andromeda_item.setImageBitmap(fileThumbnail.videoThumbnail(currentFile.getFile().getAbsolutePath()));
                    break;*/
                case APK:
                    holder.iv_andromeda_item.setImageDrawable(fileThumbnail.apkLogo(context, currentFile.getFile().getAbsolutePath()));
                    break;
            }
        } catch (Exception ignored) {
        }

        holder.rv_andromeda_item_root.setOnClickListener(view -> {
            if (mOnItemClickListener != null)
                mOnItemClickListener.onItemClick(currentFile, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return files.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rv_andromeda_item_root;
        private ImageView iv_andromeda_item;
        private TextView tv_andromeda_item_name;
        private TextView tv_andromeda_item_subtitle;
        private ImageView iv_andromeda_itemSelected;

        ViewHolder(View itemView) {
            super(itemView);
            rv_andromeda_item_root = itemView.findViewById(R.id.rv_andromeda_item_root);
            iv_andromeda_item = itemView.findViewById(R.id.iv_andromeda_item);
            tv_andromeda_item_name = itemView.findViewById(R.id.tv_andromeda_item_name);
            tv_andromeda_item_subtitle = itemView.findViewById(R.id.tv_andromeda_item_subtitle);
            iv_andromeda_itemSelected = itemView.findViewById(R.id.iv_andromeda_itemSelected);
        }
    }
}