package main.mp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private boolean paid = false;
    private boolean isJobDone = false;
    private Map<Client, List<Product>> facturator = new HashMap<>();

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

    synchronized public void facturer(Client client) {
        // enregistrer dans les facturation
        facturator.put(client, List.copyOf(scannedProducts));
        paid = true;
        notifyAll();// notifier le client qu'il peut sortir de la casise
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
        /**
         * on attend si il reste des client dans le magasin et
         * soit il n'y a pas de client à la caisse (mais il en reste dans les raysons
         * ou à la pile de chariots )
         * soit il y a un client mais il n'a pas encore fini de déposer ses articles
         */
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
        // si tous les client sont sorti --> rien à faire
        if (!isJobDone) {
            if (tapis[currentCaissierCase] != null) {
                System.out.println(
                        "Caissier--- " + caissier.getName() + " prend "
                                + tapis[currentCaissierCase].getName() + " du client " + currentClient.getName());
                scannedProducts.add(tapis[currentCaissierCase]);
                tapis[currentCaissierCase] = null;
                incCurrentCaissierCase();
                notifyAll();// notifie le client à la caisse qu'il peut déposer
            }
            if (!paid && clientDone && isEmpty()) {// payé si tous les produits ont été déposé et scanné
                facturer(currentClient);
            }
        }

    }

    @Override
    public String toString() {
        String s = "";

        if (currentClient != null) {
            s += "Client actuel: \n" + currentClient.getName();
            s += " qui ";
            if (clientDone) {
                if (paid) {
                    s += "a fini de déposé mais n'a pas encore payé\n";
                } else {
                    s += "a fini de déposé et a payé\n";

                }
            } else {
                s += "est toujours en train de déposer ses articles.\n";
            }
            s += "Articles déposés: \n";
            for (Product product : scannedProducts) {
                s += product.getName() + "\n ";
            }

        } else {
            s += "Client actuel: aucun\n";

        }
        s += "\n";

        s += "Facturation: \n";
        s += "\n";
        double chiffreDaffaire = 0;
        for (Client client : facturator.keySet()) {
            s += "Client: " + client.getName() + "\n";
            s += "Articles: \n";
            double sommeParClient = 0;
            if (facturator.get(client).size() == 0) {
                s += "Aucun article\n";
            }
            // int nbrItems=0;
            for (Product product : facturator.get(client)) {
                s += product.getName() + " " + product.getPrice() + "$\n";
                sommeParClient += product.getPrice();

            }

            BigDecimal truncatedDouble = new BigDecimal(sommeParClient);
            truncatedDouble = truncatedDouble.setScale(2, RoundingMode.HALF_DOWN);
            chiffreDaffaire += truncatedDouble.doubleValue();

            s += "Total facture: " + truncatedDouble + "$\n";
            s += "\n";

        }
        s += "\n";
        s += "\n";
        s += "Chiffre d'affaire: " + chiffreDaffaire + "$\n";

        return s;
    }

}
