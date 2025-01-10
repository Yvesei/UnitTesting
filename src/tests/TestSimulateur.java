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
	public void verifierMouvementAscenseur() {
		if (Constantes.DEBUG)
			System.out.println("Ascenseur démarre au 3e étage et se dirige vers le 1er étage");

		ascenseur = new Ascenseur(3, this);
		this.appels[0] = Direction.DOWN;

		ascenseur.start();

		// Pause pour permettre le déplacement de l'ascenseur
		try { Thread.sleep(500); }
		catch (InterruptedException e) { System.out.print("Erreur pendant Thread.sleep\n"); }

		// Validation de la position de l'ascenseur
		assertEquals(1, ascenseur.etage);

		if (Constantes.DEBUG)
			System.out.println("Ascenseur est arrivé au 1er étage.");

		// Prochaine destination : 2e étage
		this.appels[1] = Direction.UP;

		try { Thread.sleep(500); }
		catch (InterruptedException e) { System.out.print("Erreur pendant Thread.sleep\n"); }

		assertEquals(2, ascenseur.etage);

		if (Constantes.DEBUG)
			System.out.println("Ascenseur est arrivé au 2e étage.");
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 3})
	public void verifierLimitesAscenseur(int etageInitial) {
		ascenseur = new Ascenseur(etageInitial, this);
		ascenseur.start();

		try { Thread.sleep(200); }
		catch (InterruptedException e) { System.out.print("Erreur pendant Thread.sleep\n"); }

		if (ascenseur.etage == 1) {
			assertEquals(Direction.UP, ascenseur.dir);
			if (Constantes.DEBUG)
				System.out.println("Ascenseur au 1er étage : direction UP.");
		} else if (ascenseur.etage == Constantes.ETAGES) {
			assertEquals(Direction.DOWN, ascenseur.dir);
			if (Constantes.DEBUG)
				System.out.println("Ascenseur au dernier étage : direction DOWN.");
		}
	}

	@Test
	public void verifierUsager() {
		int etageAppel = 1;
		int destination = 3;

		Usager utilisateur = new Usager("Jean", etageAppel, destination, this);
		utilisateur.start();

		try { Thread.sleep(500); }
		catch (InterruptedException e) { System.out.print("Erreur pendant Thread.sleep\n"); }

		assertNotEquals(Direction.NONE, this.appels[etageAppel - 1]);

		if (Constantes.DEBUG)
			System.out.println("L'usager a utilisé l'ascenseur avec succès.");
	}
}