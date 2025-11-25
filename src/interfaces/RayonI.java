package interfaces;

import java.util.List;

import main.Client;
import main.Product;

public interface RayonI {
    String getId();

    void prendre(Client client);

    void ajouter(Product p);

    List<Product> getProducts();

}
