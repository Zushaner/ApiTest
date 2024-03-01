package src.models;

import lombok.*;

import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class EntityModel {
    private Integer id;
    private List<Integer> importantNumbers;
    private String title;
    private AdditionalDataModel additionalData;
    @With private Boolean verified;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityModel that = (EntityModel) o;

        if (!Objects.equals(importantNumbers, that.importantNumbers))
            return false;
        if (!Objects.equals(title, that.title)) return false;
        if (!Objects.equals(additionalData, that.additionalData))
            return false;
        return Objects.equals(verified, that.verified);
    }

}
