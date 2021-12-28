package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import Models.BookmarkImage;
import Models.BookmarkImageGroup;
import Models.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Database properties
    private Context context;
    private static final String DATABASE_NAME = "BookmarkImage.db";
    private static final int DATABASE_VERSION = 1;

    //User table properties
    private static final String USERTABLE_NAME = "User";
    private static final String UserTable_Column_Id = "Id";
    private static final String UserTable_Column_Fullname = "Fullname";
    private static final String UserTable_Column_DateOfBirth = "DateOfBirth";
    private static final String UserTable_Column_Email = "Email";
    private static final String UserTable_Column_Username = "Username";
    private static final String UserTable_Column_Password = "Password";
    private static final String UserTable_Column_Gender = "Gender";
    private static final String UserTable_Column_AdditionalJson = "AdditionalJson";
    private static final String UserTable_Column_TimeCreated = "TimeCreated";

    //BookmarkImage properties
    private static final String BOOKMARKIMAGE_NAME = "BookmarkImage";
    private static final String BookmarkImageTable_Column_Id = "Id";
    private static final String BookmarkImageTable_Column_ParentGroupId = "ParentGroupId";
    private static final String BookmarkImageTable_Column_FromWebImage = "FromWebImage";
    private static final String BookmarkImageTable_Column_WebImageUrl = "WebImageUrl";
    private static final String BookmarkImageTable_Column_LocalImagePath = "LocalImagePath";
    private static final String BookmarkImageTable_Column_Description = "Description";
    private static final String BookmarkImageTable_Column_Type = "Type";
    private static final String BookmarkImageTable_Column_Favourite = "Favourite";

    //BookmarkImageGroup properties
    private static final String BOOKMARKIMAGEGROUP_NAME = "BookmarkImageGroup";
    private static final String BookmarkImageGroupTable_Column_Id = "Id";
    private static final String BookmarkImageGroupTable_Column_ParentUserId = "ParentUserId";
    private static final String BookmarkImageGroupTable_Column_Title = "Title";
    private static final String BookmarkImageGroupTable_Column_Description = "Description";
    private static final String BookmarkImageGroupTable_Column_PrivateGroup = "PrivateGroup";
    private static final String BookmarkImageGroupTable_Column_PrivateGroupPassword = "PrivateGroupPassword";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        SQLiteDatabase db = this.getWritableDatabase();


    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String createUserTableQuery =
                    "CREATE TABLE " + USERTABLE_NAME +
                            " (" + UserTable_Column_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            UserTable_Column_Fullname + " TEXT, " +
                            UserTable_Column_DateOfBirth + " TEXT, " +
                            UserTable_Column_Username + " TEXT, " +
                            UserTable_Column_Email + " TEXT, " +
                            UserTable_Column_Password + " TEXT, " +
                            UserTable_Column_AdditionalJson + " TEXT, " +
                            UserTable_Column_Gender + " TEXT, " +
                            UserTable_Column_TimeCreated + " TEXT);";
            db.execSQL(createUserTableQuery);

            String createBookmarkImageTableQuery =
                    "CREATE TABLE " + BOOKMARKIMAGE_NAME +
                            " (" + BookmarkImageTable_Column_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            BookmarkImageTable_Column_ParentGroupId + " INTEGER, " +
                            BookmarkImageTable_Column_FromWebImage + " BOOLEAN, " +
                            BookmarkImageTable_Column_WebImageUrl + " TEXT, " +
                            BookmarkImageTable_Column_LocalImagePath + " TEXT, " +
                            BookmarkImageTable_Column_Description + " TEXT, " +
                            BookmarkImageTable_Column_Type + " TEXT, " +
                            BookmarkImageTable_Column_Favourite + " TEXT);";
            db.execSQL(createBookmarkImageTableQuery);

            String createBookmarkImageGroupTableQuery =
                    "CREATE TABLE " + BOOKMARKIMAGEGROUP_NAME +
                            " (" + BookmarkImageGroupTable_Column_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            BookmarkImageGroupTable_Column_ParentUserId + " INTEGER, " +
                            BookmarkImageGroupTable_Column_PrivateGroup + " BOOLEAN, " +
                            BookmarkImageGroupTable_Column_PrivateGroupPassword + " TEXT, " +
                            BookmarkImageGroupTable_Column_Title + " TEXT, " +
                            BookmarkImageGroupTable_Column_Description + " TEXT);";

            db.execSQL(createBookmarkImageGroupTableQuery);
        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            String dropUserTableQuery = "DROP TABLE IF EXISTS " + USERTABLE_NAME;
            db.execSQL(dropUserTableQuery);
            String dropBookmarkImageTableQuery = "DROP TABLE IF EXISTS " + BOOKMARKIMAGE_NAME;
            db.execSQL(dropBookmarkImageTableQuery);
            String dropBookmarkImageGroupTableQuery = "DROP TABLE IF EXISTS " + BOOKMARKIMAGEGROUP_NAME;
            db.execSQL(dropBookmarkImageGroupTableQuery);
            onCreate(db);
        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //USER CONTEXT
    //region AddUsser
    public boolean AddUser(User inUser) {
        try {
            if (inUser != null) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues cv = new ContentValues();

                cv.put(UserTable_Column_Fullname, inUser.GetFullname());
                cv.put(UserTable_Column_DateOfBirth, inUser.GetDateOfBirth());
                cv.put(UserTable_Column_Email, inUser.GetEmail());
                cv.put(UserTable_Column_Password, inUser.GetPassword());
                cv.put(UserTable_Column_TimeCreated, inUser.GetTimeCreated().toString());
                cv.put(UserTable_Column_Username, inUser.GetUsername().toString());
                cv.put(UserTable_Column_Gender, inUser.getGender());

                long result = db.insert(USERTABLE_NAME, null, cv);
                Toast.makeText(context, "Nalog uspesno dodat", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(context, "Nalog nije pravilno kreiran", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    //endregion
    //region GetUser (by Id)
    public User GetUser(int inId) {
        User toRet = null;
        try {
            String getUserQuery = "SELECT * FROM " + USERTABLE_NAME + " WHERE " + UserTable_Column_Id + " = " + inId;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(getUserQuery, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(UserTable_Column_Id));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Fullname));
                    String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_DateOfBirth));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Email));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Password));
                    String username = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Username));
                    String gender = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Gender));

                    toRet = new User(id, name, dateOfBirth, email, password, username, gender);
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return toRet;
    }

    //endregion
    //region GetUser (by Email or Username)
    public User GetUser(String inEmailOrUsername) {
        User toRet = null;
        try {
            String getUserQuery = "SELECT * FROM " + USERTABLE_NAME + " WHERE " + UserTable_Column_Email + " ='" + inEmailOrUsername + "'";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(getUserQuery, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(UserTable_Column_Id));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Fullname));
                    String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_DateOfBirth));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Email));
                    String password = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Password));
                    String username = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Username));
                    String gender = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Gender));

                    toRet = new User(id, name, dateOfBirth, email, password, username, gender);
                }
            }
            if (toRet == null) {
                //Toast.makeText(context, "Pokusaj drugi", Toast.LENGTH_LONG).show();
                getUserQuery = "SELECT * FROM " + USERTABLE_NAME + " WHERE " + UserTable_Column_Username + " ='" + inEmailOrUsername + "'";
                db = this.getWritableDatabase();
                cursor = db.rawQuery(getUserQuery, null);
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(UserTable_Column_Id));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Fullname));
                        String dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_DateOfBirth));
                        String email = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Email));
                        String password = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Password));
                        String username = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Username));
                        String gender = cursor.getString(cursor.getColumnIndexOrThrow(UserTable_Column_Gender));

                        toRet = new User(id, name, dateOfBirth, email, password, username, gender);
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return toRet;
    }

    //endregion
    //region Delete user
    public boolean DeleteUser(User user) {
        return DeleteUser(user.GetId());
    }
    public boolean DeleteUser(int userId) {
        //String deleteUserQuery = "DELETE FROM "+USERTABLE_NAME+" WHERE "+
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(USERTABLE_NAME, UserTable_Column_Id + "=" + userId, null) > 0;
    }
    //endregion
    //region Truncate user table
    public void TruncateUserTable() {
        try {
            String truncateUserTableQuery = "DELETE FROM " + USERTABLE_NAME;
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(truncateUserTableQuery);
        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    //endregion

    //BOOKMARK IMAGE GROUP CONTEXT
    //region AddBookmarkImageGroup
    public boolean AddBookmarkImageGroup(BookmarkImageGroup inGroup) {
        try {
            if (inGroup != null) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues cv = new ContentValues();

                cv.put(BookmarkImageGroupTable_Column_ParentUserId, inGroup.getParentUserId());
                cv.put(BookmarkImageGroupTable_Column_Title, inGroup.getTitle());
                cv.put(BookmarkImageGroupTable_Column_Description, inGroup.getDescription());
                cv.put(BookmarkImageGroupTable_Column_PrivateGroup, inGroup.getPrivateGroup());
                cv.put(BookmarkImageGroupTable_Column_PrivateGroupPassword, inGroup.getPrivateGroupPassword());

                long result = db.insert(BOOKMARKIMAGEGROUP_NAME, null, cv);
                Toast.makeText(context, "Grupa uspešno dodata", Toast.LENGTH_SHORT).show();
                if (result != 0)
                    return true;
            } else {
                Toast.makeText(context, "Gropa nije pravilno kreirana", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }
    //endregion
    //region GetGroupsByUserId (by parentUserId)
    public ArrayList<BookmarkImageGroup> GetGroupsByUserId(int inParentUserId) {
        ArrayList<BookmarkImageGroup> toRet = null;
        try {
            String getUserQuery = "SELECT * FROM " + BOOKMARKIMAGEGROUP_NAME + " WHERE " + BookmarkImageGroupTable_Column_ParentUserId + " = " + inParentUserId;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(getUserQuery, null);
            if (cursor != null && cursor.getCount() > 0) {
                toRet = new ArrayList<>();
                while (cursor.moveToNext()) {

                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(BookmarkImageGroupTable_Column_Id));
                    int parentUserId = cursor.getInt(cursor.getColumnIndexOrThrow(BookmarkImageGroupTable_Column_ParentUserId));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(BookmarkImageGroupTable_Column_Title));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(BookmarkImageGroupTable_Column_Description));
                    int privateGroup = cursor.getInt(cursor.getColumnIndexOrThrow(BookmarkImageGroupTable_Column_PrivateGroup));
                    String privateGroupPassword = cursor.getString(cursor.getColumnIndexOrThrow(BookmarkImageGroupTable_Column_PrivateGroupPassword));

                    BookmarkImageGroup tmpGroup = new BookmarkImageGroup(id,parentUserId,title,description,privateGroup,privateGroupPassword);

                    toRet.add(tmpGroup);
                    //Toast.makeText(context, "Debug: Group added to toRetList", Toast.LENGTH_LONG).show();

                }
            }else {
                //Toast.makeText(context, "Debug: Nothing found", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return toRet;
    }
    //endregion
    //region Delete BookmarkImageGroup
    public boolean DeleteBookmarkImageGroup(BookmarkImageGroup group) {
        return DeleteBookmarkImageGroup(group.getId());
    }

    public boolean DeleteBookmarkImageGroup(int groupId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(BOOKMARKIMAGEGROUP_NAME, BookmarkImageGroupTable_Column_Id + " = " + groupId, null) > 0;
    }
    //endregion

    //BOOKMARK IMAGE CONTEXT
    //region AddBookmarkImage
    public boolean AddBookmarkImage(BookmarkImage inImage) {
        try {
            if (inImage != null) {
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues cv = new ContentValues();

                cv.put(BookmarkImageTable_Column_ParentGroupId, inImage.getParentGroupId());
                cv.put(BookmarkImageTable_Column_FromWebImage, inImage.getFromWebImage());
                cv.put(BookmarkImageTable_Column_WebImageUrl, inImage.getWebImageUrl());
                cv.put(BookmarkImageTable_Column_LocalImagePath, inImage.getLocalImagePath());

                long result = db.insert(BOOKMARKIMAGE_NAME, null, cv);
                Toast.makeText(context, "Slika uspešno dodata", Toast.LENGTH_SHORT).show();
                if (result != 0)
                    return true;
            } else {
                Toast.makeText(context, "SLika nije pravilno kreirana", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false;
    }
    //endregion
    //region GetImagesByGroupId (by parentGroupId)
    public ArrayList<BookmarkImage> GetImagesByGroupId(int inParentGroupId) {
        ArrayList<BookmarkImage> toRet = null;
        try {
            String getUserQuery = "SELECT * FROM " + BOOKMARKIMAGE_NAME + " WHERE " + BookmarkImageTable_Column_ParentGroupId + " = " + inParentGroupId;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(getUserQuery, null);
            if (cursor != null && cursor.getCount() > 0) {
                toRet = new ArrayList<>();
                while (cursor.moveToNext()) {

                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(BookmarkImageTable_Column_Id));
                    int parentGroupId = cursor.getInt(cursor.getColumnIndexOrThrow(BookmarkImageTable_Column_ParentGroupId));
                    int fromWebImage = cursor.getInt(cursor.getColumnIndexOrThrow(BookmarkImageTable_Column_FromWebImage));
                    String webImageUrl = cursor.getString(cursor.getColumnIndexOrThrow(BookmarkImageTable_Column_WebImageUrl));
                    String localImagePath = cursor.getString(cursor.getColumnIndexOrThrow(BookmarkImageTable_Column_LocalImagePath));

                    BookmarkImage tmpImg = new BookmarkImage(id,parentGroupId,fromWebImage,webImageUrl,localImagePath);

                    toRet.add(tmpImg);
                    //Toast.makeText(context, "Debug: Group added to toRetList", Toast.LENGTH_LONG).show();

                }
            }else {
                //Toast.makeText(context, "Debug: Nothing found", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return toRet;
    }
    //endregion
    //region Delete BookmarkImage
    public boolean DeleteBookmarkImage(BookmarkImage image) {
        return DeleteBookmarkImage(image.getId());
    }

    public boolean DeleteBookmarkImage(int imageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(BOOKMARKIMAGE_NAME, BookmarkImageTable_Column_Id + " = " + imageId, null) > 0;
    }
    //endregion

}//[Class]
