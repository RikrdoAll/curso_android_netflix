package br.com.naotemigual.netflixremake;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.naotemigual.netflixremake.model.Movie;
import br.com.naotemigual.netflixremake.model.MovieDetail;
import br.com.naotemigual.netflixremake.util.MovieDetailAsyncTask;

public class MovieActivity extends AppCompatActivity implements MovieDetailAsyncTask.MovieDetailLoader{

    private TextView txtTitle;
    private TextView txtDesc;
    private TextView txtCast;
    private RecyclerView recyclerView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        txtTitle = findViewById(R.id.text_view_title);
        txtDesc = findViewById(R.id.text_view_desc);
        txtCast = findViewById(R.id.text_view_cast);
        imageView = findViewById(R.id.image_view_cover); 
        recyclerView = findViewById(R.id.recycler_view_similar);

        Toolbar toobar = findViewById(R.id.toolbar);
        setSupportActionBar(toobar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
             getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        }

        LayerDrawable drawable = (LayerDrawable) ContextCompat.getDrawable(this, R.drawable.shadows);

//        troca foto
        if(drawable != null) {
            Drawable movieCover = ContextCompat.getDrawable(this, R.drawable.movie);
            drawable.setDrawableByLayerId(R.id.cover_drawble, movieCover);
            ((ImageView) findViewById(R.id.image_view_cover)).setImageDrawable(drawable);
        }

        txtTitle.setText("Batman");
        txtDesc.setText("O joven Bruce Wane O joven Bruce Wane O joven Bruce Wane O joven Bruce Wane O joven Bruce Wane O joven Bruce Wane O joven Bruce Wane O joven Bruce Wane O joven Bruce Wane O joven Bruce Wane ");
        txtCast.setText(getString(R.string.cast, "Chirstina Bale, Michael Caine, Ricardo, Gabriel, Bianca"));


        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < 30; i++) {

            Movie movie = new Movie();
            movies.add(movie);
        }

        MovieAdapater movieAdapater = new MovieAdapater(movies);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(movieAdapater);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("id");

            Log.i("Teste", "Posição do filme selecionado: " + id);
            if (id > 3) {
                Toast.makeText(this, "Filme indisponível "+ id, Toast.LENGTH_LONG).show();
//                Dialog dialog = new Dialog(this.getBaseContext());
//                dialog.setTitle("filme não disponível");
//                dialog.show();
                return;
            }


            MovieDetailAsyncTask movieDetailAsyncTask = new MovieDetailAsyncTask(this);
            movieDetailAsyncTask.setMovieDetailLoader(this);
            movieDetailAsyncTask.execute("https://tiagoaguiar.co/api/netflix/" + id);
        }
    }

    @Override
    public void onResult(MovieDetail movieDetail) {

        txtTitle.setText(movieDetail.getMovie().getTitle());
        txtDesc.setText(movieDetail.getMovie().getDesc());
        txtCast.setText(movieDetail.getMovie().getCast());

//        movieDetail.getMovie().getCoverUrl();



//        recyclerView = findViewById(R.id.recycler_view_similar);



    }

    private class MovieHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_cover);
        }
    }


    private class MovieAdapater extends RecyclerView.Adapter<MovieHolder> {

        private final List<Movie> movies;

        public MovieAdapater(List<Movie> movies) {
            this.movies = movies;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item_similar, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
            Movie movie = movies.get(position);
//            holder.imageView.setImageResource(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }


}