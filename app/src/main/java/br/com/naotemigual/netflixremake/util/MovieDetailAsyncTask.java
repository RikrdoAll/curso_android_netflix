package br.com.naotemigual.netflixremake.util;

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

import javax.net.ssl.HttpsURLConnection;

import br.com.naotemigual.netflixremake.model.Movie;
import br.com.naotemigual.netflixremake.model.MovieDetail;

/***
 * 21/06/2021 
 *
 * @author Ricardo Pires
 ***/

public class MovieDetailAsyncTask extends AsyncTask<String, Void, MovieDetail> {


    private final WeakReference<Context> context;
    private ProgressDialog dialog;
    private MovieDetailLoader movieDetailLoader;

    public void setMovieDetailLoader(MovieDetailLoader movieDetailLoader) {
        this.movieDetailLoader = movieDetailLoader;
    }

    public MovieDetailAsyncTask(Context context) {
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
    protected MovieDetail doInBackground(String... params) {
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

            MovieDetail movieDetail = getMovieDetail(new JSONObject(jsonAsString));

            inputStream.close();
            return movieDetail;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    private MovieDetail getMovieDetail(JSONObject jsonObject) throws JSONException {
        MovieDetail movieDetail = new MovieDetail();

        movieDetail.getMovie().setId(jsonObject.getInt("id"));
        movieDetail.getMovie().setTitle(jsonObject.getString("title"));
        movieDetail.getMovie().setDesc(jsonObject.getString("desc"));
        movieDetail.getMovie().setCast(jsonObject.getString("cast"));
        movieDetail.getMovie().setCoverUrl(jsonObject.getString("cover_url"));

        JSONArray categoriasJson = jsonObject.getJSONArray("movie");
        for (int i = 0; i < categoriasJson.length(); i++) {
            JSONObject movieJson = categoriasJson.getJSONObject(i);

            Movie movie = new Movie();
            movie.setId(movieJson.getInt("id"));
            movie.setCoverUrl(movieJson.getString("cover_url"));
            movieDetail.getMoviesSimilar().add(movie);
            if (isCancelled())
                return null;
        }

        return movieDetail;
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
    protected void onPostExecute(MovieDetail movieDetail) {
        super.onPostExecute(movieDetail);
        dialog.dismiss();

        if (movieDetailLoader != null)
            movieDetailLoader.onResult(movieDetail);
    }

    public interface MovieDetailLoader {
        void onResult(MovieDetail movieDetail);
    }
}




