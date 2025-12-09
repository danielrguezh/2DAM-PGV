package com.docencia.rest.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docencia.rest.domain.Producto;
import com.docencia.rest.mappers.DetalleProductoMapper;
import com.docencia.rest.mappers.ProductoMapper;
import com.docencia.rest.modelo.DetalleProductoDocument;
import com.docencia.rest.modelo.ProductoEntity;
import com.docencia.rest.repository.interfaces.DetalleProductoRepository;
import com.docencia.rest.repository.interfaces.ProductoRepository;
import com.docencia.rest.service.interfaces.ProductoServiceInterface;
import java.util.Objects;

@Service
public class ProductoService implements ProductoServiceInterface {

    final private ProductoRepository productoRepository;

    final private DetalleProductoRepository detalleProductoRepository;

    final private ProductoMapper productoMapper;

    final private DetalleProductoMapper detalleProductoMapper;


    public ProductoService(ProductoRepository productoRepository, DetalleProductoRepository detalleProductoRepository,
            ProductoMapper productoMapper, DetalleProductoMapper detalleProductoMapper) {
        this.productoRepository = productoRepository;
        this.detalleProductoRepository = detalleProductoRepository;
        this.productoMapper = productoMapper;
        this.detalleProductoMapper = detalleProductoMapper;
    }


    @Override
    public Optional<Producto> find(Producto producto) {
        if (producto == null) {
            return Optional.empty();
        }
        return findById(producto.getId());
    }

    @Override
    public Optional<Producto> findById(int id) {
Optional<ProductoEntity> entityOpt = productoRepository.findById(id);
        if (entityOpt.isEmpty()) {
            return Optional.empty();
        }

        ProductoEntity entity = entityOpt.get();
        DetalleProductoDocument detalleDoc = detalleProductoRepository.findByProductoId(id).orElse(null);

        Producto producto = productoMapper.toDomain(entity, detalleDoc);
        return Optional.of(producto);
    }

    @Override
    public List<Producto> findAll() {
        List<ProductoEntity> entities = productoRepository.findAll();

        return entities.stream()
                .map(entity -> {
                    DetalleProductoDocument detalleDoc =
                            detalleProductoRepository.findByProductoId(entity.getId()).orElse(null);
                    return productoMapper.toDomain(entity, detalleDoc);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Producto save(Producto producto) {
        ProductoEntity entityToSave = productoMapper.toEntity(producto);
        entityToSave = productoRepository.save(entityToSave);
        if (producto.getDetalleProducto() == null) {
            return productoMapper.toDomain(entityToSave);
        }
        DetalleProductoDocument detalleDoc = detalleProductoMapper.toDocument(producto.getDetalleProducto());
        detalleDoc.setProductoId(entityToSave.getId());
        detalleDoc = detalleProductoRepository.save(detalleDoc);

        return productoMapper.toDomain(entityToSave, detalleDoc);
    }

    @Override
    public boolean delete(Producto producto) {
        if (producto == null) {
            return false;
        }
        int id = producto.getId();
        if (!productoRepository.existsById(id)) {
            return false;
        }
        productoRepository.deleteById(id);
        detalleProductoRepository.deleteByProductoId(id);
        return true;
    }

    @Override
    public boolean deleteById(int id) {
        Producto producto = new Producto(id);
        return delete(producto);
    }

}
