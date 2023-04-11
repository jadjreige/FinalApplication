package algonquin.cst2335.finalapplication.KittenPlaceholder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import algonquin.cst2335.finalapplication.R;
import algonquin.cst2335.finalapplication.databinding.KittenDetailsLayoutBinding;

public class KittenDetailsFragment extends Fragment {

    Kitten selected;
    ImageView kitten;


    public KittenDetailsFragment(Kitten newKittenValue){ selected= newKittenValue;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);

        KittenDetailsLayoutBinding binding = KittenDetailsLayoutBinding.inflate(inflater);
        String width = "Width: " +String.valueOf(selected.getWidth());
        String height = "Height: " +String.valueOf(selected.getHeight());
        String dateTime = "Saved on:\n\n" + String.valueOf(selected.getDateTime());
        binding.kittenWidth.setText(width);
        binding.kittenHeight.setText(height);
        binding.timeDate.setText(dateTime);
        String URL = "https://placekitten.com/"+selected.getWidth()+"/"+selected.getHeight();
        Picasso.get().load(URL).into(binding.kittenImageView);
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        binding.fragmentLocation.setOnClickListener(clk ->{

            fragmentTransaction.remove(this);
            fragmentTransaction.commit();


        });


        return binding.getRoot();
    }

}
