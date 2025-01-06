package code;

/**
 * Classe qui decrit l'etat et le comportement simule d'une porte 
 * d'ascenseur virtuelle.
 */
public class Porte extends Thread 
{
    /** Etage a laquelle se situe la porte. */
	private int etagePorte;
    /** Temps en millisecondes durant lequel la porte reste ouverte. */
    private long delai;
    /** Indicateur decrivant si la porte est ouverte ou non. */
    public boolean porteOuverte;
    /** L'instance du simulateur qui gere le syteme. */	
    private Simulateur sim;
    
    static private boolean DEBUG =Constantes.DEBUG;
    
    /** Constructeur */
    public Porte(int etage, Simulateur sim)
    {
		this.etagePorte =etage;
		this.delai =Constantes.DELAIPORTE;
		this.porteOuverte =false;
		this.sim = sim;
    }
    /** Constructeur supplementaire qui permet de specifier un temps 
     * d'attente different pour l'ouverture des portes. */
    public Porte(int etage, long delai, Simulateur sim)
    {
		this.etagePorte =etage;
		this.delai =delai;
		this.porteOuverte =false;
		this.sim = sim;
    }

    @Override
    public String toString()
    {
    	return "Porte[" +
    			"etage=" + etagePorte +
    			", ouverture=" + porteOuverte + 
    			"]";
    }

    
    public void run()
    {
		while(true)
		{
		    // [1] Attendre que l'ascenseur soit a l'arret a l'etage 
			while (!sim.check_arretCetEtage(etagePorte))
			    try { Thread.sleep(100); } // Courte pause pour allouer le controle aux autres thread...
		    	catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }					

		    // [2] Ouvrir la porte 
	    	sim.ajouter_evenement("* Porte etage "+etagePorte+":\t* ouverture\n");
			porteOuverte=true;
	
		    // [3] Attendre avant de refermer 
		    try 
		    {
		    	Thread.sleep(delai);
		    }
		    catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }
	
		    // [4] Fermer la porte 
		    sim.ajouter_evenement("* Porte etage "+etagePorte+":\t* fermeture\n");
		    porteOuverte=false;
	
		    // [5] Signaler a l'ascenseur de redemarrer 
		    sim.sig_redemarreAscenceur();
		}
    }
    
    public boolean check_porteOuverte()
    {
    	return porteOuverte;
    }
    
}
