package UI;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memoriesapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.Memorie;

public class MemorieRecyclerAdapter extends RecyclerView.Adapter<MemorieRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Memorie> memorieList;

    public MemorieRecyclerAdapter(Context context, List<Memorie> memorieList) {
        this.context = context;
        this.memorieList = memorieList;
    }

    @NonNull
    @Override
    public MemorieRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context)
               .inflate(R.layout.memorie_row, parent, false);

       return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MemorieRecyclerAdapter.ViewHolder holder, int position) {
        Memorie memorie = memorieList.get(position);
        String imageUrl;

        holder.title.setText(memorie.getTitle());
        holder.thoughts.setText(memorie.getThought());
        holder.name.setText(memorie.getUserName());
        imageUrl = memorie.getImageUrl();

        //Inspired by: https://medium.com/@shaktisinh/time-a-go-in-android-8bad8b171f87
        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(memorie
                .getTimeAdded()
                .getSeconds() * 1000); //because we gat milliseconds and we need seconds
        holder.dateAdded.setText(timeAgo);


        // Inspired by: https://square.github.io/picasso/
        //Use Picasso library to download and show image
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.image_three)
                .fit()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return memorieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView thoughts;
        public TextView dateAdded;
        public TextView name;

        public ImageView image;
        public ImageButton shareButton;

        String userId;
        String username;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            title = (TextView) itemView.findViewById(R.id.journal_title_list);
            thoughts = (TextView) itemView.findViewById(R.id.journal_thought_list);
            dateAdded = (TextView) itemView.findViewById(R.id.journal_timestamp_list);
            name = (TextView) itemView.findViewById(R.id.journal_row_username);
            image = (ImageView) itemView.findViewById(R.id.journal_image_list);
        }
    }
}
