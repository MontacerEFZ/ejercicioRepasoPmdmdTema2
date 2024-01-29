package montacer.elfazazi.ejerciciorepasopmdmdtema2.modelos;

import java.util.List;

public class Comidas{
	private List<Comida> meals;

	public void setMeals(List<Comida> meals){
		this.meals = meals;
	}

	public List<Comida> getMeals(){
		return meals;
	}

	@Override
 	public String toString(){
		return 
			"Comidas{" + 
			"meals = '" + meals + '\'' + 
			"}";
		}
}