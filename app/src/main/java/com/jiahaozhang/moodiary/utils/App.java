package com.jiahaozhang.moodiary.utils;

import android.database.sqlite.SQLiteDatabase;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

/**
 * Created by acanprince on 2017/12/9.
 *
 * @author acanprince
 * @version $Rev$
 * @des $   从APP 获取全局的Context，创建便进来进行初始化
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */

public class App extends LitePalApplication {
    public static final String TAG = "Heart_Of_Sun~";

    @Override
    public void onCreate() {
        super.onCreate();
        SQLiteDatabase db = Connector.getDatabase();//初始化数据库
    }
}
