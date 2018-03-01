package com.jiahaozhang.moodiary.ui;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.jiahaozhang.moodiary.R;
import com.jiahaozhang.moodiary.adapter.DrawerListAdapter;
import com.jiahaozhang.moodiary.bean.Note;
import com.jiahaozhang.moodiary.bean.NoteType;
import com.jiahaozhang.moodiary.utils.DoubleClickExitHelper;
import com.jiahaozhang.moodiary.utils.NoteUtils;
import com.nineoldandroids.animation.ObjectAnimator;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


/**
 * 主页测试init()界面.
 */
public class HomeActivity extends AppCompatActivity {

    public static final String CURRENT_NOTE_MENU_KEY = "current_note_menu_key";
    private static final String TAG = "HomeActivity";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawerlayout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.drawer_listview)
    ListView mDrawerListView;

    @BindView(R.id.drawer_menu)
    View drawerRootView;

    @BindView(R.id.exit_app)
    Button exitAppBut;

    @BindView(R.id.edit_note_type)
    Button editNoteTypeBut;
    @BindView(R.id.iv_drawer_bg)
    ImageView mIvDrawerBg;

    private SearchView searchView;


    private ActionBarDrawerToggle mDrawerToggle;

    public List<NoteType> mNoteTypeList;
    private DrawerListAdapter drawerListAdapter;
    private int currentItem = 0;

    private boolean rightHandOn = false;
    private boolean cardLayout = true;

    private boolean showItemIndicator[] = {true, true, true};
    private int orderListIndicator = 0;

    private SharedPreferences sharedPreferences;

    private NoteListFragment noteListFragment;

    private boolean noteAddEvent = false;
    private boolean noteClearEvent = false;
    private boolean noteRestoreDefaultEvent = false;
    private boolean noteRefreshNoteTypeEvent = false;

    private String currentNoteTypeString;

    private DoubleClickExitHelper mDoubleClickExitHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (savedInstanceState != null) {
            currentItem = savedInstanceState.getInt(CURRENT_NOTE_MENU_KEY);
        }
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mDoubleClickExitHelper = new DoubleClickExitHelper(this);

        sharedPreferences = getSharedPreferences(SettingActivity.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

        initToolBar();
        initDrawListView();
        initRecyclerView();
    }


    private void openOrCloseDrawer() {
        if (mDrawerLayout.isDrawerOpen(drawerRootView)) {
            mDrawerLayout.closeDrawer(drawerRootView);
        } else {
            mDrawerLayout.openDrawer(drawerRootView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (rightHandOn != sharedPreferences.getBoolean(getString(R.string.right_hand_mode_key), false)) {
            rightHandOn = !rightHandOn;
            if (rightHandOn) {
                setMenuListViewGravity(Gravity.END);
            } else {
                setMenuListViewGravity(Gravity.START);
            }
        }

        if (cardLayout != sharedPreferences.getBoolean(getString(R.string.card_note_item_layout_key), true)) {
            cardLayout = !cardLayout;
            noteListFragment.selectLayoutManager(cardLayout);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (noteAddEvent) {
            //checkNoteListFragment();
            noteListFragment.reloadNewestAddNote();
            noteAddEvent = false;
        }
        if (noteClearEvent) {
            noteListFragment.clearNoteList();
            noteClearEvent = false;
        }
        if (noteRestoreDefaultEvent) {
            drawerListAdapter.refreshDrawerList(initNoteType());
            noteListFragment.clearNoteList();
            noteRestoreDefaultEvent = false;
        }
        if (noteRefreshNoteTypeEvent) {
            drawerListAdapter.refreshDrawerList(initNoteType());
            noteRefreshNoteTypeEvent = false;
        }
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //mDrawerToggle.syncState();
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openOrCloseDrawer();
                }
            });
        }
    }


    //保存当前 默认笔记在第一栏目上.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_NOTE_MENU_KEY, currentItem);
    }

    private void initToolBar() {
        //toolbar.setTitle(getResources().getString(R.string.notes_menu_default));//设置标题
        setSupportActionBar(toolbar);//设置toolbar
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

    }


    private void initDrawListView() {
        //开始设置动态 drawer
        // 给侧滑内容的背景添加动画效果

        mIvDrawerBg = (ImageView) findViewById(R.id.iv_drawer_bg);
        float curTranslationY = mIvDrawerBg.getTranslationY();
//        Log.d(TAG, "getTranslationY is :"+curTranslationY);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mIvDrawerBg, "translationY", curTranslationY,
                -100f, 10, curTranslationY);
        animator.setDuration(5000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();
        //绘制动画结束


        mNoteTypeList = initNoteType();
        currentNoteTypeString = mNoteTypeList.get(currentItem).getNoteTypeString();
        toolbar.setTitle(currentNoteTypeString);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                toolbar.setTitle(getResources().getString(R.string.toolbar_title));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                toolbar.setTitle(currentNoteTypeString);
            }
        };
        mDrawerToggle.syncState();

        mDrawerLayout.setDrawerListener(mDrawerToggle);//设置监听器

        drawerListAdapter = new DrawerListAdapter(this, mNoteTypeList);
        mDrawerListView.setAdapter(drawerListAdapter);

        mDrawerListView.setItemChecked(currentItem, true);

        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawerLayout.closeDrawer(drawerRootView);
                if (currentItem != position) {
                    currentNoteTypeString = mNoteTypeList.get(position).getNoteTypeString();
                    toolbar.setTitle(currentNoteTypeString);

                    mDrawerListView.setItemChecked(position, true);
                    changeToSelectNoteType(position);

                    currentItem = position;
                }
            }
        });

        rightHandOn = sharedPreferences.getBoolean(getString(R.string.right_hand_mode_key), false);
        if (rightHandOn) {
            setMenuListViewGravity(Gravity.END);
        }
//        //设置 全屏模式
//        DisplayMetrics metric = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int windowsWight = metric.widthPixels;
//        int  windowsHeight = metric.heightPixels;
//
//        ViewGroup.LayoutParams leftParams = drawerRootView.getLayoutParams();
//        leftParams.height = windowsHeight;
//        leftParams.width = windowsWight;
//        drawerRootView.setLayoutParams(leftParams);
    }

    private List<NoteType> initNoteType() {
        List<NoteType> noteTypes;
        if (sharedPreferences.getBoolean(getString(R.string.first_init_app_key), true)) {
            noteTypes = new ArrayList<NoteType>();
            String[] defaultNoteTypeList = getResources().getStringArray(R.array.default_notetype_list);
            for (int i = 0; i < defaultNoteTypeList.length; i++) {
                NoteType noteType = new NoteType();
                noteType.setNoteType(i);
                noteType.setNoteTypeString(defaultNoteTypeList[i]);
                noteType.save();
                noteTypes.add(noteType);
            }
            sharedPreferences.edit().putBoolean(getString(R.string.first_init_app_key), false).commit();
        } else {
            noteTypes = DataSupport.order("notetype").find(NoteType.class);
        }
        return noteTypes;
    }

    private void initRecyclerView() {
        if (sharedPreferences.getBoolean(getString(R.string.card_note_item_layout_key), true)) {
            cardLayout = true;
        } else {
            cardLayout = false;
        }
        FragmentManager fm = getSupportFragmentManager();
        noteListFragment = NoteListFragment.newInstance(currentItem, cardLayout);
        fm.beginTransaction().replace(R.id.drawer_content, noteListFragment, null).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        //searchItem.expandActionView();
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        ComponentName componentName = getComponentName();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName));

        searchView.setQueryHint(getString(R.string.search_note));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                noteListFragment.filter(s);
                if (s.length() == 0) {
                    noteListFragment.showFloatActionBut();
                } else {
                    noteListFragment.hideFloatActionBut();
                }
                return true;
            }
        });
        /*MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                recyclerAdapter.setUpFactor();
                refreshLayout.setEnabled(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                refreshLayout.setEnabled(true);
                return true;
            }
        });*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
           /* case R.id.action_search:
                if (mDrawerLayout.isDrawerOpen(drawerRootView)) {
                    mDrawerLayout.closeDrawer(drawerRootView);
                }
                break;*/
            case R.id.action_settings:
                showNoteSettings();
                break;
            case R.id.action_orderItem:
                orderNoteList();
                break;
            case R.id.action_showItem:
                setNoteListShowItem();
                break;
            case R.id.action_cleanNoteItem:
                NoteType clearNoteType = (DataSupport.where("notetype = ?", String.valueOf(currentItem)).find(NoteType.class, true)).get(0);
                List<Note> clearNoteList = clearNoteType.getNoteList();
                for (Note note : clearNoteList) {
                    note.delete();
                }
                noteListFragment.clearNoteList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void changeToSelectNoteType(int selectNoteItem) {
        //checkNoteListFragment();
        noteListFragment.reloadNoteListAsSelectNoteType(selectNoteItem);
    }

    private void setMenuListViewGravity(int gravity) {
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) drawerRootView.getLayoutParams();
        params.gravity = gravity;
        drawerRootView.setLayoutParams(params);
    }

    private void showNoteSettings() {
        Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
        startActivity(intent);
    }

    private void orderNoteList() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_order_type_note_list));
        String orderItem[] = {getString(R.string.order_createTime_item), getString(R.string.order_laseEdit_item), getString(R.string.order_title_item)};
        builder.setSingleChoiceItems(orderItem, orderListIndicator, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                orderListIndicator = which;
            }
        });
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        noteListFragment.setorderNoteListType(orderListIndicator);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                    default:
                        break;
                }
            }
        };
        builder.setPositiveButton(R.string.sure, listener);
        builder.setNegativeButton(R.string.cancel, listener);
        builder.show();
    }


    public void setNoteListShowItem() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.select_show_note_list_item));
        String showItem[] = {getString(R.string.note_list_noteType_item), getString(R.string.note_list_createTime_item), getString(R.string.note_list_lastEditorTime_item)};
        builder.setMultiChoiceItems(showItem, showItemIndicator, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                showItemIndicator[which] = isChecked;
            }
        });
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        noteListFragment.setShowNoteListItem(showItemIndicator);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                    default:
                        break;
                }
            }
        };
        builder.setPositiveButton(R.string.sure, listener);
        builder.setNegativeButton(R.string.cancel, listener);
        builder.show();
    }

    public void onEvent(Integer event) {
        switch (event) {
            case NoteUtils.NOTE_ADD_EVENT:
                noteAddEvent = true;
                break;
            case NoteUtils.NOTE_UPDATE_EVENT:
                changeToSelectNoteType(currentItem);
                break;
            case NoteUtils.NOTE_CLEARALL_EVENT:
                noteClearEvent = true;
                break;
            case NoteUtils.NOTE_RESTORY_DEFAULT_EVENT:
                noteRestoreDefaultEvent = true;
                break;
            case NoteUtils.NOTE_TYPE_UPDATE_EVENT:
                noteRefreshNoteTypeEvent = true;
                break;
            case NoteUtils.CHANGE_THEME_EVENT:
                this.recreate();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @OnClick(R.id.edit_note_type)
    public void onEditNoteType() {
        //mDrawerLayout.closeDrawer(drawerRootView);

        Intent intent = new Intent(this, EditNoteTypeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.exit_app)
    public void onExitApp() {
        File file= new File("/data/data/"+getPackageName().toString()+"/shared_prefs","config.xml");

        if(file.exists()){

            file.delete();
            Log.d(TAG, "删除config.xml成功");
        }

        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(drawerRootView)) {
                mDrawerLayout.closeDrawer(drawerRootView);
                return true;
            }
            return mDoubleClickExitHelper.onKeyDown(keyCode, event);
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mDrawerLayout.isDrawerOpen(drawerRootView)) {
                mDrawerLayout.closeDrawer(drawerRootView);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
