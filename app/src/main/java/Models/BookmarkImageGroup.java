package Models;

import java.io.Serializable;

public class BookmarkImageGroup implements Serializable {
    private int Id;
    private int ParentUserId;

    private String Title;
    private String Description;

    private boolean PrivateGroup;
    private String PrivateGroupPassword;


    //Complex
    private User ParentGroup;

    public BookmarkImageGroup() {

    }

    public BookmarkImageGroup(int id, int parentUserId, String title, String description, Boolean privateGroup, String privateGroupPassword) {
        this.Id = id;
        this.ParentUserId = parentUserId;
        this.Title = title;
        this.Description = description;
        this.PrivateGroup = privateGroup;
        this.PrivateGroupPassword = privateGroupPassword;
    }

    public BookmarkImageGroup(int id, int parentUserId, String title, String description, int privateGroup, String privateGroupPassword) {
        this.Id = id;
        this.ParentUserId = parentUserId;
        this.Title = title;
        this.Description = description;
        if(privateGroup ==1 ){
            this.PrivateGroup = true;
        }
        else{
            this.PrivateGroup = false;
        }
        this.PrivateGroupPassword = privateGroupPassword;
    }

    //region Properties get;set;
    public int getId() {
        return Id;
    }

    public void setParentUserId(int parentUserId) {
        ParentUserId = parentUserId;
    }

    public int getParentUserId() {
        return ParentUserId;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTitle() {
        return Title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }

    public void setPrivateGroup(boolean privateGroup) {
        PrivateGroup = privateGroup;
    }

    public boolean getPrivateGroup() {
        return PrivateGroup;
    }

    public void setPrivateGroupPassword(String privateGroupPassword) {
        PrivateGroupPassword = privateGroupPassword;
    }

    public String getPrivateGroupPassword() {
        return PrivateGroupPassword;
    }
    //endregion
}//[Class]
