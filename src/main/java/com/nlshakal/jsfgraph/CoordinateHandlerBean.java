package com.nlshakal.jsfgraph;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CoordinateHandlerBean implements Serializable {
    private double x;
    private double y;
    private double r;
}