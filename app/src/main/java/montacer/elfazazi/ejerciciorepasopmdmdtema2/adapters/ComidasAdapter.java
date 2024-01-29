package montacer.elfazazi.ejerciciorepasopmdmdtema2.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import montacer.elfazazi.ejerciciorepasopmdmdtema2.R;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.activities.RecetaActivity;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.helpers.Constantes;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Categoria;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Comida;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Comidas;

public class ComidasAdapter extends RecyclerView.Adapter<ComidasAdapter.ComidasVH> {

    private int resources;
    private List<Comida> objects;
    private Context context;

    public ComidasAdapter(int resources, List<Comida> objects, Context context) {
        this.resources = resources;
        this.objects = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public ComidasVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(resources, null);

        itemView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        return new ComidasVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ComidasVH holder, int position) {
    Comida comida = objects.get(position);

    holder.nombre.setText(comida.getStrMeal());

        Picasso.get()
                .load(comida.getStrMealThumb())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.foto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecetaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constantes.IDCOMIDA, comida.getIdMeal());
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ComidasVH extends RecyclerView.ViewHolder{

        ImageView foto;
        TextView nombre;
        public ComidasVH(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imFotoRow);
            nombre = itemView.findViewById(R.id.lbNombreRow);
        }
    }
}
