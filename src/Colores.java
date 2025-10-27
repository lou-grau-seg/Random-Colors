import java.util.*; //ArraysList - List

public class Colores {
    private List<String> nombres = new ArrayList(); //lista con los nombres de los colores
    private List<String> codigos = new ArrayList(); /*lista con los codigos en hexadecimal
    pertenecientes a cada color*/

    /*Los nombres de los colores y sus respectivos codigos se encuentran en
    las mismas posiciones en diferentes listas */


    public Colores() {
        this.nombres.add("ROJO");
        this.nombres.add("VERDE");
        this.nombres.add("AZUL");
        this.nombres.add("AMARILLO");
        this.nombres.add("NARANJA");
        this.nombres.add("VIOLETA");
        this.nombres.add("GRIS");
        this.nombres.add("CELESTE");
        this.nombres.add("ROSA");
        this.nombres.add("NEGRO");

        this.codigos.add("#FF0000");
        this.codigos.add("#00FF00");
        this.codigos.add("#0000FF");
        this.codigos.add("#FFFF00");
        this.codigos.add("#FFA500");
        this.codigos.add("#8A2BE2");
        this.codigos.add("#808080");
        this.codigos.add("#00FFFF");
        this.codigos.add("#FFC0CB");
        this.codigos.add("#000000");
    }

    public List<String> getNombres() {
        return this.nombres;
    }

    public List<String> getCodigos() {
        return this.codigos;
    }
}