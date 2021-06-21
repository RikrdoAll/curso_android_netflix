package br.com.naotemigual.netflixremake.util;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import br.com.naotemigual.netflixremake.MainActivity;
import br.com.naotemigual.netflixremake.model.Category;

/***
 * 20/06/2021 
 *
 * @author Ricardo Pires
 ***/
public class JsonDownloadFutureTask extends FutureTask<List<Category>> {

    public JsonDownloadFutureTask(Callable<List<Category>> callable) {
        super(callable);
    }

    public JsonDownloadFutureTask(Runnable runnable, List<Category> result) {
        super(runnable, result);
    }
}
