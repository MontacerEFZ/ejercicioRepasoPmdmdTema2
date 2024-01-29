package montacer.elfazazi.ejerciciorepasopmdmdtema2.conexiones;


import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Categorias;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Comidas;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiConexiones {
    @GET("/api/json/v1/1/categories.php")
    Call<Categorias> getCategorias();

    @GET("/api/json/v1/1/filter.php")
    Call<Comidas> getComidas(@Query("c") String categoria);
}
