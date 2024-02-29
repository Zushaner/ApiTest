package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ObjectModel {
    private Integer id;
    private List<Integer> importantNumbers;
    private String title;
    private AdditionalData additionalData;
    private Boolean verified;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectModel that = (ObjectModel) o;

        if (!Objects.equals(importantNumbers, that.importantNumbers))
            return false;
        if (!Objects.equals(title, that.title)) return false;
        if (!Objects.equals(additionalData, that.additionalData))
            return false;
        return Objects.equals(verified, that.verified);
    }

}
