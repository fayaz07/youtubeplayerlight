package avani07.myyoutubeplayer;

public class SaveData {
    String lastplayed;
    int nooftimes;

    public SaveData() {
    }

    public SaveData(String lastplayed, int nooftimes) {
        this.lastplayed = lastplayed;
        this.nooftimes = nooftimes;
    }

    public String getLastplayed() {
        return lastplayed;
    }

    public void setLastplayed(String lastplayed) {
        this.lastplayed = lastplayed;
    }

    public int getNooftimes() {
        return nooftimes;
    }

    public void setNooftimes(int nooftimes) {
        this.nooftimes = nooftimes;
    }
}
