package bd.edu.seu.softwaredevelopment.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class Response {

    private int status;
    private String message;
    private UserDto user;
    private List<UserDto> users;
    private SupplierDto supplier;
    private List<SupplierDto> suppliers;
    private CategoryDto category;
    private List<CategoryDto> categories;
    private ProductDto product;
    private List<ProductDto> products;
    private TransactionDto transaction;
    private List<TransactionDto> transactions;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public Response() {}
}


