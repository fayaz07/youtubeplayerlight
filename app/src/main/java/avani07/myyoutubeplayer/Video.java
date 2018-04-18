package avani07.myyoutubeplayer;


import java.io.Serializable;

public class Video implements Serializable{

    private String image;
    private String title;
    private String url;

    public Video() {
    }

    public Video(String image, String title, String url) {

        this.title = title;
        this.url = url;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String  getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
