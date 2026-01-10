package bd.edu.seu.softwaredevelopment.interfaces;

import bd.edu.seu.softwaredevelopment.dtos.SupplierDto;
import java.util.List;

public interface SupplierServiceInterface {
    // Save a new supplier to MongoDB
    SupplierDto saveSupplier(SupplierDto supplierDto);

    // Retrieve all suppliers
    List<SupplierDto> getAllSuppliers();

    // Find a specific supplier by their String ID
    SupplierDto getSupplierById(String id);

    // Update existing supplier data
    SupplierDto updateSupplier(String id, SupplierDto supplierDto);

    // Remove a supplier from the database
    void deleteSupplier(String id);
}