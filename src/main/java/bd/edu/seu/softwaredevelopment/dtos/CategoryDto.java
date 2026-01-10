package bd.edu.seu.softwaredevelopment.dtos;

import java.util.List;

public class CategoryDto {
    private String id;
    private String name;
    private List<ProductDto> products;

    public CategoryDto() {
    }

    public CategoryDto(String id, String name, List<ProductDto> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}

