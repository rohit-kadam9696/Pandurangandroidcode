package com.twd.chitboyapp.spsskl;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.twd.chitboyapp.spsskl.constant.ConnectionUpdator;
import com.twd.chitboyapp.spsskl.constant.Constant;
import com.twd.chitboyapp.spsskl.constant.ConstantFunction;
import com.twd.chitboyapp.spsskl.constant.DateTimeChecker;
import com.twd.chitboyapp.spsskl.constant.MenuHandler;
import com.twd.chitboyapp.spsskl.database.DBHelper;
import com.twd.chitboyapp.spsskl.databinding.ActivityHomeBinding;
import com.twd.chitboyapp.spsskl.interfaces.RefreshComplete;
import com.twd.chitboyapp.spsskl.pojo.MenuBean;
import com.twd.chitboyapp.spsskl.ui.SubMenuFragment;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RefreshComplete {

    //private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private NavigationView navigationView;
    private List<MenuBean> menuBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.appBarHome.toolbar;
        setSupportActionBar(toolbar);
        ConnectionUpdator connectionUpdator = new ConnectionUpdator(this);
        connectionUpdator.startObserve(this);
        DateTimeChecker dateTimeChecker = new DateTimeChecker();
        dateTimeChecker.checkAutoDate(this, true);


        DrawerLayout drawer = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       /* mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();*/
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        // NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        addMenuItemInNavMenuDrawer();
        setHeader();
    }

    private void setHeader() {
        ConstantFunction cf = new ConstantFunction();
        View header = navigationView.getHeaderView(0);
        TextView txtcode = header.findViewById(R.id.txtcode);
        TextView txtname = header.findViewById(R.id.txtname);
        String[] key = new String[]{getResources().getString(R.string.chitboyprefchit_boy_id), getResources().getString(R.string.chitboyprefname)};
        String[] values = new String[]{"", ""};
        String[] result = cf.getSharedPrefValue(HomeActivity.this, key, values);
        txtcode.setText(getResources().getString(R.string.code) + " : " + result[0]);
        txtname.setText(getResources().getString(R.string.name) + " : " + result[1]);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Constant.onBackpress(HomeActivity.this);
        }
    }


    private void addMenuItemInNavMenuDrawer() {
        ConstantFunction cf = new ConstantFunction();
        String[] key = new String[]{getResources().getString(R.string.chitboyprefgroupids)};
        String[] values = new String[]{""};
        String[] result = cf.getSharedPrefValue(HomeActivity.this, key, values);
        DBHelper dbHelper = new DBHelper(HomeActivity.this);
        if (!result[0].equalsIgnoreCase("")) {
            menuBeans = dbHelper.getMenu(result[0]);
            Menu menu = navigationView.getMenu();
            menu.clear();
            int itemsize = menuBeans.size();
            int selpos = 0;
            String[] data = cf.getSharedPrefValue(HomeActivity.this, new String[]{getResources().getString(R.string.preflastgroup)}, new String[]{"-1"});
            for (int i = 0; i < itemsize; i++) {
                MenuBean menuBean = menuBeans.get(i);
                if (menuBean.getGroupId().equals(data[0])) {
                    selpos = i;
                }
                menu.add(menuBean.getGroupName());
            }
            if (itemsize > 0) {
                navigationView.getMenu().getItem(selpos).setChecked(true);
                onNavigationItemSelected(navigationView.getMenu().getItem(selpos));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        MenuHandler cf = new MenuHandler();
        return cf.openCommon(this, item, HomeActivity.this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuHandler cf = new MenuHandler();
        return cf.performRefreshOption(menu, this);
    }


    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int jarsize = menuBeans.size();
        String itemName = (String) item.getTitle();
        int groupId = -1;
        for (int i = 0; i < jarsize; i++) {
            MenuBean menuBean = menuBeans.get(i);
            if (itemName.equalsIgnoreCase(menuBean.getGroupName())) {
                groupId = Integer.parseInt(menuBean.getGroupId());
                break;
            }
        }
        //Toast.makeText(this, "groupId : " + groupId, Toast.LENGTH_SHORT).show();

        Fragment inputFragment = SubMenuFragment.newInstance(String.valueOf(groupId));
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_home, inputFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRefreshComplete() {
        addMenuItemInNavMenuDrawer();
        setHeader();
    }
}