package br.com.junio.castgroup.common.model;

import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private long code;
    private String desc;

    @NotNull
    @Override
    public String toString() {
        return desc;
    }
}
