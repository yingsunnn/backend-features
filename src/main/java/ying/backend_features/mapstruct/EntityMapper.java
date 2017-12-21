package ying.backend_features.mapstruct;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityMapper {
    Entry dtoToEntity (EntryDTO entryDTO);

    @InheritInverseConfiguration
    EntryDTO  entityToDTO (Entry entry);

}
