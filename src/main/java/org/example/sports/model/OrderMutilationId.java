package org.example.sports.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class OrderMutilationId implements Serializable {
    private Long order;
    private Long mutilation;


}
