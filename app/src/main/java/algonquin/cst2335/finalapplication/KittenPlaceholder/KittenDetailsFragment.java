package algonquin.cst2335.finalapplication.KittenPlaceholder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.finalapplication.databinding.KittenDetailsLayoutBinding;

public class KittenDetailsFragment extends Fragment {

    Kitten selected;

    public KittenDetailsFragment(Kitten newKittenValue){ selected= newKittenValue;}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater,container,savedInstanceState);

        KittenDetailsLayoutBinding binding = KittenDetailsLayoutBinding.inflate(inflater);
        String width = "Width: " +String.valueOf(selected.getWidth());
        String height = "Height: " +String.valueOf(selected.getHeight());
        String dateTime = "Saved on:\n\n  " + String.valueOf(selected.getDateTime());
        binding.kittenWidth.setText(width);
        binding.kittenHeight.setText(height);
        binding.timeDate.setText(dateTime);

        return binding.getRoot();
    }

}
