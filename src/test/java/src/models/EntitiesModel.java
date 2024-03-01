package src.models;

import lombok.*;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class EntitiesModel {
    private List<EntityModel> entities;
}
