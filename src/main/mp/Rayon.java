package main.mp;

import java.util.List;

import main.Afficheur;
import main.products.Product;
import java.util.LinkedList;

import main.products.ProductFactory;
import main.threads.Client;

public class Rayon {
    private String id;// le nom du produit dans ce rayon
    private List<Product> products;// liste des produits actuellement disponible au rayon

    public List<Product> getProducts() {
        return products;
    }

    /**
     * @param p produit à ajouter
     */
    synchronized public int ajouter(Product p) {
        if (products.size() < Superette.MAX_SIZE_RAYON) {
            products.add(p);
            notify();// notify simple suffit car un seul wait (on ajout produit par produit)
            return 1;
        }
        return 0;

    }

    /**
     * @param client le client qui prend le produit
     */
    synchronized public void prendre(Client client) {
        // le client attend tant que son besoin du rayon n'est pas satisfait
        // à notifier par le chef de rayon
        while (products.size() == 0) {
            try {
                System.out.println(Afficheur.colorer("Client-Rayon",
                        "Client---" + client.getName() + " attend le remplissage du rayon " + id));
                wait();
            } catch (Exception e) {
            }
        }
        products.remove(0);
        System.out.println(Afficheur.colorer("Client-Rayon",
                "Client---" + client.getName() + " a pris 1 "
                        + id
                        + " -> nouveau stock = "
                        + getProducts().size()));
    }

    public String getId() {
        return id;
    }

    /**
     * 
     * @param id identifiant du rayon
     * @ensure rayons sont créés déjà plein ou vide selon:
     *         Superette.ARE_RAYONS_PLEIN_PAR_DEFAUT
     */
    public Rayon(String id) {
        this.id = id;
        products = new LinkedList<Product>();
        if (Superette.ARE_RAYONS_PLEIN_PAR_DEFAUT) {
            for (int i = 0; i < Superette.MAX_SIZE_RAYON / 2; i++) {
                products.add(ProductFactory.createProduct(id));

            }
        }

    }
}
