package main.threads;

import main.mp.Superette;

public class Caissier extends Thread {
    Superette superette;

    public Superette getSuperette() {
        return superette;
    }

    public Caissier(Superette superette) {
        this.superette = superette;
    }

    @Override
    public void run() {
        while (Superette.NBR_INIT_CLIENTS > superette.getNbrClientSortie()) {
            superette.getCaisse().prendre(this);
        }
        System.out.println("Le caissier a fini son travail");
    }
}