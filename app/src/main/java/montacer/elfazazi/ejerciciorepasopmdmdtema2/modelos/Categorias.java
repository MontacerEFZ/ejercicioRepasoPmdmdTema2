package montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos;

import java.util.List;

public class Categorias{
	private List<Categoria> categories;

	public void setCategories(List<Categoria> categories){
		this.categories = categories;
	}

	public List<Categoria> getCategories(){
		return categories;
	}

	@Override
 	public String toString(){
		return 
			"Categorias{" + 
			"categories = '" + categories + '\'' + 
			"}";
		}
}