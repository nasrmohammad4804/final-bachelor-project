package com.nasr;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class Doc {

    private Long id;
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doc doc = (Doc) o;
        return Objects.equals(id, doc.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
