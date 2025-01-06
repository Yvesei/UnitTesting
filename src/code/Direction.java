package code;

/**
 * Classe permettant de decrire une direction de mouvement vertical.
 */
public class Direction 
{
    /** Attribut textuel decrivant la direction. */
	private final String name;

    /**
     * Constructeur prive
     * @param name Description textuelle de la direction verticale.
     */
    private Direction(String name) 
    {
    	this.name = name;
    }

    @Override
    public String toString() 
    {
    	return name;
    }

    /** Decrit une direction verticale vers le haut. */
    public static final Direction UP = new Direction("UP");
    /** Decrit une direction verticale vers le bas. */
    public static final Direction DOWN = new Direction("DOWN");
    /** Decrit aucune direction verticale. */
    public static final Direction NONE = new Direction("NONE");
}
