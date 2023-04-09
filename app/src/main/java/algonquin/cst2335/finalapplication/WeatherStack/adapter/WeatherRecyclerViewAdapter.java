package algonquin.cst2335.finalapplication.WeatherStack.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.WeatherStack.database.WeatherStack;
import algonquin.cst2335.finalapplication.WeatherStack.interfaces.RecyclerItemClickCallback;

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder> {

    /** the context refrence **/
    private Context mContext;

    /** the list of Weathers saved in database **/
    private List<WeatherStack> listWeathers;

    /** the RecyclerView item click callback **/
    private RecyclerItemClickCallback recyclerItemClickCallback;

    /**
     * constructor
     * @param context
     * @param listWeathers
     */
    public WeatherRecyclerViewAdapter(Context context, List<WeatherStack> listWeathers){
        this.listWeathers = listWeathers;
        this.mContext = context;
    }

    /**
     * set RecyclerItem click callback
     * @param recyclerItemClickCallback
     */
    public void setRecyclerItemClickCallback(RecyclerItemClickCallback recyclerItemClickCallback) {
        this.recyclerItemClickCallback = recyclerItemClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_favourite_layout, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // binding recycler view with data
        WeatherStack weatherStack = listWeathers.get(position);
        holder.txtCityName.setText(weatherStack.getCityName());
        holder.txtCountryName.setText(weatherStack.getCountryName());
        Picasso.get()
                .load(weatherStack.getWeatherThumnailIcon())
                .resize(70,70)
                .into(holder.imgThumbnail);

        // implementing recyclerview item's click listerner
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerItemClickCallback.onItemClick(holder.getAdapterPosition());
            }
        });

        // implementing recyclerview item's long click listerner
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                recyclerItemClickCallback.onItemLongClick(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listWeathers.size();
    }


    /**
     * this class represents recyclerView item
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        // thumbnail image
        ImageView imgThumbnail;

        // city name
        TextView txtCityName;

        // country name
        TextView txtCountryName;

        // date time
        TextView txtDateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumbnail = itemView.findViewById(R.id.imgWeatherIcon);
            txtCityName = itemView.findViewById(R.id.txtCityName);
            txtCountryName = itemView.findViewById(R.id.txtCountryName);
            txtDateTime = itemView.findViewById(R.id.txtDataTime);
        }
    }
}
