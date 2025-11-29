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
    // le tapis de la caisse
    private Product[] tapis = new Product[Superette.SIZE_CAISSE_TAPIS];
    // position d'avancement du client
    private int currentClientCase = 0;
    // position d'avancement du caissier
    private int currentCaissierCase = 0;
    // les produits scanné
    private List<Product> scannedProducts = new ArrayList<Product>();
    // le client à la caissie(unique)
    private Client currentClient = null;
    // si le client a finit de déposer ses produits sur le tapis
    private boolean clientDone = true;
    // si le client a été facturé
    private boolean paid = false;
    // si tous les client ont quité le supermarché
    private boolean isJobDone = false;
    // un historique des vente
    private Map<Client, List<Product>> facturator = new HashMap<>();

    /**
     * @ensure incremente la position d'avancement du client de maniere circulaire
     */
    private void incCurrentClientCase() {
        if (currentClientCase == Superette.SIZE_CAISSE_TAPIS - 1) {
            currentClientCase = 0;
        } else {
            currentClientCase++;
        }
    }

    /**
     * @ensure incremente la position d'avancement du caissier de maniere circulaire
     */
    private void incCurrentCaissierCase() {
        if (currentCaissierCase == Superette.SIZE_CAISSE_TAPIS - 1) {
            currentCaissierCase = 0;
        } else {
            currentCaissierCase++;
        }
    }

    /**
     * @ensure ajouter le client actuel et ses produits à l'historique de la vente
     * @ensure le marquer comme payé,
     * @ensure le notifier du paiment pour qu'il puisse sortire de la caisse
     */
    synchronized public void facturer() {
        Client copyClient = currentClient;
        facturator.put(copyClient, List.copyOf(scannedProducts));
        paid = true;
        notifyAll();
    }

    /**
     * 
     * @param client le client qui veut entrer en caisse
     * @ensure que le client attende s'il y a déjà qqun à la caisse
     * @ensure considèrer qu'il n'a pas encore déposé tous ses produits
     * @ensure notifier le caissier qui attend qu'un client entre en caisse
     */
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
        notifyAll();
    }

    synchronized public void reveillerCaissierPourRentrer() {
        isJobDone = true;
        notifyAll();
    }

    /**
     * 
     * @param client qui veut sortir de la caisse
     * @ensure que client attend s'il n'a pas encore payé(car caissier est en
     *         train de scanner les produit)
     * @ensure le client suivant est marqué comme pas payé
     * @ensure les produits scanné sont remis à 0 produits
     * 
     * 
     */
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

    /**
     * 
     * @param client      le client qui dépose
     * @param product     le produit déposé
     * @param lastProduct is c'est le dernier produit à être déposé
     * @ensure notifier le caissier qui attend qu'un client dépose un produit
     * @ensure marquer que le client a fini de déposer si dernier porduit
     * @ensure product est déposé à la case du client
     * 
     */
    synchronized public void deposer(Client client, Product product, boolean lastProduct) {
        while (tapis[currentClientCase] != null) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        if (lastProduct) {
            System.out.println("Client---" + client.getName() + " dépose le dernier article " + product.getName());

        } else {
            System.out.println("Client---" + client.getName() + " dépose " + product.getName());

        }
        tapis[currentClientCase] = product;
        incCurrentClientCase();
        if (lastProduct) {
            this.clientDone = true;

        }
        notifyAll();
    }

    /**
     * 
     * @param caissier le caissier qui prend le produit
     * @ensure la case du caissier est remis à null
     * @ensure le produit pris devient dans scannedProducts
     * @ensure la case du caissier est remis à null
     * @ensure facturer le client si(il y en a un):
     *         il a fini de déposer et le caissier a fini de prendre(et qu'il a pas
     *         déjà payé)
     * @ensure notifier le client qui attend qu'il dépose un produit
     * 
     */
    synchronized public void prendre(Caissier caissier) {
        /**
         * on attend si il reste des client dans le magasin et
         * soit il n'y a pas de client à la caisse (mais il en reste dans les raysons
         * ou à la pile de chariots )
         * soit il y a un client mais il n'a pas encore fini de déposer ses articles
         */
        while (((tapis[currentCaissierCase] == null && !clientDone) || currentClient == null) && !isJobDone) {
            try {
                if (tapis[currentCaissierCase] == null && !clientDone) {
                    System.out.println("Caissier---" + caissier.getName()
                            + " attend car tapis vide mais client n' pas encore fini");
                } else if (currentClient == null) {
                    System.out.println("Caissier---" + caissier.getName() + " attend car pas de client");
                }
                wait();
            } catch (Exception e) {
            }
        }
        // prendre s'il y a qqchose à prendre
        if (tapis[currentCaissierCase] != null) {
            System.out.println(
                    "Caissier--- " + caissier.getName() + " encaisse "
                            + tapis[currentCaissierCase].getName() + " du client " + currentClient.getName());
            scannedProducts.add(tapis[currentCaissierCase]);
            tapis[currentCaissierCase] = null;
            incCurrentCaissierCase();
            notifyAll();
        }
        // facturer si tout est bon
        if (!paid && clientDone && tapis[currentCaissierCase] == null && currentClient != null) {
            facturer();
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

            chiffreDaffaire += sommeParClient;
            s += "\n";
            s += "Total facture: " + truncate(sommeParClient) + "$\n";
            s += "\n\n";
        }
        s += "\n\n";
        s += "Nombre de client facturé: " + facturator.keySet().size() + " " + Superette.NBR_INIT_CLIENTS + "\n";
        s += "Chiffre d'affaire: " + truncate(chiffreDaffaire) + "$\n";

        return s;
    }

    private double truncate(double value) {
        BigDecimal truncatedSum = new BigDecimal(value);
        truncatedSum = truncatedSum.setScale(2, RoundingMode.HALF_DOWN);
        return truncatedSum.doubleValue();
    }
}
