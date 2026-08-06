package com.nectavox.nxcore.models.item;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomModelDataData {

    @Builder.Default
    private List<Float> floats = List.of();

    @Builder.Default
    private List<String> strings = List.of();

    @Builder.Default
    private List<Boolean> flags = List.of();

    @Builder.Default
    private List<Integer> colors = List.of();
}