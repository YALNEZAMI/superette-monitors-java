package main;

import java.util.ArrayList;
import java.util.List;

import main.mp.Superette;
import main.threads.Caissier;
import main.threads.ChefRayon;
import main.threads.Client;

public class App {
    public static void main(String[] args) throws Exception {
        // init superette
        Superette superette = new Superette();
        // init clients
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < Superette.NBR_INIT_CLIENTS; i++) {
            clients.add(new Client(superette));
        }
        // init caissier
        Caissier caissier = new Caissier(superette);
        // init chefRayon
        ChefRayon chefRayon = new ChefRayon(superette);
        // lancer les threads des clients
        for (Client client : clients) {
            client.start();
        }
        // lancer le thread du chef de rayon
        chefRayon.start();
        // lancer le thread du caissier
        caissier.start();
        // joindre les threads
        for (Client client : clients) {
            client.join();
        }
        chefRayon.join();
        caissier.join();
        // affichage de l'état final
        System.out.println("\n\n\n");
        System.out.println("____Etat final de la supertte____");
        System.out.println("_________________________________________");
        System.out.println(superette.toString());

    }
}
