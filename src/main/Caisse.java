package main;

import java.util.ArrayList;
import java.util.List;

public class Caisse {
    private static int SIZE = 10;
    private Product[] tapis = new Product[SIZE];
    private int currentClientCase = 0;
    private int currentCaissierCase = 0;
    private List<Product> scannedProducts = new ArrayList<Product>();
    private Client currentClient = null;
    private boolean clientDone = true;
    private double chiffreDaffaire = 0;

    private void incCurrentClientCase() {
        if (currentClientCase == SIZE - 1) {
            currentClientCase = 0;
        } else {
            currentClientCase++;
        }
    }

    private void incCurrentCaissierCase() {
        if (currentCaissierCase == SIZE - 1) {
            currentCaissierCase = 0;
        } else {
            currentCaissierCase++;
        }
    }

    private void payer(Client client) {
        int somme = 0;
        for (Product product : scannedProducts) {
            somme += product.getPrice();
        }
        chiffreDaffaire += somme;
        System.out.println(somme + "payé par :" + client.getName());
        currentClient = null;
        scannedProducts.clear();
    }

    private boolean isEmpty() {
        boolean b = false;
        for (int i = 0; i < tapis.length; i++) {
            if (tapis[i] == null) {
                b = true;
                break;
            }
        }
        System.out.println("tapis vide? :" + b);
        return b;
    }

    public double getChiffreDaffaire() {
        return chiffreDaffaire;
    }

    synchronized public void entrerEnCaisse(Client client) {
        while (currentClient != null) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        currentClient = client;
    }

    synchronized public void deposer(Client client, Product product, boolean lastProduct) {
        while (!client.equals(currentClient) || tapis[currentClientCase] != null) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        tapis[currentClientCase] = product;
        incCurrentClientCase();
        if (lastProduct) {
            this.clientDone = true;
        }
    }

    synchronized public void prendre() {
        while ((tapis[currentCaissierCase] == null && !clientDone) || currentClient == null) {
            try {
                System.out.println("caissier attend car tapis vide mais client n' pas encore fini");
                wait();
            } catch (Exception e) {
            }
        }
        System.out.println(
                "caissier prend " + tapis[currentCaissierCase].getName() + " du client " + currentClient.getName());
        scannedProducts.add(tapis[currentCaissierCase]);
        tapis[currentCaissierCase] = null;
        incCurrentCaissierCase();
        if (clientDone && isEmpty()) {
            payer(currentClient);
        }
    }

    @Override
    public String toString() {
        String s = "Products:\n";
        for (Product product : scannedProducts) {
            s += product.getName() + "\n ";
        }
        if (currentClient != null) {
            s += "Client actuel: \n" + currentClient.getName();

        } else {
            s += "Client actuel: abscent\n";

        }
        s += "Client done: " + clientDone + "\n";
        s += "Chiffre d'affaire" + chiffreDaffaire + "\n";
        return s;
    }

}
