package montacer.elfazazi.ejerciciorepasopmdmdtema2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import montacer.elfazazi.ejerciciorepasopmdmdtema2.R;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.conexiones.ApiConexiones;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.conexiones.RetrofitObject;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.databinding.ActivityMainBinding;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.databinding.ActivityRecetaBinding;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.helpers.Constantes;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Categoria;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Comida;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Comidas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecetaActivity extends AppCompatActivity {
    ActivityRecetaBinding binding;
    private Comida comida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecetaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String id = getIntent().getExtras().getString(Constantes.IDCOMIDA);
        if (id != null){
            cargarReceta(id);
        }
    }

    private void cargarReceta(String id) {
        ApiConexiones api = RetrofitObject.getConexion().create(ApiConexiones.class);

        Call<Comidas> getReceta = api.getRecetas(id);
        getReceta.enqueue(new Callback<Comidas>() {
            @Override
            public void onResponse(Call<Comidas> call, Response<Comidas> response) {
                if (response.code() == HttpURLConnection.HTTP_OK){
                    comida = response.body().getMeals().get(0);
                    if (comida!= null){
                        rellenarVista();
                    }
                }
            }

            @Override
            public void onFailure(Call<Comidas> call, Throwable t) {
                Toast.makeText(RecetaActivity.this, "error"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rellenarVista() {
        binding.lbNombreReceta.setText(comida.getStrMeal());
        binding.lbCategoriaReceta.setText(comida.getStrCategory());
        binding.lbAreaReceta.setText(comida.getStrArea());
        binding.lbInstruccionesReceta.setText(comida.getStrInstructions());

        Picasso.get()
                .load(comida.getStrMealThumb())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.imFotoReceta);
    }
}