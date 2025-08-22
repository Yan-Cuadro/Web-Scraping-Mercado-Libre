package org.project.model;

public class Producto {

    private String title;
    private String price;

    public Producto() {
    }

    public Producto(String title, String price) {
        this.title = title;
        this.price = price;
    }

    // Opcional: Métodos getter y setter
    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
