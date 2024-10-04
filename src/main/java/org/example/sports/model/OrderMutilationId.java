package org.example.sports.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class OrderMutilationId implements Serializable {
    private Long order;
    private Long mutilation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderMutilationId that = (OrderMutilationId) o;
        return Objects.equals(order, that.order) &&
                Objects.equals(mutilation, that.mutilation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, mutilation);
    }
}
