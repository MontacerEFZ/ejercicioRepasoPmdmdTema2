package montacer.elfazazi.ejerciciorepasopmdmdtema2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import montacer.elfazazi.ejerciciorepasopmdmdtema2.R;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.adapters.ComidasAdapter;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.conexiones.ApiConexiones;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.conexiones.RetrofitObject;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.databinding.ActivityComidasBinding;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.databinding.ActivityMainBinding;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.helpers.Constantes;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Categoria;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Categorias;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Comida;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Comidas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ComidasActivity extends AppCompatActivity {
    ActivityComidasBinding binding;

    private ArrayList<Comida> lisaComidas;
    private ComidasAdapter adapter;
    private RecyclerView.LayoutManager lm;

    private Retrofit retrofit;
    private ApiConexiones api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComidasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lisaComidas = new ArrayList<>();
        adapter = new ComidasAdapter(R.layout.row_view_holder, lisaComidas, this);
        lm = new LinearLayoutManager(this);

        binding.contentComidas.setAdapter(adapter);
        binding.contentComidas.setLayoutManager(lm);

        retrofit = RetrofitObject.getConexion();
        api = retrofit.create(ApiConexiones.class);

        String categoria = getIntent().getExtras().getString(Constantes.CATEGORIA);
        if (categoria != null) {
            cargarComidas(categoria);
        }
    }

    private void cargarComidas(String categoria) {
        Call<Comidas> getComidas = api.getComidas(categoria);

        getComidas.enqueue(new Callback<Comidas>() {
            @Override
            public void onResponse(Call<Comidas> call, Response<Comidas> response) {
                if (response.code() == HttpURLConnection.HTTP_OK){
                    lisaComidas.addAll(response.body().getMeals());
                    adapter.notifyItemRangeInserted(0, lisaComidas.size());
                }
            }

            @Override
            public void onFailure(Call<Comidas> call, Throwable t) {
                Toast.makeText(ComidasActivity.this, "error"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}