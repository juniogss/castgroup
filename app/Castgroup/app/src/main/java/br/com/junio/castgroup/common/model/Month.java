package br.com.junio.castgroup.common.model;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Month {
    private String value;
    private String label;

    @NotNull
    @Override
    public String toString() {
        return label;
    }
}
