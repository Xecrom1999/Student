package app.ariel.student;

/**
 * Created by user on 25/01/16.
 */
public class Note {

    private String title;
    private String date;
    private String xPos;
    private String yPos;
    private int isTop;

    public Note(String title, String date, String xPos, String yPos, int isTop) {
        this.title = title;
        this.date = date;
        this.xPos = xPos;
        this.yPos = yPos;
        this.isTop = isTop;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getxPos() {
        return xPos;
    }

    public void setxPos(String xPos) {
        this.xPos = xPos;
    }

    public String getyPos() {
        return yPos;
    }

    public void setyPos(String yPos) {
        this.yPos = yPos;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int isTop() {
        return isTop;
    }

    public void setTop(int top) {
        isTop = top;
    }
}