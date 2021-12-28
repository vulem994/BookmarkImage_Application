package Models;


import androidx.annotation.Nullable;

import java.io.Serializable;

public class BookmarkImage implements Serializable {

    private int Id;
    private int ParentGroupId;

    private boolean FromWebImage;
    private String WebImageUrl;
    private String LocalImagePath;

    private String Description;
    private String Type;
    private boolean Favourite;

    // private int SearchCounter = 0;
    // private int SeenCounter = 0;

    //Complex
    private BookmarkImageGroup ParentGroup;

    //Constructor
    public BookmarkImage() {

    }

    public BookmarkImage(int id, int parentGroupId, int fromWebImage, String webImageUrl, String localImagePath) {
        this.Id = id;
        this.ParentGroupId = parentGroupId;
        if (fromWebImage == 1)
            this.FromWebImage = true;
        else
            this.FromWebImage = false;
        this.WebImageUrl = webImageUrl;
        this.LocalImagePath = localImagePath;
    }

    //region Properties get;set;
    public int getId() {
        return Id;
    }

    public void setParentGroupId(int parentGroupId) {
        ParentGroupId = parentGroupId;
    }

    public int getParentGroupId() {
        return ParentGroupId;
    }

    public void setFromWebImage(boolean fromWebImage) {
        FromWebImage = fromWebImage;
    }

    public boolean getFromWebImage() {
        return FromWebImage;
    }

    public String getWebImageUrl() {
        return WebImageUrl;
    }

    public void setWebImageUrl(String webImageUrl) {
        WebImageUrl = webImageUrl;
    }

    public String getLocalImagePath() {
        return LocalImagePath;
    }

    public void setLocalImagePath(String localImagePath) {
        LocalImagePath = localImagePath;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescription() {
        return Description;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setFavourite(boolean favourite) {
        Favourite = favourite;
    }

    public boolean getFavourite() {
        return Favourite;
    }

    //public int getSearchCounter() {
    //   return SearchCounter;
    //}

    //public void increaseSearchCounter() {
    //    SearchCounter++;
    //}

    //public int getSeenCounter() {
    //  return SeenCounter;
    //}

    //public void increaseSeenCounter() {
    //  SeenCounter++;
    //}

    //Complex (Eventualy mplement later)
    public BookmarkImageGroup getParentGroup() {
        return ParentGroup;
    }

    public void setParentGroup(BookmarkImageGroup parentGroup) {
        ParentGroup = parentGroup;
    }
    //endregion
}//[Class]
