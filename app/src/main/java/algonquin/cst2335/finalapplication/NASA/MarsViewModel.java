package algonquin.cst2335.finalapplication.NASA;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MarsViewModel extends ViewModel {

    public MutableLiveData<MarsDTO> selectedPhoto = new MediatorLiveData<>();
}
