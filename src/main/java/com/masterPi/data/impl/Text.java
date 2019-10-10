package com.masterPi.data.impl;
import javax.persistence.*;

@Entity
@Table(name = "Text")

public class Text {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String text;
    private String title;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "index_id", referencedColumnName = "id")
    private Index index;

    public Text() {
    }

    public Text(String text, String title, Index index) {
        this.text = text;
        this.title = title;
        this.index = index;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Index getIndex() {
        return index;
    }

    public void setIndex(Index index) {
        this.index = index;
    }
}
