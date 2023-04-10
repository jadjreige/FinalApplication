package algonquin.cst2335.finalapplication.KittenPlaceholder;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KittenImageViewModel extends ViewModel {
    public MutableLiveData<ImageView> kittenImages = new MutableLiveData<>();
}
