package com.masterPi.data.impl;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SelectedText")
public class SelectedText {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    @Column(name = "id")
    private Long id;

    private Long textIdSelected;

    public SelectedText() {
    }

    public SelectedText(Long textIdSelected) {
        this.textIdSelected = textIdSelected;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTextIdSelected() {
        return textIdSelected;
    }

    public void setTextIdSelected(Long textIdSelected) {
        this.textIdSelected = textIdSelected;
    }
}
