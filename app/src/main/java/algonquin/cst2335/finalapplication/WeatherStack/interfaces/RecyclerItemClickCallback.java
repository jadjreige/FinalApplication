package algonquin.cst2335.finalapplication.WeatherStack.interfaces;

/**
 * @author Imandeep Singh Sidhu
 * This class holds methods for opening and deleting the weather details
 */
public abstract class RecyclerItemClickCallback {

    abstract public void onItemClick(int position);
    public void onItemLongClick(int position) {}

}
