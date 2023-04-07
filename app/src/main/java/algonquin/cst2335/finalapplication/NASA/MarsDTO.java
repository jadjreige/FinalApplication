package algonquin.cst2335.finalapplication.NASA;

public class MarsDTO {

    private String imageUrl;
    private String name;

    public MarsDTO (String image, String name) {
        this.imageUrl = image;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
