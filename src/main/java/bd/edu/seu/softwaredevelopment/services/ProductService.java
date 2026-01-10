package bd.edu.seu.softwaredevelopment.services;

import bd.edu.seu.softwaredevelopment.dtos.ProductDto;
import bd.edu.seu.softwaredevelopment.interfaces.ProductServiceInterface;
import bd.edu.seu.softwaredevelopment.models.Product;
import bd.edu.seu.softwaredevelopment.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements ProductServiceInterface {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductDto saveProduct(ProductDto dto, MultipartFile imageFile) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setBrand(dto.getBrand()); // ML feature for brand-based demand
        product.setPrice(dto.getUnitPrice());
        product.setCategoryId(dto.getCategory());
        product.setSupplierId(dto.getSupplierId()); // Link to Supplier for inventory chain
        product.setStockQuantity(dto.getStockQuantity());
        product.setSku(dto.getSku());

        // TODO: Implement image saving to a static folder or cloud storage

        productRepository.save(product);
        return dto;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, MultipartFile imageFile) {
        Product product = productRepository.findById(productDto.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productDto.getName());
        product.setBrand(productDto.getBrand());
        product.setPrice(productDto.getUnitPrice());
        product.setCategoryId(productDto.getCategory());
        product.setStockQuantity(productDto.getStockQuantity());
        product.setSku(productDto.getSku());

        productRepository.save(product);
        return productDto;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        // Fetch all products and map them to DTOs to avoid returning null
        return productRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto getProductById(String id) {
        return productRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDto> searchProduct(String input) {
        // Simple case-insensitive search logic
        return productRepository.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(input.toLowerCase()) ||
                        p.getSku().toLowerCase().contains(input.toLowerCase()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Helper method to convert Entity to DTO
    private ProductDto mapToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSku(product.getSku());
        dto.setUnitPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategory(product.getCategoryId());
        dto.setBrand(product.getBrand());
        dto.setSupplierId(product.getSupplierId());
        // dto.setImageUrl(product.getImageUrl()); // Map image if implemented
        return dto;
    }}
