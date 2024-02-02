package montacer.elfazazi.ejerciciorepasopmdmdtema2.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
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
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ArrayList<Comida> listaComidas;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecetaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listaComidas = new ArrayList<>();
        database = FirebaseDatabase.getInstance("https://ejemplofirebasebpmdmtema2-default-rtdb.europe-west1.firebasedatabase.app/");

        sp = getSharedPreferences(Constantes.ULTIMA_RECETA, MODE_PRIVATE);

        String id = getIntent().getExtras().getString(Constantes.IDCOMIDA);
        if (id != null){
            cargarReceta(id);
        }

        binding.brnGuardarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    //esta logeado:
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Constantes.EMAIL, user.getEmail()); //clave, valor
                    editor.putString(Constantes.RECETA, comida.getStrMeal());
                    editor.apply();

                        reference = database.getReference(user.getUid()).child("recetas");
                            //estamos cogiendo la referencia del nodo del usuario con el .getUid y añadiendole con el .child una lista llamada "recetas"
                                //entonces la referencia acaba siendo esa lista "recetas"

                    reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        //con el .get() "escucha" solo 1 vez, para escuchar constantemente seria .addValueEventListener
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()){
                                //si fuesemos a recoger solo un String o int (datos primitivos) pondriamos String.class y lo recogeriamos
                                //pero como es una lista, hay q usar un GenericTypeIndicator
                                GenericTypeIndicator<ArrayList<Comida>> gti = new GenericTypeIndicator<ArrayList<Comida>>() {};
                                ArrayList<Comida> temp = task.getResult().getValue(gti);
                                if (temp != null){
                                    listaComidas.addAll(temp);
                                }

                                //escribir en la bd:
                                listaComidas.add(comida);
                                reference.setValue(listaComidas);
                                Toast.makeText(RecetaActivity.this, "guardado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else{ //no esta logeado
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