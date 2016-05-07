package com.movies.popular.jrvm.com.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivityFragment extends Fragment {

    private static final String STATES_MOVIES = "states_movies";
    private static final int SETT_ID = 1;
    MainGridViewAdapter mGridAdapter;
    ArrayList<MainGridViewItem> mGridData = new ArrayList<>();
    SharedPreferences prefis;

    final String TITLE_EXTRA = "title_extra";
    final String POSTER_EXTRA = "poster_extra";
    final String SYNOPSIS_EXTRA = "synopsis_extra";
    final String RATING_EXTRA = "rating_extra";
    final String RELEASE_DATE_EXTRA = "release_date_extra";

    public MainActivityFragment() {
    }

    public void updateTMDB() {
        prefis = PreferenceManager.getDefaultSharedPreferences(getActivity());
        new TMDBTask().execute(prefis.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default)));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivityForResult(new Intent(getActivity(), SettingsActivity.class), SETT_ID);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATES_MOVIES, mGridData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        GridView mGridView = (GridView) root.findViewById(R.id.main_gridview);

        mGridAdapter = new MainGridViewAdapter(getActivity(), R.layout.main_gridview_item, mGridData);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(TITLE_EXTRA, mGridAdapter.getItem(position).getdMTitle());
                intent.putExtra(POSTER_EXTRA, mGridAdapter.getItem(position).getdMPoster());
                intent.putExtra(SYNOPSIS_EXTRA, mGridAdapter.getItem(position).getdMSynopsis());
                intent.putExtra(RATING_EXTRA, String.valueOf(mGridAdapter.getItem(position).getdMRating()));
                intent.putExtra(RELEASE_DATE_EXTRA, mGridAdapter.getItem(position).getdMReleaseDate());

                startActivity(intent);

            }
        });

        if (savedInstanceState != null) {
            mGridData = savedInstanceState.getParcelableArrayList(STATES_MOVIES);
            arrayToGridAdapter(mGridData);
        } else {
            updateTMDB();
        }

        return root;
    }

    public void arrayToGridAdapter(ArrayList<MainGridViewItem> dArray) {
        mGridAdapter.clear();
        for (int i = 0; i < dArray.size(); i++) {
            mGridAdapter.add(new MainGridViewItem(dArray.get(i).getdMTitle(),
                    "http://image.tmdb.org/t/p/w185/" + dArray.get(i).getdMPoster(),
                    dArray.get(i).getdMSynopsis(),
                    dArray.get(i).getdMRating(),
                    dArray.get(i).getdMReleaseDate()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETT_ID) {
            updateTMDB();
            if ((prefis.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default))
                    .equals(getString(R.string.pref_sort_default)))) {
                new Toast(getActivity()).makeText(getActivity(), "Sorted by Most Popular Movies", Toast.LENGTH_SHORT).show();
            } else {
                new Toast(getActivity()).makeText(getActivity(), "Sorted by Highest Rated Movies", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class TMDBTask extends AsyncTask<String, Void, ArrayList<MainGridViewItem>> {

        private final String LOG_TAG = TMDBTask.class.getSimpleName();

        @Override
        protected ArrayList<MainGridViewItem> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String strJsonObject = null;

            try {
                final String TMDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String APPKEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(TMDB_BASE_URL + params[0]).buildUpon()
                        .appendQueryParameter(APPKEY_PARAM, BuildConfig.TMDB_API_KEY)
                        .build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                strJsonObject = buffer.toString();

            } catch (IOException e) {

                Log.e(LOG_TAG, "Error ", e);
                return null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getTMDBDataFromJson(strJsonObject);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        private ArrayList<MainGridViewItem> getTMDBDataFromJson(String jsonStr) throws JSONException {

            ArrayList<MainGridViewItem> resultData = new ArrayList<>();
            resultData.clear();

            final String resultadosJ = "results";
            final String originaltitleJ = "original_title";
            final String posterJ = "poster_path";
            final String synopsisJ = "overview";
            final String userratingJ = "vote_average";
            final String releasedateJ = "release_date";


            JSONObject data1 = new JSONObject(jsonStr);
            JSONArray resultados = data1.getJSONArray(resultadosJ);

            for (int i = 0; i < resultados.length(); i++) {
                JSONObject detalles = resultados.getJSONObject(i);
                resultData.add(new MainGridViewItem(detalles.getString(originaltitleJ),
                        detalles.getString(posterJ), detalles.getString(synopsisJ),
                        detalles.getDouble(userratingJ), detalles.getString(releasedateJ)));
            }
            return resultData;
        }

        @Override
        protected void onPostExecute(ArrayList<MainGridViewItem> lists) {
            super.onPostExecute(lists);

            if (lists != null) {
                mGridData.clear();
                mGridData = lists;
                arrayToGridAdapter(lists);
            }
        }

    }
}
