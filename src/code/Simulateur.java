package code;

import java.util.Vector;
import java.util.Random;


/**
 * Classe qui execute le comportement d'un ascenseur virtuel 
 * ainsi que des usagers qui l'utilisent.
 */
public class Simulateur
{
    /** L'ascenseur */
    protected Ascenseur ascenseur;
    /** Une porte pour chaque etage du simulateur*/
    protected Porte portes[];

    /** Appel signales pour chaque etage */
    public Direction appels[];	// NONE : aucun appel.  UP/DOWN : appel a l'etage [i] dans direction indiqu�e
    /** Destinations signales pour chaque etage */
    public boolean destinations[]; // true : destination voulue a l'etage [i]

    /** Le nombre d'usager qui ont termines */
    protected int usager_term = 0;

    /** Canal de communication: arret/demarrage ascenseur */
    protected int etageArret; // Correspond a l'eage de l'arret. '-1' est le signal de demarrage

    /** File d'evenements : la descriptions des evenements qui ont eu lieu */
    protected Vector<String> evenements;

    /** Nombre aleatoire */
    protected Random rand;



    /** Main : methode appellee lors de l'execution. */
    public static void main (String[] args)
    {
        // Initialiser et demarrer le systeme
        (new Simulateur()).runSimulateur();
    }


    /** Constructeur qui initialise la simulation. */
    public Simulateur()
    {
        // CREER LES ATTRIBUTS
        destinations = new boolean[Constantes.ETAGES];
        appels = new Direction[Constantes.ETAGES];
        portes = new Porte[Constantes.ETAGES];
        evenements = new Vector<String>();
        rand = new Random();

        // INITIALISATIONS
        // Initialiser les attributs
        ascenseur =null; // aucun ascenceur encore...

        for(int i=0; i<Constantes.ETAGES; i++)
        {
            appels[i] =Direction.NONE; 	// aucun appels
            destinations[i] =false;		// aucune destination
            portes[i] =null;			// aucune porte active
        }
        // Aucun arret signale au debut de la simulation
        etageArret =-1;
    }


    /** Lancement de la simulation. */
    public void runSimulateur()
    {
        // CREATIONS OBJETS
        if(Constantes.DEBUG) {
            System.out.print("\nDebut de la simulation\n");
        }

        // Creer les portes (de 0 a 2 pour les etages 1 a 3)
        for (int i=0;i<Constantes.ETAGES;i++)
        {
            portes[i] =new Porte(i+1,this); // (etage, simulateur)
            portes[i].start();
        }

        // Creer les usagers (partie du preambule)
        (new Usager("0",2,3,this)).start(); // (nom, etage, dest, simulateur)
        (new Usager("1",3,1,this)).start();

        // Creer l'ascenseur  (partie du preambule)
        ascenseur = new Ascenseur(1, this); // (etage, simulateur)
        ascenseur.start();

    }



    // LES SIGNAUX : 'sig_' pour signal sortant, 'check_' pour attente d'un signal
    public void sig_arretAscenseur(int etage)
    {
        this.etageArret =etage;
    }
    public boolean check_arretCetEtage(int etage)
    {
        return etageArret==etage ? true : false;
    }


    public void sig_redemarreAscenceur()
    {
        this.etageArret =-1;
    }
    public boolean check_peutRedemarrer()
    {
        return etageArret==-1 ? true : false;
    }



    public boolean check_porteOuverteCetEtage(int etage)
    {
        return portes[etage-1]==null ?
                false : portes[etage-1].check_porteOuverte();
    }
    public boolean check_directionAscenseur(Direction dir)
    {
        return ascenseur==null ?
                false : ascenseur.dir==dir ?
                true : false;
    }

    /** Signal d'arrive d'un usager, et fin de l'application... */
    public void sig_usagerTermine()
    {
        usager_term ++;

        if (usager_term == Constantes.USAGERS)
        {
            System.out.print("Fin de la simulation\n\n");
            System.exit(0);
        }
    }



    /** Methode qui genere un bool aleatoire (pour distraction usager) */
    public boolean non_determinisme()
    {
        return rand.nextBoolean();
    }





    /** Traitement des evenements */
    private void afficher_evenements()
    {
        System.out.print("S�quence d'�v�nements:\n\n");
        for(int i=0;i<evenements.size();i++)
            System.out.print((i+1) +".\t"+ evenements.get(i));
    }

    /** Traitement des evenements */
    public void ajouter_evenement(String evt)
    {
        // Ajouter l'evenement
        evenements.add(evt);

        // Imprimer l'evenement
        if (Constantes.DEBUG) {
            System.out.print(evenements.size() +".\t"+ evt);
            System.out.flush();
        }
    }


}
