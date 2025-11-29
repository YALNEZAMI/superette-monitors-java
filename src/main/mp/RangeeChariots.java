package main.mp;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import main.threads.Client;

public class RangeeChariots {
    private Stack<Chariot> chariots = new Stack<>();

    public RangeeChariots() {
        for (int i = 0; i < Superette.NBR_CHARIOTS; i++) {
            chariots.push(new Chariot("" + i));
        }
    }

    synchronized public Chariot prendreChariot(Client client) {
        while (chariots.isEmpty()) {
            try {
                System.out.println(client.getName() + " attend pour chariot");

                wait();
            } catch (Exception e) {
            }
        }
        System.out.println(client.getName() + " a pris chariot");
        Chariot c = chariots.pop();
        return c;
    }

    public Stack<Chariot> getChariots() {
        return chariots;
    }

    synchronized public void restituerChariot(Client client) {
        // while (chariots.size() == MAX_SIZE) {
        // try {
        // wait();
        // } catch (Exception e) {
        // }
        // }
        System.out.println(client.getName() + " a restitué le chariot " + client.getChariot().getId());
        chariots.push(client.getChariot());
        notifyAll();
    }

    @Override
    public String toString() {
        String s = chariots.size() + "/" + Superette.NBR_CHARIOTS + " chariots: \n";
        // for (Chariot chariot : chariots) {
        // s += chariot.getId() + "\n";
        // }
        return s;
    }

    public class Chariot {

        private String id;
        private Map<String, Integer> products;

        public String getId() {
            return id;
        }

        public Chariot(String id) {
            this.id = id;
            products = new HashMap<String, Integer>();
            for (String productName : Superette.availableProductsNames) {
                products.put(productName, 0);
            }
        }

        public void setProduct(String product, int nbr) {
            products.put(product, nbr);
        }

        public void removeProduct(String product) {
            products.remove(product);
        }

        public Map<String, Integer> getProducts() {
            return products;
        }
    }

}
