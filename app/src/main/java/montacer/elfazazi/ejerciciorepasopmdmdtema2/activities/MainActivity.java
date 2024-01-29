package montacer.elfazazi.ejerciciorepasopmdmdtema2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import montacer.elfazazi.ejerciciorepasopmdmdtema2.R;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.adapters.CategoriasAdapter;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.conexiones.ApiConexiones;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.conexiones.RetrofitObject;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.databinding.ActivityMainBinding;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Categoria;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Categorias;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private ArrayList<Categoria> listaCategorias;
    private CategoriasAdapter adapter;
    private RecyclerView.LayoutManager lm;

    private Retrofit retrofit;
    private ApiConexiones api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listaCategorias = new ArrayList<>();
        adapter = new CategoriasAdapter(R.layout.row_view_holder, listaCategorias, this);
        lm = new LinearLayoutManager(this);

        binding.contentCategorias.setAdapter(adapter);
        binding.contentCategorias.setLayoutManager(lm);

        retrofit = RetrofitObject.getConexion();
        api = retrofit.create(ApiConexiones.class);

        cargarCategorias();
    }

    private void cargarCategorias() {
        Call<Categorias> getCategorias = api.getCategorias();

        getCategorias.enqueue(new Callback<Categorias>() {
            @Override
            public void onResponse(Call<Categorias> call, Response<Categorias> response) {
                if (response.code() == HttpURLConnection.HTTP_OK){
                    ArrayList<Categoria> temp = (ArrayList<Categoria>) response.body().getCategories();
                    listaCategorias.addAll(temp);
                    adapter.notifyItemRangeInserted(0, temp.size());
                }
            }

            @Override
            public void onFailure(Call<Categorias> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}