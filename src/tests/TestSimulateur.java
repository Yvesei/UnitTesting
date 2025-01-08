package tests;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import code.Ascenseur;
import code.Constantes;
import code.Direction;
import code.Simulateur;
import code.Usager;
import code.Porte;

/**
 * Driver qui permet d'executer les tests unitaires sur le simulateur d'ascenseur.
 */
public class TestSimulateur extends Simulateur
{
	/** Test unitaire sur la porte. */
	@Test
	public void testPorte1()
	{
		System.out.println("\n-> Test 1: ouverture/fermeture d'une porte au 1er l'etage\n");

		portes[0] = new Porte(1,Constantes.DELAIPORTE,this);
		portes[0].start();

		// Valider le comportement : la porte est bien fermee?
		assertFalse(portes[0].check_porteOuverte());
		assertEquals(etageArret,-1,0);
		System.out.println("(a) La "+portes[0]+" est bien fermee, et aucun signal d'arret.");


		// Simuler l'arrêt de l'ascenseur au 1er etage
		etageArret=1;

		// Attendre ouverture de la porte (1/2 delai)...
		try { Thread.sleep(Constantes.DELAIPORTE/2); }
		catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }

		// Valider le comportement : la porte est ouverte?
		assertTrue(portes[0].check_porteOuverte());
		System.out.println("(b) La "+portes[0]+" est bien ouverte.");

		// Attendre fermeture de la porte (delai complet)...
		try { Thread.sleep(Constantes.DELAIPORTE); }
		catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }

		// Valider le comportement : la porte s'est refermee?
		assertFalse(portes[0].check_porteOuverte());
		System.out.println("(c) La "+portes[0]+" s'est bien refermee.");
	}


	@Test
	@DisplayName("==> Test Ascenseur 1 : Fonctionnement")
	public void testAscenseurComportement() {
		if(Constantes.DEBUG)
			System.out.println("ascenseur est � l'�tage 3 et il va � l'�tage 1");

		ascenseur = new Ascenseur(3, this); /*1, 2, 3*/
		int dist = 0; /*0, 1, 2*/
		this.appels[dist] = Direction.DOWN;

		ascenseur.start();

		// Attendre le mouvement de l'ascenseur
		try { Thread.sleep(500); }
		catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }

		/* Valider le comportement de l'ascenseur */
		assertTrue(ascenseur.etage == dist+1);

		if(Constantes.DEBUG) System.out.println("ascenseur est arriv� � l'�tage 1");
		if(Constantes.DEBUG) System.out.println("prochaine destination est : �tage 2");

		dist = 1;
		this.appels[dist] = Direction.UP;

		// Attendre le mouvement de l'ascenseur
		try { Thread.sleep(500); }
		catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }

		/* Valider le comportement de l'ascenseur */
		assertTrue(ascenseur.etage == dist+1);

		if(Constantes.DEBUG) System.out.println("ascenseur est arriv� � l'�tage 2");
	}

	@ParameterizedTest
	@ValueSource (ints = {1, 3})
	@DisplayName("==> Test Ascenseur 2 : Les Limites")
	public void testAscenseurLimite(int etage) {
		ascenseur = new Ascenseur(etage,this);
		ascenseur.start();

		// Attendre le mouvement de l'ascenseur
		try { Thread.sleep(200); }
		catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }

		if(ascenseur.etage == 1) {
			assertTrue(ascenseur.dir.equals(Direction.UP));
			if(Constantes.DEBUG) System.out.println("Ascenseur est � l'�tage 1 => dir == UP");
		}
		else if(ascenseur.etage == Constantes.ETAGES) {
			assertTrue(ascenseur.dir.equals(Direction.DOWN));
			if(Constantes.DEBUG) System.out.println("Ascenseur est � l'�tage 3 => dir == DOWN");
		}
	}

	@Test
	@DisplayName("==> Test Usager 1 : Appel de l'ascenseur")
	public void testUsager() {

		int etageAppel = 1;
		int dist = 3;

		Usager user = new Usager("toto", etageAppel, dist, this);

		user.start();

		// Attendre le mouvement de l'ascenseur
		try { Thread.sleep(500); }
		catch(InterruptedException e) { System.out.print("Erreur dans Thread.sleep\n"); }

		// validation du comportement d'usager
		assertFalse(this.appels[etageAppel-1].equals(Direction.NONE));

		if (Constantes.DEBUG)
			System.out.println("Usager utilise l'ascenseur au moins une fois");

	}

}
