package main;

import java.util.Stack;

public class RangeeChariots {
    Stack<Chariot> chariots = new Stack<>();

    public RangeeChariots() {
        for (int i = 0; i < Superette.NBR_CHARIOTS; i++) {
            chariots.push(new Chariot("" + i));
        }
    }

    synchronized public Chariot prendreChariot() {
        while (chariots.isEmpty()) {
            try {
                wait();
            } catch (Exception e) {
            }
        }
        Chariot c = chariots.pop();
        notifyAll();
        return c;
    }

    synchronized public void returnChariot(Chariot chariot) {
        // while (chariots.size() == MAX_SIZE) {
        // try {
        // wait();
        // } catch (Exception e) {
        // }
        // }

        chariots.push(chariot);
        notifyAll();
    }

    @Override
    public String toString() {
        String s = chariots.size() + " chariots: \n";
        for (Chariot chariot : chariots) {
            s += chariot.getId() + "\n";
        }
        return s;
    }

    private class Chariot {

        String id;

        public String getId() {
            return id;
        }

        public Chariot(String id) {
            this.id = id;
        }
    }

}
