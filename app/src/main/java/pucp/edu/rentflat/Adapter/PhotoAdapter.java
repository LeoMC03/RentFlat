package pucp.edu.rentflat.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import pucp.edu.rentflat.Propietario.PropiNewInmuebleActivity;
import pucp.edu.rentflat.R;

public class PhotoAdapter  extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{

    private Activity activity;
    private List<String> images;

    public PhotoAdapter(Activity activity,List<String> images){
        this.activity = activity;
        this.images = images;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(activity).load(images.get(position)).placeholder(AppCompatResources.getDrawable(activity,R.drawable.ic_camera)).into(holder.ivImage);
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ImageUpload);
            itemView.findViewById(R.id.btnrmvImageUpload).setOnClickListener(v ->{
                ((PropiNewInmuebleActivity)  activity).removerFoto(position);
            });
        }
    }
}
