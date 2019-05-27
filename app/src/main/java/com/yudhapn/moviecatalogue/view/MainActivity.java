package com.yudhapn.moviecatalogue.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.yudhapn.moviecatalogue.R;
import com.yudhapn.moviecatalogue.util.NetworkReceiver;
import com.yudhapn.moviecatalogue.viewmodel.MainViewModel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NetworkReceiver.NetworkCallback {
    SearchView searchView;
    String currentQuery;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private MainViewModel viewModel;
    private NetworkReceiver receiver;
    private Snackbar snackbar;
    private Menu menu;

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        Set<Integer> allLayout = new HashSet<>();
        allLayout.add(R.id.movie_dest);
        allLayout.add(R.id.tvshow_dest);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(allLayout).build();
        navController = NavHostFragment.findNavController(Objects.requireNonNull(getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment)));
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        snackbar = Snackbar.make(findViewById(R.id.main_layout)
                , R.string.connection, Snackbar.LENGTH_INDEFINITE);
        receiver = new NetworkReceiver(this);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, filter);
        onHideBottomNavigation(navController);
    }

    private void onHideBottomNavigation(NavController navController) {
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (menu != null) {
                if (destination.getId() == R.id.detail_tvshow_dest
                        || destination.getId() == R.id.detail_movie_dest
                        || destination.getId() == R.id.favorite_dest) {
                    bottomNavigationView.setVisibility(View.GONE);
                    if (searchView != null) {
                        MenuItem item = menu.findItem(R.id.action_search);
                        item.collapseActionView();
                        item.setVisible(false);
                        menu.findItem(R.id.action_favorite).setVisible(false);
                        menu.findItem(R.id.action_settings).setVisible(false);
                    }
                } else if (destination.getId() == R.id.search_dest) {
                    bottomNavigationView.setVisibility(View.GONE);
                    MenuItem item = menu.findItem(R.id.action_search);
                    menu.findItem(R.id.action_favorite).setVisible(false);
                    if (!item.isActionViewExpanded()) {
                        menu.findItem(R.id.action_settings).setVisible(true);
                        item.setVisible(true);
                        item.expandActionView();
                        viewModel.getQuerySearch().observe(this, query -> searchView.setQuery(query, false));
                    }
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    if (searchView != null) {
                        menu.findItem(R.id.action_favorite).setVisible(true);
                        menu.findItem(R.id.action_settings).setVisible(true);
                        menu.findItem(R.id.action_search).setVisible(true);
                        searchView.setVisibility(View.VISIBLE);
                    }
                }
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchView = (SearchView) (menu.findItem(R.id.action_search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setIconified(false);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    currentQuery = query;
                    viewModel.setQuerySearch(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                return true;
            case R.id.action_favorite:
                navController.navigate(R.id.favorite_dest);
                return true;
            case R.id.action_search:
                navController.navigate(R.id.search_dest);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected() {
        snackbar.dismiss();
    }

    @Override
    public void onDisconnected() {
        snackbar.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}