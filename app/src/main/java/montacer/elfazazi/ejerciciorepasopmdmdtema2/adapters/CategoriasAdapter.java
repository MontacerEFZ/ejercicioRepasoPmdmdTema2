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
import montacer.elfazazi.ejerciciorepasopmdmdtema2.activities.ComidasActivity;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.helpers.Constantes;
import montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos.Categoria;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.CategoriaVH> {
    private int resources;
    private List<Categoria> objects;
    private Context context;

    public CategoriasAdapter(int resources, List<Categoria> objects, Context context) {
        this.resources = resources;
        this.objects = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoriaVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(resources, null);

        itemView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        return new CategoriaVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaVH holder, int position) {
        Categoria categoria = objects.get(position);

        holder.nombre.setText(categoria.getStrCategory());

        Picasso.get()
                .load(categoria.getStrCategoryThumb())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.foto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ComidasActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constantes.CATEGORIA, categoria.getStrCategory());
                intent.putExtras(bundle);

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class CategoriaVH extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView nombre;
        public CategoriaVH(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imFotoRow);
            nombre = itemView.findViewById(R.id.lbNombreRow);
        }
    }
}
