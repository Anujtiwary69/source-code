package co.stutzen.shopzen.bo;

public class CategoryBO {

    private int id;

    private int cateIcon;

    private String cateName;

    public CategoryBO(String title, int icon){
        this.cateName = title;
        this.cateIcon = icon;
    }

    public CategoryBO(int id, String title, int icon){
        this.cateName = title;
        this.cateIcon = icon;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCateIcon() {
        return cateIcon;
    }

    public void setCateIcon(int cateIcon) {
        this.cateIcon = cateIcon;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }
}
