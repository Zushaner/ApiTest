package src.models;

import lombok.*;

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
