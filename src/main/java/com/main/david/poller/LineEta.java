package com.main.david.poller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class LineEta {
    private String lineNumber;
    private Date eta;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineEta lineEta = (LineEta) o;
        return Objects.equals(lineNumber, lineEta.lineNumber) &&
                Objects.equals(eta, lineEta.eta);
    }

    @Override
    public int hashCode() {

        return Objects.hash(lineNumber);
    }
}
