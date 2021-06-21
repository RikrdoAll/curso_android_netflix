package br.com.naotemigual.netflixremake.util;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.net.ssl.HttpsURLConnection;

import br.com.naotemigual.netflixremake.model.Category;
import br.com.naotemigual.netflixremake.model.Movie;

/***
 * 20/06/2021 
 *
 * @author Ricardo Pires
 ***/
public class CategoryAsyncTask extends AsyncTask<String, Void, List<Category>> {


    private final WeakReference<Context> context;
    private ProgressDialog dialog;

    private CategorLoader categorLoader;

    public void setCategorLoader(CategorLoader categorLoader) {
        this.categorLoader = categorLoader;
    }

    public CategoryAsyncTask(Context context) {
        this.context = new WeakReference<>(context);
    }

    // main-thread
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Context con = this.context.get();
        if (con != null)
            dialog = ProgressDialog.show(con, "Carregando", "", true);
    }

    // thread - background
    @Override
    protected List<Category> doInBackground(String... params) {
        String url = params[0];

        try {
            URL requestUrl = new URL(url);
            HttpsURLConnection urlConnection = (HttpsURLConnection) requestUrl.openConnection();
            urlConnection.setReadTimeout(2000);
            urlConnection.setConnectTimeout(2000);
            int responseCode = urlConnection.getResponseCode();
            if (responseCode > 400) {
                throw new IOException("Error connection server. Código do Erro: " + responseCode);
            }

            InputStream inputStream = urlConnection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            String jsonAsString = toString(bufferedInputStream);

            Log.d("Teste", jsonAsString);


            List<Category> categories = getCatogories(new JSONObject(jsonAsString));

            inputStream.close();
            return categories;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    private List<Category> getCatogories(JSONObject jsonObject) throws JSONException {
        List<Category> categories = new ArrayList<>();

        JSONArray categoriasJson = jsonObject.getJSONArray("category");
        for (int i = 0; i < categoriasJson.length(); i++) {
            JSONObject categoriaJson = categoriasJson.getJSONObject(i);

            Category category = new Category();
            category.setName(categoriaJson.getString("title"));

            JSONArray moviesJson = categoriaJson.getJSONArray("movie");

            for (int j = 0; j < moviesJson.length(); j++) {

                JSONObject movieJson = moviesJson.getJSONObject(j);
                Movie movie = new Movie();
                movie.setCoverUrl(movieJson.getString("cover_url"));

                category.getMovies().add(movie);

                if (isCancelled())
                    return null;
            }

            categories.add(category);
        }

        return categories;
    }

    private String toString(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = inputStream.read(bytes)) > 0) {
            byteArrayOutputStream.write(bytes, 0, lidos);
        }
        return new String(byteArrayOutputStream.toByteArray());
    }

    // main-thread
    @Override
    protected void onPostExecute(List<Category> categories) {
        super.onPostExecute(categories);
        dialog.dismiss();

        if (categorLoader != null)
            categorLoader.onResult(categories);
    }

    public interface CategorLoader {
        void onResult(List<Category> categories);
    }
}



