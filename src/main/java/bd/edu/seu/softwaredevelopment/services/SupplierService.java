package bd.edu.seu.softwaredevelopment.services;

import bd.edu.seu.softwaredevelopment.dtos.SupplierDto;
import bd.edu.seu.softwaredevelopment.interfaces.SupplierServiceInterface;
import bd.edu.seu.softwaredevelopment.models.Supplier;
import bd.edu.seu.softwaredevelopment.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierService implements SupplierServiceInterface {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public SupplierDto saveSupplier(SupplierDto dto) {
        Supplier supplier = new Supplier(dto.getName(), dto.getContactInfo(), dto.getAddress());
        Supplier saved = supplierRepository.save(supplier);
        return mapToDto(saved);
    }

    @Override
    public List<SupplierDto> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierDto getSupplierById(String id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        return mapToDto(supplier);
    }

    @Override
    public SupplierDto updateSupplier(String id, SupplierDto dto) {
        Supplier existing = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        existing.setName(dto.getName());
        existing.setContactInfo(dto.getContactInfo());
        existing.setAddress(dto.getAddress());
        return mapToDto(supplierRepository.save(existing));
    }

    @Override
    public void deleteSupplier(String id) {
        supplierRepository.deleteById(id);
    }

    private SupplierDto mapToDto(Supplier s) {
        SupplierDto dto = new SupplierDto();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setContactInfo(s.getContactInfo());
        dto.setAddress(s.getAddress());
        return dto;
    }
}