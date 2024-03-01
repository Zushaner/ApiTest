package src.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AdditionalDataModel {
    private String additionInfo;
    private Integer additionNumber;
    private Integer additionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdditionalDataModel data = (AdditionalDataModel) o;

        if (!Objects.equals(additionInfo, data.additionInfo)) return false;
        return Objects.equals(additionNumber, data.additionNumber);
    }
}
