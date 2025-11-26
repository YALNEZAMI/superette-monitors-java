package main;

import java.util.ArrayList;
import java.util.List;

import main.mp.Superette;
import main.threads.Caissier;
import main.threads.ChefRayon;
import main.threads.Client;

public class App {
    public static void main(String[] args) throws Exception {
        List<Client> clients = new ArrayList<>();
        Superette superette = new Superette();
        for (int i = 0; i < Superette.NBR_INIT_CLIENTS; i++) {
            clients.add(new Client("client" + i, superette));
        }

        Caissier caissier = new Caissier(superette);
        ChefRayon chefRayon = new ChefRayon(superette);
        for (Client client : clients) {
            client.start();
        }
        chefRayon.start();
        caissier.start();

        for (Client client : clients) {
            client.join();
        }
        chefRayon.join();
        caissier.join();
        System.out.println("____Etat de la supertte____");
        System.out.println(superette.toString());

    }
}
