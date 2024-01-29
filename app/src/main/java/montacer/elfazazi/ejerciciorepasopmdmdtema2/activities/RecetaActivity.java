package montacer.elfazazi.ejerciciorepasopmdmdtema2.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecetaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String id = getIntent().getExtras().getString(Constantes.IDCOMIDA);
        if (id != null){
            cargarReceta(id);
        }

        binding.brnGuardarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {


                }else{
                    startActivity(new Intent(RecetaActivity.this, LoginActivity.class));
                }
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
         getMenuInflater().inflate(R.menu.menu_logout, menu);
         return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         super.onOptionsItemSelected(item);
         if (item.getItemId() == R.id.logoutMenu){
             FirebaseAuth.getInstance().signOut();
         }
         return true;
    }
}