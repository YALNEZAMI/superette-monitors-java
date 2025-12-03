package main;

public class Afficheur {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
    public static final String YELLOW = "\u001B[33m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";

    /**
     * 
     * @param compartiment le nom du thread ou variable partagée concerné
     *                     si pas pris en charge alors reste blanc
     * @param sentence     la phrase à afficher
     * @return la phrase colorée
     * @ensure un affichage distinguable et compréhensible
     */
    // compartiment et couleurs
    // Client-Caisse = Client passé en caisse --> cyan
    // Client-Rayon = Client qui passe aux rayons --> violet
    // Caisse ou Caissier --> vert
    // Rayon ou ChefRayon --> bleu
    // Chariot --> jaune
    public static String colorer(String compartiment, String sentence) {
        switch (compartiment) {
            case "Caisse":
            case "Caissier":
                return GREEN + sentence + RESET;
            case "Rayon":
            case "ChefRayon":
                return BLUE + sentence + RESET;

            case "Chariot":
                return YELLOW + sentence + RESET;
            case "Client-Caisse":
                return CYAN + sentence + RESET;
            case "Client-Rayon":
                return PURPLE + sentence + RESET;

            default:
                return sentence;
        }

    }

}
