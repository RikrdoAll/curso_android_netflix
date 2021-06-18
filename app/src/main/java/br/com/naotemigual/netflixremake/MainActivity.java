package br.com.naotemigual.netflixremake;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.naotemigual.netflixremake.model.Category;
import br.com.naotemigual.netflixremake.model.Movie;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Teste 1"));
        categories.add(new Category("Teste 2"));
        categories.add(new Category("Teste 3"));
        categories.add(new Category("Teste 4"));
        categories.add(new Category("Teste 5"));
        categories.add(new Category("Teste 6"));
        categories.add(new Category("Teste 7"));

        for (Category category : categories) {

            category.setMovies(new ArrayList<>());
            for (int i = 0; i < 20; i++) {
                Movie movie = new Movie();

                if (i%2 == 0)
                    movie.setCoverUrl(R.drawable.movie);
                else
                    movie.setCoverUrl(R.drawable.movie_4);

                category.getMovies().add(movie);
            }
        }

        MainAdapter mainAdapter = new MainAdapter(categories);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(mainAdapter);
    }

    private class MovieHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_cover);
        }
    }

    private class CategoryHolder extends RecyclerView.ViewHolder {
        final TextView textViewTitle;
        final RecyclerView recyclerViewMovie;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            recyclerViewMovie = itemView.findViewById(R.id.recycler_view_movie);
        }
    }

    private class MainAdapter extends RecyclerView.Adapter<CategoryHolder> {

        private final List<Category> categories;

        public MainAdapter(List<Category> categories) {
            this.categories = categories;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CategoryHolder(getLayoutInflater().inflate(R.layout.category_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MainActivity.CategoryHolder holder, int position) {
            Category category = categories.get(position);
            holder.textViewTitle.setText(category.getName());
            MovieAdapater movieAdapater = new MovieAdapater(category.getMovies());
            holder.recyclerViewMovie.setAdapter(new MovieAdapater(category.getMovies()));
            holder.recyclerViewMovie.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.HORIZONTAL, false));
        }

        @Override
        public int getItemCount() {
            return categories.size();
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
            return new MovieHolder(getLayoutInflater().inflate(R.layout.movie_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MainActivity.MovieHolder holder, int position) {
            Movie movie = movies.get(position);
//            holder.imageView.setImageResource(movie.getCoverUrl());
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }
    }

}