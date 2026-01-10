package bd.edu.seu.softwaredevelopment.interfaces;

import bd.edu.seu.softwaredevelopment.dtos.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductServiceInterface {
    ProductDto saveProduct(ProductDto productDto, MultipartFile imageFile);
    ProductDto updateProduct(ProductDto productDto, MultipartFile imageFile);
    List<ProductDto> getAllProducts();
    ProductDto getProductById(String id);
    void deleteProduct(String id);
    List<ProductDto> searchProduct(String input);
}
