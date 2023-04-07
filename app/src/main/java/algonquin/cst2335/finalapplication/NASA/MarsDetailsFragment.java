package algonquin.cst2335.finalapplication.NASA;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import algonquin.cst2335.finalapplication.databinding.MarsDetailsLayoutBinding;

public class MarsDetailsFragment extends Fragment {

    MarsDTO selected;

    public MarsDetailsFragment(MarsDTO s) {
        selected = s;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        MarsDetailsLayoutBinding binding = MarsDetailsLayoutBinding.inflate(inflater);

        Picasso.get().load(selected.getImageUrl()).into(binding.imageLarge);
        binding.roverName.setText(selected.getName());
        binding.imageLink.setText(selected.getImageUrl());

        return binding.getRoot();
    }
}
