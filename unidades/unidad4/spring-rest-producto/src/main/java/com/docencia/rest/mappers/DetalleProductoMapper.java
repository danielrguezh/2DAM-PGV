package com.docencia.rest.mappers;

import org.mapstruct.Mapper;

import com.docencia.rest.domain.DetalleProducto;
import com.docencia.rest.modelo.DetalleProductoDocument;

@Mapper(componentModel = "spring", imports = DetalleProducto.class )
public interface DetalleProductoMapper {

    DetalleProductoDocument toDocument(DetalleProducto source);

    DetalleProducto toDomain(DetalleProductoDocument source);

}
