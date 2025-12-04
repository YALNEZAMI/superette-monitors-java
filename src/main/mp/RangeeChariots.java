package main.mp;

import java.util.Stack;

import main.Afficheur;
import main.threads.Client;

public class RangeeChariots {
    // les chariots sont emboité, donc stack pour bien les représenter
    private Stack<Chariot> chariots = new Stack<>();

    public RangeeChariots() {
        for (int i = 0; i < Superette.NBR_CHARIOTS; i++) {
            chariots.push(new Chariot("" + i));
        }
    }

    public Stack<Chariot> getChariots() {
        return chariots;
    }

    /**
     * 
     * @param client le client qui prend le chariot
     * @return retourn le chariot pris
     */
    synchronized public Chariot prendreChariot(Client client) {
        // si pas de chariot dispo, attendre
        while (chariots.isEmpty()) {
            try {
                System.out
                        .println(Afficheur.colorer("Chariot", "Client---" + client.getName() + " attend pour chariot"));
                wait();
            } catch (Exception e) {
            }
        }
        Chariot c = chariots.pop();
        System.out.println(
                Afficheur.colorer("Chariot", "Client---" + client.getName() + " a pris chariot n°" + c.getId()));
        return c;
    }

    /**
     * 
     * @param client le client qui restitue le chariot
     *               --> pas de contrainte pour le nombre
     */
    synchronized public void restituerChariot(Client client) {
        System.out.println(Afficheur.colorer("Chariot",
                "Client---" + client.getName() + " a restitué le chariot n°" + client.getChariot().getId()));
        chariots.push(client.getChariot());
        notify();// notify simple car un seul chariot est réstitué à la fois(un seul wait)
    }

    @Override
    public String toString() {
        String s = chariots.size() + "/" + Superette.NBR_CHARIOTS + " chariots: \n";
        if (chariots.size() < Superette.NBR_CHARIOTS) {
            s += "Il manque " + (Superette.NBR_CHARIOTS - chariots.size()) + "\n";
        }
        return s;
    }

}
