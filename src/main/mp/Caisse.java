package main.mp;

import java.util.ArrayList;
import java.util.List;

import main.products.Product;
import main.threads.Caissier;
import main.threads.Client;

public class Caisse {
    private Product[] tapis = new Product[Superette.SIZE_CAISSE_TAPIS];
    private int currentClientCase = 0;
    private int currentCaissierCase = 0;
    private List<Product> scannedProducts = new ArrayList<Product>();
    private Client currentClient = null;
    private boolean clientDone = true;
    private double chiffreDaffaire = 0;
    private boolean paid = false;
    private boolean isJobDone = false;

    private void incCurrentClientCase() {
        if (currentClientCase == Superette.SIZE_CAISSE_TAPIS - 1) {
            currentClientCase = 0;
        } else {
            currentClientCase++;
        }
    }

    private void incCurrentCaissierCase() {
        if (currentCaissierCase == Superette.SIZE_CAISSE_TAPIS - 1) {
            currentCaissierCase = 0;
        } else {
            currentCaissierCase++;
        }
    }

    synchronized public void payer(Client client) {
        while (!isEmpty()) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        double somme = 0;
        for (Product product : scannedProducts) {
            System.out.println("pricing: " + product.getPrice());
            somme += product.getPrice();
        }
        chiffreDaffaire += somme;
        System.out.println(somme + " payé par :" + client.getName());
        paid = true;
        notify();
    }

    private boolean isEmpty() {
        boolean b = false;
        for (int i = 0; i < tapis.length; i++) {
            if (tapis[i] == null) {
                b = true;
                break;
            }
        }
        // System.out.println("tapis vide? :" + b);
        return b;
    }

    private boolean isFull() {
        boolean b = true;
        for (int i = 0; i < tapis.length; i++) {
            if (tapis[i] == null) {
                b = false;
                break;
            }
        }
        System.out.println("tapis full? :" + b);
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
        System.out.println(client.getName() + " entre en caisse");
        currentClient = client;
        clientDone = false;
        notifyAll();// reveiller le caissier car attend le client d'entrer
    }

    synchronized public void reveillerCaissierPourRentrer() {
        isJobDone = true;
        notifyAll();
    }

    synchronized public void sortirCaisse(Client client) {
        while (!paid) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        paid = false;
        currentClient = null;
        scannedProducts.clear();
        System.out.println(client.getName() + " sort de caisse");
        notifyAll();
    }

    synchronized public void deposer(Client client, Product product, boolean lastProduct) {
        while (isFull()) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        if (lastProduct) {
            System.out.println("Client---" + client.getName() + " deposer le dernier article " + product.getName());

        } else {
            System.out.println("Client---" + client.getName() + " deposer " + product.getName());

        }
        tapis[currentClientCase] = product;
        incCurrentClientCase();
        if (lastProduct) {
            this.clientDone = true;

        }
        notifyAll();
    }

    synchronized public void prendre(Caissier caissier) {
        while (((isEmpty() && !clientDone) || currentClient == null) && !isJobDone) {
            try {
                if (isEmpty() && !clientDone) {
                    System.out.println("Caissier---" + caissier.getName()
                            + " attend car tapis vide mais client n' pas encore fini");

                } else if (currentClient == null) {
                    System.out.println("Caissier---" + caissier.getName() + " attend car pas de client");
                }
                wait();
            } catch (Exception e) {
            }
        }
        if (!isJobDone) {
            if (tapis[currentCaissierCase] != null) {
                System.out.println(
                        "Caissier--- " + caissier.getName() + " prend "
                                + tapis[currentCaissierCase].getName() + " du client " + currentClient.getName());
                scannedProducts.add(tapis[currentCaissierCase]);
                tapis[currentCaissierCase] = null;
                incCurrentCaissierCase();
                notifyAll();// tapis avec case libre
            }
            if (!paid) {
                payer(currentClient);
            }
        }

    }

    @Override
    public String toString() {
        String s = "Scanned products:\n";
        for (Product product : scannedProducts) {
            s += product.getName() + "\n ";
        }
        if (currentClient != null) {
            s += "Client actuel: \n" + currentClient.getName();
            if (clientDone) {
                s += " (done)\n";
                if (!paid) {
                    s += "client attend pour paiment";
                }
            } else {
                s += " (not done)\n";
            }

        } else {
            s += "Client actuel: abscent\n";

        }
        s += "Chiffre d'affaire : " + chiffreDaffaire + "\n";
        return s;
    }

}
